/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * ModuleSpecifier for Import/Export declarations
 * <p>
 * When used with {@link ImportDeclaration} 
 * The {@link #discoverableName} refers to the name of 
 * the export imported from the module. The {@link #local}
 * refers to the binding imported into the local module scope.
 * For basic named import such as <code>import {foo} from "mod"</code>
 * discoverableName and local are equivalent nodes. 
 * </p>
 * <p>
 * {@link #isDefault} is used with {@link ImportDeclaration} to signal 
 * a default import such as <code>import foo from "mod.js".</code>
 * </p>
 * <p>
 * {@link #namespace} is used with imports to signal a namespace import
 * e.g., <code>* as foo</code> in <code>import * as foo from "mod.js"</code>.
 * </p>
 * 
 * 
 * <p>
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 *</p>
 *
 * @author Gorkem Ercan
 */
public class ModuleSpecifier extends ASTNode {
	
	
	/**
	 * The "local" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor LOCAL_PROPERTY =
		new ChildPropertyDescriptor(ModuleSpecifier.class, "local", SimpleName.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "discoverableName" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor DISCOVERABLE_NAME_PROPERTY =
				new ChildPropertyDescriptor(ModuleSpecifier.class, "discoverableName", SimpleName.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$
	
	
	/**
	 * The "isDefault" structural property of this node type.
	 *  
	 */
	public static final SimplePropertyDescriptor DEFAULT_PROPERTY =
		new SimplePropertyDescriptor(ModuleSpecifier.class, "isDefault", boolean.class, MANDATORY); //$NON-NLS-1$

	/**
	 * The "namespace" structural property of this node type.
	 *
	 */
	public static final SimplePropertyDescriptor NAMESPACE_PROPERTY =
				new SimplePropertyDescriptor(ModuleSpecifier.class, "namespace", boolean.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(5);
		createPropertyList(ModuleSpecifier.class, propertyList);
		addProperty(LOCAL_PROPERTY, propertyList);
		addProperty(DISCOVERABLE_NAME_PROPERTY, propertyList);
		addProperty(DEFAULT_PROPERTY, propertyList);
		addProperty(NAMESPACE_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the AST.JLS* constants
	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 *  
	 */
	public static List<StructuralPropertyDescriptor> propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
	
	private SimpleName local;
	
	private SimpleName discoverableName;
	
	private boolean isDefault;
	
	private boolean namespace;
	
	/**
	 * Creates a new AST for a module speficier. 
	 * 
	 * @param ast
	 */

	ModuleSpecifier(AST ast) {
		super(ast);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalStructuralPropertiesForType(int)
	 */
	List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == LOCAL_PROPERTY) {
			if (get) {
				return getLocal();
			} else {
				setLocal((SimpleName) child);
				return null;
			}
		}
		if (property == DISCOVERABLE_NAME_PROPERTY) {
			if (get) {
				return getDiscoverableName();
			} else {
				setDiscoverableName((SimpleName) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalGetSetBooleanProperty(org.eclipse.wst.jsdt.core.dom.SimplePropertyDescriptor, boolean, boolean)
	 */
	boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		if (property == DEFAULT_PROPERTY) {
			if (get) {
				return isDefault();
			} else {
				setDefault(value);
				return false;
			}
		}
		if (property == NAMESPACE_PROPERTY) {
			if (get) {
				return isNamespace();
			} else {
				setNamespace(value);
				return false;
			}
		}
		return super.internalGetSetBooleanProperty(property, get, value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return MODULE_SPECIFIER;
	}

	/**
	 * Returns this node's local name
	 * @return
	 */
	public SimpleName getLocal() {
		return local;
	}
	
	/**
	 * Sets the local name of this node to the given value.
	 * 
	 * @param local
	 */
	public void setLocal(SimpleName local) {
		if (local == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.local;
		preReplaceChild(oldChild,local, LOCAL_PROPERTY);
		this.local = local;
		postReplaceChild(oldChild, local, LOCAL_PROPERTY);
	}
	
	/**
	 * Returns this node's discoverable name
	 * @return
	 */ 
	public SimpleName getDiscoverableName() {
		return discoverableName;
	}
	
	/**
	 * Sets the discoverable name of this node
	 * @param discoverableName
	 */
	public void setDiscoverableName(SimpleName name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.discoverableName;
		preReplaceChild(oldChild,name, DISCOVERABLE_NAME_PROPERTY);
		this.discoverableName = name;
		postReplaceChild(oldChild, name, DISCOVERABLE_NAME_PROPERTY);
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		preValueChange(DEFAULT_PROPERTY);
		this.isDefault = isDefault;
		postValueChange(DEFAULT_PROPERTY);
	}

	public boolean isNamespace() {
		return namespace;
	}

	public void setNamespace(boolean namespace) {
		preValueChange(NAMESPACE_PROPERTY);
		this.namespace = namespace;
		postValueChange(NAMESPACE_PROPERTY);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#subtreeMatch0(org.eclipse.wst.jsdt.core.dom.ASTMatcher, java.lang.Object)
	 */
	boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		return matcher.match(this, other);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#clone0(org.eclipse.wst.jsdt.core.dom.AST)
	 */
	ASTNode clone0(AST target) {
		ModuleSpecifier result = new ModuleSpecifier(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setDiscoverableName((SimpleName) getDiscoverableName().clone(target));
		result.setLocal((SimpleName) getLocal().clone(target));
		result.setDefault(isDefault());
		result.setNamespace(isNamespace());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			acceptChild(visitor, getLocal());
			acceptChild(visitor, getDiscoverableName());
		}
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return BASE_NODE_SIZE + 4 *4;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return
			memSize()
					+ ((local == null) ? 0 : getLocal().treeSize())
					+ ((discoverableName == null) ? 0 :getDiscoverableName().treeSize());
	}

}
