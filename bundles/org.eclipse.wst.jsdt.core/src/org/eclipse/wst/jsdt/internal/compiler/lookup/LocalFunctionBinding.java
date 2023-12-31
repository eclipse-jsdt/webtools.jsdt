/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.lookup;

public class LocalFunctionBinding extends MethodBinding {
	final static char[] LocalFunctionPrefix = { '$', 'L', 'o', 'c', 'a', 'l', 'f', 'u', 'n', 'c', '$' };


	public LocalFunctionBinding(int modifiers, char[] selector,
			TypeBinding returnType, TypeBinding[] parameters, ReferenceBinding declaringClass) {
		super(modifiers, selector, returnType, parameters,
				declaringClass);
	}



}
