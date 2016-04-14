/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.dom.binding;

import org.eclipse.wst.jsdt.core.dom.SimpleName;

/**
 * Name (Symbol) in AST with enclosing Class/Function Scope.
 * 
 * @since 2.0
 *
 */
public interface IAstName {

	/**
	 * The location of the name in the AST
	 *
	 * @return the AST Node corresponding to the name
	 */
	SimpleName getNode();

	/**
	 * The Name
	 *
	 * @return
	 */
	String getName();


	/**
	 * Return the Class/Function scope that contains name
	 *
	 * @return the Class/Function scope that contains name
	 */
	Scope getScope();


}