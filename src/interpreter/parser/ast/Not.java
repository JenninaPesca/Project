package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Not extends UnaryOp{
	
	public Not(Exp exp) {
		super(exp);
	}
//modifica
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitNot(exp);
	}
}
