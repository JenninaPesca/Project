package interpreter.parser;

import static interpreter.parser.TokenType.*;

import interpreter.parser.ast.*;

/*
Prog ::= StmtSeq 'EOF'
 StmtSeq ::= Stmt (';' StmtSeq)?
 Stmt ::= 'var'? ID '=' Exp | 'print' Exp |  'for' ID ':' Exp '{' StmtSeq '}' | 'if' '('Exp')' '{'StmtSeq'}' ('else' '{' StmtSeq '}')? 
 			| 'do' '{' StmtSeq '}' 'while' '('Exp')'
 ExpSeq ::= Exp (',' ExpSeq)?
 Exp ::= Equality ('&&' Exp)? | Equality
 Equality ::= Prefix ('==' Equality)* | Prefix
 Prefix ::= Add ('::' Prefix)* | Add
 Add ::= Mul ('+' Add)* | Mul
 Mul::= Atom ('*' Mul)* | Atom
 Atom ::= '-' Atom | '!' Atom | 'opt' Atom | 'empty' Atom | 'def' Atom | 'get' Atom | '[' ExpSeq ']' | BOOL | BIN | NUM | ID  | '(' Exp ')'
 
 //modifica: Equality nome adeguato a ==??
 //controlla: grammatica corretta?
*/

public class StreamParser implements Parser {

	private final Tokenizer tokenizer;

	private void tryNext() throws ParserException {
		//System.out.println("INIZIO (StreamParser) tryNext"); //CANCELLA
		try {
			//System.out.println("	chiamo la next"); //CANCELLA
			tokenizer.next();
		} catch (TokenizerException e) {
			throw new ParserException(e);
		}
		//System.out.println("FINE (StreamParser) tryNext"); //CANCELLA

	}

	private void match(TokenType expected) throws ParserException {
		//System.out.println("INIZIO (StreamParser) match con expected: "+expected); //CANCELLA
		final TokenType found = tokenizer.tokenType();
		//System.out.println("	found: "+found); //CANCELLA
		if (found != expected)
			throw new ParserException(
					"Expecting " + expected + ", found " + found + "('" + tokenizer.tokenString() + "')");
		//System.out.println("FINE (StreamParser) match"); //CANCELLA
	}

	private void consume(TokenType expected) throws ParserException {
		/*System.out.println("INIZIO (StreamParser) consume"); //CANCELLA
		System.out.println("	chiamo match"); //CANCELLA*/
		match(expected);
		//System.out.println("	chiamo tryNext"); //CANCELLA
		tryNext();
		//System.out.println("FINE (StreamParser) consume"); //CANCELLA
	}

	private void unexpectedTokenError() throws ParserException {
		throw new ParserException("Unexpected token " + tokenizer.tokenType() + "('" + tokenizer.tokenString() + "')");
	}

	public StreamParser(Tokenizer tokenizer) {
		//System.out.println("(StreamParser) costruttore"); //CANCELLA
		this.tokenizer = tokenizer;
	}

	@Override
	public Prog parseProg() throws ParserException {
		/*System.out.println("INIZIO (StreamParser) ParseProg"); //CANCELLA
		System.out.println("	chiamo tryNext"); //CANCELLA*/
		tryNext(); // one look-ahead symbol
		Prog prog = new ProgClass(parseStmtSeq());
		//System.out.println("	chiamo match"); //CANCELLA
		match(EOF);
		//System.out.println("FINE (StreamParser) ParseProg"); //CANCELLA
		return prog;
	}

	private StmtSeq parseStmtSeq() throws ParserException {
		//System.out.println("INIZIO (StreamParser) ParseStmtSeq"); //CANCELLA
		Stmt stmt = parseStmt();
		System.out.println("	stmt: "+stmt); //CANCELLA
		if (tokenizer.tokenType() == STMT_SEP) {
			//System.out.println("	chiama tryNext"); //CANCELLA
			tryNext();
			//System.out.println("FINE (StreamParser) ParseStmtSeq more"); //CANCELLA
			return new MoreStmt(stmt, parseStmtSeq());
		}
		//System.out.println("FINE (StreamParser) ParseStmtSeq single"); //CANCELLA
		return new SingleStmt(stmt);
	}

	private ExpSeq parseExpSeq() throws ParserException {
		//System.out.println("INIZIO (StreamParser) ParseExpSeq"); //CANCELLA
		Exp exp = parseExp();
		//System.out.println("	exp: "+exp); //CANCELLA
		if (tokenizer.tokenType() == EXP_SEP) {
			//System.out.println("	chiama tryNext"); //CANCELLA
			tryNext();
			//System.out.println("FINE (StreamParser) ParseExpSeq more"); //CANCELLA
			return new MoreExp(exp, parseExpSeq());
		}
		//System.out.println("FINE (StreamParser) ParseExpSeq single"); //CANCELLA
		return new SingleExp(exp);
	}

	private Stmt parseStmt() throws ParserException {
		//System.out.println("INIZIO (StreamParser) ParseStmt"); //CANCELLA
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case PRINT:
			return parsePrintStmt();
		case VAR:
			return parseVarStmt();
		case IDENT:
			return parseAssignStmt();
		case FOR:
			return parseForEachStmt();
		/*fatto da me inizio*/
		case IF:
			System.out.println("             caso if");
			return parseIfElseStmt();
		case DO:
			System.out.println("             caso do");

			return parseDoWhileStmt();
		/*fatto da me fine*/
		}
	}

	private PrintStmt parsePrintStmt() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parsePrintStmt");
		//System.out.println("	chiamo consume con PRINT");
		consume(PRINT); // or tryNext();
		//System.out.println("FINE (StreamParser) parsePrintStamt");
		return new PrintStmt(parseExp());
	}

	private VarStmt parseVarStmt() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parseVarStmt");
		//System.out.println("	chiamo consume con VAR");
		consume(VAR); // or tryNext();
		//System.out.println("	chiamo parseIdent");
		Ident ident = parseIdent();
		//System.out.println("	ident: "+ident);
		//System.out.println("	chiamo consume con ASSIGN");
		consume(ASSIGN);
		//System.out.println("FINE (StreamParser) parseVarStmt che chiama parseExp");
		return new VarStmt(ident, parseExp());
	}

	private AssignStmt parseAssignStmt() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parseAssignStmt");
		//System.out.println("	chiamo parseIdent");
		Ident ident = parseIdent();
		//System.out.println("	ident: "+ident);
		//System.out.println("	chiamo consume con ASSIGN");
		consume(ASSIGN);
		//System.out.println("FINE (StreamParser) parseAssignStmt che chiama parseExp");
		return new AssignStmt(ident, parseExp());
	}

	private ForEachStmt parseForEachStmt() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parseForEachStmt");
		//System.out.println("	chiamo consume con FOR");
		consume(FOR); // or tryNext();
		//System.out.println("	chiamo parseIdent()");
		Ident ident = parseIdent();
		//System.out.println("	ident: "+ident);
		//System.out.println("	chiamo consume con IN");
		consume(IN);
		//System.out.println("	chiamo parseExp");
		Exp exp = parseExp();
		//System.out.println("	exp: "+exp);
		//System.out.println("	chiamo consume con OPEN_BLOCK");
		consume(OPEN_BLOCK);
		//System.out.println("	chiamo patseStmtSeq()");
		StmtSeq stmts = parseStmtSeq();
		//System.out.println("	stmts: "+stmts);
		//System.out.println("	chiamo consume con CLOSE_BLOCK");
		consume(CLOSE_BLOCK);
		//System.out.println("FINE (StreamParser) parseForEachStmt");
		return new ForEachStmt(ident, exp, stmts);
	}
	//fatto da me inizio  modifica:da fare
	// 'if' '('Exp')' '{'StmtSeq'}' ('else' '{' StmtSeq '}')?
	private IfThenStmt parseIfElseStmt() throws ParserException {
		System.out.println("INIZIO parseIfElseStmt");
		consume(IF); // or tryNext();
		consume(OPEN_PAR);
//		System.out.println("       chiamo parseexp");
		Exp exp = parseExp();
		System.out.println("      consume close:par");
		consume(CLOSE_PAR);
		consume(OPEN_BLOCK);
		StmtSeq stmts = parseStmtSeq();
		consume(CLOSE_BLOCK);
		/*if(tokenizer.tokenType() == ELSE) {
			tryNext();
			consume(OPEN_BLOCK);
			StmtSeq stmts2 = parseStmtSeq();
			consume(CLOSE_BLOCK);
			return new IfElseStmt(exp, stmts, stmts2);
		}*/
		return new IfThenStmt(exp, stmts);
	}
	
	//'do' '{' StmtSeq '}' 'while' '('Exp')'
	private DoWhileStmt parseDoWhileStmt() throws ParserException {
		consume(DO);
		consume(OPEN_BLOCK);
		StmtSeq stmts = parseStmtSeq();
		consume(CLOSE_BLOCK);
		consume(WHILE);
		consume(OPEN_PAR);
		Exp exp = parseExp();
		consume(CLOSE_PAR);
		return new DoWhileStmt(stmts,exp);
		
	}
	// fatto da me inizio

	private Exp parseExp() throws ParserException {
//		System.out.println("INIZIO (StreamParser) parseExp");
//		System.out.println("	chiamo parseAdd");
		Exp exp = parseEquality();
		System.out.println("	exp: "+exp);
		if (tokenizer.tokenType() == LOGICAND) {
//			System.out.println("	chiamo tryNext");
			tryNext();
//			System.out.println("	chiamo parseExp");
			exp = new LogicAnd(exp, parseEquality());
		}
//		System.out.println("FINE (StreamParser) parseExp exp di tipo Prefix: "+ exp);
		return exp;
	}

	private Exp parseEquality() throws ParserException {
		Exp exp = parsePrefix();
		while (tokenizer.tokenType() == EQUALITY) {
			tryNext();
			exp = new Equality(exp, parsePrefix());
		}
		return exp;
	}
	
	private Exp parsePrefix() throws ParserException {
		Exp exp = parseAdd();
		while (tokenizer.tokenType() == PREFIX) {
			tryNext();
			exp = new Prefix(exp, parseAdd());
		}
		return exp;
	}
// fatto da me fine
	private Exp parseAdd() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parseAdd");
		//System.out.println("	chiamo parseMul");
		Exp exp = parseMul();
		//System.out.println("	exp: "+exp);
		while (tokenizer.tokenType() == PLUS) {
			//System.out.println("	tokenizer.tokenType(): "+tokenizer.tokenType());
			//System.out.println("	chiamo tryNext");
			tryNext();
			//System.out.println("	chiamo parseMul");
			exp = new Add(exp, parseMul());
		}
		//System.out.println("FINE (StreamParser) parseAdd exp di tipo Add: "+exp);
		return exp;
	}

	private Exp parseMul() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parseMul");
		//System.out.println("	chiamo parseAtom");
		Exp exp = parseAtom();
		//System.out.println("exp: "+exp);
		while (tokenizer.tokenType() == TIMES) {
			//System.out.println("	tokenizer.tokenType(): "+tokenizer.tokenType());
			//System.out.println("	chiamo trynext");
			tryNext();
			//System.out.println("	chiamo parseAtom()");
			exp = new Mul(exp, parseAtom());
		}
		//System.out.println("FINE (StreamParser) parseMul exp di tipo Mul: "+exp);
		return exp;
	}

	private Exp parseAtom() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parseAtom");
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case NUM:
			//System.out.println("FINE (StreamParser) ParseAtom caso NUM"); //CANCELLA
			//System.out.println("    chiamo parseNum");
			return parseNum();
		case IDENT:
			//System.out.println("FINE (StreamParser) ParseAtom caso IDENT"); //CANCELLA
			//System.out.println("    chiamo parseIdent");
			return parseIdent();
		/*fatto da me inizio*/ //'!' Atom | 'opt' Atom | 'empty' Atom | 'def' Atom | 'get' Atom 
		case NOT:
			return parseNot();
		case OPT:
			return parseOpt();
		case EMPTY:
			return parseEmpty();
		case DEF:
			return parseDef();
		case GET:
			return parseGet();
		/*fatto da me fine*/
		case MINUS:
			//System.out.println("FINE (StreamParser) ParseAtom caso MINUS"); //CANCELLA
			//System.out.println("    chiamo parseMinus");
			return parseMinus();
		case OPEN_LIST:
			//System.out.println("FINE (StreamParser) ParseAtom caso OPEN_LIST"); //CANCELLA
			//System.out.println("    chiamo parseList");
			return parseList();
		case OPEN_PAR:
			//System.out.println("FINE (StreamParser) ParseAtom caso OPEN_PAR"); //CANCELLA
			//System.out.println("    chiamo parseRoundPar");
			return parseRoundPar();
		}
	}

	private IntLiteral parseNum() throws ParserException {
		//System.out.println("INIZIO (StreamParser) ParseNUM "); //CANCELLA
		//System.out.println("	guardo cosa c'è dentro tokenizer.intValue();"); //CANCELLA
		int val = tokenizer.intValue();
		//System.out.println("	val: "+val); //CANCELLA
		//System.out.println("     chiamo consume con NUM");
		consume(NUM); // or tryNext();
		//System.out.println("FINE (StreamParser) parseNum");
		return new IntLiteral(val);
	}

	private Ident parseIdent() throws ParserException {
		//System.out.println("INIZIO (StreamParser) ParseIdent "); //CANCELLA
		//System.out.println("	guardo cosa c'è dentro tikenizer.tokenString"); //CANCELLA
		String name = tokenizer.tokenString();
		//System.out.println("	name: "+name); //CANCELLA
		//System.out.println("	chiamo consume con IDENT"); //CANCELLA
		consume(IDENT); // or tryNext();
		//System.out.println("FINE (StreamParser) ParseNUM "); //CANCELLA
		return new SimpleIdent(name);
	}
	
	/*fatto da me inizio*/
	private Not parseNot() throws ParserException {
		consume(NOT);
		return new Not(parseAtom()); //controlla: perchè parse atom??
									//perchè se devo usare operatore binario devo usare le parentesi (per le precedenze) che sono dentro ad atom
	}
	
	private Opt parseOpt() throws ParserException {
		consume(OPT);
		return new Opt(parseAtom());
	}
	
	private Empty parseEmpty() throws ParserException {
		consume(EMPTY);
		return new Empty(parseAtom());
	}
	
	private Def parseDef() throws ParserException {
		consume(DEF);
		return new Def(parseAtom());
	}
	
	private Get parseGet() throws ParserException {
		consume(GET);
		return new Get(parseAtom());
	}
	/*fatto da me fine*/
	private Sign parseMinus() throws ParserException {
		//System.out.println("INIZIO (StreamParser) parseMinus "); //CANCELLA
		//System.out.println("	chiamo consume con MINUS"); //CANCELLA
		consume(MINUS); // or tryNext();
		//System.out.println("FINE (StreamParser) ParseMinus ritorna un Sign(parseAtom())"); //CANCELLA
		return new Sign(parseAtom());
	}

	private ListLiteral parseList() throws ParserException {
		//System.out.println("INIZIO (StreamParser) ParseList "); //CANCELLA
		//System.out.println("	chiamo consume con OPEN_LIST"); //CANCELLA
		consume(OPEN_LIST); // or tryNext();
		//System.out.println("	chiamo parseExpseq"); //CANCELLA
		ExpSeq exps = parseExpSeq();
		//System.out.println("	exps: "+exps); //CANCELLA
		//System.out.println("	chiamo consume con CLOSE_LIST"); //CANCELLA
		consume(CLOSE_LIST);
		//System.out.println("FINE (StreamParser) ParseList "); //CANCELLA
		return new ListLiteral(exps);
	}

	private Exp parseRoundPar() throws ParserException {
		//System.out.println("INIZIO (StreamParser) ParseRoundPar "); //CANCELLA
		//System.out.println("	chiamo consume con OPEN_PAR"); //CANCELLA
		consume(OPEN_PAR); // or tryNext();
		//System.out.println("	chiamo parseExp"); //CANCELLA
		Exp exp = parseExp();
		//System.out.println("	exp: "+exp); //CANCELLA
		//System.out.println("	chiamo consume con CLOSE_PAR"); //CANCELLA
		consume(CLOSE_PAR);
		//System.out.println("FINE (StreamParser) ParseRoundPar "); //CANCELLA
		return exp;
	}

}
