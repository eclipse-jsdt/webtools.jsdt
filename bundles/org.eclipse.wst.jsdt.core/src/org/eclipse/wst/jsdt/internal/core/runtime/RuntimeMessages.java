/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 
package org.eclipse.wst.jsdt.internal.core.runtime;

import org.eclipse.osgi.util.NLS;

/**
 * @since 2.0
 */
public class RuntimeMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.internal.core.runtime.RuntimeMessages"; //$NON-NLS-1$

	public static String JSRuntimeManager_DuplicatedRuntimeException;
	public static String JSRuntimeManager_UnexistingRuntimeException;
	public static String JSRuntimeManager_UnexistingRuntimeTypeException;
	
	public static String JSRunnerConfiguration_NullFile_Error;
	public static String JSRunnerConfiguration_NullRuntimeArgs_Error;
	public static String JSRunnerConfiguration_NullProgramArgs_Error;
	
	public static String JSRuntimeDefinitionsContainer_WrongXMLFormat_Error;
	
	public static String AbstractJSRuntimeInstall_MissingId_Error;
	public static String AbstractJSRuntimeInstall_MissingType_Error;
	public static String AbstractJSRuntimeInstall_MissingName_Error;
	
	public static String AbstractJSRuntimeType_DuplicateRuntimeInstall_Error;
	public static String AbstractJSRuntimeType_NullPath_Error;
	public static String AbstractJSRuntimeType_UnexistingInstallLocation_Error;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, RuntimeMessages.class);
	}


}
