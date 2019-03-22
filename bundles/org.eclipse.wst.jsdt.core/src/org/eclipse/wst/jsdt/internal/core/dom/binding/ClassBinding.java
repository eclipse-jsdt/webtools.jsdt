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
import org.eclipse.wst.jsdt.core.dom.IPackageBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.IVariableBinding;

/**
 * Class binding
 *
 * @since 2.0
 *
 */
public class ClassBinding extends BindingBase implements ITypeBinding {

	protected ITypeBinding declaringClass;

	public ClassBinding(ClassDeclaration declaration, List<IReference> refs, int modifiers) {
		super(declaration, modifiers);
		setDeclaration(this);
		for (int i = 1; i < refs.size(); i++) {
			IReference r = refs.get(i);
			this.references.add(new ClassBinding(r, modifiers, this));
		}
	}

	public ClassBinding(IReference reference, int modifiers, ClassBinding typeDeclaration) {
		super(reference, modifiers);
		setDeclaration(typeDeclaration);
	}

	@Override
	public int getKind() {
		return IBinding.TYPE;
	}

	@Override
	public ITypeBinding getDeclaringClass() {
		return declaringClass;
	}

	public String toString() {
		String result = "{CB name: " + getName(); //$NON-NLS-1$
		result += (" key: " + getKey()) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		return result;
	}

	@Override
	public ITypeBinding createArrayType(int dimension) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBinaryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeBinding getComponentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableBinding[] getDeclaredFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFunctionBinding[] getDeclaredMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDeclaredModifiers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITypeBinding[] getDeclaredTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFunctionBinding getDeclaringMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDimensions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ITypeBinding getElementType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeBinding getErasure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPackageBinding getPackage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeBinding getSuperclass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeBinding getTypeDeclaration() {
		return (ITypeBinding) getDeclaration();
	}

	@Override
	public boolean isAnonymous() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isArray() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAssignmentCompatible(ITypeBinding variableType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCastCompatible(ITypeBinding type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClass() {
		return true;
	}

	@Override
	public boolean isFromSource() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMember() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNested() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNullType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrimitive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSubTypeCompatible(ITypeBinding type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTopLevel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCompilationUnit() {
		// TODO Auto-generated method stub
		return false;
	}

}
