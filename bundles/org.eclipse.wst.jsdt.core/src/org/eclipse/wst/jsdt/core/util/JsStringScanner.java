/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.util;

/**
 * Scanner to parse JavaScript string as it is written in JavaSript source
 * code like {@code "aaa\n"} or {@code 'aaa\n"\ u{58}"'}. It's used in
 * {@link StringLiteral} to convert JavaScript string into native Java String
 *
 * @since 2.0
 */
public class JsStringScanner {

	/**
	 * JavaScript string source to scan
	 */
	private String source;

	/**
	 * Current scanner position
	 */
	private int index;

	/**
	 * Source length
	 */
	private int length;

	/**
	 * Current read symbol
	 */
	private int current;

	/**
	 * Output accumulator
	 */
	private StringBuilder result = new StringBuilder();

	/**
	 * Create new Scanner
	 *
	 * @param source
	 *            JavaScript string to scan
	 */
	public JsStringScanner(String source) {
		setSource(source);
	}

	/**
	 * Set JavaScript string to parse
	 *
	 * @param source
	 *            JavaScript string to scan
	 */
	public void setSource(String source) {
		this.source = source;
		index = 0;
		length = source.length();
		result.setLength(0);
	}

	/**
	 * Scans JavaScript string set in the constructor or via
	 * {@link #setSource(String)}
	 *
	 * @exception IllegalArgumentException
	 *                in case of parse error
	 * @return literal representation of the scanned JavaScript string
	 */
	public String scan() {
		read();
		int quote = current;
		if (quote != '"' && quote != '\'') {
			error();
		}
		read();
		while (!isEof()) {
			if (current == quote) {
				return result.toString();
			}
			else if (current == '\\') {
				scanEscape();
			}
			else if (isEol()) {
				error();
			}
			else {
				append();
				read();
			}
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Scan Escape Sequence
	 *
	 * @exception IllegalArgumentException
	 *                in case of parse error
	 */
	private void scanEscape() {
		read();
		checkEof();
		if (isEol()) {
			int ch = current;
			read();
			if (ch == '\r' && current == '\n') {
				read();
			}
		}
		else {
			switch (current) {
				case 'n' :
					append('\n');
					read();
					break;
				case 'r' :
					append('\r');
					read();
					break;
				case 't' :
					append('\t');
					read();
					break;
				case 'u' :
					read();
					scanUnicodeEscape();
					break;
				case 'x' :
					read();
					scanHexDigits(2);
					break;
				case 'b' :
					append('\b');
					read();
					break;
				case 'f' :
					append('\f');
					read();
					break;
				case 'v' :
					append("\u000B"); //$NON-NLS-1$
					read();
					break;
				default :
					if (current >= '0' && current <= '7') {
						int code = 0;
						for (int toRead = (current >= '0' && current <= '3') ? 3 : 2; toRead > 0 && current >= '0' && current <= '7'; toRead--) {
							code = (code << 3) + (current - '0');
							read();
							checkEof();
						}
						append(codePointToString(code));
					}
					else if (current == '8' || current == '9') {
						error();
					}
					else {
						append();
						read();
					}
			}
		}
	}

	/**
	 * Scan unicode escape sequence
	 *
	 * @exception IllegalArgumentException
	 *                in case of parse error
	 */
	private void scanUnicodeEscape() {
		checkEof();
		if (current == '{') {
			read();
			scanHexDigits();
			if (current != '}') {
				error();
			}
			read();
		}
		else {
			scanHexDigits(4);
		}
	}

	/**
	 * Scans arbitrary number of hex digits and adds corresponding unicode
	 * symbol to output
	 *
	 * @exception IllegalArgumentException
	 *                in case of parse error
	 */
	private void scanHexDigits() {
		int hexDigits = 0;
		while (!isEof()) {
			int hex = hexValue(true);
			if (hex == -1) {
				break;
			}
			hexDigits = (hexDigits << 4) | hex;
			if (hexDigits > 0x10FFFF) {
				error();
			}
			read();
		}
		append(codePointToString(hexDigits));
	}

	/**
	 * Scans defined number of hex digits and adds corresponding unicode
	 * symbol to output.
	 *
	 * @exception IllegalArgumentException
	 *                in case of parse error
	 * @param number
	 *            expected number of digits
	 */
	private void scanHexDigits(int number) {
		int hex = 0;
		for (int i = 0; i < number; i++, read()) {
			hex = (hex << 4) | hexValue(false);
		}
		append(codePointToString(hex));
	}

	/**
	 * Scans one hex digit and returns value. In case of error if
	 * {@code optional = true} then {@code -1} is returned else
	 * {@code IllegalArgumentException} is thrown
	 *
	 * @exception IllegalArgumentException
	 *                in case of parse error
	 * @param optional
	 *            is digit optional
	 * @return value of scanned hex digit or {@code -1} if error is detected
	 *         and {@code optional = true}
	 */
	private int hexValue(boolean optional) {
		if (current >= '0' && current <= '9') {
			return current - '0';
		}
		else if (current >= 'a' && current <= 'f') {
			return 10 + (current - 'a');
		}
		else if (current >= 'A' && current <= 'F') {
			return 10 + (current - 'A');
		}
		if (optional) {
			return -1;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Reads one symbol, makes it available in {@link #current}
	 */
	private void read() {
		if (index < length) {
			current = source.charAt(index++);
		}
		else {
			current = -1;
		}
	}

	/**
	 * Appends current symbol to the output
	 */
	private void append() {
		result.append((char) current);
	}

	/**
	 * Appends given symbol to output
	 *
	 * @param ch
	 *            symbol to add to the output
	 */
	private void append(char ch) {
		result.append(ch);
	}

	/**
	 * Appends given string to output
	 *
	 * @param s
	 *            string to add to the output
	 */
	private void append(String s) {
		result.append(s);
	}

	/**
	 * Signal error
	 */
	private static void error() {
		throw new IllegalArgumentException();
	}

	/**
	 * Check if unexpected end of input is reached
	 */
	private void checkEof() {
		if (isEof()) {
			error();
		}
	}

	/**
	 * Returns {@code true} if current symbol is one of the line terminators
	 *
	 * @return
	 */
	private boolean isEol() {
		return (current == '\r') || (current == '\n') || (current == '\u2028') || (current == '\u2029');
	}

	/**
	 * Returns {@code true} if end of input is reached
	 *
	 * @return
	 */
	private boolean isEof() {
		return (current == -1);
	}

	/**
	 * Creates unicode symbol from given code-point
	 *
	 * @param cp
	 *            code-point to create unicode symbol from
	 * @return unicode symbol
	 */
	private static String codePointToString(int cp) {
		if (cp <= 0xFFFF) {
			return Character.toString((char) cp);
		}
		return new String(new int[]{cp}, 0, 1);
	}

}
