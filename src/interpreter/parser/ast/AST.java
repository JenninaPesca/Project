package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public interface AST {
	<T> T accept(Visitor<T> visitor);
}
