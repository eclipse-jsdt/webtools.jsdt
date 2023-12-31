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
package org.eclipse.wst.jsdt.internal.compiler.lookup;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ast.ImportReference;

public class ImportConflictBinding extends ImportBinding {
public ReferenceBinding conflictingTypeBinding; // must ensure the import is resolved

public ImportConflictBinding(char[][] compoundName, Binding methodBinding, ReferenceBinding conflictingTypeBinding, ImportReference reference) {
	super(compoundName, false, methodBinding, reference);
	this.conflictingTypeBinding = conflictingTypeBinding;
}
public char[] readableName() {
	return CharOperation.concatWith(compoundName, '.');
}
public String toString() {
	return "method import : " + new String(readableName()); //$NON-NLS-1$
}
}
