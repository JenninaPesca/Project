package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class BoolLiteral extends PrimLiteral<Boolean>{
	
	public BoolLiteral(boolean value) {
		super(value);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return null;
	}  //modifica!!!!!

}
