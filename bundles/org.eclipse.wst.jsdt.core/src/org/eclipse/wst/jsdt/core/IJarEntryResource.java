/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;

/**
 * A jar entry corresponding to a non-Java resource in an archive {@link IPackageFragment} or {@link IPackageFragmentRoot}.
 * <p>
 * One can navigate the non-Java resource tree using the {@link #getChildren()} and {@link #getParent()} methods.
 * Jar entry resources are either files ({@link #isFile()} returns true) or directories ({@link #isFile()} returns false).
 * Files don't have any children and the returned array is always empty.
 * </p><p>
 * Jar entry resources that refer to the same element are guaranteed to be equal, but not necessarily identical.
 * <p>
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public interface IJarEntryResource extends IStorage {

	/**
	 * Returns the list of children of this jar entry resource.
	 * Returns an empty array if this jar entry is a file, or if this jar entry is a directory and it has no children.
	 *
	 * @return the children of this jar entry resource
	 */
	IJarEntryResource[] getChildren();

	/**
	 * Returns the full, absolute path of this jar entry resource relative to the archive this jar
	 * entry belongs to.
	 * <p>
	 * A jar entry resource's full path indicates the route from the root of the archive
	 * to the jar entry resource.  Within an archive, there is exactly one such path
	 * for any given jar entry resource. The returned path never has a trailing separator.
	 * </p>
	 *
	 * @return the absolute path of this jar entry resource
	 */
	IPath getFullPath();

	/**
	 * Returns the parent of this jar entry resource. This is either an {@link IJarEntryResource}, an {@link IPackageFragment}
	 * or an {@link IPackageFragmentRoot}.
	 *
	 * @return the parent of this jar entry resource
	 */
	Object getParent();

	/**
	 * Returns the package fragment root this jar entry file belongs to.
	 *
	 * @return the package fragment root this jar entry file belongs to.
	 */
	IPackageFragmentRoot getPackageFragmentRoot();

	/**
	 * Returns <code>true</code> if this jar entry represents a file.
	 * Returns <code>false</code> if it is a directory.
	 *
	 * @return whether this jar entry is a file
	 */
	boolean isFile();

}
