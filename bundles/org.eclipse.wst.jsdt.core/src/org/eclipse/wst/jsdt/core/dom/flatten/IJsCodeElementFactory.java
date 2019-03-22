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

/**
 * Factory of JsCodeElements. Creates basic types of elements such as token,
 * semicolon, optional semicolon, sequence etc
 *
 * @author Eugene Melekhov
 * @since 2.0
 *
 */
public interface IJsCodeElementFactory {

	/**
	 * Create new string token
	 *
	 * @param token
	 *            token value
	 * @return new token
	 */
	JsCodeIRGenerator.JsCodeElement token(String token);

	/**
	 * Create new semicolon
	 *
	 * @return new semicolon
	 */
	JsCodeIRGenerator.JsCodeElement semi();

	/**
	 * Create new optional semicolon
	 *
	 * @return new optional semicolon
	 */
	JsCodeIRGenerator.JsCodeElement semiOpt();

	/**
	 * Create new element that presents given element surrounded by <code>start</code> and <code>end</code> string
	 * tokens
	 *
	 * @param start
	 *            start of the wrapper
	 * @param element
	 *            element to wrap
	 * @param end
	 *            end of the wrapper
	 * @return new wrapper element
	 */
	JsCodeIRGenerator.JsCodeElement wrap(String start, JsCodeIRGenerator.JsCodeElement element, String end);

	/**
	 * Create new element that presents given element enclosed in parenthesis
	 *
	 * @param element
	 *            element to enclose
	 * @return new element that presents given element enclosed in parenthesis
	 */
	JsCodeIRGenerator.JsCodeElement paren(JsCodeIRGenerator.JsCodeElement element);

	/**
	 * Create new element that presents given element enclosed in brackets
	 *
	 * @param element
	 *            element to enclose
	 * @return new element that presents given element enclosed in brackets
	 */
	JsCodeIRGenerator.JsCodeElement brack(JsCodeIRGenerator.JsCodeElement element);

	/**
	 * Create new element that presents given element enclosed in braces
	 *
	 * @param element
	 *            element to enclose
	 * @return new element that presents given element enclosed in braces
	 */
	JsCodeIRGenerator.JsCodeElement braces(JsCodeIRGenerator.JsCodeElement element);

	/**
	 * Creates new sequence from array of elements
	 *
	 * @param elements
	 *            array of elements to include into sequence
	 * @return new sequence element
	 */
	JsCodeIRGenerator.JsCodeElement seq(JsCodeIRGenerator.JsCodeElement[] elements);

	/**
	 * Creates new sequence from list of elements
	 *
	 * @param elements
	 *            list of elements to include into sequence
	 * @return new sequence element
	 */
	JsCodeIRGenerator.JsCodeElement seq(List<JsCodeIRGenerator.JsCodeElement> elements);

	/**
	 * Creates new comma separated sequence from array of elements
	 *
	 * @param elements
	 *            array of elements to include into sequence
	 * @return new sequence element
	 */
	JsCodeIRGenerator.JsCodeElement seqCs(JsCodeIRGenerator.JsCodeElement[] elements);

	/**
	 * Creates new comma separated sequence from list of elements
	 *
	 * @param elements
	 *            list of elements to include into sequence
	 * @return new sequence element
	 */
	JsCodeIRGenerator.JsCodeElement seqCs(List<JsCodeIRGenerator.JsCodeElement> elements);

	/**
	 * Creates new raw sequence from list of elements. Elements of the
	 * sequence are emitted as is without any spaces between them that could
	 * potentially insert output stream
	 *
	 * @param elements
	 *            list of elements to include into sequence
	 * @return new sequence element
	 */
	JsCodeIRGenerator.JsCodeElement seqRaw(List<JsCodeIRGenerator.JsCodeElement> elements);

}
