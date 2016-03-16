/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class Messages extends NLS {	
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();
	
	//Launch node application configuration
	public static String LAUNCH_CONFIGURATION_ARGUMENTS_TAB;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB;
    public static String LAUNCH_CONFIGURATION_ARGUMENTS_TAB_NODE_ARGUMENTS_TEXT;
    public static String LAUNCH_CONFIGURATION_ARGUMENTS_TAB_APP_ARGUMENTS_TEXT;
    public static String LAUNCH_CONFIGURATION_ARGUMENTS_TAB_VARIABLES_TEXT;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_MAIN_FILE_TEXT;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_WORKSPACE_BUTTON;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_MAIN_FILE_TITLE;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_MAIN_FILE_DESCRIPTION;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_MAIN_FILE_DOES_NOT_EXIST;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_MAIN_FILE;
 
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
