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
 * 
 * Assignment pattern
 * 
 * @author Gorkem Ercan
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class AssignmentName extends Name {
	
	/**
	 * The "left" structural property of this node type
	 */
	public static final ChildPropertyDescriptor LEFT_PROPERTY =
				new ChildPropertyDescriptor(AssignmentName.class, "left", Name.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$
	

	/**
	 * The "right" structural property of this node type
	 */
	public static final ChildPropertyDescriptor RIGHT_PROPERTY =
				new ChildPropertyDescriptor(AssignmentName.class, "right", Expression.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	
	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(3);
		createPropertyList(AssignmentName.class, propertyList);
		addProperty(RIGHT_PROPERTY, propertyList);
		addProperty(LEFT_PROPERTY, propertyList);
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
	public static List<StructuralPropertyDescriptor> propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
	
	
	private Name left;
	private Expression right;
	
	/**
	 * @param ast
	 */
	AssignmentName(AST ast) {
		super(ast);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.Name#appendName(java.lang.StringBuffer)
	 */
	void appendName(StringBuffer buffer) {
		buffer.append(toString());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalStructuralPropertiesForType(int)
	 */
	List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return ASSIGNMENT_NAME;
	}

	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == LEFT_PROPERTY) {
			if (get) {
				return getLeft();
			} else {
				setLeft((Name) child);
				return null;
			}
		}
		
		if (property == RIGHT_PROPERTY) {
			if (get) {
				return getRight();
			} else {
				setRight((Expression) child);
				return null;
			}
		}
		
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
	
	public Name getLeft() {
		return left;
	}
	
	public void setLeft(Name left) {
		if (left == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.left;
		preReplaceChild(oldChild, left, LEFT_PROPERTY);
		this.left = left;
		postReplaceChild(oldChild, left, LEFT_PROPERTY);
	}
	
	public Expression getRight() {
		return right;
	}
	
	public void setRight(Expression right) {
		if (right == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.right;
		preReplaceChild(oldChild, right, RIGHT_PROPERTY);
		this.right = right;
		postReplaceChild(oldChild, right, RIGHT_PROPERTY);
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
		AssignmentName result = new AssignmentName(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setLeft((Name) getLeft().clone(target));
		result.setRight((Expression) getRight().clone(target));
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		if(visitor.visit(this)){
			acceptChild(visitor, getLeft());
			acceptChild(visitor, getRight());
		}
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize()
			+ (this.right == null ? 0 :getRight().treeSize())
			+ (this.left == null ? 0 : getLeft().treeSize());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return BASE_NAME_NODE_SIZE + 2 * 4;
	}

}
