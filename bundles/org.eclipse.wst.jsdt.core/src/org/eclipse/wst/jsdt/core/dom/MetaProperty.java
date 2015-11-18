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
 * MetaProperty node 
 * <pre>
 * 	new.target
 * </pre>
 * @author Gorkem Ercan
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class MetaProperty extends Expression {

	/**
	 * The "meta" structural property of this node type.
	 *  
	 */
	public static final SimplePropertyDescriptor META_PROP_PROPERTY =
		new SimplePropertyDescriptor(MetaProperty.class, "meta", String.class, MANDATORY); //$NON-NLS-1$
	/**
	 * The "propertyName" structural property of this node type.
	 *  
	 */
	public static final SimplePropertyDescriptor PROPERTY_NAME_PROPERTY =
		new SimplePropertyDescriptor(MetaProperty.class, "propertyName", String.class, MANDATORY); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(3);
		createPropertyList(MetaProperty.class, propertyList);
		addProperty(META_PROP_PROPERTY, propertyList);
		addProperty(PROPERTY_NAME_PROPERTY, propertyList);
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
	
	private String meta;
	private String propertyName;
	
	/**
	 * @param ast
	 */
	MetaProperty(AST ast) {
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
		if (property == META_PROP_PROPERTY) {
			if (get) {
				return getMeta();
			} else {
				setMeta((String) value);
				return null;
			}
		}
		if (property == PROPERTY_NAME_PROPERTY) {
			if (get) {
				return getPropertyName();
			} else {
				setPropertyName((String) value);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetObjectProperty(property, get, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return META_PROPERTY;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		if(meta == null){
			throw new IllegalArgumentException();
		}
		preValueChange(META_PROP_PROPERTY);
		this.meta = meta;
		postValueChange(META_PROP_PROPERTY);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		if( propertyName == null){
			throw new IllegalArgumentException();
		}
		preValueChange(PROPERTY_NAME_PROPERTY);
		this.propertyName = propertyName;
		postValueChange(PROPERTY_NAME_PROPERTY);	
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
		MetaProperty result = new MetaProperty(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setPropertyName( this.getPropertyName());
		result.setMeta(this.getMeta());
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
		return memSize() ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return BASE_NODE_SIZE + 2 * 4 + stringSize(meta) + stringSize(propertyName);
	}

}
