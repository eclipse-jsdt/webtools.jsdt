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
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.core.runtime.preferences.PreferenceModifyListener;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class JavaCorePreferenceModifyListener extends PreferenceModifyListener {

	static int PREFIX_LENGTH = JavaModelManager.CP_CONTAINER_PREFERENCES_PREFIX.length();
	JavaModel javaModel = JavaModelManager.getJavaModelManager().getJavaModel();

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.PreferenceModifyListener#preApply(org.eclipse.core.runtime.preferences.IEclipsePreferences)
	 */
	public IEclipsePreferences preApply(IEclipsePreferences node) {
		Preferences instance = node.node(InstanceScope.SCOPE);
		cleanJavaCore(instance.node(JavaScriptCore.PLUGIN_ID));
		return super.preApply(node);
	}

	/**
	 * Clean imported preferences from obsolete keys.
	 *
	 * @param preferences JavaScriptCore preferences.
	 */
	void cleanJavaCore(Preferences preferences) {
		try {
			String[] keys = preferences.keys();
			for (int k = 0, kl= keys.length; k<kl; k++) {
				String key = keys[k];
				if (key.startsWith(JavaModelManager.CP_CONTAINER_PREFERENCES_PREFIX) && !isJavaProjectAccessible(key)) {
					preferences.remove(key);
				}
			}
		} catch (BackingStoreException e) {
			// do nothing
		}
	}

	/**
	 * Returns whether a java project referenced in property key
	 * is still longer accessible or not.
	 *
	 * @param propertyName
	 * @return true if a project is referenced in given key and this project
	 * 	is still accessible, false otherwise.
	 */
	boolean isJavaProjectAccessible(String propertyName) {
		int index = propertyName.indexOf('|', PREFIX_LENGTH);
		if (index > 0) {
			final String projectName = propertyName.substring(PREFIX_LENGTH, index).trim();
			JavaProject project = (JavaProject) javaModel.getJavaScriptProject(projectName);
			if (project.getProject().isAccessible()) {
				return true;
			}
		}
		return false;
	}

}
