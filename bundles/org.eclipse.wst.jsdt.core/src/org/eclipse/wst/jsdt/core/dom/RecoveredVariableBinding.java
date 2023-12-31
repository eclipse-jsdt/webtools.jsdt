/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;

/**
 * This class represents the recovered binding for a variable
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
class RecoveredVariableBinding implements IVariableBinding {

	private VariableDeclaration variableDeclaration;
	private BindingResolver resolver;

	RecoveredVariableBinding(BindingResolver resolver, VariableDeclaration variableDeclaration) {
		this.resolver = resolver;
		this.variableDeclaration = variableDeclaration;
	}
	public Object getConstantValue() {
		return null;
	}

	public ITypeBinding getDeclaringClass() {
		ASTNode parent = this.variableDeclaration.getParent();
		while (parent != null && parent.getNodeType() != ASTNode.TYPE_DECLARATION) {
			parent = parent.getParent();
		}
		if (parent != null) {
			return ((TypeDeclaration) parent).resolveBinding();
		}
		return null;
	}

	public IFunctionBinding getDeclaringMethod() {
		ASTNode parent = this.variableDeclaration.getParent();
		while (parent != null && parent.getNodeType() != ASTNode.FUNCTION_DECLARATION) {
			parent = parent.getParent();
		}
		if (parent != null) {
			return ((FunctionDeclaration) parent).resolveBinding();
		}
		return null;
	}

	public String getName() {
		return this.variableDeclaration.getName().getIdentifier();
	}

	public ITypeBinding getType() {
		return this.resolver.getTypeBinding(this.variableDeclaration);
	}

	public IVariableBinding getVariableDeclaration() {
		return this;
	}

	public int getVariableId() {
		return 0;
	}

	public boolean isEnumConstant() {
		return false;
	}

	public boolean isField() {
		return this.variableDeclaration.getParent() instanceof FieldDeclaration;
	}

	public boolean isParameter() {
		return this.variableDeclaration instanceof SingleVariableDeclaration;
	}

	public IJavaScriptElement getJavaElement() {
		return null;
	}

	public String getKey() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Recovered#"); //$NON-NLS-1$
		if (variableDeclaration != null) {
			buffer
				.append("variableDeclaration") //$NON-NLS-1$
//				.append(this.variableDeclaration.getClass())
				.append(this.variableDeclaration.getName().getIdentifier())
				.append(this.variableDeclaration.getExtraDimensions());
		}
		return String.valueOf(buffer);
	}

	public int getKind() {
		return IBinding.VARIABLE;
	}

	public int getModifiers() {
		return 0;
	}

	public boolean isDeprecated() {
		return false;
	}

	public boolean isEqualTo(IBinding binding) {
		if (binding.isRecovered() && binding.getKind() == IBinding.VARIABLE) {
			return this.getKey().equals(binding.getKey());
		}
		return false;
	}

	public boolean isRecovered() {
		return true;
	}

	public boolean isGlobal() {
		// TODO Auto-generated method stub
		return false;
	}
}
