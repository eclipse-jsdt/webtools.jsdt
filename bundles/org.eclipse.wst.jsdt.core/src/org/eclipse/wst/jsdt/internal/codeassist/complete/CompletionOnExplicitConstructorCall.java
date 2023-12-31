/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.codeassist.complete;

/*
 * Completion node build by the parser in any case it was intending to
 * reduce a explicit constructor call containing the cursor.
 * e.g.
 *
 *	class X {
 *    X() {
 *      this(1, 2, [cursor]
 *    }
 *  }
 *
 *	---> class X {
 *         X() {
 *           <CompleteOnExplicitConstructorCall:this(1, 2)>
 *         }
 *       }
 *
 * The source range is always of length 0.
 * The arguments of the constructor call are all the arguments defined
 * before the cursor.
 */

import org.eclipse.wst.jsdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;

public class CompletionOnExplicitConstructorCall extends ExplicitConstructorCall {

	public CompletionOnExplicitConstructorCall(int accessMode) {
		super(accessMode);
	}

	public StringBuffer printStatement(int tab, StringBuffer output) {

		printIndent(tab, output);
		output.append("<CompleteOnExplicitConstructorCall:"); //$NON-NLS-1$
		if (this.qualification != null) this.qualification.printExpression(0, output).append('.');
		if (this.accessMode == This) {
			output.append("this("); //$NON-NLS-1$
		} else {
			output.append("super("); //$NON-NLS-1$
		}
		if (this.arguments != null) {
			for (int i = 0; i < this.arguments.length; i++) {
				if (i > 0) output.append(", "); //$NON-NLS-1$
				this.arguments[i].printExpression(0, output);
			}
		}
		return output.append(")>;"); //$NON-NLS-1$
	}

	public void resolve(BlockScope scope) {

		ReferenceBinding receiverType = scope.enclosingSourceType();

		if (this.arguments != null) {
			int argsLength = this.arguments.length;
			for (int a = argsLength; --a >= 0;)
				this.arguments[a].resolveType(scope);
		}

		if (this.accessMode != This && receiverType != null) {
			if (receiverType.isHierarchyInconsistent())
				throw new CompletionNodeFound();
			receiverType = receiverType.getSuperBinding();
		}
		if (receiverType == null)
			throw new CompletionNodeFound();
		else
			throw new CompletionNodeFound(this, receiverType, scope);
	}
}
