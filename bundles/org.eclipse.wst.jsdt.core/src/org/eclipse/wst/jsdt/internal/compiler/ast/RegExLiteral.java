/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IRegExLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.impl.StringConstant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;
/**
 * 
 *@deprecated
 */
public class RegExLiteral extends Literal implements IRegExLiteral {

	char[] source;

	public RegExLiteral(char[] token, int start, int end) {

		this(start,end);
		this.source = token;
	}

	public RegExLiteral(int s, int e) {

		super(s,e);
	}

	public void computeConstant() {

		constant = StringConstant.fromValue(String.valueOf(source));
	}

	public TypeBinding literalType(BlockScope scope) {

		return scope.getJavaLangRegExp();
	}

	public StringBuffer printExpression(int indent, StringBuffer output) {

		// handle some special char.....
		for (int i = 0; i < source.length; i++) {
			switch (source[i]) {
//				case '\b' :
//					output.append("\\b"); //$NON-NLS-1$
//					break;
//				case '\t' :
//					output.append("\\t"); //$NON-NLS-1$
//					break;
//				case '\n' :
//					output.append("\\n"); //$NON-NLS-1$
//					break;
//				case '\f' :
//					output.append("\\f"); //$NON-NLS-1$
//					break;
//				case '\r' :
//					output.append("\\r"); //$NON-NLS-1$
//					break;
//				case '\"' :
//					output.append("\\\""); //$NON-NLS-1$
//					break;
//				case '\'' :
//					output.append("\\'"); //$NON-NLS-1$
//					break;
//				case '\\' : //take care not to display the escape as a potential real char
//					output.append("\\\\"); //$NON-NLS-1$
//					break;
				default :
					output.append(source[i]);
			}
		}
		return output;
	}

	public char[] source() {

		return source;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
	public int getASTType() {
		return IASTNode.REG_EX_LITERAL;
	
	}
}
