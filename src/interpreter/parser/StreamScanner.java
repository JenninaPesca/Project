package interpreter.parser;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class StreamScanner implements Scanner {
	private static final Pattern any = Pattern.compile(".*");
	private final Matcher matcher;
	private final BufferedReader buffReader;
	private MatchResult result = Pattern.compile("").matcher("").toMatchResult();

	private void reset(int start, int end, Pattern pat) {
		//System.out.println("INIZIO (StreamScnner) reset con start: "+start+" end: "+end+" pattern "+pat); //CANCELLA
		matcher.region(start, end);
		matcher.usePattern(pat);
		//System.out.println("FINE (StreamScnner) reset"); //CANCELLA
	}

	private String skip() {
		//System.out.println("INIZIO (StreamScnner) skip"); //CANCELLA
		String skipped;
		int regionEnd = matcher.regionEnd();
		Pattern pat = matcher.pattern();
		int end = matcher.find() ? matcher.start() : matcher.regionEnd();
		reset(matcher.regionStart(), end, any);
		matcher.lookingAt();
		skipped = matcher.group();
		//System.out.println("	matcher.group: "+skipped); //CANCELLA
		reset(end, regionEnd, pat);
		//System.out.println("FINE (parseScanner) skip"); //CANCELLA
		return skipped;
	}

	public StreamScanner(String regex, Reader reader) {
		//System.out.println("INIZIO (StreamScanner) costruttore"); //CANCELLA
		matcher = Pattern.compile(regex).matcher("");
		buffReader = new BufferedReader(reader);
		//System.out.println("FINE (streamScanner) costruttore"); //CANCELLA
	}

	@Override
	public void next() throws ScannerException {
//		System.out.println("INIZIO (StreamScnner) next"); //CANCELLA
		if (!hasNext())
			throw new ScannerException("Unexpected end of the stream");
		boolean matched = matcher.lookingAt();
		result = matcher.toMatchResult();
	//	System.out.println(" result(matcher.toMatchResult(): "+result); //CANCELLA
		if (!matched)
			throw new ScannerException("Unrecognized string " + skip());
		else
		//	System.out.println(" 	sposto la regione con inizio: "+matcher.end()); //CANCELLA
			matcher.region(matcher.end(), matcher.regionEnd());
		//System.out.println("FINE (StreamScnner) next"); //CANCELLA
	}

	@Override
	public boolean hasNext() throws ScannerException {
//		System.out.println("INIZIO (StreamScnner) hasnext"); //CANCELLA
		String line;
		if (matcher.regionStart() == matcher.regionEnd()) {
			try {
//				System.out.println(" 	aspetta input"); //CANCELLA
				line = buffReader.readLine();
//				System.out.println(" 	line: "+line); //CANCELLA
			} catch (IOException e) {
				throw new ScannerException(e);
			}
			if (line == null) {
				matcher.reset("");
//				System.out.println("FINE (StreamScnner) hasnext false"); //CANCELLA
				return false;
			}
			matcher.reset(line + " ");
		}
//		System.out.println("FINE (StreamScnner) hasNext true"); //CANCELLA
		return true;
	}

	@Override
	public String group() {
//		System.out.println("(StreamScanner) group"); //CANCELLA
//		System.out.println("	result.group: "+result.group()); //CANCELLA
		return result.group();
	}

	@Override
	public String group(int group) {
//		System.out.println("(StreamScanner) group con intero"); //CANCELLA
//		System.out.println("	"+group); //CANCELLA
		return result.group(group);
	}

	@Override
	public void close() throws ScannerException {
//		System.out.println("(StreamScanner) close"); //CANCELLA
		try {
			buffReader.close();
		} catch (IOException e) {
			throw new ScannerException(e);
		}
		
	}

}




