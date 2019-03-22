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
package org.eclipse.wst.jsdt.internal.ui.workingsets;

import org.eclipse.jface.action.IMenuManager;

public interface IWorkingSetActionGroup {

	public static final String ACTION_GROUP= "working_set_action_group"; //$NON-NLS-1$
	
	public void fillViewMenu(IMenuManager mm);
	
	public void cleanViewMenu(IMenuManager menuManager);

}
