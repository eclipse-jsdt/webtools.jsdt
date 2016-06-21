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

import org.eclipse.osgi.util.NLS;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();

	public static String GruntLaunchError_Title;
	public static String GruntLaunchError_Message;
	public static String GruntLaunchTab_Main;
	public static String GruntLaunchTab_ErrorNotExist;
	public static String GruntLaunchTab_WarningTaskNotExist;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
