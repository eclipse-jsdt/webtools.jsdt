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

import java.util.List;

import org.eclipse.wst.jsdt.core.dom.flatten.JsCodeIRGenerator.JsCodeElement;

/**
 * Default factory for creating JsCodeElements.
 *
 * @author Eugene Melekhov
 * @since 2.0
 *
 */
@SuppressWarnings("nls")
public class JsCodeElementFactory implements IJsCodeElementFactory {

	private static JsCodeIRGenerator.JsCodeElement[] EMPTY_SEQ = new JsCodeIRGenerator.JsCodeElement[0];

	private static Semi semicolon = new Semi();

	private static SemiOpt optionalSemicolon = new SemiOpt();

	/**
	 * String token
	 *
	 */
	public static class Token extends JsCodeIRGenerator.JsCodeElement {

		private final String token;

		public Token(String token) {
			this.token = token;
		}

		@Override
		public void emit(JsCodeOutputStream out) {
			out.write(this.token);
		}
	}

	/**
	 *
	 * Semicolon
	 *
	 */
	public static class Semi extends JsCodeIRGenerator.JsCodeElement {

		@Override
		public void emit(JsCodeOutputStream out) {
			out.writeSemicolon();
		}
	}

	/**
	 * Optional semicolon
	 */
	public static class SemiOpt extends JsCodeIRGenerator.JsCodeElement {

		@Override
		public void emit(JsCodeOutputStream out) {
			out.writeOptionalSemicolon();
		}
	}

	/**
	 * Simple wrapper for other element. Emits itself as sequence of <code>start</code> token, element, <code>end</code>
	 * token
	 */
	public static class Wrapper extends JsCodeIRGenerator.JsCodeElement {

		private final JsCodeIRGenerator.JsCodeElement element;
		private final String start;
		private final String end;

		public Wrapper(String start, JsCodeIRGenerator.JsCodeElement element, String end) {
			this.start = start;
			this.element = element;
			this.end = end;
		}

		@Override
		public void emit(JsCodeOutputStream out) {
			out.write(start);
			element.emit(out);
			out.write(end);
		}
	}

	/**
	 *
	 * Element enclosed in parenthesis
	 *
	 */
	public static class Parens extends Wrapper {

		public Parens(JsCodeIRGenerator.JsCodeElement element) {
			super("(", element, ")");
		}
	}

	/**
	 *
	 * Element enclosed in brackets
	 *
	 */
	public class Brackets extends Wrapper {

		public Brackets(JsCodeIRGenerator.JsCodeElement element) {
			super("[", element, "]");
		}
	}

	/**
	 *
	 * Element enclosed in braces
	 *
	 */
	public static class Braces extends Wrapper {

		public Braces(JsCodeIRGenerator.JsCodeElement element) {
			super("{", element, "}");
		}
	}

	/**
	 * Sequence of elements
	 */
	public class Sequence extends JsCodeIRGenerator.JsCodeElement {

		public final JsCodeIRGenerator.JsCodeElement[] elements;

		public Sequence(JsCodeIRGenerator.JsCodeElement[] elements) {
			this.elements = elements;
		}

		@Override
		public void emit(JsCodeOutputStream out) {
			for (JsCodeIRGenerator.JsCodeElement element : elements) {
				if (element != null) {
					element.emit(out);
				}
			}
		}
	}

	/**
	 * Comma separated sequence of elements
	 */
	public class SequenceCs extends Sequence {

		public SequenceCs(JsCodeIRGenerator.JsCodeElement[] elements) {
			super(elements);
		}

		@Override
		public void emit(JsCodeOutputStream out) {
			int i, e;

			// Skip null elements at the beginning
			for (i = 0, e = elements.length; i < e && elements[i] == null; i++) {
			}
			if (i < e) {
				this.elements[i++].emit(out);
				for (; i < e; i++) {
					if (elements[i] != null) {
						out.write(",");
						elements[i].emit(out);
					}
				}
			}
		}
	}

	/**
	 * Sequence of elements
	 */
	public class SequenceRaw extends Sequence {

		public SequenceRaw(JsCodeIRGenerator.JsCodeElement[] elements) {
			super(elements);
		}

		@Override
		public void emit(JsCodeOutputStream out) {
			out.setRawMode();
			super.emit(out);
			out.unsetRawMode();
		}
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement token(String token) {
		return new Token(token);
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement semi() {
		return semicolon;
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement semiOpt() {
		return optionalSemicolon;
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement wrap(String start, JsCodeIRGenerator.JsCodeElement element,
			String end) {
		return new Wrapper(start, element, end);
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement paren(JsCodeIRGenerator.JsCodeElement element) {
		return new Parens(element);
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement brack(JsCodeIRGenerator.JsCodeElement element) {
		return new Brackets(element);
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement braces(JsCodeIRGenerator.JsCodeElement element) {
		return new Braces(element);
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement seq(JsCodeIRGenerator.JsCodeElement[] elements) {
		return new Sequence(elements);
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement seq(List<JsCodeIRGenerator.JsCodeElement> elements) {
		return new Sequence(elements.toArray(EMPTY_SEQ));
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement seqCs(JsCodeIRGenerator.JsCodeElement[] elements) {
		return new SequenceCs(elements);
	}

	@Override
	public JsCodeIRGenerator.JsCodeElement seqCs(List<JsCodeIRGenerator.JsCodeElement> elements) {
		return new SequenceCs(elements.toArray(EMPTY_SEQ));
	}

	@Override
	public JsCodeElement seqRaw(List<JsCodeElement> elements) {
		return new SequenceRaw(elements.toArray(EMPTY_SEQ));
	}

}
