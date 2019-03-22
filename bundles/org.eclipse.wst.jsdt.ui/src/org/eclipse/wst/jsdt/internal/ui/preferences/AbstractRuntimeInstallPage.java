/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 
package org.eclipse.wst.jsdt.internal.ui.preferences;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;

public abstract class AbstractRuntimeInstallPage extends WizardPage {
	
	/**
	 * Name of the original runtime being edited, or <code>null</code> if none.
	 */
	private String fOriginalName = null;
	
	/**
	 * Status of runtime name (to notify of name already in use)
	 */
	private IStatus fNameStatus = Status.OK_STATUS;
	
	private String[] fExistingNames;
		
	/**
	 * Constructs a new page with the given page name.
	 * 
	 * @param pageName the name of the page
	 */
	protected AbstractRuntimeInstallPage(String pageName) {
		super(pageName);
	}

	/**
     * Creates a new wizard page with the given name, title, and image.
     *
     * @param pageName the name of the page
     * @param title the title for this wizard page,
     *   or <code>null</code> if none
     * @param titleImage the image descriptor for the title of this wizard page,
     *   or <code>null</code> if none
     */
	protected AbstractRuntimeInstallPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * Called when the runtime install page wizard is closed by selecting 
	 * the finish button. Implementers typically override this method to 
	 * store the page result (new/changed vm install returned in 
	 * getSelection) into its model.
	 * 
	 * @return if the operation was successful. Only when returned
	 * <code>true</code>, the wizard will close.
	 */
	public abstract boolean finish();
	
	/**
	 * Returns the edited or created runtime install. This method
	 * may return <code>null</code> if no runtime install exists.
	 * 
	 * @return the edited or created runtime install.
	 */
	public abstract JSRuntimeWorkingCopy getSelection();

	/**
	 * Sets the runtime install to be edited. 
	 * 
	 * @param runtime the runitme install to edit
	 */
	public void setSelection(JSRuntimeWorkingCopy runtime) {
		fOriginalName = runtime.getName();
	}
	
	/**
	 * Updates the name status based on the new name. This method should be called
	 * by the page each time the runtime name changes.
	 * 
	 * @param newName new name of runtime install
	 */
	protected void nameChanged(String newName) {
		fNameStatus = Status.OK_STATUS;
		if (newName == null || newName.trim().length() == 0) {
			int sev = IStatus.ERROR;
			if (fOriginalName == null || fOriginalName.length() == 0) {
				sev = IStatus.WARNING;
			}
			fNameStatus = new Status(sev, JavaScriptPlugin.getPluginId(), 
						PreferencesMessages.AbstractRuntimeInstallPage_MissingRuntimeName_Message);
		} else {
			if (isDuplicateName(newName)) {
				fNameStatus = new Status(IStatus.ERROR, JavaScriptPlugin.getPluginId(), 
							NLS.bind(PreferencesMessages.AbstractRuntimeInstallPage_DuplicateName_Error, newName)); 
			} else {
				IStatus s = ResourcesPlugin.getWorkspace().validateName(newName, IResource.FILE);
				if (!s.isOK()) {
					fNameStatus = new Status(IStatus.ERROR, JavaScriptPlugin.getPluginId(), 
								NLS.bind(PreferencesMessages.AbstractRuntimeInstallPage_InvalidFileName_Error, s.getMessage())); 
				}
			}
		}
		updatePageStatus();
	}
	
	/**
	 * Returns whether the name is already in use by an existing runtime
	 * 
	 * @param name new name
	 * @return whether the name is already in use
	 */
	private boolean isDuplicateName(String name) {
		if (fExistingNames != null) {
			for (int i = 0; i < fExistingNames.length; i++) {
				if (name.equals(fExistingNames[i])) {
					return true;
				}
			}
		}
		return false;
	}	
	
	/**
	 * Sets the names of existing runtimes, not including the runtime being edited. This method
	 * is called by the wizard and clients should not call this method.
	 * 
	 * @param names existing runtime names or an empty array
	 */
	public void setExistingNames(String[] names) {
		fExistingNames = names;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage() {
		return null;
	}
	
	/**
	 * Sets this page's message based on the status severity.
	 * 
	 * @param status status with message and severity
	 */
	protected void setStatusMessage(IStatus status) {
		if (status.isOK()) {
			setMessage(status.getMessage());
		} else {
			switch (status.getSeverity()) {
			case IStatus.ERROR:
				setMessage(status.getMessage(), IMessageProvider.ERROR);
				break;
			case IStatus.INFO:
				setMessage(status.getMessage(), IMessageProvider.INFORMATION);
				break;
			case IStatus.WARNING:
				setMessage(status.getMessage(), IMessageProvider.WARNING);
				break;
			default:
				break;
			}
		}
	}	
	
	/**
	 * Returns the current status of the name being used for the runtime.
	 * 
	 * @return status of current runtime name
	 */
	protected IStatus getNameStatus() {
		return fNameStatus;
	}
	
	/**
	 * Updates the status message on the page, based on the status of the VM and other
	 * status provided by the page.
	 */
	protected void updatePageStatus() {
		IStatus max = Status.OK_STATUS;
		IStatus[] vmStatus = getRuntimeStatus();
		for (int i = 0; i < vmStatus.length; i++) {
			IStatus status = vmStatus[i];
			if (status.getSeverity() > max.getSeverity()) {
				max = status;
			}
		}
		if (fNameStatus.getSeverity() > max.getSeverity()) {
			max = fNameStatus;
		}
		if (max.isOK()) {
			setMessage(null, IMessageProvider.NONE);
		} else {
			setStatusMessage(max);
		}
		setPageComplete(max.isOK() || max.getSeverity() == IStatus.INFO);
	}	
	
	/**
	 * Returns a collection of status messages pertaining to the current edit
	 * status of the runtime on this page. An empty collection or a collection of
	 * OK status objects indicates all is well.
	 * 
	 * @return collection of status objects for this page
	 */
	protected abstract IStatus[] getRuntimeStatus();
}
