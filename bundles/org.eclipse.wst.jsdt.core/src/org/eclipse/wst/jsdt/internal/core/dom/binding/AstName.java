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
 * @since 2.0
 */
public class AstName implements IAstName {

	/**
	 * AST node
	 */
	protected final SimpleName node;

	/**
	 * Scope
	 */
	protected final Scope scope;

	/**
	 * Create AstName object
	 *
	 * @param node
	 *            AST node of the Name
	 * @param scope
	 *            Class/Function scope
	 */
	public AstName(SimpleName node, Scope scope) {
		this.node = node;
		this.scope = scope;
	}

	@Override
	public SimpleName getNode() {
		return node;
	}

	@Override
	public String getName() {
		return getNode().getIdentifier();
	}

	@Override
	public Scope getScope() {
		return scope;
	}

}
