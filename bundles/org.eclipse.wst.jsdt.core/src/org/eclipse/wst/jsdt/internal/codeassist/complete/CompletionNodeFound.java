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

import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;

public class CompletionNodeFound extends RuntimeException {

	public ASTNode astNode;
	public Binding qualifiedBinding;
	public Scope scope;
	public boolean insideTypeAnnotation = false;

	private static final long serialVersionUID = 6981437684184091462L; // backward compatible

public CompletionNodeFound() {
	this(null, null, null, false); // we found a problem in the completion node
}
public CompletionNodeFound(ASTNode astNode, Binding qualifiedBinding, Scope scope) {
	this(astNode, qualifiedBinding, scope, false);
}
public CompletionNodeFound(ASTNode astNode, Binding qualifiedBinding, Scope scope, boolean insideTypeAnnotation) {
	this.astNode = astNode;
	this.qualifiedBinding = qualifiedBinding;
	this.scope = scope;
	this.insideTypeAnnotation = insideTypeAnnotation;
}
public CompletionNodeFound(ASTNode astNode, Scope scope) {
	this(astNode, null, scope, false);
}
public CompletionNodeFound(ASTNode astNode, Scope scope, boolean insideTypeAnnotation) {
	this(astNode, null, scope, insideTypeAnnotation);
}
}
