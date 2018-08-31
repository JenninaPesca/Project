package interpreter;

import static java.lang.System.err;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import interpreter.parser.Parser;
import interpreter.parser.ParserException;
import interpreter.parser.StreamParser;
import interpreter.parser.StreamTokenizer;
import interpreter.parser.Tokenizer;
import interpreter.parser.ast.Prog;
import interpreter.visitors.evaluation.Eval;
import interpreter.visitors.evaluation.EvaluatorException;
import interpreter.visitors.typechecking.TypeCheck;
import interpreter.visitors.typechecking.TypecheckerException;

public class Main {
	public static void main(String[] args) {
//System.out.println("INIZIO del main"); //CANCELLA
		try (Tokenizer tokenizer = new StreamTokenizer(
				args.length > 0 ? new FileReader(args[0]) : new InputStreamReader(System.in))) {
//System.out.println("creo nuovo parser"); //CANCELLA
			Parser parser = new StreamParser(tokenizer);
//System.out.println("creo nuovo prog"); //CANCELLA
			Prog prog = parser.parseProg();
//System.out.println("accept new Typechek"); //CANCELLA
			prog.accept(new TypeCheck());
//System.out.println("accept new eval"); //CANCELLA
			prog.accept(new Eval());
		} catch (ParserException e) {
			err.println("Syntax error: " + e.getMessage());
		} catch (IOException e) {
			err.println("I/O error: " + e.getMessage());
		} catch (TypecheckerException e) {
			err.println("Static error: " + e.getMessage());
		} catch (EvaluatorException e) {
			err.println("Dynamic error: " + e.getMessage());
		} catch (Throwable e) {
			err.println("Unexpected error.");
			e.printStackTrace();
		}
//System.out.println("FINE del main"); //CANCELLA
	}
}
