/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.codeassist.complete;

import org.eclipse.wst.jsdt.internal.compiler.ast.StringLiteral;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ClassScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

/*
 * Completion node build by the parser in any case it was intending to
 * reduce a string literal.
 * e.g.
 *
 *	class X {
 *    void foo() {
 *      String s = "a[cursor]"
 *    }
 *  }
 *
 *	---> class X {
 *         void foo() {
 *           String s = <CompleteOnStringLiteral:a>
 *         }
 *       }
 */

public class CompletionOnStringLiteral extends StringLiteral {
	public int contentStart;
	public int contentEnd;
	public CompletionOnStringLiteral(char[] token, int s, int e, int cs, int ce, int lineNumber) {
		super(token, s, e, lineNumber);
		this.contentStart = cs;
		this.contentEnd = ce;
	}

	public CompletionOnStringLiteral(int s, int e, int cs, int ce) {
		super(s,e);
		this.contentStart = cs;
		this.contentEnd = ce;
	}
	public TypeBinding resolveType(ClassScope scope) {
		throw new CompletionNodeFound(this, null, scope);
	}
	public TypeBinding resolveType(BlockScope scope) {
		throw new CompletionNodeFound(this, null, scope);
	}

	public StringBuffer printExpression(int indent, StringBuffer output) {
		output.append("<CompletionOnString:"); //$NON-NLS-1$
		output = super.printExpression(indent, output);
		return output.append('>');
	}
}
