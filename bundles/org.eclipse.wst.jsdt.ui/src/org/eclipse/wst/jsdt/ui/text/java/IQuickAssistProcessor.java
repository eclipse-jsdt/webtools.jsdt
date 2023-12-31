/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.text.java;

import org.eclipse.core.runtime.CoreException;


/**
 * Interface to be implemented by contributors to the extension point
 * <code>org.eclipse.wst.jsdt.ui.quickAssistProcessors</code>.
 *
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves. */
public interface IQuickAssistProcessor {

	/**
	 * Evaluates if quick assists can be created for the given context. This evaluation must be precise.
	 *
	 * @param context The invocation context
	 * @return Returns <code>true</code> if quick assists can be created
	 * @throws CoreException CoreException can be thrown if the operation fails
	 */
	boolean hasAssists(IInvocationContext context) throws CoreException;

	/**
	 * Collects quick assists for the given context.
	 *
	 * @param context Defines current compilation unit, position and a shared AST
	 * @param locations The locations of problems at the invocation offset. The processor can decide to only
	 * 			add assists when there are no errors at the selection offset.
	 * @return Returns the assists applicable at the location or <code>null</code> if no proposals
	 * 			can be offered.
	 * @throws CoreException CoreException can be thrown if the operation fails
	 */
	IJavaCompletionProposal[] getAssists(IInvocationContext context, IProblemLocation[] locations) throws CoreException;

}
