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
 * ExportDeclaration AST node type
 * 
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves
 * 
 * @author gercan
 *
 */
public class ExportDeclaration extends ASTNode {


	/**
	 * The "source" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor SOURCE_PROPERTY =
		new ChildPropertyDescriptor(ExportDeclaration.class, "source", StringLiteral.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "specifiers" structural property of this node type
	 */
	public static final ChildListPropertyDescriptor SPECIFIERS_PROPERTY =
				new ChildListPropertyDescriptor(ExportDeclaration.class, "specifiers", ModuleSpecifier.class, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "declaration" structural property of this node type.
	 */
	public static final ChildPropertyDescriptor DECLARATION_PROPERTY =
		new ChildPropertyDescriptor(ExportDeclaration.class, "declaration", ProgramElement.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "isDefault" structural property of this node type.
	 */
	public static final SimplePropertyDescriptor DEFAULT_PROPERTY =
		new SimplePropertyDescriptor(ExportDeclaration.class, "isDefault", boolean.class, MANDATORY); //$NON-NLS-1$

	/**
	 * The "all" structural property of this node type.
	 */
	public static final SimplePropertyDescriptor ALL_PROPERTY =
				new SimplePropertyDescriptor(ExportDeclaration.class, "all", boolean.class, MANDATORY); //$NON-NLS-1$

	
	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;
	
	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(6);
		createPropertyList(ExportDeclaration.class, propertyList);
		addProperty(SOURCE_PROPERTY, propertyList);
		addProperty(SPECIFIERS_PROPERTY, propertyList);
		addProperty(DECLARATION_PROPERTY, propertyList);
		addProperty(DEFAULT_PROPERTY, propertyList);
		addProperty(ALL_PROPERTY, propertyList);
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
	
	private StringLiteral source;
	
	private NodeList specifiers = new NodeList(SPECIFIERS_PROPERTY);
	
	private ProgramElement declaration;
	
	private boolean isDefault ;
	
	private boolean isAll ;

	/**
	 * @param ast
	 */
	ExportDeclaration(AST ast) {
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
	final boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		if (property == DEFAULT_PROPERTY) {
			if (get) {
				return isDefault();
			} else {
				setDefault(value);
				return false;
			}
		}
		if (property == ALL_PROPERTY) {
			if (get) {
				return isAll();
			} else {
				setAll(value);
				return false;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetBooleanProperty(property, get, value);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == SPECIFIERS_PROPERTY) {
			return specifiers();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}
	
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == DECLARATION_PROPERTY) {
			if (get) {
				return getDeclaration();
			} else {
				setDeclaration( (ProgramElement) child);
				return null;
			}
		}
		if (property == SOURCE_PROPERTY) {
			if (get) {
				return getSource();
			} else {
				setSource( (StringLiteral) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return EXPORT_DECLARATION;
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#subtreeMatch0(org.eclipse.wst.jsdt.core.dom.ASTMatcher, java.lang.Object)
	 */
	boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		return matcher.match(this, other);
	}

	public List specifiers() {
		return this.specifiers;
	}
	
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		preValueChange(DEFAULT_PROPERTY);
		this.isDefault = isDefault;
		postValueChange(DEFAULT_PROPERTY);
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		preValueChange(ALL_PROPERTY);
		this.isAll = isAll;
		postValueChange(ALL_PROPERTY);
	}

	public StringLiteral getSource() {
		return source;
	}

	public void setSource(StringLiteral source) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.source;
		preReplaceChild(oldChild, source, SOURCE_PROPERTY);
		this.source = source;
		postReplaceChild(oldChild, source, SOURCE_PROPERTY);
	}

	public ProgramElement getDeclaration() {
		return declaration;
	}

	public void setDeclaration(ProgramElement declaration) {
		if (declaration == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.declaration;
		preReplaceChild(oldChild, declaration, DECLARATION_PROPERTY);
		this.declaration = declaration;
		postReplaceChild(oldChild,declaration, DECLARATION_PROPERTY);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#clone0(org.eclipse.wst.jsdt.core.dom.AST)
	 */
	ASTNode clone0(AST target) {
		ExportDeclaration result = new ExportDeclaration(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setDefault(isDefault());
		result.setAll(isAll());
		if(getDeclaration() != null){
			result.setDeclaration((ProgramElement) getDeclaration().clone(target));
		}
		result.setSource((StringLiteral) getSource().clone(target));
		result.specifiers.addAll(ASTNode.copySubtrees(target, specifiers()));
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren){
			acceptChild(visitor, getDeclaration());
			acceptChild(visitor, getSource());
			acceptChildren(visitor, this.specifiers);
		}
		visitor.endVisit(this);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize()
					+ this.specifiers.listSize()
					+(source == null ? 0 : getSource().treeSize())
					+(declaration == null ? 0 : getSource().treeSize());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return BASE_NODE_SIZE + 5 *4;
	}

}
