/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.jsdt.js.common.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.js.common.CommonPlugin;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.Location;

public class EditorUtility {

	/**
	 * Tests if a is currently shown in an editor
	 * 
	 * @return the IEditorPart if shown, null if element is not open in an
	 *         editor
	 */
	public static IEditorPart isOpenInEditor(ITask task) {
		IEditorInput input = new FileEditorInput(task.getBuildFile());
		IWorkbenchPage p = CommonPlugin.getActivePage();
		if (p != null) {
			return p.findEditor(input);
		}
		return null;
	}

	public static void revealInEditor(IEditorPart editorPart, ITask task) {
		revealInEditor(editorPart, task, null);
	}

	public static void revealInEditor(IEditorPart editorPart, ITask task, IWorkbenchPage page) {
		if (task == null)
			return;

		ITextEditor textEditor = null;
		if (editorPart instanceof ITextEditor)
			textEditor = (ITextEditor) editorPart;
		else if (editorPart instanceof IAdaptable)
			textEditor = (ITextEditor) editorPart.getAdapter(ITextEditor.class);
		if (textEditor != null) {
			Location location = task.getLocation();
			if (location != null) {
				int start = location.getStart();
				if (start > 0) {
					int length = location.getLength();
					textEditor.selectAndReveal(start, length);
					if (page != null) {
						page.activate(editorPart);
					}
				}
			}
		} else {
			Location location = task.getLocation();
			if (location != null) {
				IFile fileResource = task.getBuildFile();
				int start = location.getStart();
				try {
					IMarker marker = fileResource.createMarker("org.eclipse.core.resources.textmarker"); //$NON-NLS-1$
					marker.setAttribute("lineNumber", start); //$NON-NLS-1$
					editorPart = IDE.openEditor(page, marker, true);
					marker.delete();
				} catch (CoreException e) {
					CommonPlugin.logError(e);
				}
			}
		}

	}
}
