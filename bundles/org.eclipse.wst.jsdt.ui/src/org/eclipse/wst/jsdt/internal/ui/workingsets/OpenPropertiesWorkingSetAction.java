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
package org.eclipse.wst.jsdt.internal.ui.workingsets;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetEditWizard;
import org.eclipse.wst.jsdt.ui.actions.SelectionDispatchAction;


public class OpenPropertiesWorkingSetAction extends SelectionDispatchAction {

	public OpenPropertiesWorkingSetAction(IWorkbenchSite site) {
		super(site);
		setText(WorkingSetMessages.OpenPropertiesWorkingSetAction_label); 
		setEnabled(false);
	}
	
	public void selectionChanged(IStructuredSelection selection) {
		setEnabled(getWorkingSet(selection) != null);
	}

	private IWorkingSet getWorkingSet(IStructuredSelection selection) {
		if (selection.size() != 1)
			return null;
		Object element= selection.getFirstElement();
		if (!(element instanceof IWorkingSet))
			return null;
		IWorkingSet ws= (IWorkingSet)element;
		if (!ws.isEditable())
			return null;
		return ws;
	}
	
	public void run(IStructuredSelection selection) {
		IWorkingSet ws= getWorkingSet(selection);
		if (ws == null)
			return;
		IWorkingSetEditWizard wizard= PlatformUI.getWorkbench().
			getWorkingSetManager().createWorkingSetEditWizard(ws);
		WizardDialog dialog= new WizardDialog(getShell(), wizard);
		dialog.open();
	}
}
