package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual=new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(EOF, 0, 25, ""));
	}
	
	@Test
	public void testKWsToID(){
		runtest("Boolean",
				new Token(ID, 0, 0, "Boolean"),
				new Token(EOF, 0, 7, ""));
	}
	
	@Test
	public void testKWsToIDA(){
		runtest("\"Boolean\"",
				new Token(STRING_LITERAL, 0, 0, "Boolean"),
				new Token(EOF, 0, 9, ""));
	}
	
	@Test
	public void testIDA(){
		runtest("cz_3007",
				new Token(ID, 0, 0, "cz_3007"),
				new Token(EOF, 0, 7, ""));
	}
	
	@Test
	public void testIDB(){
		runtest("CZ_3007",
				new Token(ID, 0, 0, "CZ_3007"),
				new Token(EOF, 0, 7, ""));
	}
	
	@Test
	public void testIDErrorA(){
		runtest("_cz3007",
				(Token)null,
				new Token(ID, 0, 1, "cz3007"),
				new Token(EOF, 0, 7, ""));
	}
	
	@Test
	public void testIDErrorB(){
		runtest("0_cz3007",
				new Token(INT_LITERAL, 0, 0, "0"),
				(Token)null,
				new Token(ID, 0, 2, "cz3007"),
				new Token(EOF, 0, 8, ""));
	}
	
	@Test
	public void testIntLitreal(){
		runtest("1234",
				new Token(INT_LITERAL, 0, 0, "1234"),
				new Token(EOF, 0, 4, "")
				);
	}
	
	@Test
	public void testIntliteralSuperfluousZero(){
		runtest("001",
				new Token(INT_LITERAL, 0, 0, "001"),
				new Token(EOF, 0, 3, "")
				);
	}
	
	@Test
	public void testIntliteralSigned(){
		runtest("-1",
				new Token(MINUS, 0, 0, "-"),
				new Token(INT_LITERAL, 0, 1, "1"),
				new Token(EOF, 0, 2, "")
				);
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				(Token)null,
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"", 
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}
	
//	@Test
//	public void testStringLiteralA() {
//		runtest("\"a\n\"", 
//				//new Token(STRING_LITERAL, 0, 0, "a\n"),
//				(Token)null,
//				new Token(EOF, 0, 4, ""));
//	}
	
	@Test
	public void testStringLiteralA2() {
		runtest("\"a\"\n", 
				new Token(STRING_LITERAL, 0, 0, "a"),
				(Token)null,
				new Token(EOF, 0, 4, ""));
	}
	
//	@Test
//	public void testStringLiteralA3() {
//		runtest("\"\n\"", 
//				new Token(STRING_LITERAL, 0, 0, ""),
//				(Token)null,
//				new Token(EOF, 0, 3, ""));
//	}
	
	@Test
	public void testADeclaration(){
		runtest(
				"return 666;",
				new Token(RETURN, 0, 0, "return"),
				new Token(INT_LITERAL, 0, 7, "666"),
				new Token(SEMICOLON, 0, 10, ";"),
				new Token(EOF, 0, 11, "")
				);
	}
	

}
