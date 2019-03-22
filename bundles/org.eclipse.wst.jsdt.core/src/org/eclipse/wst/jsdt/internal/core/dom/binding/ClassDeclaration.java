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

import org.eclipse.wst.jsdt.core.dom.SimpleName;

/**
 * Class Declaration
 *
 * @since 2.0
 *
 */
public class ClassDeclaration extends DeclarationBase {

	ClassDeclaration(SimpleName node, Scope scope, int index) {
		super(node, scope, index, Kind.CLASS);
	}

	@Override
	public String toString() {
		return "CD " + getName(); //$NON-NLS-1$
	}

}
