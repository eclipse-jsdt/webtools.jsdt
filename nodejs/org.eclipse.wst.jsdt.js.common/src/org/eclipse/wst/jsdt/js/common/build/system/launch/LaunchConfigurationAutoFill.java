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
package org.eclipse.wst.jsdt.js.common.build.system.launch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.wst.jsdt.js.common.CommonPlugin;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class LaunchConfigurationAutoFill {

	/**
	 * Filters configurations for launch configurations with the same build file as a task.
	 *
	 * @param configurations - the launch configurations to filter
	 * @param task - the target task
	 * @param buildAttribute - the name of the launch configuration attribute that stores the build file.
	 * @return an array of launch configurations that target the same build file as task
	 */
	public static ILaunchConfiguration[] getAllLaunchConfigurations(
			ILaunchConfiguration[] configurations, ITask task, String buildAttribute) {

		List<ILaunchConfiguration> validConfigs = new ArrayList<>();
		try {
			for (ILaunchConfiguration conf : configurations) {
				String buildFileAttribute = conf.getAttribute(buildAttribute, (String) null);
				String buildFilePath = task.getBuildFile().getLocation().toOSString();
				// Launch Configuration per build file (i.e. Gruntfile.js / gulpfile.js)
				if (buildFilePath.equals(buildFileAttribute)) {
					validConfigs.add(conf);
				}
			}
		} catch (CoreException e) {
			CommonPlugin.logError(e, e.getMessage());
		}

		return validConfigs.toArray(new ILaunchConfiguration[validConfigs.size()]);
	}
}
