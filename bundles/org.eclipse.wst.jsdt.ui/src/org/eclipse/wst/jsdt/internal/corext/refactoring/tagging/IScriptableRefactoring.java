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
package org.eclipse.wst.jsdt.internal.corext.refactoring.tagging;

import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;

/**
 * Interface for refactorings which can be initialized when run as a refactoring
 * script.
 * 
 * 
 */
public interface IScriptableRefactoring {

	/**
	 * Initializes the refactoring with the refactoring arguments.
	 * 
	 * @param arguments
	 *            the refactoring arguments
	 * @return an object describing the status of the initialization. If the
	 *         status has severity <code>FATAL_ERROR</code>, the refactoring
	 *         will not be executed.
	 */
	public RefactoringStatus initialize(RefactoringArguments arguments);
}
