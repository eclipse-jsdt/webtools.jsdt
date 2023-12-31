/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.gulp.internal;

import org.eclipse.wst.jsdt.js.gulp.GulpPlugin;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class GulpConstants {

	private GulpConstants() {
	}

	// Launch constants
	public static final String LAUNCH_CONFIGURATION_ID = GulpPlugin.PLUGIN_ID + ".gulpLaunchConfigurationType"; //$NON-NLS-1$
	public static final String PROJECT = GulpPlugin.PLUGIN_ID + ".PROJECT"; //$NON-NLS-1$
	public static final String DIR = GulpPlugin.PLUGIN_ID + ".DIR"; //$NON-NLS-1$
	public static final String COMMAND = GulpPlugin.PLUGIN_ID + ".COMMAND"; //$NON-NLS-1$
	public static final String BUILD_FILE = GulpPlugin.PLUGIN_ID + ".BUILD_FILE"; //$NON-NLS-1$
	public static final String PARAMETERS = GulpPlugin.PLUGIN_ID + ".PARAMETERS"; //$NON-NLS-1$

	public static final String GULP = "gulp"; //$NON-NLS-1$
	public static final String GULP_FILE_JS = "gulpfile.js"; //$NON-NLS-1$
	public static final String DEFAULT_COMMAND = "default"; //$NON-NLS-1$

	public static final String RUN_COMMAND = "run"; //$NON-NLS-1$
}
