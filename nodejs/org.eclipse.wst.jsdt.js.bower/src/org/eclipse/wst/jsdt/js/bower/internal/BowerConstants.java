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
package org.eclipse.wst.jsdt.js.bower.internal;

import org.eclipse.wst.jsdt.js.bower.BowerPlugin;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class BowerConstants {

	private BowerConstants() {
	}

	// Launch constants
	public static final String LAUNCH_CONFIGURATION_ID = BowerPlugin.PLUGIN_ID + ".bowerLaunchConfigurationType"; //$NON-NLS-1$
	public static final String LAUNCH_PROJECT = BowerPlugin.PLUGIN_ID + ".PROJECT"; //$NON-NLS-1$
	public static final String LAUNCH_DIR = BowerPlugin.PLUGIN_ID + ".DIR"; //$NON-NLS-1$
	public static final String LAUNCH_COMMAND = BowerPlugin.PLUGIN_ID + ".COMMAND"; //$NON-NLS-1$

	public static final String BOWER = "bower"; //$NON-NLS-1$
	public static final String BOWERRC = ".bowerrc"; //$NON-NLS-1$
	public static final String BOWER_COMPONENTS = "bower_components"; //$NON-NLS-1$
	public static final String BOWER_JSON = "bower.json"; //$NON-NLS-1$
	public static final String UTF_8 = "UTF-8"; //$NON-NLS-1$

	// Default bower.json values
	public static final String DEFAULT_NAME = "Bower"; //$NON-NLS-1$
	public static final String DEFAULT_VERSION = "0.0.0"; //$NON-NLS-1$
	public static final String DEFAULT_LICENSE = "MIT"; //$NON-NLS-1$
	public static final String[] DEFAULT_IGNORE = { "**/.*", "node_modules", "bower_components", "test", "tests" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

}
