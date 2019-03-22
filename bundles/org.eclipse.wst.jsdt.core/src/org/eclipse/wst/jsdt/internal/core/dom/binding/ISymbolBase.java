/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.dom.binding;

/**
 *
 * Extended information for Name from AST. It might be Class/Function/Variable
 * declaration or reference to the Class/Function/Variable. It serves as the
 * base for Declaration and Reference
 * 
 * @since 2.0
 *
 */
public interface ISymbolBase extends IAstName {

	/**
	 * Return symbol's index in the enclosing scope
	 *
	 * @return symbol's index in the enclosing scope
	 */
	int getIndex();

	/**
	 * Returns the key for this binding.
	 * <p>
	 * Within an AST each symbol will have a distinct key. The keys are
	 * generated in a manner that is predictable and as stable as possible.
	 * </p>
	 * <p>
	 * The exact details of how the keys are generated is unspecified.
	 * However, it is a function of the following information:
	 * <ul>
	 * <li>scope - the name of enclosing function/class</li>
	 * <li>index</li>
	 * <li>symbol name</li>
	 * </ul>
	 * </p>
	 *
	 * @return the key for this symbol
	 */
	String getKey();

}
