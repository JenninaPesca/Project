package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Equality extends BinaryOp {

	public Equality(Exp left, Exp right) {
		super(left, right);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitEquality(left, right);
	}
	
}
