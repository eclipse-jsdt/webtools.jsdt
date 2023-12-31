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
package org.eclipse.wst.jsdt.ui.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.wst.jsdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.wst.jsdt.internal.ui.dialogs.StatusUtil;

/**
 * Base class for wizard page responsible to create JavaScript elements. The class
 * provides API to update the wizard's status line and OK button according to
 * the value of a <code>IStatus</code> object.
 * 
 * <p>
 * Clients may subclass.
 * </p>
 * 
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves. */
public abstract class NewElementWizardPage extends WizardPage {

	private IStatus fCurrStatus;
	
	private boolean fPageVisible;

	/**
	 * Creates a <code>NewElementWizardPage</code>.
	 * 
	 * @param name the wizard page's name
	 */	
	public NewElementWizardPage(String name) {
		super(name);
		fPageVisible= false;
		fCurrStatus=  new StatusInfo();
	}
		
	// ---- WizardPage ----------------
	
	/*
	 * @see WizardPage#becomesVisible
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		fPageVisible= visible;
		// policy: wizards are not allowed to come up with an error message
		if (visible && fCurrStatus.matches(IStatus.ERROR)) {
			StatusInfo status= new StatusInfo();
			status.setError("");  //$NON-NLS-1$
			fCurrStatus= status;
		} 
		updateStatus(fCurrStatus);
	}	

	/**
	 * Updates the status line and the OK button according to the given status
	 * 
	 * @param status status to apply
	 */
	protected void updateStatus(IStatus status) {
		fCurrStatus= status;
		setPageComplete(!status.matches(IStatus.ERROR));
		if (fPageVisible) {
			StatusUtil.applyToStatusLine(this, status);
		}
	}
	
	/**
	 * Updates the status line and the OK button according to the status evaluate from
	 * an array of status. The most severe error is taken.  In case that two status with 
	 * the same severity exists, the status with lower index is taken.
	 * 
	 * @param status the array of status
	 */
	protected void updateStatus(IStatus[] status) {
		updateStatus(StatusUtil.getMostSevere(status));
	}	
			
}
