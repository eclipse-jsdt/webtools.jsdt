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

package org.eclipse.wst.jsdt.internal.ui.javaeditor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;


public class GotoMatchingBracketAction extends Action {

	public final static String GOTO_MATCHING_BRACKET= "GotoMatchingBracket"; //$NON-NLS-1$

	private final JavaEditor fEditor;

	public GotoMatchingBracketAction(JavaEditor editor) {
		super(JavaEditorMessages.GotoMatchingBracket_label);
		Assert.isNotNull(editor);
		fEditor= editor;
		setEnabled(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.GOTO_MATCHING_BRACKET_ACTION);
	}

	public void run() {
		fEditor.gotoMatchingBracket();
	}
}
