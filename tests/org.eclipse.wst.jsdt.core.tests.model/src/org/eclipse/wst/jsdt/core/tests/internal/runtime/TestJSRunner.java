/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.wst.jsdt.core.tests.internal.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.JSRunnerConfiguration;

/**
 * @author orlandor@mx1.ibm.com
 *
 */
public class TestJSRunner implements IJSRunner {
	private IJSRuntimeInstall jsRuntimeInstall;
	
	public TestJSRunner(@SuppressWarnings("hiding") IJSRuntimeInstall jsRuntimeInstall) {
		this.jsRuntimeInstall = jsRuntimeInstall;
	}

	public IProcess run(JSRunnerConfiguration configuration, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		List<String> cmdLine = new ArrayList<String>();
		cmdLine.add(jsRuntimeInstall.getInstallLocation().getPath());

		String [] runtimeArguments = combineRuntimeArgs(configuration, jsRuntimeInstall);
		if (runtimeArguments.length != 0) {
			for (String argument : runtimeArguments) {
				cmdLine.add(argument);
			}
		}

		String mainFile = configuration.getFileToLaunch();
		cmdLine.add(mainFile);

		String [] appArguments = configuration.getProgramArguments();
		if (appArguments.length != 0) {
			for (String argument : appArguments) {
				cmdLine.add(argument);
			}
		}

		File workingPath = null;
		String workingDirectory = configuration.getWorkingDirectory();
		if (workingDirectory.length() > 0){
			workingPath = new File(workingDirectory);
		}

		String[] cmds = {};
		cmds = cmdLine.toArray(cmds);
		
		ProcessBuilder builder = new ProcessBuilder(cmds);
		builder.directory(workingPath);
		try {
			Process process = builder.start();
			builder.redirectErrorStream(true);
			return DebugPlugin.newProcess(launch, process, "Runtime test process"); //$NON-NLS-1$
		} catch (Exception ex) {
		}
		
		return null;
	}
	
	protected String[] combineRuntimeArgs(JSRunnerConfiguration configuration, IJSRuntimeInstall vmInstall) {
		String[] launchVMArgs= configuration.getJSRuntimeArguments();
		String[] vmVMArgs = vmInstall.getJSRuntimeArguments();
		if (vmVMArgs == null || vmVMArgs.length == 0) {
			return launchVMArgs;
		}
		
		String[] allVMArgs = new String[launchVMArgs.length + vmVMArgs.length];
		System.arraycopy(vmVMArgs, 0, allVMArgs, 0, vmVMArgs.length);
		System.arraycopy(launchVMArgs, 0, allVMArgs, vmVMArgs.length, launchVMArgs.length);
		return allVMArgs;
	}
	
}
