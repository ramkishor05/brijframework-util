package org.brijframework.util.printer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import org.brijframework.util.reflect.InstanceUtil;

public class GraphParser {

	private StreamTokenizer	tokenizer;
	private File			sourceFile;
	private FileReader		fileReader;
	private char			commentChar	= '#';
	static GraphParser parser;
	Object object;

	public static GraphParser getParser(String  path) {
		parser = (GraphParser) InstanceUtil.getSingletonInstance(GraphParser.class,new File(path));
		parser.sourceFile =new File(path);
		parser.init();
		return parser;
	}
	
	public static GraphParser getParser(File path) {
		parser = (GraphParser) InstanceUtil.getSingletonInstance(GraphParser.class,path);
		parser.sourceFile =path;
		parser.init();
		return parser;
	}
	
	
	private void init() {
		isTokenizable(sourceFile);
	}

	public GraphParser(File sourceFile) {
		isTokenizable(sourceFile);
	}

	public GraphParser(File sourceFile, char commentChar) {
		this.commentChar = commentChar;
		isTokenizable(sourceFile);
	}

	public GraphParser(String aString) {
		if (!isTokenizable(new File(aString))) {
			tokenizer = new StreamTokenizer(new StringReader(aString));
		}
	}

	public GraphParser(String aString, char commentChar) {
		this.commentChar = commentChar;
		if (!isTokenizable(new File(aString))) {
			tokenizer = new StreamTokenizer(new StringReader(aString));
		}
	}

	public GraphParser(StringBuffer strBuf) {
		tokenizer = new StreamTokenizer(new StringReader(strBuf.toString()));
	}

	public GraphParser(StringBuffer strBuf, char commentChar) {
		this.commentChar = commentChar;
		tokenizer = new StreamTokenizer(new StringReader(strBuf.toString()));
	}

	// Important public methods

	public Hashtable<?,?> getHashtable() {
		streamTokenInitializer();
		Object val = makeObjectCumExceptionCatcher();
		if (val instanceof Hashtable) {
			return (Hashtable<?,?>) val;
		}
		else {
			throw new RuntimeException("Invalid file");
		}
	}

	public Collection<?> getCollection() {
		streamTokenInitializer();
		Object val = makeObjectCumExceptionCatcher();
		if (val instanceof Collection<?>) {
			return  (Collection<?>) val;
		}
		else {
			throw new RuntimeException("Invalid file");
		}
	}

	private Object makeObjectCumExceptionCatcher() {
		try {
			Object obje = makeObject();
			if (fileReader != null) {
				fileReader.close();
			}
			this.sourceFile = null;
			return obje;
		}
		catch (IOException exc) {
			exc.printStackTrace();
			throw new RuntimeException("Unexpected IOException thrown");
		}
	}

	private Object makeObject() throws IOException {
		int c = tokenizer.nextToken();
		switch (c) {
			case '"':
			case '\'':
			case StreamTokenizer.TT_WORD:
				System.out.println("tokenizer.sval="+tokenizer.sval);
				return "";
			case StreamTokenizer.TT_NUMBER:
				double d = tokenizer.nval;
				if (Math.floor(d) == d) {
					return new Integer((int) d);
				}
				else {
					return new Double(d);
				}
			case '{':
				tokenizer.pushBack();
				return makeDictionary();
			case '[':
				tokenizer.pushBack();
				return makeCollection();
			default:
				throw saParserException(tokenizer.lineno(), ' ');
		}
	}

	private Hashtable<?,?> makeDictionary() throws IOException {
		Hashtable<Object,Object> hash = new Hashtable<>();
		int c = tokenizer.nextToken();

		if (c != '{') {
			throw saParserException(tokenizer.lineno(), '{');
		}

		while (true) {
			if (endOfElement('}')) {
				break;
			}

			Object key = makeObject();
			if (tokenizer.nextToken() != '=') {
				throw saParserException(tokenizer.lineno(), '=');
			}

			Object value = makeObject();
			if (tokenizer.nextToken() != ';') {
				throw saParserException(tokenizer.lineno(), ';');
			}
			if (value.getClass().equals(String.class) && ((String) value).toLowerCase().equals("no")) {
				value = Boolean.FALSE;
			}
			if (value.getClass().equals(String.class) && ((String) value).toLowerCase().equals("yes")) {
				value = Boolean.TRUE;

			}
			hash.put(key, value);
		}
		return hash;
	}
	
	public Object getObject(){
		return this.object;
	}

	private Collection<?>  makeCollection() throws IOException {
		Collection<Object> vector = new ArrayList<>();

		int c = tokenizer.nextToken();
		if (c != '[') {
			throw saParserException(tokenizer.lineno(), ']');
		}

		while (true) {
			if (endOfElement(']')) {
				break;
			}
			Object element = makeObject();
			vector.add(element);
			if (endOfElement(']')) {
				break;
			}
			if (tokenizer.nextToken() != ',') {
				throw saParserException(tokenizer.lineno(), ',');
			}
		}
		return vector;
	}

	private void streamTokenInitializer() {
		tokenizer.resetSyntax();
		tokenizer.whitespaceChars(0, 32);
		tokenizer.wordChars(33, 255);
		tokenizer.commentChar(this.commentChar);
		tokenizer.quoteChar('\'');
		tokenizer.quoteChar('"');

		tokenizer.ordinaryChar('=');
		tokenizer.ordinaryChar(',');
		tokenizer.ordinaryChar(';');

		tokenizer.ordinaryChar('{');
		tokenizer.ordinaryChar('}');
		tokenizer.ordinaryChar('(');
		tokenizer.ordinaryChar(')');
		tokenizer.ordinaryChar('[');
		tokenizer.ordinaryChar(']');
		tokenizer.ordinaryChar('<');
		tokenizer.ordinaryChar('>');

		tokenizer.eolIsSignificant(false);
		tokenizer.parseNumbers();
	}

	
	private boolean endOfElement(char endChar) throws IOException {
		if (tokenizer.nextToken() == endChar) {
			return true;
		}
		else {
			tokenizer.pushBack();
			return false;
		}
	}

	@SuppressWarnings("unused")
	private Date makeDate(String date) {
		return org.brijframework.util.casting.DateUtil.dateObject(date);
	}

	@SuppressWarnings("unused")
	private Date makeDateTime(String date) {
		return org.brijframework.util.casting.DateUtil.sqlDateTimeObject(date);
	}


	private boolean isTokenizable(File sourceFile) {
		try {
			if (sourceFile.isFile()) {
				fileReader = new FileReader(sourceFile);
				tokenizer = new StreamTokenizer(fileReader);
				this.sourceFile = sourceFile;
				return true;
			}
			else {
				return false;
			}
		}
		catch (FileNotFoundException exc) {
			throw new RuntimeException("File not found" + sourceFile);
		}
		catch (SecurityException e) {
			throw new RuntimeException("Have Security Exception");
		}
	}

	public void setCommentChar(char commentChar) {
		this.commentChar = commentChar;
	}

	private RuntimeException saParserException(int lineNo, char expectedToken) {
		if (this.sourceFile != null) {
			String fileName = this.sourceFile.getName();
			return new RuntimeException("File " + fileName + " CANNOT be parsed. Unexpected token at line :" + lineNo+ " expecting " + expectedToken);
		}
		return new RuntimeException("Unexpected token at line number: " + lineNo + " expecting " + expectedToken);
	}

	

}
