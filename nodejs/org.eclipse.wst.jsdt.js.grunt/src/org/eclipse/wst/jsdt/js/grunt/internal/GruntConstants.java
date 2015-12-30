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
package org.eclipse.wst.jsdt.js.grunt.internal;

import org.eclipse.wst.jsdt.js.grunt.GruntPlugin;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class GruntConstants {

	private GruntConstants() {
	}
	
	// Launch constants
	public static final String LAUNCH_CONFIGURATION_ID = GruntPlugin.PLUGIN_ID + ".gruntLaunchConfigurationType"; //$NON-NLS-1$
	public static final String PROJECT = GruntPlugin.PLUGIN_ID + ".PROJECT"; //$NON-NLS-1$
	public static final String DIR = GruntPlugin.PLUGIN_ID + ".DIR"; //$NON-NLS-1$
	public static final String COMMAND = GruntPlugin.PLUGIN_ID + ".COMMAND"; //$NON-NLS-1$
	public static final String BUILD_FILE = GruntPlugin.PLUGIN_ID + ".BUILD_FILE"; //$NON-NLS-1$

	public static final String GRUNT = "grunt"; //$NON-NLS-1$
	public static final String GRUNT_FILE_JS = "gruntfile.js"; //$NON-NLS-1$

}
