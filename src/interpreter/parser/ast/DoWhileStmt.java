package interpreter.parser.ast;

import static java.util.Objects.requireNonNull;

import interpreter.visitors.Visitor;

public class DoWhileStmt implements Stmt {
	private final StmtSeq block; //sequenza di stmt da eseguire
	private final Exp exp; //condizione while controlla:deve essere booleana??
	
	public DoWhileStmt(StmtSeq block, Exp exp) {
		this.block = requireNonNull(block);
		this.exp = requireNonNull(exp);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + block + "," + exp + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitDoWhileStmt(block, exp);
	}

}
