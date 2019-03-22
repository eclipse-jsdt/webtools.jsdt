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
package org.eclipse.wst.jsdt.chromium.debug.js;

import org.eclipse.osgi.util.NLS;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();

	public static String LAUNCH_CONFIGURATION_FAILED_EXECUTION_ERROR;
	public static String CHROMIUM_RUNTIME_NAME;
	public static String CHROMIUM_RUNTIME_PROVIDER_NAME;
	public static String LAUNCH_CONFIGURATION_NO_RUNNER_FOUND_ERROR; 
	
	public static String ERROR_DIALOG_TITLE;
	public static String ERROR_UNABLE_TO_DETECT_LAUNCH_URL;
	public static String ERROR_UNABLE_TO_DETECT_DEBUGGING_PROJECT;
	public static String ERROR_UNABLE_TO_DETECT_WEBAPP_FOLDER;

	private Messages() {
	}
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
