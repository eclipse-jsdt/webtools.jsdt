/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.wst.jsdt.js.gulp.internal;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;

public class GulpProjectPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IContainer container = null;

		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IWorkbenchPart part = page.getActivePart();
				if (part != null) {
					IFile activeEditorFile = getEditorFile(part);

					if (activeEditorFile != null) {
						if (RunGulpTaskCommand.GULP_FILE_NAME.equals(activeEditorFile.getName())) {
							return true;
						} else {
							container = activeEditorFile.getProject();
						}
					}
				}

				if (container == null) {
					IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
					if (selection instanceof TreeSelection) {
						TreeSelection treeSelection = (TreeSelection) selection;
						Object element = treeSelection.getFirstElement();

						if (element != null) {
							element.toString();
							if (element instanceof IFile
									&& RunGulpTaskCommand.GULP_FILE_NAME.equals(((IFile) element).getName())) {
								return true;
							} else if (element instanceof ITask) {
								return true;
							}
						}

						TreePath path = treeSelection.getPaths()[0];

						Object first = path.getFirstSegment();
						if (first instanceof IAdaptable) {
							container = ((IAdaptable) first).getAdapter(IContainer.class);
						}
					} else {
						Object firstElement = selection.getFirstElement();
						if (firstElement instanceof IAdaptable) {
							container = ((IAdaptable) firstElement).getAdapter(IContainer.class);
						}
					}
				}

			}
		}

		if (container != null) {
			try {
				IFile file = WorkbenchResourceUtil.findFileRecursively(container, RunGulpTaskCommand.GULP_FILE_NAME);

				return file != null && file.exists();
			} catch (CoreException ex) {
			}
		}

		return false;
	}

	private static IFile getEditorFile(IWorkbenchPart part) {
		IFile file = null;

		if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;

			if (editorPart.getEditorInput() instanceof IFileEditorInput) {
				IFileEditorInput fileEditorInput = (IFileEditorInput) editorPart.getEditorInput();
				file = fileEditorInput.getFile();
			}
		}

		return file;
	}

}
