package interpreter.visitors.evaluation;

public interface Value {
	/* default conversion methods */
	default int asInt() {
		throw new EvaluatorException("Expecting an integer value");
	}

	default ListValue asList() {
		throw new EvaluatorException("Expecting a list value");
	}
	//fatto da me inizio
	default boolean asBool() {
		throw new EvaluatorException("Expecting a bool value");
	}
	
	default EmptyOptValue asEmptyOptValue() {
		throw new EvaluatorException("Expecting a EmptyOpt value");
	}
	
	default OptValue asOptValue() {
		throw new EvaluatorException("Expecting a Opt value");
	}
	//fatto da me fine
}
