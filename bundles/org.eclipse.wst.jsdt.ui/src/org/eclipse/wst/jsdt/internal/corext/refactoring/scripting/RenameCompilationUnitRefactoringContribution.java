/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.scripting;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.wst.jsdt.core.refactoring.IJavaScriptRefactorings;
import org.eclipse.wst.jsdt.core.refactoring.descriptors.RenameJavaScriptElementDescriptor;
import org.eclipse.wst.jsdt.internal.corext.refactoring.JDTRefactoringContribution;
import org.eclipse.wst.jsdt.internal.corext.refactoring.rename.JavaRenameRefactoring;
import org.eclipse.wst.jsdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;

/**
 * Refactoring contribution for the rename compilation unit refactoring.
 * 
 * 
 */
public final class RenameCompilationUnitRefactoringContribution extends JDTRefactoringContribution {

	/**
	 * {@inheritDoc}
	 */
	public Refactoring createRefactoring(final RefactoringDescriptor descriptor) throws CoreException {
		return new JavaRenameRefactoring(new RenameCompilationUnitProcessor(null));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public RefactoringDescriptor createDescriptor() {
		return new RenameJavaScriptElementDescriptor(IJavaScriptRefactorings.RENAME_JAVASCRIPT_UNIT);
	}
}
