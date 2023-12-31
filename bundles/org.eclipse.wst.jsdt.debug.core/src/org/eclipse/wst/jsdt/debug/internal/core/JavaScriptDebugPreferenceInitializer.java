/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.core;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Preference Initializer
 * 
 * @since 1.0
 */
public class JavaScriptDebugPreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * Constructor
	 */
	public JavaScriptDebugPreferenceInitializer() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IEclipsePreferences prefs = DefaultScope.INSTANCE.getNode(JavaScriptDebugPlugin.PLUGIN_ID);
		if(prefs != null) {
			prefs.putBoolean(Constants.SUSPEND_ON_ALL_SCRIPT_LOADS, false);
			prefs.putBoolean(Constants.SUSPEND_ON_THROWN_EXCEPTION, true);
			prefs.putBoolean(Constants.DELETE_EXT_PROJECT_ON_EXIT, false);
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				JavaScriptDebugPlugin.log(e);
			}
		}
	}

}
