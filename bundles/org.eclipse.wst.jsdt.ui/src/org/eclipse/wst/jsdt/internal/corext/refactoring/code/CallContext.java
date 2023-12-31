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
package org.eclipse.wst.jsdt.internal.corext.refactoring.code;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.wst.jsdt.internal.corext.dom.CodeScopeBuilder;

public class CallContext {

	public ASTNode invocation;
	public String[] arguments;
	public String receiver; 
	public boolean receiverIsStatic;
	public CodeScopeBuilder.Scope scope;
	public int callMode;
	public ImportRewrite importer;

	public CallContext(ASTNode inv, CodeScopeBuilder.Scope s, int cm, ImportRewrite i) {
		super();
		invocation= inv;
		scope= s;
		callMode= cm;
		importer= i;
	}
	
	public ITypeBinding getReceiverType() {
		Expression expression= Invocations.getExpression(invocation);
		if (expression != null) {
			return expression.resolveTypeBinding();
		}
		IFunctionBinding method= Invocations.resolveBinding(invocation);
		if (method != null) {
			return method.getDeclaringClass();
		}
		return null;
	}
}
