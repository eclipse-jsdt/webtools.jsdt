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
package org.eclipse.wst.jsdt.core.refactoring.descriptors;

import org.eclipse.wst.jsdt.core.refactoring.IJavaScriptRefactorings;

/**
 * Refactoring descriptor for the convert member type refactoring.
 * <p>
 * An instance of this refactoring descriptor may be obtained by calling
 * {@link org.eclipse.ltk.core.refactoring.RefactoringContribution#createDescriptor()} on a refactoring
 * contribution requested by invoking
 * {@link org.eclipse.ltk.core.refactoring.RefactoringCore#getRefactoringContribution(String)} with the
 * appropriate refactoring id.
 * </p>
 * <p>
 * Note: this class is not intended to be instantiated by clients.
 * </p>
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public final class ConvertMemberTypeDescriptor extends JavaScriptRefactoringDescriptor {

	/**
	 * Creates a new refactoring descriptor.
	 */
	public ConvertMemberTypeDescriptor() {
		super(IJavaScriptRefactorings.CONVERT_MEMBER_TYPE);
	}
}
