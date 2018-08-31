package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Get extends UnaryOp {

	public Get(Exp exp) {
		super(exp);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitGet(exp);
	}
}
