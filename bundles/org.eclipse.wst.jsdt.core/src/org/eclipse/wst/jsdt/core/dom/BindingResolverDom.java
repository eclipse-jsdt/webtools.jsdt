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
package org.eclipse.wst.jsdt.core.dom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.internal.core.dom.binding.BindingBase;
import org.eclipse.wst.jsdt.internal.core.dom.binding.ClassBinding;
import org.eclipse.wst.jsdt.internal.core.dom.binding.ClassDeclaration;
import org.eclipse.wst.jsdt.internal.core.dom.binding.FunctionBinding;
import org.eclipse.wst.jsdt.internal.core.dom.binding.FunctionDeclaration;
import org.eclipse.wst.jsdt.internal.core.dom.binding.IDeclaration;
import org.eclipse.wst.jsdt.internal.core.dom.binding.IReference;
import org.eclipse.wst.jsdt.internal.core.dom.binding.SymbolCollector;
import org.eclipse.wst.jsdt.internal.core.dom.binding.VariableBinding;
import org.eclipse.wst.jsdt.internal.core.dom.binding.VariableDeclaration;

/**
 * Internal class for resolving bindings using DomAst.
 *
 * This is the initial implementation that doesn't use any of the old compiler
 * classes and operates only on the DOM Ast. At the moment it provides limited
 * functionality and it should be changed as whole architecture is
 * cleaned/refactored.
 *
 * Provisional API: This class/interface is part of an interim API that is
 * still under development and expected to change significantly before
 * reaching stability. It is being made available at this early stage to
 * solicit feedback from pioneering adopters on the understanding that any
 * code that uses this API will almost certainly be broken (repeatedly) as the
 * API evolves.
 *
 * @since 2.0
 */
class BindingResolverDom extends BindingResolver {

	/**
	 * Unit to resolve bindings for
	 */
	JavaScriptUnit unit;

	/**
	 * AST to bindings map
	 */
	Map<ASTNode, BindingBase> ast2BindingsMap = new HashMap<>();

	/**
	 * Binding key to binding map
	 */
	Map<String, BindingBase> key2BindingsMap = new HashMap<>();

	/**
	 * Temporary map used only during resolving to wire function/methos
	 * bindings to enclosing classes. It's cleared after resolving.
	 */
	private Map<ASTNode, BindingBase> classBindingsMap = new HashMap<>();

	/**
	 * Temporary map used only during resolving to wire variable bindings to
	 * enclosing functions. It's cleared after resolving.
	 */
	private Map<ASTNode, BindingBase> functionBindingsMap = new HashMap<>();

	/**
	 * Create resolver for given JavaScriptUnit
	 *
	 * @param unit
	 *            unit to resolve bindings
	 */
	BindingResolverDom(JavaScriptUnit unit) {
		this.unit = unit;
	}

	/**
	 * Resolve all unit bindings
	 */
	void resolve() {

		SymbolCollector c = new SymbolCollector(unit);
		c.process();

		// Process Classes First
		for (Entry<IDeclaration, List<IReference>> x : c.getClassReferences().entrySet()) {
			processClassDeclaration((ClassDeclaration) x.getKey(), x.getValue());
		}

		// Process Functions
		for (Entry<IDeclaration, List<IReference>> x : c.getFunctionReferences().entrySet()) {
			processFunctionDeclaration((FunctionDeclaration) x.getKey(), x.getValue());
		}

		// Process Variables
		for (Entry<IDeclaration, List<IReference>> x : c.getVariableReferences().entrySet()) {
			processVariableDeclaration((VariableDeclaration) x.getKey(), x.getValue());
		}

		// Clear temporary maps
		classBindingsMap.clear();
		functionBindingsMap.clear();

	}

	/**
	 * Process found class declarations. Creates bindings for class
	 * declarations and references
	 *
	 * @param decl
	 *            class declaration
	 * @param references
	 *            list of references to class
	 */
	private void processClassDeclaration(ClassDeclaration decl, List<IReference> references) {
		ClassBinding cb = new ClassBinding(decl, references, 0);
		putClassBinding(cb);
		for (BindingBase b : cb.getReferences()) {
			putBinding(b);
		}
	}

	/**
	 * Process found function declarations. Creates bindings for function
	 * declarations and references
	 *
	 * @param decl
	 *            function declaration
	 * @param references
	 *            list of references to function
	 */
	private void processFunctionDeclaration(FunctionDeclaration decl, List<IReference> references) {
		ASTNode clsDeclaration = decl.getScope().getClassDeclarationLocation();
		ITypeBinding clsBinding = ((ITypeBinding) (clsDeclaration == null ? null : classBindingsMap.get(clsDeclaration)));
		FunctionBinding fb = new FunctionBinding(decl, references, clsBinding, 0);
		putFunctionBinding(fb);
		for (BindingBase b : fb.getReferences()) {
			putBinding(b);
		}
	}

	/**
	 * Process found variable declarations. Creates bindings for variable
	 * declarations and references
	 *
	 * @param decl
	 *            variable declaration
	 * @param references
	 *            list of references to variable
	 */
	private void processVariableDeclaration(VariableDeclaration decl, List<IReference> references) {
		ASTNode fnDeclaration = decl.getScope().getFunctionDeclarationLocation();
		IFunctionBinding fnBinding = ((IFunctionBinding) (fnDeclaration == null ? null : functionBindingsMap.get(fnDeclaration)));
		VariableBinding vb = new VariableBinding(decl, references, fnBinding, 0);
		putBinding(vb);
		for (BindingBase b : vb.getReferences()) {
			putBinding(b);
		}
	}


	/**
	 * Puts class binding into two maps used for lookup and into temporary map
	 * for later use in resolve process
	 *
	 * @param binding
	 */
	private void putClassBinding(BindingBase binding) {
		classBindingsMap.put(binding.getNode(), binding);
		putBinding(binding);
	}

	/**
	 * Puts function binding into two maps used for lookup and into temporary
	 * map for later use in resolve process
	 *
	 * @param binding
	 */
	private void putFunctionBinding(BindingBase binding) {
		functionBindingsMap.put(binding.getNode(), binding);
		putBinding(binding);
	}

	/**
	 * Puts binding into two maps used for lookup
	 *
	 * @param binding
	 */
	private void putBinding(BindingBase binding) {
		ast2BindingsMap.put(binding.getNode(), binding);
		key2BindingsMap.put(binding.getKey(), binding);
	}

	// Overridden methods from ancestor

	@Override
	ASTNode findDeclaringNode(IBinding binding) {
		if (binding instanceof BindingBase) {
			return ((BindingBase) binding).getNode();
		}
		return null;
	}

	@Override
	ASTNode findDeclaringNode(String bindingKey) {
		if (bindingKey != null) {
			BindingBase binding = key2BindingsMap.get(bindingKey);
			if (binding != null) {
				return binding.getNode();
			}
		}
		return null;
	}

	@Override
	IBinding resolveName(Name name) {
		return ast2BindingsMap.get(name);
	}

	@Override
	IFunctionBinding resolveMethod(org.eclipse.wst.jsdt.core.dom.FunctionDeclaration method) {
		return (IFunctionBinding) ast2BindingsMap.get(method.getMethodName());
	}

	@Override
	IFunctionBinding resolveMethod(FunctionInvocation method) {
		return (IFunctionBinding) ast2BindingsMap.get(method.getName());
	}

	@Override
	ITypeBinding resolveType(TypeDeclaration type) {
		return (ITypeBinding) ast2BindingsMap.get(type.getName());
	}

	@Override
	IVariableBinding resolveVariable(org.eclipse.wst.jsdt.core.dom.VariableDeclaration variable) {
		return (IVariableBinding) ast2BindingsMap.get(variable.getName());
	}


}
