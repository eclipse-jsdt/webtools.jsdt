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
package org.eclipse.wst.jsdt.internal.core.search;

import org.eclipse.wst.jsdt.core.search.TypeNameRequestor;
import org.eclipse.wst.jsdt.internal.compiler.env.AccessRestriction;

/**
 * Wrapper used to link {@link IRestrictedAccessTypeRequestor} with {@link TypeNameRequestor}.
 * This wrapper specifically allows usage of internal method {@link BasicSearchEngine#searchAllTypeNames(
 * 	char[] packageName,
 * 	int packageMatchRule,
 * 	char[] typeName,
 * 	int typeMatchRule,
 * 	int searchFor,
 * 	org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope scope,
 * 	IRestrictedAccessTypeRequestor nameRequestor,
 * 	int waitingPolicy,
 * 	org.eclipse.core.runtime.IProgressMonitor monitor) }.
 * from  API method {@link org.eclipse.wst.jsdt.core.search.SearchEngine#searchAllTypeNames(
 * 	char[] packageName,
 * 	int packageMatchRule,
 * 	char[] typeName,
 * 	int matchRule,
 * 	int searchFor,
 * 	org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope scope,
 * 	TypeNameRequestor nameRequestor,
 * 	int waitingPolicy,
 * 	org.eclipse.core.runtime.IProgressMonitor monitor) }.
 */
public class TypeNameRequestorWrapper implements IRestrictedAccessTypeRequestor {
	TypeNameRequestor requestor;
	public TypeNameRequestorWrapper(TypeNameRequestor requestor) {
		this.requestor = requestor;
	}
	public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] superTypeNames, char[][] enclosingTypeNames, String path, AccessRestriction access) {
		this.requestor.acceptType(modifiers, packageName, simpleTypeName, enclosingTypeNames, path);
	}
}
