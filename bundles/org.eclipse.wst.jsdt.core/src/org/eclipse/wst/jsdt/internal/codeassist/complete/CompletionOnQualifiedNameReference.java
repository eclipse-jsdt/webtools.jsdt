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
 * reduce a qualified name reference containing the completion identifier.
 * e.g.
 *
 *	class X {
 *    Y y;
 *    void foo() {
 *      y.fred.ba[cursor]
 *    }
 *  }
 *
 *	---> class X {
 *         Y y;
 *         void foo() {
 *           <CompleteOnName:y.fred.ba>
 *         }
 *       }
 *
 * The source range of the completion node denotes the source range
 * which should be replaced by the completion.
 */

import org.eclipse.wst.jsdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemFieldBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class CompletionOnQualifiedNameReference extends QualifiedNameReference {
	public char[] completionIdentifier;
	public boolean isInsideAnnotationAttribute;
public CompletionOnQualifiedNameReference(char[][] previousIdentifiers, char[] completionIdentifier, long[] positions, boolean isInsideAnnotationAttribute) {
	super(previousIdentifiers, positions, (int) (positions[0] >>> 32), (int) positions[positions.length - 1]);
	this.completionIdentifier = completionIdentifier;
	this.isInsideAnnotationAttribute = isInsideAnnotationAttribute;
}
public StringBuffer printExpression(int indent, StringBuffer output) {

	output.append("<CompleteOnName:"); //$NON-NLS-1$
	for (int i = 0; i < tokens.length; i++) {
		output.append(tokens[i]);
		output.append('.');
	}
	output.append(completionIdentifier).append('>');
	return output;
}
public TypeBinding resolveType(BlockScope scope) {
	// it can be a package, type, member type, local variable or field
	binding = scope.getBinding(tokens, this);
	if (!binding.isValidBinding()) {
		if (binding instanceof ProblemFieldBinding) {
			scope.problemReporter().invalidField(this, (FieldBinding) binding);
		} else if (binding instanceof ProblemReferenceBinding) {
			scope.problemReporter().invalidType(this, (TypeBinding) binding);
		} else {
			scope.problemReporter().unresolvableReference(this, binding);
		}

		if (binding.problemId() == ProblemReasons.NotFound) {
			throw new CompletionNodeFound(this, binding, scope);
		}

		throw new CompletionNodeFound();
	}

	throw new CompletionNodeFound(this, binding, scope);
}


public boolean isSpecialNode()
{
	return true;
}
}
