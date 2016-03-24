/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom.flatten;

import org.eclipse.wst.jsdt.core.dom.ASTNode;

/**
 * Internal ES code generator for presenting an AST in a quick and dirty
 * fashion. For various reasons the resulting string is not necessarily legal
 * ECMAScript code; and even if it is legal ECMAScript code, it is not
 * necessarily the string that corresponds to the given AST. Although useless
 * for most purposes, it's fine for generating debug print strings.
 * <p>
 * Example usage: <code>
 * <pre>
 *    String result = JsCodeGenerator.generate(node);
 * </pre>
 * </code>
 * </p>
 *
 * Provisional API: This class/interface is part of an interim API that is
 * still under development and expected to change significantly before
 * reaching stability. It is being made available at this early stage to
 * solicit feedback from pioneering adopters on the understanding that any
 * code that uses this API will almost certainly be broken (repeatedly) as the
 * API evolves.
 *
 * @since 2.0
 */
public class TrivialJsCodeGenerator {

	/**
	 * Generate JS representation of the given AST node
	 *
	 * @param node
	 *            AST node to generate JS source code
	 * @return generated JS representation of the given AST node
	 */
	public static String generate(ASTNode node) {
		StringBuilder sb = new StringBuilder();
		new JsCodeIRGenerator(new JsCodeElementFactory()).generate(node)
				.emit(new JsCodeStringBuilderOutputStream(sb));
		return sb.toString();
	}

	/**
	 * Append generated JS representation of the given AST node to the given
	 * StringBuilder
	 *
	 * @param node
	 *            AST node to generate JS source code
	 * @param builder
	 *            StringBuilder to add generated JS source code to
	 */
	public static void generate(ASTNode node, StringBuilder builder) {
		new JsCodeIRGenerator(new JsCodeElementFactory()).generate(node).emit(new JsCodeStringBuilderOutputStream(builder));
	}

}
