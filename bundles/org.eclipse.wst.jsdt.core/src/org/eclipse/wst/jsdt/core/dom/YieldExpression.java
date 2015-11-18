/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
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
 * Yield expression AST Node type
 * 
 * <pre>
 * YieldExpression:
 *   yield [expression];
 *  </pre>
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 *   
 * @author Gorkem Ercan
 *
 */
public class YieldExpression extends Expression {
	
	
	public static final SimplePropertyDescriptor DELEGATE_PROPERTY =
				new SimplePropertyDescriptor(YieldExpression.class, "delegate", Boolean.class, MANDATORY); //$NON-NLS-1$
	
	public static final ChildPropertyDescriptor ARGUMENT_PROPERTY = 
				new ChildPropertyDescriptor(YieldExpression.class, "argument", Expression.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$
	
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;
	
	static{
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(3);
		createPropertyList(YieldExpression.class, propertyList);
		addProperty(ARGUMENT_PROPERTY, propertyList);
		addProperty(DELEGATE_PROPERTY, propertyList);
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
	
	
	/**
	 * Argument for the yield
	 */
	private Expression argument;
	
	/**
	 * indicates of this yields to another generator
	 */
	private Boolean delegate = Boolean.FALSE;
	
	
	YieldExpression(AST ast) {
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
	final Object internalGetSetObjectProperty(SimplePropertyDescriptor property, boolean get, Object value) {
		if (property == DELEGATE_PROPERTY) {
			if (get) {
				return getDelegate();
			} else {
				setDelegate((Boolean)value);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetObjectProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == ARGUMENT_PROPERTY) {
			if (get) {
				return getArgument();
			} else {
				setArgument((Expression) child);
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
		return YIELD_EXPRESSION;
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
		YieldExpression result = new YieldExpression(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setDelegate(getDelegate());
		if(getArgument() != null){
			result.setArgument((Expression) getArgument().clone(target));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		if(visitor.visit(this)){
			acceptChild(visitor, getArgument());
		}
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize() + (this.argument == null ? 0 : getArgument().treeSize());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return BASE_NODE_SIZE + 2 * 4;
	}
	
	/**
	 * Returns the argument for the yield or null
	 * 
	 * @return the argument expression node 
	 */
	public Expression getArgument() {
		return this.argument;
	}

	/**
	 * Set the argument for this yield expression 
	 * 
	 * @param argument the argument expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
	public void setArgument(Expression argument) {
		if(argument == null ){
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.argument;
		preReplaceChild(oldChild, argument, ARGUMENT_PROPERTY);
		this.argument = argument;
		postReplaceChild(oldChild, argument, ARGUMENT_PROPERTY);
	}

	/**
	 * Return true if yield is delegate.
	 * @return
	 */
	public Boolean getDelegate() {
		return delegate;
	}

	/**
	 * Sets if this yield expression is delegate
	 * @param delegate
	 * @exception IllegalArgumentException if argument is null
	 */
	public void setDelegate(Boolean delegate) {
		if(delegate == null ){
			throw new IllegalArgumentException();
		}
		preValueChange(DELEGATE_PROPERTY);
		this.delegate = delegate;
		postValueChange(DELEGATE_PROPERTY);
	}

}
