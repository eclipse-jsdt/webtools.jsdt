/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.viewsupport;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;

/**
 * Listener to be informed on text selection changes in an editor (post selection), including the corresponding AST.
 * The AST is shared and must not be modified.
 * Listeners can be registered in a <code>SelectionListenerWithASTManager</code>.
 */
public interface ISelectionListenerWithAST {
	
	/**
	 * Called when a selection has changed. The method is called in a post selection event in an background
	 * thread.
	 * 
	 * @param part The editor part in which the selection change has occurred.
	 * @param selection The new text selection
	 * @param astRoot The AST tree corresponding to the editor's input. This AST is shared and must
	 * not be modified.
	 */
	void selectionChanged(IEditorPart part, ITextSelection selection, JavaScriptUnit astRoot);

}
