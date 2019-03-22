/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.code.flow;

import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;

class ReturnFlowInfo extends FlowInfo {
	
	public ReturnFlowInfo(ReturnStatement node) {
		super(getReturnFlag(node));
	}
	
	public void merge(FlowInfo info, FlowContext context) {
		if (info == null)
			return;
			
		assignAccessMode(info);
	}
	
	private static int getReturnFlag(ReturnStatement node) {
		Expression expression= node.getExpression();
		if (expression == null /* The only binding resolver implementation can't resolve "void" anyway || expression.resolveTypeBinding() == node.getAST().resolveWellKnownType("void")*/) //$NON-NLS-1$
			return VOID_RETURN;
		return VALUE_RETURN;
	}
}


