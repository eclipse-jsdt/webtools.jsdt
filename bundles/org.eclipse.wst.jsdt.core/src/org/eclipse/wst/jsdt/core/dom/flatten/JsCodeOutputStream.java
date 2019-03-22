/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom.flatten;

/**
 * Stream to write source code emitted from program elements.
 *
 * It takes care about optional semicolons and necessary spaces between tokens
 * and operators. Derived classes must implement <code>put</code> function.
 *
 * @author Eugene Melekhov
 * @since 2.0
 *
 */
@SuppressWarnings("nls")
public abstract class JsCodeOutputStream {

	/**
	 * Last written character
	 */
	protected char lastChar = (char) -1;

	/**
	 * Was optional semicolon "written"
	 */
	protected boolean optionalSemicolon;

	/**
	 * Raw output mode. In this mode write would not try to insert spaces,
	 * optional semicolons etc.
	 */
	protected int rawMode;

	/**
	 * Write token. Takes care about necessary spaces between
	 * keywords/identifiers and some operators
	 *
	 * @param token
	 *            token to write
	 */
	public void write(String token) {
		if (token.isEmpty()) {
			return;
		}

		if (this.optionalSemicolon) {
			this.optionalSemicolon = false;
			if (!token.equals("}")) {
				put(";");
				lastChar = ';';
			}
		}

		if (rawMode == 0) {
			char firstChar = token.charAt(0);
			// Don't allow operators and/or identifiers to be glued
			if (((lastChar == '+' || lastChar == '-') && lastChar == firstChar) || (isIdentifierChar(lastChar) && isIdentifierChar(firstChar))
				|| (lastChar == '/' && (firstChar == 'i' || firstChar == '/'))) {
				put(" ");
			}
		}
		put(token);
		this.lastChar = token.charAt(token.length() - 1);
	}

	/**
	 * Write semicolon
	 */
	public void writeSemicolon() {
		write(";");
	}

	/**
	 * Write optional semicolon
	 */
	public void writeOptionalSemicolon() {
		this.optionalSemicolon = true;
	}

	/**
	 * Check if character can be met in identifier
	 *
	 * @param ch
	 *            character to check
	 * @return <code>true</code> if character may be part of the identifier
	 */
	private static boolean isIdentifierChar(int ch) {

		// Simple case
		if (ch < 0x80) {
			return (ch >= 0x61 && ch <= 0x7A // a..z
						|| ch >= 0x41 && ch <= 0x5A // A..Z
						|| ch >= 0x30 && ch <= 0x39 // 0..9
						|| ch == 0x24 || ch == 0x5F); // dollar and
														// underscore;
		}

		// Unicode case
		switch (Character.getType(ch)) {
			case Character.UPPERCASE_LETTER :
			case Character.LOWERCASE_LETTER :
			case Character.TITLECASE_LETTER :
			case Character.MODIFIER_LETTER :
			case Character.OTHER_LETTER :
			case Character.NON_SPACING_MARK :
			case Character.COMBINING_SPACING_MARK :
			case Character.DECIMAL_DIGIT_NUMBER :
			case Character.LETTER_NUMBER :
			case Character.CONNECTOR_PUNCTUATION :
				return true;
			default :
				return ch == 0x200C || ch == 0x200D;
		}
	}

	/**
	 * Write string to output
	 *
	 * @param s
	 *            string to write
	 */
	protected abstract void put(String s);

	/**
	 *
	 */
	public void setRawMode() {
		rawMode++;
	}

	/**
	 *
	 */
	public void unsetRawMode() {
		rawMode--;
	}

}
