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

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public final class NodeConstants {
	// Node.js constants and launch parameters
	public static final String LAUNCH_CONFIGURATION_TYPE_ID = "org.eclipse.wst.jsdt.js.node.NodeLaunchConfigurationType";
	public static final String EMPTY = "";
	public static final String ATTR_WORKING_DIRECTORY = "attr_working_directory";
	public static final String ATTR_APP_PATH = "attr_app_path";
	public static final String ATTR_NODE_ARGUMENTS = "attr_node_arguments";
	public static final String ATTR_APP_ARGUMENTS = "attr_app_arguments";
	public static final String ATTR_APP_PROJECT = "attr_app_project";
	public static final String ATTR_HOST_FIELD = "attr_host_field";
	public static final String ATTR_PORT_FIELD = "attr_port_field";
	public static final String ATTR_ADD_NETWORK_CONSOLE_FIELD = "attr_add_network_console_field";
	public static final String ATTR_BREAK_FIELD = "attr_break_field";
	public static final String PROCESS_MESSAGE = "Node.js process";
	public static final String PACKAGE_JSON = "package.json";
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 5858;

	// Chromium V8 constants and launch parameters
	public static final String CHROMIUM_LAUNCH_CONFIGURATION_TYPE_ID = "org.eclipse.wst.jsdt.chromium.debug.ui.LaunchType$StandaloneV8";
	public static final String CHROMIUM_DEBUG_HOST = "debug_host"; //$NON-NLS-1$
	public static final String CHROMIUM_DEBUG_PORT = "debug_port"; //$NON-NLS-1$
	public static final String ADD_NETWORK_CONSOLE = "add_network_console"; //$NON-NLS-1$
	public static final String BREAKPOINT_SYNC_DIRECTION = "breakpoint_startup_sync_direction"; //$NON-NLS-1$
	// By default Node.js debug launch should merge local and remote breakpoints
	public static final String MERGE = "MERGE"; //$NON-NLS-1$
	public static final String SOURCE_LOOKUP_MODE = "source_lookup_mode"; //$NON-NLS-1$
	public static final String WIP_BACKEND_ID = "wip_backend_id"; //$NON-NLS-1$
	// By default Node.js debug launch should use exact match detection of workspace resources
	public static final String EXACT_MATCH = "EXACT_MATCH"; //$NON-NLS-1$

	private NodeConstants() {
	}

}
