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
package org.eclipse.wst.jsdt.internal.ui.typehierarchy;

import org.eclipse.wst.jsdt.internal.ui.actions.AbstractToggleLinkingAction;
import org.eclipse.wst.jsdt.ui.ITypeHierarchyViewPart;


/**
 * This action toggles whether the type hierarchy links its selection to the active
 * editor.
 * 
 * 
 */
public class ToggleLinkingAction extends AbstractToggleLinkingAction {
	
	private ITypeHierarchyViewPart fHierarchyViewPart;
	
	/**
	 * Constructs a new action.
	 */
	public ToggleLinkingAction(ITypeHierarchyViewPart part) {
		setChecked(part.isLinkingEnabled());
		fHierarchyViewPart= part;
	}

	/**
	 * Runs the action.
	 */
	public void run() {
		fHierarchyViewPart.setLinkingEnabled(isChecked());
	}

}
