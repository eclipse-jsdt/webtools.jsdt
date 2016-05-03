/*******************************************************************************
 * Copyright (c) 2016 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
 
package org.eclipse.wst.jsdt.core;

/**
 * Represents an export declaration in JavaScript unit.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p> 
 */
public interface IExportDeclaration extends IJavaScriptElement, ISourceReference, ISourceManipulation {
	/**
	 * Returns the name that has been exported.
	 *
	 * @return the name that has been exported
	 */
	String getElementName();
}
