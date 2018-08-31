package interpreter.visitors.typechecking;

import static interpreter.visitors.typechecking.PrimtType.*;

import interpreter.environments.EnvironmentException;
import interpreter.environments.GenEnvironment;
import interpreter.parser.ast.Exp;
import interpreter.parser.ast.ExpSeq;
import interpreter.parser.ast.Ident;
import interpreter.parser.ast.SimpleIdent;
import interpreter.parser.ast.Stmt;
import interpreter.parser.ast.StmtSeq;
import interpreter.visitors.Visitor;

public class TypeCheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	private void checkBinOp(Exp left, Exp right, Type type) {
		type.checkEqual(left.accept(this));
		type.checkEqual(right.accept(this));
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new TypecheckerException(e);
		}
		return null;
	}

	// static semantics for statements; no value returned by the visitor

	@Override
	public Type visitAssignStmt(Ident ident, Exp exp) {
		Type found = env.lookup(ident);
		found.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		Type ty = exp.accept(this).getListElemType();
		env.enterLevel();
		env.dec(ident, ty);
		block.accept(this);
		env.exitLevel();
		return null;
	}
/*fatto da me inizio*/
	//@Override 
	public Type visitIfThenStmt(Exp exp, StmtSeq then_seq) {
		BOOL.checkEqual(exp.accept(this)); 	//BOOL.checkEqual(exp.accept(this).getListElemType());
		env.enterLevel();
		then_seq.accept(this);
		env.exitLevel();
		return null;
	}
	
	public Type visitIfThenElseStmt(Exp exp, StmtSeq then_seq, StmtSeq else_seq) {
		visitIfThenStmt(exp, then_seq);
		env.enterLevel();
		else_seq.accept(this);
		env.exitLevel();
		return null;
	}
	
	public Type visitDoWhile(StmtSeq block, Exp exp) {
		env.enterLevel();
		block.accept(this);
		env.exitLevel();
		BOOL.checkEqual(exp.accept(this)); 	//BOOL.checkEqual(exp.accept(this).getListElemType());
		return null;	
	}
	
/*fatto da me fine*/
	@Override
	public Type visitPrintStmt(Exp exp) {
		exp.accept(this);
		return null;
	}

	@Override
	public Type visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Type visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}
  
	// static semantics of expressions; a type is returned by the visitor
	/*operatori binari*/
	@Override
	public Type visitAdd(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitIntLiteral(int value) {
		return INT;
	}

	@Override
	public Type visitListLiteral(ExpSeq exps) {
		return new ListType(exps.accept(this));
	}

	@Override
	public Type visitMul(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitPrefix(Exp left, Exp right) {
		Type elemType = left.accept(this);
		return new ListType(elemType).checkEqual(right.accept(this));
	}

	@Override
	public Type visitSign(Exp exp) {
		return INT.checkEqual(exp.accept(this));
	}
	
	/*fatto da me inizio*/
	public Type visitNot(Exp exp) {
		BOOL.checkEqual(exp.accept(this));
		return BOOL;
	}
	
	public Type VisitOpt(Exp exp) {
		exp.accept(this);
		return OPT;
	}
	
	public Type VisitEmpty(Exp exp) {
		
		return OPT;
	}
	/*fatto da me fine*/
	@Override
	public Type visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}

	// static semantics of sequences of expressions
	// a type is returned by the visitor

	@Override
	public Type visitSingleExp(Exp exp) {
		return exp.accept(this);
	}

	@Override
	public Type visitMoreExp(Exp first, ExpSeq rest) {
		Type found = first.accept(this);
		return found.checkEqual(rest.accept(this));
	}

}
