/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints;

import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.internal.corext.dom.Bindings;
import org.eclipse.wst.jsdt.internal.corext.dom.TypeRules;

public abstract class ConstraintVariable {
	/**
	 * The type binding, or <code>null</code>.
	 */
	private final ITypeBinding fTypeBinding;

	/**
	 * @param binding the type binding, or <code>null</code>
	 */
	protected ConstraintVariable(ITypeBinding binding) {
		fTypeBinding= binding;
	}

	public boolean canBeAssignedTo(ConstraintVariable targetVariable) {
		if (fTypeBinding == null || targetVariable.fTypeBinding == null)
			return false;
		return TypeRules.canAssign(fTypeBinding, targetVariable.fTypeBinding);
	}

	public String toResolvedString() {
		if (fTypeBinding == null)
			return "<NULL BINDING>"; //$NON-NLS-1$
		return Bindings.asString(fTypeBinding);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return toResolvedString();
	}
		
	/**
	 * @return the type binding or <code>null</code>
	 */
	//TODO: rename to getTypeBinding()
	public ITypeBinding getBinding() {
		return fTypeBinding;
	}

	/**
	 * For storing additional information associated with constraint variables. 
	 * Added in anticipation of the generics-related refactorings.
	 */
	private Object fData;
	
	public Object getData(){
		return fData;
	}
	
	public void setData(Object data){
		fData= data;
	}
}
