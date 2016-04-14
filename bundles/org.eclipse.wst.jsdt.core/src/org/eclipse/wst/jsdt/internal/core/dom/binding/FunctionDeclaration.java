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
 * Function Declaration
 * 
 * @since 2.0
 *
 */
public class FunctionDeclaration extends DeclarationBase {

	FunctionDeclaration(SimpleName node, Scope scope, int index) {
		super(node, scope, index, Kind.FUNCTION);
	}

	@Override
	public String toString() {
		return "FD " + getName(); //$NON-NLS-1$
	}

}
