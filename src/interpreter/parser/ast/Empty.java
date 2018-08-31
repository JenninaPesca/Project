package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Empty extends UnaryOp{

	public Empty(Exp exp) {
		super(exp);
	}
//modifica
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitEmpty(exp);
	}
}
