/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.jsdt.js.common.CommonPlugin;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public final class WorkbenchResourceUtil {

	private WorkbenchResourceUtil() {
	}
	
	/**
	 * Must be called from SWT UI thread only
	 * @return The active {@link Shell}
	 */
	public static Shell getActiveShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	/**
	 * Must be called from SWT UI thread only
	 * @return The active {@link IWorkbenchPart}
	 */
	public static IWorkbenchPart getActivePart() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
	}

	public static void openInEditor(final IFile file, String editorID) throws PartInitException {
		IEditorRegistry editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
		if (editorID == null || editorRegistry.findEditor(editorID) == null) {
			editorID = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getFullPath().toString()).getId();
		}

		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.openEditor(new FileEditorInput(file), editorID, true, IWorkbenchPage.MATCH_ID);
	}

	public static void createFile(final IFile file, final String content) throws CoreException {
		if (!file.exists()) {
			InputStream source = new ByteArrayInputStream(content.getBytes());
			try {
				file.create(source, IResource.NONE, null);
			} catch (CoreException e) {
				CommonPlugin.logError(e);
			}
		}
	}

	public static void updateFile(IFile file, String content) throws CoreException {
		if (file != null && file.exists()) {
			InputStream source = new ByteArrayInputStream(content.getBytes());
			file.setContents(source, true, true, new NullProgressMonitor());
		}
	}

	public static IProject getSelectedProject() {
		IWorkbenchWindow workbenchWindow = CommonPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (workbenchWindow != null) {
			IWorkbenchPage activePage = workbenchWindow.getActivePage();
			if (activePage != null) {
				ISelection selection = activePage.getSelection();

				if (selection instanceof StructuredSelection) {
					StructuredSelection structuredSelection = (StructuredSelection) selection;
					Object firstElement = structuredSelection.getFirstElement();
					IResource resource = ResourceUtil.getResource(firstElement);
					if (resource != null) {
						return resource.getProject();
					}
				}
			}
		}
		return null;
	}

	public static IProject getProject(String name) {
		if (name != null) {
			try {
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
				if (project != null && project.exists()) {
					return project;
				}
			} catch (IllegalArgumentException e) {
			}
		}

		return null;
	}

	public static IResource getResource(IProject project, String filePath) {
		if (project != null && project.exists()) {
			try {
				IResource resource = project.findMember(new Path(filePath));
				if (resource != null && resource.exists()) {
					return resource;
				}
			} catch (IllegalArgumentException e) {
			}
		}

		return null;
	}

	public static IContainer getContainerFromSelection(IStructuredSelection selection) {
		IContainer container = null;
		if (selection != null && !selection.isEmpty()) {
			Object selectedObject = selection.getFirstElement();
			if (selectedObject instanceof IContainer) {
				container = (IContainer) selectedObject;
			} else if (selectedObject instanceof IFile) {
				container = ((IFile) selectedObject).getParent();
			}
		}
		return container;
	}

	public static String getAbsolutePath(IResource resource) {
		IPath path = null;
		String absoluteLocation = null;
		if (resource != null) {
			path = resource.getRawLocation();
			path = (path != null) ? path : resource.getLocation();
			if (path != null) {
				absoluteLocation = path.toOSString();
			}
		}
		return absoluteLocation;
	}

	public static void showConsoleView() throws PartInitException {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			if (activePage != null) {
				activePage.showView(IConsoleConstants.ID_CONSOLE_VIEW);
			}
		}
	}

	public static IFile findFileRecursively(IContainer container, String name) throws CoreException {
		for (IResource r : container.members()) {
			if (r instanceof IContainer) {
				IFile file = findFileRecursively((IContainer) r, name);
				if (file != null && file.exists()) {
					return file;
				}
			} else if (r instanceof IFile && r.getName().equals(name) && r.exists()) {
				return (IFile) r;
			}
		}
		return null;
	}

	public static File getFile(String path) {
		if (path != null) {
			File file = new File(path);
			if (file.isFile() && file.exists()) {
				return file;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static IFile getFileForLocation(String path) {
		if (path == null) {
			return null;
		}
		IPath filePath = new Path(path);
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath);
		if (files.length > 0) {
			return files[0];
		}
		return null;
	}

	public static void showErrorDialog(final String title, final String message, final IStatus status) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Shell shell = Display.getDefault().getActiveShell();
				ErrorDialog.openError((shell != null) ? shell : new Shell(), title, message, status);
			}
		});
	}

	public static IFile getEditorFile(IWorkbenchPart part) {
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

	/**
	 * This method tests the current workbench state and returns one of the
	 * following:
	 *
	 * 1. A StructuredSelection containing the currently edited file if it
	 * matches the specified name 2. The current selection if its first element
	 * is a file matching the specified name 3. The current selection if its
	 * first element is an ITask 4. A StructuredSelection containing the first
	 * IFile instance in the currently selected container that matches the
	 * specified name.
	 *
	 * @param fileName
	 *            The file name to test for
	 * @return The file or ITask selection depending on the documented
	 *         conditions
	 */
	public static ISelection getNamedFileOrTaskSelection(String fileName, Class<? extends ITask> taskClass) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IContainer container = null;

			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IWorkbenchPart part = page.getActivePart();
				if (part != null) {
					IFile activeEditorFile = getEditorFile(part);

					if (activeEditorFile != null) {
						if (activeEditorFile.getName() != null && activeEditorFile.getName().equals(fileName)) {
							return new StructuredSelection(activeEditorFile);
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
							if (element instanceof IFile) {
								IFile fileElement = (IFile) element;
								if (fileElement.getName() != null && fileElement.getName().equals(fileName)) {
									return treeSelection;
								}
							} else if (taskClass != null && taskClass.isInstance(element)) {
								return selection;
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

			if (container != null) {
				try {
					IFile file = WorkbenchResourceUtil.findFileRecursively(container, fileName);

					if (file != null && file.exists()) {
						return new StructuredSelection(file);
					}
				} catch (CoreException ex) {
				}
			}

		}
		return null;
	}

}
