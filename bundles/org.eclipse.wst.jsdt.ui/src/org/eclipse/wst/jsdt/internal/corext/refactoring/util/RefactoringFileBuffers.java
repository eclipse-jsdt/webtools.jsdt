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
package org.eclipse.wst.jsdt.internal.corext.refactoring.util;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;


/**
 * Helper methods to deal with file buffers in refactorings.
 */
public final class RefactoringFileBuffers {

	/**
	 * Connects to and acquires a text file buffer for the specified compilation unit.
	 * <p>
	 * All text file buffers acquired by a call to {@link RefactoringFileBuffers#acquire(IJavaScriptUnit)}
	 * must be released using {@link RefactoringFileBuffers#release(IJavaScriptUnit)}.
	 * </p>
	 * 
	 * @param unit the compilation unit to acquire a text file buffer for
	 * @return the text file buffer, or <code>null</code> if no buffer could be acquired
	 * @throws CoreException if no buffer could be acquired
	 */
	public static ITextFileBuffer acquire(final IJavaScriptUnit unit) throws CoreException {
		Assert.isNotNull(unit);
		final IResource resource= unit.getResource();
		if (resource != null && resource.getType() == IResource.FILE) {
			final IPath path= resource.getFullPath();
			FileBuffers.getTextFileBufferManager().connect(path, LocationKind.IFILE, new NullProgressMonitor());
			return FileBuffers.getTextFileBufferManager().getTextFileBuffer(path, LocationKind.IFILE);
		}
		return null;
	}

	/**
	 * Returns the text file buffer for the specified compilation unit.
	 * 
	 * @param unit the compilation unit whose text file buffer to retrieve
	 * @return the associated text file buffer, or <code>null</code> if no text file buffer is managed for the compilation unit
	 */
	public static ITextFileBuffer getTextFileBuffer(final IJavaScriptUnit unit) {
		Assert.isNotNull(unit);
		final IResource resource= unit.getResource();
		if (resource == null || resource.getType() != IResource.FILE)
			return null;
		return FileBuffers.getTextFileBufferManager().getTextFileBuffer(resource.getFullPath(), LocationKind.IFILE);
	}

	/**
	 * Releases the text file buffer associated with the compilation unit.
	 * 
	 * @param unit the compilation unit whose text file buffer has to be released
	 * @throws CoreException if the buffer could not be successfully released
	 */
	public static void release(final IJavaScriptUnit unit) throws CoreException {
		Assert.isNotNull(unit);
		final IResource resource= unit.getResource();
		if (resource != null && resource.getType() == IResource.FILE)
			FileBuffers.getTextFileBufferManager().disconnect(resource.getFullPath(), LocationKind.IFILE, new NullProgressMonitor());
	}

	private RefactoringFileBuffers() {
		// Not for instantiation
	}
}
