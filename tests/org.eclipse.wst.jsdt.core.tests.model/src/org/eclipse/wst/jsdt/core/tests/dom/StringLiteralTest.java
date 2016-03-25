/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *   Eugene Melekhov - initial implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.tests.dom;

import static org.junit.Assert.assertEquals;

import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.junit.Test;

@SuppressWarnings("nls")
public class StringLiteralTest {

	// ---------------------- Basic simple cases -------------------------------
	@Test
	public void testBasic1() {
		testFromEscape("", "\"\"");
	}

	@Test
	public void testBasic2() {
		testFromEscape("", "''");
	}

	@Test
	public void testBasic3() {
		testFromEscape("a", "'a'");
	}

	@Test
	public void testBasic4() {
		testFromEscape("\"", "\"\\\"\"");
	}

	@Test
	public void testBasic5() {
		testFromEscape("'", "'\\''");
	}

	@Test
	public void testBasic6() {
		testFromEscape("a", "\"a\"");
	}

	@Test
	public void testBasic7() {
		testFromEscape("\"", "'\"'");
	}

	// ----------------------\x 2 hex digits -----------------------------------
	@Test
	public void testHexEscape1() {
		testFromEscape("X", "\"\\x58\"");
	}

	@Test
	public void testHexEscape2() {
		testFromEscape("XY", "\"\\x58Y\"");
	}

	@Test
	public void testHexEscape3() {
		testFromEscape("YX", "\"Y\\x58\"");
	}

	public void testHexEscape4() {
		testFromEscape("X9", "\"\\x589\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHexEscape5() {
		testFromEscape("X", "\"\\x5\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHexEscape6() {
		testFromEscape("X", "\"\\x\"");
	}

	// ------------------- \ u {..} Unicode hex digits ---------------------------

	@Test
	public void testUnicodeNew1() {
		testFromEscape("X", "\"\\u{58}\"");
	}

	@Test
	public void testUnicodeNew2() {
		testFromEscape("X", "\"\\u{058}\"");
	}

	@Test
	public void testUnicodeNew3() {
		testFromEscape("X", "\"\\u{0058}\"");
	}

	@Test
	public void testUnicodeNew4() {
		testFromEscape("XX", "\"\\u{0058}X\"");
	}

	@Test
	public void testUnicodeNew5() {
		testFromEscape("XX", "\"X\\u{0058}\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnicodeNew6() {
		testFromEscape("X", "\"\\u{0058\"");
	}

	// ------------------- \ u... Unicode 4 hex digits ---------------------------
	@Test
	public void testUnicode1() {
		testFromEscape("X", "\"\\u0058\"");
	}

	@Test
	public void testUnicode2() {
		testFromEscape("XX", "\"\\u0058X\"");
	}

	@Test
	public void testUnicode3() {
		testFromEscape("XX", "\"X\\u0058\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnicode4() {
		testFromEscape("X", "\"\\u058\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnicode5() {
		testFromEscape("X", "\"\\u58\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnicode6() {
		testFromEscape("X", "\"\\u8\"");
	}

	// ------------------- \xxx Octal Digits ------------------------------------

	@Test
	public void testOctal1() {
		testFromEscape("X", "\"\\130\"");
	}

	@Test
	public void testOctal2() {
		testFromEscape("YX", "\"Y\\130\"");
	}

	@Test
	public void testOctal3() {
		testFromEscape("XY", "\"\\130Y\"");
	}

	@Test
	public void testOctal4() {
		testFromEscape("\u00FF", "\"\\377\"");
	}

	@Test
	public void testOctal5() {
		testFromEscape("\u001F8", "\"\\378\"");
	}

	@Test
	public void testOctal6() {
		testFromEscape(" 1", "\"\\401\"");
	}

	@Test
	public void testOctal7() {
		testFromEscape("1 1", "\"1\\401\"");
	}

	@Test
	public void testOctal8() {
		testFromEscape("W", "\"\\W\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOctal9() {
		testFromEscape("\\", "\"\\\"");
	}

	// ------------------------------- From Literal ----------------------------

	@Test
	public void testFromLiteral1() {
		testFromLiteral("\0\1\2\3\4\5\6\7\b\n\r\t\u000B\f\u2028\u2029",
				"\"\\0\\1\\2\\3\\4\\5\\6\\7\\b\\n\\r\\t\\v\\f\\u2028\\u2029\"");
	}

	// ------------------------------- All in one ------------------------------
	@Test
	public void testAllInOneFromEscape() {
		testFromEscape("\0\b\n\r\t\u000B\f\\\"'\u2028\u2029日本", "\"\\0\\b\\n\\r\\t\\v\\f\\\\\\\"'\\u2028\\u2029日本\"");
	}

	// ----------------------------- Helper function ---------------------------
	private void testFromEscape(String literalValue, String escapedValue) {
		StringLiteral sl = AST.newAST(AST.JLS3).newStringLiteral();
		sl.setEscapedValue(escapedValue);
		assertEquals(literalValue, sl.getLiteralValue());
	}

	private void testFromLiteral(String literalValue, String escapedValue) {
		StringLiteral sl = AST.newAST(AST.JLS3).newStringLiteral();
		sl.setLiteralValue(literalValue);
		assertEquals(escapedValue, sl.getEscapedValue());
	}

}
