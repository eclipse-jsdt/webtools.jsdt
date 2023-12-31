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
package org.eclipse.wst.jsdt.internal.core.search.matching;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.internal.compiler.util.SimpleSet;

public class DeclarationOfReferencedTypesPattern extends TypeReferencePattern {

protected SimpleSet knownTypes;
protected IJavaScriptElement enclosingElement;

public DeclarationOfReferencedTypesPattern(IJavaScriptElement enclosingElement) {
	super(null, null, R_PATTERN_MATCH);

	this.enclosingElement = enclosingElement;
	this.knownTypes = new SimpleSet();
	((InternalSearchPattern)this).mustResolve = true;
}
}
