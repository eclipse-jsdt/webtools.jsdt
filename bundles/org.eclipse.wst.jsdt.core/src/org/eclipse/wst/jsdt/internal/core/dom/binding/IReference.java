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
package org.eclipse.wst.jsdt.internal.core.dom.binding;

/**
 *
 * Reference to the symbol.
 * <p>
 * In the following example there are two declarations: a,b and two references
 * c,d
 *
 * <pre>
 * function a(b) {
 *   return c+d;
 * }
 * </pre>
 * <p>
 * 
 * @since 2.0
 *
 */
public interface IReference extends ISymbolBase {

	/**
	 * Return the declaration this symbol refers to. May be null
	 *
	 * @return the declaration this symbol refers to.
	 */
	IDeclaration getDeclaration();

}
