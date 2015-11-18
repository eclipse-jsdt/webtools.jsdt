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
 * Elements for {@link TemplateLiteral} node
 *
 * @author Gorkem Ercan
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 * 
 */
public class TemplateElement extends ASTNode {
	
	/**
	 * The "rawValue" structural property of this node type.
	 *  
	 */
	public static final SimplePropertyDescriptor RAW_VALUE_PROPERTY =
		new SimplePropertyDescriptor(TemplateElement.class, "rawValue", String.class, MANDATORY); //$NON-NLS-1$

	
	/**
	 * The "tail" structural property of this node type.
	 *  
	 */
	public static final SimplePropertyDescriptor TAIL_PROPERTY =
		new SimplePropertyDescriptor(TemplateElement.class, "tail", boolean.class, MANDATORY); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(3);
		createPropertyList(TemplateElement.class, propertyList);
		addProperty(RAW_VALUE_PROPERTY, propertyList);
		addProperty(TAIL_PROPERTY, propertyList);
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
	
	/**
	 * raw value of this template.
	 */
	private String rawValue;
	
	/**
	 * tail value;
	 */
	private boolean tail;

	/**
	 * @param ast
	 */
	TemplateElement(AST ast) {
		super(ast);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalStructuralPropertiesForType(int)
	 */
	List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}
	
	final Object internalGetSetObjectProperty(SimplePropertyDescriptor property, boolean get, Object value) {
		if (property == RAW_VALUE_PROPERTY) {
			if (get) {
				return getRawValue();
			} else {
				setRawValue((String)value);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetObjectProperty(property, get, value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalGetSetBooleanProperty(org.eclipse.wst.jsdt.core.dom.SimplePropertyDescriptor, boolean, boolean)
	 */
	boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		if (property == TAIL_PROPERTY) {
			if (get) {
				return isTail();
			} else {
				setTail(value);
				return value;
			}
		}	
		return super.internalGetSetBooleanProperty(property, get, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return TEMPLATE_ELEMENT;
	}

	/**
	 * Returns the raw value of the template element.
	 *  
	 * @return the value of the element
	 */
	public String getRawValue() {
		return rawValue;
	}
	/**
	 * Sets the raw value of this template element node to the given string.
	 *
	 * @param raw value
	 * @exception IllegalArgumentException if the argument is incorrect
	 */
	public void setRawValue(String rawValue) {
		if (rawValue == null ){
			throw new IllegalArgumentException();
		}
		preValueChange(RAW_VALUE_PROPERTY);
		this.rawValue = rawValue;
		postValueChange(RAW_VALUE_PROPERTY);
	}
	
	/**
	 * Returns if this element is tailing element or not
	 * 
	 * @return true if this is a tailing element
	 */
	public boolean isTail() {
		return tail;
	}
	/**
	 * Sets if the element is the tailing element or not
	 * 
	 * @param true if this is the tailing element
	 */
	public void setTail(boolean tail) {
		preValueChange(TAIL_PROPERTY);
		this.tail = tail;
		postValueChange(TAIL_PROPERTY);
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
		TemplateElement result = new TemplateElement(target);
		result.setRawValue(getRawValue());
		result.setTail(isTail());
		result.setSourceRange(this.getStartPosition(), this.getLength());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		int size = BASE_NODE_SIZE + 2 * 4 + stringSize(rawValue);
		return size;
	}

}
