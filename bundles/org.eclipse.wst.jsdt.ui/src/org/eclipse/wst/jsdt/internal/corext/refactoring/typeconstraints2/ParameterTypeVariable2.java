/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints2;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.types.TType;

/**
 * A ParameterTypeVariable is a ConstraintVariable which stands for
 * the type of a method parameter.
 */
public final class ParameterTypeVariable2 extends ConstraintVariable2 implements ISourceConstraintVariable {

	private final int fParameterIndex;
	private final String fKey;
	private IJavaScriptUnit fCompilationUnit;
	
	public ParameterTypeVariable2(TType type, int index, IFunctionBinding binding) {
		super(type);
		Assert.isNotNull(binding);
		Assert.isTrue(0 <= index);
		fParameterIndex= index;
		fKey= binding.getKey();
	}
	
	public void setCompilationUnit(IJavaScriptUnit cu) {
		fCompilationUnit= cu;
	}
	
	public IJavaScriptUnit getCompilationUnit() {
		return fCompilationUnit;
	}

	public int getParameterIndex() {
		return fParameterIndex;
	}
	
	public String getKey() {
		return fKey;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getParameterIndex() ^ getKey().hashCode();
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other.getClass() != ParameterTypeVariable2.class)
			return false;
		
		ParameterTypeVariable2 other2= (ParameterTypeVariable2) other;
		return getParameterIndex() == other2.getParameterIndex()
				&& getKey().equals(other2.getKey());
	}

	public String toString() {
		String toString= (String) getData(TO_STRING);
		return toString == null ? "[Parameter(" + fParameterIndex + "," + fKey + ")]" : toString; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}
}
