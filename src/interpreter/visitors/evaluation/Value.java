package interpreter.visitors.evaluation;

public interface Value {
	/* default conversion methods */
	default int asInt() {
		throw new EvaluatorException("Expecting an integer value");
	}

	default ListValue asList() {
		throw new EvaluatorException("Expecting a list value");
	}

}
