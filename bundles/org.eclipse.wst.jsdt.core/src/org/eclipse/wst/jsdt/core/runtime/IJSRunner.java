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

package org.eclipse.wst.jsdt.core.runtime;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

/**
 * A JavaScript runner starts a JavaScript runtime installation 
 * running a JavaScript program.
 * <p>
 * Clients may implement this interface to launch a new kind
 * of JavaScript runtime.
 * </p>
 * 
 * @since 2.0
 */
public interface IJSRunner {
		
	/**
	 * Launches a JavaScript runtime environment as specified in the 
	 * given configuration, contributing results (debug targets and processes), 
	 * to the given launch.
	 *
	 * @param configuration the configuration settings for this run
	 * @param launch the launch to contribute to
	 * @param monitor progress monitor or <code>null</code>
	 * @return IProcess a reference to the IProcess coming after launching the configuration
	 * @exception CoreException if an exception occurs while launching
	 */
	public IProcess run(JSRunnerConfiguration configuration, ILaunch launch, IProgressMonitor monitor) throws CoreException;
	
}
