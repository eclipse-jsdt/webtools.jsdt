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
 * Declaration
 *
 * @since 2.0
 *
 */
public interface IDeclaration extends ISymbolBase {

	/**
	 * Declaration kind
	 *
	 */
	enum Kind {
		CLASS, VARIABLE, FUNCTION
	}

	/**
	 * Returns declaration kind
	 *
	 * @return declaration kind
	 */
	Kind getKind();

	/**
	 * Returns <code>true</code> if it's a global declaration
	 * 
	 * @return <code>true</code> if it's a global declaration
	 */
	boolean isGlobal();

	/**
	 * Returns <code>true</code> if it's a local declaration
	 * 
	 * @return <code>true</code> if it's a global declaration
	 */
	boolean isLocal();

}
