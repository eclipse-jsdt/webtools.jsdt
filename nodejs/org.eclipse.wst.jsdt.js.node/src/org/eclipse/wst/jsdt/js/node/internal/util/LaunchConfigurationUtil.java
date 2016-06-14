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
package org.eclipse.wst.jsdt.js.node.internal.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams.PredefinedSourceWrapperIds;
import org.eclipse.wst.jsdt.chromium.debug.core.util.MementoFormat;
import org.eclipse.wst.jsdt.js.node.NodePlugin;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;

/**
 * Node application launch configuration utils
 *
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class LaunchConfigurationUtil {
	
	/**
	 * Check if the Chromium core bundle is available
	 *
	 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=493234">Bug 493234</a>
	 * @return true if the Chromium core bundle is available, false otherwise.
	 */
	public static boolean isChromiumAvailable() {
		return Platform.getBundle("org.eclipse.wst.jsdt.chromium.debug.core") != null; //$NON-NLS-1$
	}
	
	public static String resolveValue(String expression) throws CoreException {
		String expanded= null;
		try {
			expanded= getValue(expression);
		} catch (CoreException e) { //possibly just a variable that needs to be resolved at runtime
			validateVariables(expression);
			return null;
		}
		return expanded;
	}

	/**
	 * Get an existing Node.js launch configuration for a given file if it exists.
	 *
	 * @return the existing Node.js launch configuration if any, null otherwise.
	 */
	public static ILaunchConfiguration getExistingLaunchConfiguration(IFile file, ILaunchConfigurationType launchConfigTypeId,
			String attributeName) {
		if (file == null) {
			return null;
		}

		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

		try {
			ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(launchConfigTypeId);
			for (ILaunchConfiguration config : configurations) {
				boolean match = false;

				IPath configPath = getIPathFromLaunchConfig(config, attributeName);
				if (configPath != null) {
					String path = resolveValue(configPath.toOSString());
					if (path != null && path.equals(file.getRawLocation().toOSString())) {
						match = true;
					}
				}

				if (match) {
					return config;
				}
			}
		} catch (CoreException coreException) {
			return null;
		}
		return null;
	}

	/**
	 * Get an existing Node.js launch configuration of a given name if exists.
	 *
	 * @return the existing Node.js launch configuration of the given name, null otherwise.
	 */
	public static ILaunchConfiguration getLaunchByName(String name, ILaunchConfigurationType type) {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		try {
			ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(type);
			for (ILaunchConfiguration config : configurations) {
				if (config.getName().equals(name)) {
					return config;
				}
			}
		} catch (CoreException e) {
			return null;
		}
		return null;
	}

	private static IPath getIPathFromLaunchConfig(ILaunchConfiguration config, String attributeName) {
		IPath configPath = null;
		try {
			configPath = new Path(config.getAttribute(attributeName, NodeConstants.EMPTY));
		} catch (CoreException e) {
			NodePlugin.logError(e.getLocalizedMessage());
		}
		return configPath;
	}
	
	private static String getValue(String expression) throws CoreException {
		IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
		return manager.performStringSubstitution(expression);
	}
	
	private static void validateVariables(String expression) throws CoreException {
		IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
		manager.validateStringVariables(expression);
	}
	
	public static Map<String, String> getDefaultAttributes() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(IProcess.ATTR_PROCESS_TYPE, NodeConstants.ID_NODEJS_PROCESS_TYPE);
		return map;
	}
	
	/**
	 * Set attributes for V8 debugger `live edit` functionality
	 */
	public static void addSourceLookupAttr(ILaunchConfigurationWorkingCopy wc) {
		wc.setAttribute(NodeConstants.SOURCE_LOOKUP_MODE, NodeConstants.EXACT_MATCH);
		wc.setAttribute(NodeConstants.CONFIG_PROPERTY, encode(NodeConstants.PREDEFIENED_WRAPPERS));
	}

	/**
	 * Encoding predefined source wrappers via {@link MementoFormat} for correct
	 * decoding in {@link PredefinedSourceWrapperIds}
	 */
	private static String encode(List<String> wrappers) {
		StringBuilder output = new StringBuilder();
		Collections.sort(wrappers);
		for (String wrapper : wrappers) {
			output.append(wrapper.length());
			output.append('(').append(wrapper).append(')');
		}
		return output.toString();
	}
	
}
