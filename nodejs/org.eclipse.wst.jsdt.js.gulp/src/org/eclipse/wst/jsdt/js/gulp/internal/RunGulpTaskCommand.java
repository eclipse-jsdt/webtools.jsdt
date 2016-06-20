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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;
import org.eclipse.wst.jsdt.js.gulp.internal.launch.shortcut.GulpLaunch;

/**
 * Provides a command to the quick start that allows either a selected Gulp
 * task, or the default Gulp task in the selected project to be run.
 *
 * @author Shane Bryzak
 */
public class RunGulpTaskCommand extends AbstractHandler {

	public static final String GULP_FILE_NAME = "gulpfile.js";
	public static final String GULP_TASK_RUN = "run";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IContainer container = null;

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IWorkbenchPart part = page.getActivePart();
				if (part != null) {
					IFile activeEditorFile = getEditorFile(part);

					if (activeEditorFile != null) {
						if (GULP_FILE_NAME.equals(activeEditorFile.getName())) {
							new GulpLaunch().launch(new StructuredSelection(activeEditorFile), GULP_TASK_RUN);
							return null;
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
							if (element instanceof IFile && GULP_FILE_NAME.equals(((IFile) element).getName())) {
								new GulpLaunch().launch(treeSelection, GULP_TASK_RUN);
								return null;
							} else if (element instanceof ITask) {
								new GulpLaunch().launch(selection, GULP_TASK_RUN);
								return null;
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
				IFile file = WorkbenchResourceUtil.findFileRecursively(container, GULP_FILE_NAME);

				if (file.exists()) {
					StructuredSelection fileSelection = new StructuredSelection(file);

					new GulpLaunch().launch(fileSelection, GULP_TASK_RUN);
				}
			} catch (CoreException ex) {
				// Don't launch gulp
			}
		}

		return null;
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
