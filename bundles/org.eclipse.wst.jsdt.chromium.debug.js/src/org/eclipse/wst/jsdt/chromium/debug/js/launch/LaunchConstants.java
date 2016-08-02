/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.js.launch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class LaunchConstants {

	private LaunchConstants() {
	}
	
	public static final String EMPTY = ""; //$NON-NLS-1$
	public static final String LOCALHOST = "localhost"; //$NON-NLS-1$
	public static final int DEFAULT_PORT = 9222;

	// Chromium Launch Parameters
	public static final String CHROMIUM_LAUNCH_TYPE_ID = "org.eclipse.wst.jsdt.js.chromium.ChromiumLaunchConfigurationType"; //$NON-NLS-1$
	public static final String ATTR_APP_PROJECT = "attr_app_project"; //$NON-NLS-1$
	public static final String ATTR_APP_PROJECT_RELATIVE_PATH = "attr_app_project_relative_path"; //$NON-NLS-1$
	
	// URL for mapping between files in workspace and browser
	public static final String ATTR_BASE_URL = "attr_base_url"; //$NON-NLS-1$
	
	// URL for opening in Chrome / Chromium Browser
	public static final String ATTR_CHROMIUM_URL = "attr_chromium_url"; //$NON-NLS-1$

	// WIP Launch Parameters
	public static final String WIP_LAUNCH_CONFIGURATION_TYPE_ID = "org.eclipse.wst.jsdt.chromium.debug.ui.LaunchType$Wip"; //$NON-NLS-1$
	public static final String ATTR_WIP_BACKEND_CURRENT_DEV = "current development"; //$NON-NLS-1$
	public static final String ATTR_APP_ARGUMENTS = "attr_app_arguments"; //$NON-NLS-1$
	public static final String ATTR_CHROMIUM_ARGUMENTS = "attr_chromium_arguments"; //$NON-NLS-1$
	// By default WIP debug launch should merge breakpoints
	public static final String MERGE = "MERGE"; //$NON-NLS-1$
	// By default WIP debug launch should use exact match detection of workspace resources
	public static final String EXACT_MATCH = "EXACT_MATCH";//$NON-NLS-1$
	// By default source wrappers from {@link HardcodedSourceWrapProvider} must be enabled
	@SuppressWarnings("serial")
	public static final List<String> PREDEFIENED_WRAPPERS = new ArrayList<String>() {
		{
			add("org.eclipse.wst.jsdt.chromium.debug.core.model.HardcodedSourceWrapProvider$NodeJsStandardEntry"); //$NON-NLS-1$
			add("org.eclipse.wst.jsdt.chromium.debug.core.model.HardcodedSourceWrapProvider$NodeJsWithDefinedEntry"); //$NON-NLS-1$
		}
	};

}
