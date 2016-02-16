/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.common.ui.navigator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.util.EditorUtility;

public class TaskLinkHelper implements ILinkHelper {
	
	@Override
	public void activateEditor(IWorkbenchPage page, IStructuredSelection selection) {
		if (selection == null || selection.isEmpty())
			return;
		Object element = selection.getFirstElement();
		if (element instanceof ITask) {
			ITask node = (ITask) element;
			IEditorPart part = EditorUtility.isOpenInEditor(node);
			if (part != null) {
				page.bringToTop(part);
				if (element instanceof ITask) {
					EditorUtility.revealInEditor(part, node);
				}
			}
		}
	}
		
	@Override
	public IStructuredSelection findSelection(IEditorInput input) {
		return StructuredSelection.EMPTY;
	}


}
