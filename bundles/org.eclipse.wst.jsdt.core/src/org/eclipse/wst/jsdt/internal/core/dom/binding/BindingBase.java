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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.dom.IBinding;

/**
 *
 * Base for the Binding. Implements functionality common for bindings. Derived
 * classes implement missing functionality and change default behavior.
 *
 * @since 2.0
 *
 */
public abstract class BindingBase extends SymbolBase implements IBinding {

	private final int modifiers;

	protected BindingBase declaration;

	protected List<BindingBase> references = new ArrayList<>();

	public BindingBase(ISymbolBase symbol, int modifiers) {
		super(symbol.getNode(), symbol.getScope(), symbol.getIndex());
		this.modifiers = modifiers;
	}

	/**
	 * Return binding of symbol declaration this binding refer or
	 * <code>null</code>. In reference is unresolved.
	 *
	 * @return binding of symbol declaration this binding refer
	 */
	public BindingBase getDeclaration() {
		return declaration;
	}

	/**
	 * Set declaration binding
	 *
	 * @param declaration
	 */
	public void setDeclaration(BindingBase declaration) {
		this.declaration = declaration;
	}

	@Override
	public abstract int getKind();

	/**
	 * Return list of references to this binding in case if it is declaration.
	 * For reference to the existing declaration return its list or
	 * references.
	 *
	 * @return list of references to this binding or to the declaration that
	 *         it references
	 */
	public List<BindingBase> getReferences() {
		if (declaration != null && declaration != this) {
			return declaration.getReferences();
		}
		return references;
	}

	@Override
	public int getModifiers() {
		return modifiers;
	}

	@Override
	public boolean isDeprecated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRecovered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IJavaScriptElement getJavaElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqualTo(IBinding other) {
		if (other == this) {
			// identical binding - equal (key or no key)
			return true;
		}
		// Do not consider other possibilities at the moment
		return false;
	}

}
