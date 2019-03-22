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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;

public class AddRuntimeInstallWizard extends RuntimeInstallWizard {
	private IWizardPage fRuntimeInstallPage = null;
	
	private JSRuntimeWorkingCopy fResult = null;

	/**
	 * Constructs a wizard to add a new runtime install.
	 * 
	 * @param currentInstalls currently existing runtimes, used for name validation
	 */
	public AddRuntimeInstallWizard(IJSRuntimeType type, IJSRuntimeInstall[] currentInstalls) {
		super(null, type, currentInstalls);
		setForcePreviousAndNextButtons(false);
		setWindowTitle(PreferencesMessages.AddRuntimeInstallWizard_WindowTitle);
	}

	@Override
	public void addPages() {
		AbstractRuntimeInstallPage page = getPage();
		page.setWizard(this);
		JSRuntimeWorkingCopy standin = new JSRuntimeWorkingCopy(getRuntimeType(), 
					StandardRuntimeInstallPage.createUniqueId(getRuntimeType()));
		standin.setName(""); //$NON-NLS-1$
		page.setSelection(standin);
		fRuntimeInstallPage = page;
		addPage (fRuntimeInstallPage);
	}

	@Override
	protected JSRuntimeWorkingCopy getResult() {
		return fResult;
	}

	@Override
	public boolean canFinish() {
		IWizardPage currentPage = getContainer().getCurrentPage();
		return super.canFinish() && currentPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		IWizardPage currentPage = getContainer().getCurrentPage();
		if (currentPage instanceof AbstractRuntimeInstallPage) {
			AbstractRuntimeInstallPage page = (AbstractRuntimeInstallPage) currentPage;
			boolean finish = page.finish();
			fResult = page.getSelection();
			return finish;
		}
		return false;
	}	
}
