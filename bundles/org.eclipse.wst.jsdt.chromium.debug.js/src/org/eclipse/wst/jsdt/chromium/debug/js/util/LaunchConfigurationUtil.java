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
package org.eclipse.wst.jsdt.chromium.debug.js.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.jsdt.chromium.debug.js.launch.LaunchConstants;
import org.eclipse.wst.jsdt.chromium.debug.js.runtime.ChromiumRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeManager;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class LaunchConfigurationUtil {
	
	private static final String ID_CHROME_PROCESS_TYPE = "Chrome / Chromium"; //$NON-NLS-1$

	private LaunchConfigurationUtil(){
	}
	
	public static Map<String, String> getDefaultAttributes() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(IProcess.ATTR_PROCESS_TYPE, ID_CHROME_PROCESS_TYPE);
		return map;
	}
	
	public static IJSRunner getJSRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		IJSRuntimeInstall runtimeInstall = verifyJSRuntimeInstall(configuration);
		if (runtimeInstall != null) {
			return runtimeInstall.getJSRunner(mode);
		}
		return null;
	}
	
	public static String[] getEnvironment(ILaunchConfiguration configuration) throws CoreException {
		return DebugPlugin.getDefault().getLaunchManager().getEnvironment(configuration);
	}
	
	public static String getProgramArguments(ILaunchConfiguration configuration) throws CoreException {
		String appArguments = configuration.getAttribute(LaunchConstants.ATTR_APP_ARGUMENTS, LaunchConstants.EMPTY);
		return VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(appArguments);
	}
	
	public static String getChromiumArguments(ILaunchConfiguration configuration) throws CoreException {
		String chromiumArguments = configuration.getAttribute(LaunchConstants.ATTR_CHROMIUM_ARGUMENTS, LaunchConstants.EMPTY);
		String args = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(chromiumArguments);
		return args;
	}

	private static IJSRuntimeInstall verifyJSRuntimeInstall(ILaunchConfiguration configuration) throws CoreException {
		return getJSRuntimeInstall(configuration);
	}

	private static IJSRuntimeInstall getJSRuntimeInstall(ILaunchConfiguration configuration) throws CoreException {
		// As for now, always run using the default runtime install for Chrome / Chromium
		return JSRuntimeManager.getDefaultRuntimeInstall(ChromiumRuntimeType.ID);
	}
	
}
