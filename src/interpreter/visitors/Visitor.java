package interpreter.visitors;

import interpreter.parser.ast.Exp;
import interpreter.parser.ast.ExpSeq;
import interpreter.parser.ast.Ident;
import interpreter.parser.ast.Stmt;
import interpreter.parser.ast.StmtSeq;

public interface Visitor<T> {
	T visitAdd(Exp left, Exp right);

	T visitAssignStmt(Ident ident, Exp exp);

	T visitForEachStmt(Ident ident, Exp exp, StmtSeq block);

	T visitIfThenStmt(Exp exp, StmtSeq then_seq);
	
	T visitIfThenElseStmt(Exp exp, StmtSeq then_seq, StmtSeq else_seq);

	T visitBoolLiteral(boolean value); //fatto da me
	/*operatori binari*/
	T visitAdd(Exp left, Exp right);

	T visitDoWhileStmt(StmtSeq block, Exp exp);
	
	T visitIntLiteral(int value);

	T visitListLiteral(ExpSeq exps);

	T visitMoreExp(Exp first, ExpSeq rest);

	T visitMoreStmt(Stmt first, StmtSeq rest);

	T visitMul(Exp left, Exp right);

	T visitPrefix(Exp left, Exp right);
	
	T visitAnd(Exp left, Exp right); //fatto da me
	
	T visitEq(Exp left, Exp right); //fatto da me
	/*operatori unari*/
	T visitSign(Exp exp);
	
	T visitNot(Exp exp); //fatto da me
	
	T visitOpt(Exp exp); //fatto da me
	
	T visitEmpty(Exp exp); //fatto da me
	
	T visitDef(Exp exp); //fatto da me
	
	T visitGet(Exp exp); //fatto da me
	/*sequence of exp*/	
	T visitSingleExp(Exp exp);

	T visitPrintStmt(Exp exp);

	T visitProg(StmtSeq stmtSeq);

	T visitSign(Exp exp);
	
	T visitNot(Exp exp);

	T visitIdent(String name);

	T visitSingleExp(Exp exp);

	T visitSingleStmt(Stmt stmt);

	T visitVarStmt(Ident ident, Exp exp);
}
