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

import java.util.List;

import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;

/**
 * Function binding
 *
 * @since 2.0
 *
 */
public class FunctionBinding extends BindingBase implements IFunctionBinding {

	protected ITypeBinding declaringClass;

	public FunctionBinding(FunctionDeclaration declaration, List<IReference> refs, ITypeBinding declaringClass, int modifiers) {
		super(declaration, modifiers);
		setDeclaration(this);
		this.declaringClass = declaringClass;
		for (int i = 1; i < refs.size(); i++) {
			IReference r = refs.get(i);
			this.references.add(new FunctionBinding(r, modifiers, this));
		}
	}

	public FunctionBinding(IReference reference, int modifiers, FunctionBinding methodDeclaration) {
		super(reference, modifiers);
		setDeclaration(methodDeclaration);
		this.declaringClass = methodDeclaration.declaringClass;
	}

	@Override
	public int getKind() {
		return IBinding.METHOD;
	}

	@Override
	public ITypeBinding getDeclaringClass() {
		return declaringClass;
	}

	public void setDeclaringClass(ITypeBinding declaringClass) {
		this.declaringClass = declaringClass;
	}

	@Override
	public boolean isConstructor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefaultConstructor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeBinding[] getParameterTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeBinding getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFunctionBinding getMethodDeclaration() {
		return (IFunctionBinding) getDeclaration();
	}

	@Override
	public boolean isSubsignature(IFunctionBinding otherMethod) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVarargs() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean overrides(IFunctionBinding method) {
		// TODO Auto-generated method stub
		return false;
	}

	public String toString() {
		String result = "{FB name: " + getName(); //$NON-NLS-1$
		result += (" key: " + getKey()); //$NON-NLS-1$
		result += (" in " + (declaringClass != null ? declaringClass.toString() : "script")) + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

}
