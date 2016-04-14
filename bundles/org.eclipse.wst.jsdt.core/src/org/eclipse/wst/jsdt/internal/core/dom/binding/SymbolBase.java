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
 *
 * Symbol Base
 * 
 * @since 2.0
 *
 */
public class SymbolBase extends AstName implements ISymbolBase {

	protected final int index;

	public SymbolBase(SimpleName node, Scope scope, int index) {
		super(node, scope);
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public String getKey() {
		String scopeKey = getScope().getKey();
		return scopeKey + (scopeKey.endsWith("/") ? "" : "/") + getIndex() + "#" + getName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

}
