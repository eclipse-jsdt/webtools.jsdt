/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
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
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_PROJECT_TEXT;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_MAIN_FILE_TEXT;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_CONNECTION_TEXT;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_HOST_TEXT;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_PORT_TEXT;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_DEBUGGER_NETWORK_CONSOLE_TEXT;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_BREAK_TEXT;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_BROWSE_BUTTON;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_WORKSPACE_BUTTON;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_PROJECT_TITLE;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_PROJECT_DESCRIPTION;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_MAIN_FILE_TITLE;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_MAIN_FILE_DESCRIPTION;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_PROJECT_DOES_NOT_EXIST;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_PROJECT;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_MAIN_FILE_DOES_NOT_EXIST;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_MAIN_FILE;
    public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_MAIN_FILE_NOT_IN_PROJECT;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_HOST;
	public static String LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_INVALID_PORT;
	public static String LAUNCH_CONFIGURATION_DELEGATE_CHROMIUM_DEBUGGER_DELAY_TASK;
	
	public static String LAUNCH_CONFIGURATION_FAILED_EXECUTION_ERROR;
	public static String LAUNCH_CONFIGURATION_NO_RUNNER_FOUND_ERROR; 
	public static String LAUNCH_CONFIGURATION_CHROMIUM_IS_NOT_AVAILABLE_ERROR; 

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
