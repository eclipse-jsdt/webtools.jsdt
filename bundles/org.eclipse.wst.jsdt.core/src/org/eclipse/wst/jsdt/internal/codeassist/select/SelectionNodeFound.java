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
package org.eclipse.wst.jsdt.internal.codeassist.select;

import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;

public class SelectionNodeFound extends RuntimeException {

	public Binding binding;
	public boolean isDeclaration;
	private static final long serialVersionUID = -7335444736618092295L; // backward compatible

public SelectionNodeFound() {
	this(null, false); // we found a problem in the selection node
}
public SelectionNodeFound(Binding binding) {
	this(binding, false);
}
public SelectionNodeFound(Binding binding, boolean isDeclaration) {
	this.binding = binding;
	this.isDeclaration = isDeclaration;
}
}
