package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Opt extends UnaryOp{
	
	public Opt(Exp exp) {
		super(exp);
	}
//modifica
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitOpt(exp);
	}
}
