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
 * A type declaration expression AST node type.
 * <p>
 * This kind of node is used to convert a type declaration
 * node into an expression node by wrapping it.
 * </p>
 * *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 * 
 * @author Gorkem Ercan
 */
public class TypeDeclarationExpression extends Expression {
	
	/**
	 * The "declaration" structural property of this node type (added in JLS3 API).
	 *  
	 */
	public static final ChildPropertyDescriptor DECLARATION_PROPERTY =
		new ChildPropertyDescriptor(TypeDeclarationExpression.class, "declaration", AbstractTypeDeclaration.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(2);
		createPropertyList(TypeDeclarationExpression.class, propertyList);
		addProperty(DECLARATION_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}
	
	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 *  
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
	
	private AbstractTypeDeclaration declaration = null;

	TypeDeclarationExpression(AST ast) {
		super(ast);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalStructuralPropertiesForType(int)
	 */
	List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	/**
	 * Returns the abstract type declaration of this local type declaration
	 * expression
	 * @return the type declaration node
	 */
	public AbstractTypeDeclaration getDeclaration() {
		if (this.declaration == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.declaration == null) {
					preLazyInit();
					this.declaration = new TypeDeclaration(this.ast);
					postLazyInit(this.declaration, DECLARATION_PROPERTY);
				}
			}
		}
		return this.declaration;
	}

	/**
	 * Sets the abstract type declaration of this local type declaration
	 *
	 * @param decl the type declaration node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
	public void setDeclaration(AbstractTypeDeclaration declaration) {
		if (declaration == null) {
			throw new IllegalArgumentException();
		}
		// a TypeDeclarationStatement may occur inside an
		// TypeDeclaration - must check cycles
		ASTNode oldChild = this.declaration;
		preReplaceChild(oldChild, declaration, DECLARATION_PROPERTY);
		this.declaration = declaration;
		postReplaceChild(oldChild, declaration, DECLARATION_PROPERTY);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == DECLARATION_PROPERTY) {
			if (get) {
				return getDeclaration();
			} else {
				setDeclaration((AbstractTypeDeclaration) child);
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
		return TYPE_DECLARATION_EXPRESSION;
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
		TypeDeclarationExpression result = new TypeDeclarationExpression(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setDeclaration((AbstractTypeDeclaration) getDeclaration().clone(target));
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren){
			acceptChild(visitor, getDeclaration());
		}
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize()
					+ (this.declaration == null ? 0 : getDeclaration().treeSize());

	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return BASE_NODE_SIZE + 1 + 4;
	}

}
