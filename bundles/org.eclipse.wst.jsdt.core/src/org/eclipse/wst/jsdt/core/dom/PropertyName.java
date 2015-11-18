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

/**
 * AST node for a property name. A property name is a {@link SimpleName} except that
 * it allows keywords. 
 * 
 * <pre>
 * SimpleName:
 *     IdentifierName
 * </pre>
 * 
 * @author Gorkem Ercan
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 *
 */
public class PropertyName extends SimpleName {

	/**
	 * @param ast
	 */
	PropertyName(AST ast) {
		super(ast);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.SimpleName#setIdentifier(java.lang.String)
	 */
	public void setIdentifier(String identifier) {
		// update internalSetIdentifier if this is changed
		if (identifier == null) {
			throw new IllegalArgumentException();
		}
		internalSetIdentifier(identifier);
	}

}
