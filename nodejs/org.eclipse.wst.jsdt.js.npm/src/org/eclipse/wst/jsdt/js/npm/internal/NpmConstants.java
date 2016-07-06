/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.npm.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.js.npm.NpmPlugin;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class NpmConstants {

	private NpmConstants() {
	}

	// Launch constants
	public static final String LAUNCH_CONFIGURATION_ID = NpmPlugin.PLUGIN_ID + ".npmLaunchConfigurationType"; //$NON-NLS-1$
	public static final String LAUNCH_PROJECT = NpmPlugin.PLUGIN_ID + ".PROJECT"; //$NON-NLS-1$
	public static final String LAUNCH_DIR = NpmPlugin.PLUGIN_ID + ".DIR"; //$NON-NLS-1$
	public static final String LAUNCH_COMMAND = NpmPlugin.PLUGIN_ID + ".COMMAND"; //$NON-NLS-1$
	public static final String LAUNCH_SUBCOMMAND = NpmPlugin.PLUGIN_ID + ".SUBCOMMAND"; //$NON-NLS-1$

	public static final String PACKAGE_JSON = "package.json"; //$NON-NLS-1$
	public static final String NODE_MODULES = "node_modules"; //$NON-NLS-1$
	public static final String NPM = "npm"; //$NON-NLS-1$
	public static final String UTF_8 = "UTF-8"; //$NON-NLS-1$

	// Default package.json values
	public static final String DEFAULT_NAME = "js"; //$NON-NLS-1$
	public static final String DEFAULT_VERSION = "0.0.0"; //$NON-NLS-1$
	public static final String DEFAULT_DESCRIPTION = "Generated with Eclipse npm Tools"; //$NON-NLS-1$
	public static final String DEFAULT_MAIN = "index.js"; //$NON-NLS-1$
	public static final String DEFAULT_AUTHOR = ""; //$NON-NLS-1$
	public static final String DEFAULT_LICENSE = "ISC"; //$NON-NLS-1$

	@SuppressWarnings("serial")
	public static final Map<String, String> DEFAULT_SCRIPTS = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("test", "echo 'Error: no test specified' && exit 1");  //$NON-NLS-1$//$NON-NLS-2$
    }});

}
