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
 * reduce a type reference containing the completion identifier as part
 * of a qualified name.
 * e.g.
 *
 *	class X extends java.lang.Obj[cursor]
 *
 *	---> class X extends <CompleteOnType:java.lang.Obj>
 *
 * The source range of the completion node denotes the source range
 * which should be replaced by the completion.
 */

import org.eclipse.wst.jsdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class CompletionOnQualifiedTypeReference extends QualifiedTypeReference {
	public static final int K_TYPE = 0;
	public static final int K_CLASS = 1;
	public static final int K_INTERFACE = 2;
	public static final int K_EXCEPTION = 3;

	private int kind = K_TYPE;
	public char[] completionIdentifier;
public CompletionOnQualifiedTypeReference(char[][] previousIdentifiers, char[] completionIdentifier, long[] positions) {
	this(previousIdentifiers, completionIdentifier, positions, K_TYPE);
}
public CompletionOnQualifiedTypeReference(char[][] previousIdentifiers, char[] completionIdentifier, long[] positions, int kind) {
	super(previousIdentifiers, positions);
	this.completionIdentifier = completionIdentifier;
	this.kind = kind;
}
public void aboutToResolve(Scope scope) {
	getTypeBinding(scope);
}
/*
 * No expansion of the completion reference into an array one
 */
public TypeReference copyDims(int dim){
	return this;
}
protected TypeBinding getTypeBinding(Scope scope) {
	// it can be a package, type or member type
	Binding binding = scope.parent.getTypeOrPackage(tokens); // step up from the ClassScope
	if (!binding.isValidBinding()) {
		scope.problemReporter().invalidType(this, (TypeBinding) binding);
		throw new CompletionNodeFound();
	}

	throw new CompletionNodeFound(this, binding, scope);
}
public boolean isClass(){
	return this.kind == K_CLASS;
}

public boolean isInterface(){
	return this.kind == K_INTERFACE;
}

public boolean isException(){
	return this.kind == K_EXCEPTION;
}
public boolean isSuperType(){
	return this.kind == K_CLASS || this.kind == K_INTERFACE;
}
public StringBuffer printExpression(int indent, StringBuffer output) {
	switch (this.kind) {
		case K_CLASS :
			output.append("<CompleteOnClass:");//$NON-NLS-1$
			break;
		case K_INTERFACE :
			output.append("<CompleteOnInterface:");//$NON-NLS-1$
			break;
		case K_EXCEPTION :
			output.append("<CompleteOnException:");//$NON-NLS-1$
			break;
		default :
			output.append("<CompleteOnType:");//$NON-NLS-1$
			break;
	}
	for (int i = 0; i < tokens.length; i++) {
		output.append(tokens[i]);
		output.append('.');
	}
	output.append(completionIdentifier).append('>');
	return output;
}
}
