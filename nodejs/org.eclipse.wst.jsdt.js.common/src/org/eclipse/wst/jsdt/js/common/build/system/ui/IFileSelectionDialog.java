/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.common.build.system.ui;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wst.jsdt.js.common.CommonPlugin;

/**
 * Utility class to handle workspace IFile picker.
 */
public class IFileSelectionDialog extends ElementTreeSelectionDialog {
    private String[] extensions;

    private static ITreeContentProvider contentProvider = new ITreeContentProvider() {
        public Object[] getChildren(Object element) {
            if (element instanceof IContainer) {
                try {
                    return ((IContainer) element).members();
                }
                catch (CoreException e) {
                }
            }
            return null;
        }

        public Object getParent(Object element) {
            return ((IResource) element).getParent();
        }

        public boolean hasChildren(Object element) {
            return element instanceof IContainer;
        }

        public Object[] getElements(Object input) {
            return (Object[]) input;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    };

    private static final IStatus OK = new Status(IStatus.OK, CommonPlugin.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
    private static final IStatus ERROR = new Status(IStatus.ERROR, CommonPlugin.PLUGIN_ID, 0, "", null); //$NON-NLS-1$

    /*
     * Validator
     */
    private ISelectionStatusValidator validator = new ISelectionStatusValidator() {
        public IStatus validate(Object[] selection) {
            return selection.length == 1 && selection[0] instanceof IFile
                    && checkExtension(((IFile) selection[0]).getFileExtension()) ? OK : ERROR;
        }
    };

    public IFileSelectionDialog(String title, String message, String[] type) {
        this(Display.getDefault().getActiveShell(), WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(),
                contentProvider);
        this.extensions = type;

        setTitle(title);
        setMessage(message);

        setInput(computeInput());
        setValidator(validator);
    }

    public IFileSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
    }

    /*
     * Show projects
     */
    private Object[] computeInput() {
        /*
         * Refresh projects tree.
         */
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (int i = 0; i < projects.length; i++) {
            try {
                projects[i].refreshLocal(IResource.DEPTH_INFINITE, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        try {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_ONE, null);
        } catch (CoreException e) {
        }
        List<IProject> openProjects = new ArrayList<IProject>(projects.length);
        for (int i = 0; i < projects.length; i++) {
            if (projects[i].isOpen()) {
                openProjects.add(projects[i]);
            }
        }
        return openProjects.toArray();
    }

    /*
     * Check file extension
     */
    private boolean checkExtension(String name) {
        if (name.equals("*")) { //$NON-NLS-1$
            return true;
        }

        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].equals(name)) {
                return true;
            }
        }
        return false;
    }
}