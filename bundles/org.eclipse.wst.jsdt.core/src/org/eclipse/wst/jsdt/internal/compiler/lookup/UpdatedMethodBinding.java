/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.lookup;

public class UpdatedMethodBinding extends MethodBinding {

	public TypeBinding updatedDeclaringClass;

	public UpdatedMethodBinding(TypeBinding updatedDeclaringClass, int modifiers, char[] selector, TypeBinding returnType, TypeBinding[] args, ReferenceBinding declaringClass) {
		super(modifiers, selector, returnType, args, declaringClass);
		this.updatedDeclaringClass = updatedDeclaringClass;
	}

	public TypeBinding constantPoolDeclaringClass() {
		return this.updatedDeclaringClass;
	}
}
