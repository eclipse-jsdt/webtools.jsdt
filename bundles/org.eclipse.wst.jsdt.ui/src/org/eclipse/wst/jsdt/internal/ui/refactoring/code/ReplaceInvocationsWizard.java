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
package org.eclipse.wst.jsdt.internal.ui.refactoring.code;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.wst.jsdt.internal.corext.refactoring.code.ReplaceInvocationsRefactoring;
import org.eclipse.wst.jsdt.internal.ui.refactoring.RefactoringMessages;

public class ReplaceInvocationsWizard extends RefactoringWizard {
	
	public ReplaceInvocationsWizard(ReplaceInvocationsRefactoring ref){
		super(ref, DIALOG_BASED_USER_INTERFACE);
		setDefaultPageTitle(RefactoringMessages.ReplaceInvocationsWizard_title);  
	}

	protected void addUserInputPages(){
		addPage(new ReplaceInvocationsInputPage());
	}
}
