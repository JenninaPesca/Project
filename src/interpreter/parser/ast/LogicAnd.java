package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class LogicAnd extends BinaryOp{

	public LogicAnd(Exp left, Exp right) {
		super(left, right);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitLogicAnd(left, right);
	}
}
