package interpreter.parser;

import interpreter.parser.ast.Prog;

public interface Parser {

	Prog parseProg() throws ParserException;

}