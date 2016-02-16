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
package org.eclipse.wst.jsdt.js.common.build.system.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.wst.jsdt.js.common.CommonPlugin;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class LaunchConfigurationAutoFill {
	
	public static ILaunchConfiguration chooseLaunchConfiguration(ILaunchConfiguration[] configurations, ITask task, String attribute) {
		try {
			for (ILaunchConfiguration conf : configurations) {
				String buildFileAttribute = conf.getAttribute(attribute, (String) null);
				String buildFilePath = task.getBuildFile().getLocation().toOSString();
				// Launch Configuration per build file (i.e. Gruntfile.js / gulpfile.js)
				if (buildFilePath.equals(buildFileAttribute)) {
					return conf;
				}
			}
		} catch (CoreException e) {
			CommonPlugin.logError(e, e.getMessage());
		}
		return null;
	}
}
