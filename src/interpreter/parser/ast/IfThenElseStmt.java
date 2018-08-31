package interpreter.parser.ast;

import static java.util.Objects.requireNonNull;

import interpreter.visitors.Visitor;

public class IfThenElseStmt extends IfThenStmt {
	private final StmtSeq else_seq;
		
	public IfThenElseStmt(Exp exp, StmtSeq then_seq, StmtSeq else_seq) {
		super(exp, then_seq);
		this.else_seq = requireNonNull(else_seq);
		}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + exp + "," + then_seq + "," + else_seq + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIfThenElseStmt(exp, then_seq, else_seq); 
	}
}
