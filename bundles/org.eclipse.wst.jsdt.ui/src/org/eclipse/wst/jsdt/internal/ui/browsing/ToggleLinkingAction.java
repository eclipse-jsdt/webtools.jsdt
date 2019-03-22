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
package org.eclipse.wst.jsdt.internal.ui.browsing;

import org.eclipse.wst.jsdt.internal.ui.actions.AbstractToggleLinkingAction;


/**
 * This action toggles whether this package explorer links its selection to the active
 * editor.
 *
 * 
 */
public class ToggleLinkingAction extends AbstractToggleLinkingAction {

	JavaBrowsingPart fJavaBrowsingPart;

	/**
	 * Constructs a new action.
	 */
	public ToggleLinkingAction(JavaBrowsingPart part) {
		setChecked(part.isLinkingEnabled());
		fJavaBrowsingPart= part;
	}

	/**
	 * Runs the action.
	 */
	public void run() {
		fJavaBrowsingPart.setLinkingEnabled(isChecked());
	}

}
