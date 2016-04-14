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
 * Base for Class/Function/Variable declaration
 * 
 * @since 2.0
 *
 */
public class DeclarationBase extends SymbolBase implements IDeclaration {

	protected final Kind kind;

	DeclarationBase(SimpleName node, Scope scope, int index, Kind kind) {
		super(node, scope, index);
		this.kind = kind;
	}

	@Override
	public boolean isGlobal() {
		return scope.isGlobal();
	}

	@Override
	public boolean isLocal() {
		return scope.isLocal();
	}

	@Override
	public Kind getKind() {
		return kind;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DeclarationBase) || ((DeclarationBase) other).getKind() != getKind()) {
			return false;
		}

		DeclarationBase otherDecl = (DeclarationBase) other;
		return otherDecl.node == node;
	}

}
