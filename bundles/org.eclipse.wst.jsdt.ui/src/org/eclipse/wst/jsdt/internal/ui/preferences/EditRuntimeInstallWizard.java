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

import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;

public class EditRuntimeInstallWizard extends RuntimeInstallWizard {
	
	private AbstractRuntimeInstallPage fEditPage;
	
	/**
	 * Constructs a wizard to edit the given runtime.
	 * 
	 * @param runtimeInstall runtime install to edit
	 * @param allRuntimes all runtime installs being edited
	 */
	public EditRuntimeInstallWizard(JSRuntimeWorkingCopy runtimeInstall, IJSRuntimeType type, IJSRuntimeInstall[] allRuntimes) {
		super(runtimeInstall, type, allRuntimes);
		setWindowTitle(PreferencesMessages.EditRuntimeInstallWizard_WindowTitle);
	}

	@Override
	public void addPages() {
		fEditPage = getPage();
		fEditPage.setSelection(new JSRuntimeWorkingCopy(getRuntimeInstall()));
		addPage(fEditPage);
	}

	@Override
	public boolean performFinish() {
		if (fEditPage.finish()) {
			return super.performFinish();
		}
		return false;
	}

	@Override
	protected JSRuntimeWorkingCopy getResult() {
		return fEditPage.getSelection();
	}	
}
