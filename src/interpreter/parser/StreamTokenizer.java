package interpreter.parser;

import static interpreter.parser.TokenType.*;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class StreamTokenizer implements Tokenizer {
	private static final String regEx;
	private static final Map<String, TokenType> keywords = new HashMap<>();
	private static final Map<String, TokenType> symbols = new HashMap<>();

	private boolean hasNext = true; // any stream contains at least the EOF
									// token
	private TokenType tokenType;
	private String tokenString;
	private int binValue; //--fatto da me--
	private int intValue;
	private final Scanner scanner;

	
	static {
		// remark: groups must correspond to the ordinal of the corresponding
		// token type
		final String identRegEx = "([a-zA-Z][a-zA-Z0-9]*)"; // group 2
		final String binNumRegEx = "(0[bB][01]+)"; //--fatto da me-- espressione regolare per i numeri binari group1
		final String numRegEx = "(0|[1-9][0-9]*)"; // group 3
		final String skipRegEx = "(\\s+|//.*)"; // group 4
		final String symbolRegEx = "\\+|\\*|=|\\(|\\)|;|,|\\{|\\}|-|::|:|\\[|\\]";
		regEx = identRegEx + "|" + binNumRegEx + "|"  /*fatto da me*/ + numRegEx + "|" + skipRegEx + "|" + symbolRegEx;
	}

	static {
		keywords.put("true", BOOL); //fatto da me-- aggiungo la keyword true
		keywords.put("false", BOOL); //fatto da me-- aggiungo la keyword false
		keywords.put("for", FOR);
		keywords.put("print", PRINT);
		keywords.put("var", VAR);
		keywords.put("opt", OPT);
		keywords.put("empty", EMPTY);
		keywords.put("def", DEF);
		keywords.put("get", GET);
		keywords.put("if", IF);
		keywords.put("else", ELSE);
		keywords.put("do", DO);
		keywords.put("while", WHILE);
	}

	static {
		symbols.put("&&", LOGICAND);
		symbols.put("==", EQUALITY);
		symbols.put("!", NOT);
		symbols.put("+", PLUS);
		symbols.put("*", TIMES);
		symbols.put("::", PREFIX);
		symbols.put("=", ASSIGN);
		symbols.put(":", IN);
		symbols.put("(", OPEN_PAR);
		symbols.put(")", CLOSE_PAR);
		symbols.put(";", STMT_SEP);
		symbols.put(",", EXP_SEP);
		symbols.put("{", OPEN_BLOCK);
		symbols.put("}", CLOSE_BLOCK);
		symbols.put("-", MINUS);
		symbols.put("[", OPEN_LIST);
		symbols.put("]", CLOSE_LIST);
	}

	public StreamTokenizer(Reader reader) {
//		System.out.println("INIZIO (StreamTokenizer) costruttore"); //CANCELLA
		scanner = new StreamScanner(regEx, reader);
//		System.out.println("FINE (StreamTokenizer) costruttore"); //CANCELLA
	}

/*fatto da me*/
//metodo che converte la stringa binaria in un numero intero
	private int parseBin(String tokenString) {
		int i=tokenString.length();
		int binValue = 0;
		while (i>2) {
			char c = tokenString.charAt(i-1); //i-1 perch� l'ultimo indice � la lunghezza della stringa-1 dato che gli indici iniziano da 0 e la lunghezza da 1
			if (c == '1') {
				binValue = (int) (binValue + Math.pow(2.0, (double)((tokenString.length())-i)));
			}
			i--;			
		}
		return binValue;
	}
	
	private void checkType() {
//		System.out.println("INIZIO(StreamTokenizer) checkType"); //CANCELLA
//		System.out.println(" 	chiamo scanner.group"); //CANCELLA
		tokenString = scanner.group();
//		System.out.println(" 	chiamo scanner.group con "+ IDENT.ordinal()); //CANCELLA
		if (scanner.group(IDENT.ordinal()) != null) { // IDENT or a keyword
			tokenType = keywords.get(tokenString); //controlla se � una keyworld ->se non � una keyword allora � un ident
			if (tokenType == null)
				tokenType = IDENT;
System.out.println("FINE (StreamTokenizer) checkType ident"); //CANCELLA
			return;
		}
		/*--fatto da me-- inizio*/
System.out.println("	chiamo scanner.group con "+ BIN.ordinal()); //CANCELLA
		if (scanner.group(BIN.ordinal()) != null) { // BIN
			tokenType = BIN;
System.out.println("	prima di integer.parsebin");
			binValue = parseBin(tokenString); //modifica: il metodo deve essere dentro la classe BinLiteral??
														 // secondo me no perch� non modifica le variabili della classe quindi lo metto come metodo in questa classe
System.out.println("	BIN VALUE"+binValue);
System.out.println("FINE (StreamTokenizer) checkType bin"); //CANCELLA
			return;
		}
		/*--fatto da me-- fine*/
System.out.println(" 	chiamo scanner.group con "+ NUM.ordinal()); //CANCELLA
		if (scanner.group(NUM.ordinal()) != null) { // NUM
			tokenType = NUM;
			intValue = Integer.parseInt(tokenString);
System.out.println("FINE (StreamTokenizer) checkType num"); //CANCELLA
			return;
		}
		//System.out.println(" 	chiamo scanner.group con "+ SKIP.ordinal()); //CANCELLA
		if (scanner.group(SKIP.ordinal()) != null) { // SKIP
			tokenType = SKIP;
//			System.out.println("FINE (StreamTokenizer) checkType skip"); //CANCELLA
			return;
		}
		tokenType = symbols.get(tokenString); // a symbol
//		System.out.println("FINE (StreamTokenizer) checkType symbol"); //CANCELLA
		if (tokenType == null)
			throw new AssertionError("Fatal error");
	}

	@Override
	public TokenType next() throws TokenizerException {
//		System.out.println("INIZIO (StreamTokenizer) next"); //CANCELLA
		do {
			tokenType = null;
			tokenString = "";
			try {
				if (hasNext && !scanner.hasNext()) {
//					System.out.println("EOF");
					hasNext = false;
					return tokenType = EOF;
				}
//				System.out.println(" 	chiamo scanner.next"); //CANCELLA
				scanner.next();
			} catch (ScannerException e) {
				throw new TokenizerException(e);
			}
			checkType();
		} while (tokenType == SKIP);
//		System.out.println("FINE (StreamTokenizer) next"); //CANCELLA
		return tokenType;
	}

	private void checkValidToken() {
//		System.out.println("INIZIO (StreamTokenizer) checkValidToken"); //CANCELLA
		if (tokenType == null)
			throw new IllegalStateException();
//		System.out.println("FINE (StreamTokenizer) checkValidToken"); //CANCELLA
	}

	private void checkValidToken(TokenType ttype) {
//		System.out.println("INIZIO (StreamTokenizer) checkValidToken con TokenType"); //CANCELLA
//		System.out.println(" 	ttype: "+ ttype+" tokentype: "+tokenType); //CANCELLA
		if (tokenType != ttype)
			throw new IllegalStateException();
//		System.out.println("FINE (StreamTokenizer) checkValidToken con Tokentype"); //CANCELLA
	}

	@Override
	public String tokenString() {
//		System.out.println("INIZIO (StreamTokenizer) tokenString"); //CANCELLA
//		System.out.println(" 	chiamo checkvalidtoken"); //CANCELLA
		checkValidToken();
//		System.out.println("FINE (StreamTokenizer) tokenString"); //CANCELLA
		return tokenString;
	}
	/*--fatto da me inizio--*/
	public int binValue() {
		System.out.println("INIZIO (StreamTokenizer) intValue"); //CANCELLA
		System.out.println(" 	chiamo checkvalidtoken con num"); //CANCELLA
		checkValidToken(BIN);
		System.out.println("FINE (StreamTokenizer) intValue"); //CANCELLA
		return binValue;
	}
	/*fatto da me fine*/
	@Override
	public int intValue() {
//		System.out.println("INIZIO (StreamTokenizer) intValue"); //CANCELLA
//		System.out.println(" 	chiamo checkvalidtoken con num"); //CANCELLA
		checkValidToken(NUM);
//		System.out.println("FINE (StreamTokenizer) intValue"); //CANCELLA
		return intValue;
	}

	@Override
	public TokenType tokenType() {
//		System.out.println("INIZIO (StreamTokenizer) tokenType"); //CANCELLA
//		System.out.println(" 	chiamo checkvalidtoken"); //CANCELLA
		checkValidToken();
//		System.out.println("FINE (StreamTokenizer) tokenType"); //CANCELLA
		return tokenType;
	}

	@Override
	public boolean hasNext() {
//		System.out.println("(StreamTokenizer) hasNext"); //CANCELLA
		return hasNext;
	}

	@Override
	public void close() throws TokenizerException {
//		System.out.println("INIZIO (StreamTokenizer) close"); //CANCELLA
		try {
//			System.out.println(" 	chiamo scanner.close"); //CANCELLA
			scanner.close();
		} catch (ScannerException e) {
			throw new TokenizerException(e);
		}
//		System.out.println("FINE (StreamTokenizer) close"); //CANCELLA
	}
}
