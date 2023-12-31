/*******************************************************************************
 * Copyright (c) 2005, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.jsdt.internal.ui.IProductConstants;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.Logger;
import org.eclipse.wst.jsdt.internal.ui.ProductProperties;

public class NewJSWizard extends Wizard implements INewWizard {
	
	private NewJSFileWizardPage		fNewFilePage;
	private IStructuredSelection	fSelection;
	private IWorkbench fWorkbench;

	public void addPages() {
		fNewFilePage = new NewJSFileWizardPage("JSWizardNewFileCreationPage", new StructuredSelection(IDE.computeSelectedResources(fSelection))); //$NON-NLS-1$
		fNewFilePage.setTitle(NewWizardMessages.Javascript_UI_Wizard_New_Heading);
		fNewFilePage.setDescription(NewWizardMessages.Javascript_UI_Wizard_New_Description);
		addPage(fNewFilePage);
	}

	public void init(IWorkbench workbench, IStructuredSelection aSelection) {
		fWorkbench = workbench;
		fSelection = aSelection;
		setWindowTitle(NewWizardMessages.Javascript_UI_Wizard_New_Title);
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWJSFILE);
	}

	private void openEditor(final IFile file) {
		if (file != null) {
			fWorkbench.getDisplay().asyncExec(new Runnable() {
				public void run() {
					try {
						IWorkbenchWindow activeWorkbenchWindow = fWorkbench.getActiveWorkbenchWindow();
						if (activeWorkbenchWindow != null) {
							IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
							if (page != null) {
								String editorId = ProductProperties.getProperty(IProductConstants.NEW_FILE_EDITOR);
								if (editorId != null) {
									Logger.log(Logger.INFO_DEBUG, "Opening new JS file in product-specified " + editorId); //$NON-NLS-1$
									IDE.openEditor(page, file, editorId);
								}
								else {
									Logger.log(Logger.INFO_DEBUG, "Opening new JS file in default editor for content type"); //$NON-NLS-1$
									IDE.openEditor(page, file, true, true);
								}
							}
						}
					}
					catch (PartInitException e) {
						JavaScriptPlugin.log(e);
					}
				}
			});
		}
	}

	public boolean performFinish() {
		boolean performedOK = false;

		// no file extension specified so add default extension
		String fileName = fNewFilePage.getFileName();
		if (fileName.lastIndexOf('.') == -1) {
			String newFileName = fNewFilePage.addDefaultExtension(fileName);
			fNewFilePage.setFileName(newFileName);
		}

		// create a new empty file
		IFile file = fNewFilePage.createNewFile();
		// add comment to created file
		fNewFilePage.addFileComment(file, false);
		// if there was problem with creating file, it will be null, so make
		// sure to check
		if (file != null) {
			// open the file in editor
			openEditor(file);

			// everything's fine
			performedOK = true;
		}
		return performedOK;
	}

}
