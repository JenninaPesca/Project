package interpreter.parser.ast;

import static java.util.Objects.requireNonNull;

import interpreter.visitors.Visitor;

public class IfThenStmt implements Stmt{
	protected final Exp exp; //espressione dentro if controlla:deve essere booleana??
	protected final StmtSeq then_seq; //stmtseq dentro then
	//private StmtSeq secondBlock = null; //stmtseq dentro else --> NB puo essere null
	
	public IfThenStmt(Exp exp, StmtSeq then_seq) {
		this.exp = requireNonNull(exp);
		this.then_seq = requireNonNull(then_seq);
	}

	/*public IfElseStmt(Exp exp, StmtSeq firstBlock, StmtSeq secondBlock) {
		this.exp = requireNonNull(exp);
		this.firstBlock = requireNonNull(firstBlock);
		this.secondBlock = secondBlock;
	}
	*/
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + exp + "," + then_seq + ")";
		/*if (secondBlock == null)
			return getClass().getSimpleName() + "(" + exp + "," + firstBlock + ")";
		return getClass().getSimpleName() + "(" + exp + "," + firstBlock + "," + secondBlock + ")";
		*/
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIfThenStmt(exp, then_seq); //controlla: nel caso in cui non ci sia else?
	}
}
