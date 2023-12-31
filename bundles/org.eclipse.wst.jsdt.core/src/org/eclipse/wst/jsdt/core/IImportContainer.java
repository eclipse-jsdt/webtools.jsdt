/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

/**
 * Represents an import container is a child of a JavaScript unit that contains
 * all (and only) the import declarations. If a JavaScript unit has no import
 * declarations, no import container will be present.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * 
 * <b>This Interface only applies to ECMAScript 4 which is not yet supported</b>
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public interface IImportContainer extends IJavaScriptElement, IParent, ISourceReference {
/**
 * Returns the first import declaration in this import container with the given name.
 * This is a handle-only method. The import declaration may or may not exist.
 *
 * @param name the given name
 *
 * @return the first import declaration in this import container with the given name
 */
IImportDeclaration getImport(String name);
}
