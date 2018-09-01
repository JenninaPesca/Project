package interpreter.parser;

public interface Tokenizer extends AutoCloseable {

	TokenType next() throws TokenizerException;

	String tokenString();

	int binValue();
	
	int intValue();

	TokenType tokenType();

	boolean hasNext();

	public void close() throws TokenizerException;

}