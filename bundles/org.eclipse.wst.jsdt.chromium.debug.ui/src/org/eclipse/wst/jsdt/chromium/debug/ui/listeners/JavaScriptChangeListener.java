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
package org.eclipse.wst.jsdt.chromium.debug.ui.listeners;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.jsdt.chromium.debug.ui.ChromiumDebugUIPlugin;
import org.eclipse.wst.jsdt.chromium.debug.ui.actions.PushChangesAction;

/**
 * Tracking changes in JavaScript files during debug session and executing {@link PushChangesAction}
 * 
 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=488344">Bug 488344</a>
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class JavaScriptChangeListener implements IResourceChangeListener {
	private static final PushChangesAction ACTION = new PushChangesAction();
	private static final String JS = ".js"; //$NON-NLS-1$

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (IResourceChangeEvent.POST_CHANGE == event.getType()) {
			try {
				event.getDelta().accept(new IResourceDeltaVisitor() {

					@Override
					public boolean visit(IResourceDelta delta) throws CoreException {
						IResource resource = delta.getResource();
						if (resource.getName().endsWith(JS) && resource instanceof IFile) {
							final IFile file = (IFile) resource;
							Display.getDefault().asyncExec(new Runnable() {

								@Override
								public void run() {
									ACTION.createRunnable(file).run(
											ChromiumDebugUIPlugin.getActiveShell(),
											ChromiumDebugUIPlugin.getActivePart());
								}
							});
						}
						return true;
					}
				});
			} catch (CoreException e) {
				ChromiumDebugUIPlugin.logError(e, e.getMessage());
			}
		}
	}
}
