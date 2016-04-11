/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;

public abstract class RuntimeInstallWizard extends Wizard {
	
	private IJSRuntimeType fRuntimeType;
	private JSRuntimeWorkingCopy fEditRuntime;
	private String[] fExistingNames;
	
	/**
	 * Constructs a new wizard to add/edit a runtime install.
	 * 
	 * @param editRuntime the runtime being edited, or <code>null</code> if none
	 * @param currentInstalls current runtime installs used to validate name changes
	 */
	public RuntimeInstallWizard(JSRuntimeWorkingCopy editRuntime, IJSRuntimeType runtimeType, IJSRuntimeInstall[] currentInstalls) {
		fEditRuntime = editRuntime;
		fRuntimeType = runtimeType;
		List<String> names = new ArrayList<String>(currentInstalls.length);
		for (int i = 0; i < currentInstalls.length; i++) {
			IJSRuntimeInstall install = currentInstalls[i];
			if (!install.equals(editRuntime)) {
				names.add(install.getName());
			}
		}
		fExistingNames = names.toArray(new String[names.size()]);
	}
	
	/**
	 * Returns the runtime to edit, or <code>null</code> if creating a runtime
	 * 
	 * @return runtime to edit or <code>null</code>
	 */
	protected JSRuntimeWorkingCopy getRuntimeInstall() {
		return fEditRuntime;
	}
	
	/**
	 * Returns the resulting runtime after edit or creation or <code>null</code> if none.
	 * 
	 * @return resulting runtime
	 */
	protected abstract JSRuntimeWorkingCopy getResult();

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return getResult() != null;
	}
	
	/**
	 * Returns a page to use for editing a runtime install type
	 * 
	 * @param type
	 * @return
	 */
	public AbstractRuntimeInstallPage getPage() {
		StandardRuntimeInstallPage standardVMPage = new StandardRuntimeInstallPage();
		standardVMPage.setExistingNames(fExistingNames);
		return standardVMPage;
	}
	
	public IJSRuntimeType getRuntimeType () {
		return fRuntimeType;
	}

}
