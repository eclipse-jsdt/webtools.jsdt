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
package org.eclipse.wst.jsdt.internal.ui.model;

import org.eclipse.ltk.core.refactoring.RefactoringDescriptorProxy;
import org.eclipse.ltk.core.refactoring.model.AbstractRefactoringDescriptorResourceMapping;

/**
 * Refactoring descriptor resource mapping for the Java model provider.
 * 
 * 
 */
public final class JavaRefactoringDescriptorResourceMapping extends AbstractRefactoringDescriptorResourceMapping {

	/**
	 * Creates a new java refactoring descriptor resource mapping.
	 * 
	 * @param descriptor
	 *            the refactoring descriptor
	 */
	public JavaRefactoringDescriptorResourceMapping(final RefactoringDescriptorProxy descriptor) {
		super(descriptor);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getModelProviderId() {
		return JavaModelProvider.JAVA_MODEL_PROVIDER_ID;
	}
}
