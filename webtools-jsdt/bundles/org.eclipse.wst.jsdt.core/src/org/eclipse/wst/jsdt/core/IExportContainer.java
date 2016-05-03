/*******************************************************************************
 * Copyright (c) 2016, Red Hat and others.
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
 * Represents an export container; a child of a JavaScript unit that contains
 * all (and only) the export declarations. If a JavaScript unit has no export
 * declarations, no export container will be present.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 */
public interface IExportContainer extends IJavaScriptElement, IParent, ISourceReference {
	/**
	 * Returns the first export declaration in this export container with the given name.
	 * This is a handle-only method. The export declaration may or may not exist.
	 *
	 * @param name the given name
	 *
	 * @return the first export declaration in this export container with the given name
	 */
	IExportDeclaration getExport(String name);
}
