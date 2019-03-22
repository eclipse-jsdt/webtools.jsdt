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

import java.util.List;

import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.IVariableBinding;

/**
 * Variable Binding
 *
 * @since 2.0
 *
 */
public class VariableBinding extends BindingBase implements IVariableBinding {


	protected ITypeBinding type;

	protected IFunctionBinding declaringMethod;

	protected boolean isArguments;

	public VariableBinding(VariableDeclaration declaration, List<IReference> refs, IFunctionBinding declaringMethod, int modifiers) {
		super(declaration, modifiers);
		isArguments = declaration.isArguments();
		setDeclaration(this);
		this.declaringMethod = declaringMethod;
		for (int i = isArguments?0:1; i < refs.size(); i++) {
			IReference r = refs.get(i);
			this.references.add(new VariableBinding(r, modifiers, this));
		}
	}

	public VariableBinding(IReference reference, int modifiers, VariableBinding variableDeclaration) {
		super(reference, modifiers);
		isArguments = variableDeclaration.isArguments;
		setDeclaration(variableDeclaration);
		this.declaringMethod = variableDeclaration.declaringMethod;
	}


	@Override
	public int getKind() {
		return IBinding.VARIABLE;
	}

	@Override
	public boolean isField() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGlobal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isParameter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITypeBinding getDeclaringClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeBinding getType() {
		return type;
	}

	@Override
	public int getVariableId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getConstantValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFunctionBinding getDeclaringMethod() {
		return declaringMethod;
	}

	public void setDeclaringMethod(IFunctionBinding declaringMethod) {
		this.declaringMethod = declaringMethod;
	}

	@Override
	public IVariableBinding getVariableDeclaration() {
		return (IVariableBinding) getDeclaration();
	}

	public String toString() {
		String result = "{VB name: " + getName(); //$NON-NLS-1$
		result += (" key: " + getKey()); //$NON-NLS-1$
		result += (" in " + (declaringMethod != null ? declaringMethod.toString() : "script")) + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	@Override
	public String getName() {
		if (isArguments) {
			return "arguments"; //$NON-NLS-1$
		}
		else {
			return super.getName();
		}
	}


}
