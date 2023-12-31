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
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptElementDelta;
import org.eclipse.wst.jsdt.core.IProblemRequestor;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;

/**
 * Switch and IJavaScriptUnit to working copy mode
 * and signal the working copy addition through a delta.
 */
public class BecomeWorkingCopyOperation extends JavaModelOperation {

	IProblemRequestor problemRequestor;

	/*
	 * Creates a BecomeWorkingCopyOperation for the given working copy.
	 * perOwnerWorkingCopies map is not null if the working copy is a shared working copy.
	 */
	public BecomeWorkingCopyOperation(CompilationUnit workingCopy, IProblemRequestor problemRequestor) {
		super(new IJavaScriptElement[] {workingCopy});
		this.problemRequestor = problemRequestor;
	}
	protected void executeOperation() throws JavaScriptModelException {

		// open the working copy now to ensure contents are that of the current state of this element
		CompilationUnit workingCopy = getWorkingCopy();
		JavaModelManager.getJavaModelManager().getPerWorkingCopyInfo(workingCopy, true/*create if needed*/, true/*record usage*/, this.problemRequestor);
		workingCopy.openWhenClosed(workingCopy.createElementInfo(), this.progressMonitor);

		if (!workingCopy.isPrimary()) {
			// report added java delta for a non-primary working copy
			JavaElementDelta delta = new JavaElementDelta(getJavaModel());
			delta.added(workingCopy);
			addDelta(delta);
		} else {
			if (workingCopy.getResource().isAccessible()) {
				// report a F_PRIMARY_WORKING_COPY change delta for a primary working copy
				JavaElementDelta delta = new JavaElementDelta(getJavaModel());
				delta.changed(workingCopy, IJavaScriptElementDelta.F_PRIMARY_WORKING_COPY);
				addDelta(delta);
			} else {
				// report an ADDED delta
				JavaElementDelta delta = new JavaElementDelta(this.getJavaModel());
				delta.added(workingCopy, IJavaScriptElementDelta.F_PRIMARY_WORKING_COPY);
				addDelta(delta);
			}
		}

		this.resultElements = new IJavaScriptElement[] {workingCopy};
	}
	/*
	 * Returns the working copy this operation is working on.
	 */
	protected CompilationUnit getWorkingCopy() {
		return (CompilationUnit)getElementToProcess();
	}
	/*
	 * @see JavaModelOperation#isReadOnly
	 */
	public boolean isReadOnly() {
		return true;
	}

}
