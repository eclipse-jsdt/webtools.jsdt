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
package org.eclipse.wst.jsdt.internal.core.search.matching;

import org.eclipse.wst.jsdt.internal.core.index.EntryResult;
import org.eclipse.wst.jsdt.internal.core.index.Index;

public class PackageDeclarationPattern extends JavaSearchPattern {

protected char[] pkgName;

public PackageDeclarationPattern(char[] pkgName, int matchRule) {
	super(PKG_DECL_PATTERN, matchRule);
	this.pkgName = pkgName;
}
EntryResult[] queryIn(Index index) {
	// package declarations are not indexed
	return null;
}
protected StringBuffer print(StringBuffer output) {
	output.append("PackageDeclarationPattern: <"); //$NON-NLS-1$
	if (this.pkgName != null)
		output.append(this.pkgName);
	else
		output.append("*"); //$NON-NLS-1$
	output.append(">"); //$NON-NLS-1$
	return super.print(output);
}
}
