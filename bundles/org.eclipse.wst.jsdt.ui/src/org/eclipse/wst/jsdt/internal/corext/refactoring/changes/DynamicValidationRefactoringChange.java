/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.changes;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.ChangeDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringChangeDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.wst.jsdt.core.refactoring.descriptors.JavaScriptRefactoringDescriptor;
import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringCoreMessages;

/**
 * Dynamic validation state change with support for refactoring descriptors.
 * 
 * 
 */
public final class DynamicValidationRefactoringChange extends DynamicValidationStateChange {

	/** The refactoring descriptor */
	private final RefactoringDescriptor fDescriptor;

	/**
	 * Creates a new dynamic validation refactoring change.
	 * 
	 * @param descriptor
	 *            the refactoring descriptor
	 * @param name
	 *            the name of the change
	 */
	public DynamicValidationRefactoringChange(final JavaScriptRefactoringDescriptor descriptor, final String name) {
		super(name);
		Assert.isNotNull(descriptor);
		fDescriptor= descriptor;
	}

	/**
	 * Creates a new dynamic validation refactoring change.
	 * 
	 * @param descriptor
	 *            the refactoring descriptor
	 * @param name
	 *            the name of the change
	 * @param changes
	 *            the changes
	 */
	public DynamicValidationRefactoringChange(final JavaScriptRefactoringDescriptor descriptor, final String name, final Change[] changes) {
		super(name, changes);
		Assert.isNotNull(descriptor);
		Assert.isTrue(!descriptor.validateDescriptor().hasFatalError(), RefactoringCoreMessages.DynamicValidationRefactoringChange_fatal_error);
		fDescriptor= descriptor;
	}

	/**
	 * {@inheritDoc}
	 */
	public ChangeDescriptor getDescriptor() {
		return new RefactoringChangeDescriptor(fDescriptor);
	}
}
