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
package org.eclipse.wst.jsdt.internal.ui.preferences.formatter;

import org.eclipse.wst.jsdt.internal.ui.preferences.formatter.ProfileManager.CustomProfile;

public interface IProfileVersioner {

	public int getFirstVersion();

	public int getCurrentVersion();
	
    public String getProfileKind();

	/**
	 * Update the <code>profile</code> to the 
	 * current version number
	 */
	public void update(CustomProfile profile);

}
