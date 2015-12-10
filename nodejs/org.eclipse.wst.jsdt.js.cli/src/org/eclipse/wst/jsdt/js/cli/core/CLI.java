/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.cli.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.externaltools.internal.IExternalToolConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.wst.jsdt.js.cli.CLIPlugin;
import org.eclipse.wst.jsdt.js.cli.Messages;
import org.eclipse.wst.jsdt.js.cli.internal.util.ExternalProcessUtility;

/**
 * Wrapper around CLI. Provides low level 
 * access to CLI.
 *
 *@author Gorkem Ercan
 *@author "Ilya Buziuk (ibuziuk)"
 *
 */
@SuppressWarnings("restriction")
public class CLI {
	
	//Store locks for the projects.
	private static Map<String, Lock> projectLock = Collections.synchronizedMap(new HashMap<String,Lock>());
	private IProject project;
	private IPath workingDir;

		
	public CLI( IProject project, IPath workingDir) {
		if (project == null) {
			throw new IllegalArgumentException(Messages.Error_NoProjectSpecified);
		}
		
		if (workingDir == null) {
			// use the project location as the working directory
			this.workingDir = project.getLocation();
		}
		this.project = project;
		this.workingDir = workingDir;
	}
	
	public CLIResult execute(CLICommand command, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		final CLIStreamListener streamListener = new CLIStreamListener();
		IProcess process = startShell(streamListener, monitor, getLaunchConfiguration(command));
		sendCLICommand(process, command, monitor);
		CLIResult result = new CLIResult(streamListener.getErrorMessage(), streamListener.getMessage());
		throwExceptionIfError(result);
		return result;
	}
	
	private ILaunchConfiguration getLaunchConfiguration(CLICommand command) {	
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(IExternalToolConstants.ID_PROGRAM_LAUNCH_CONFIGURATION_TYPE);
		try {
			ILaunchConfiguration cfg = type.newInstance(null, command.getToolName());
			ILaunchConfigurationWorkingCopy wc = cfg.getWorkingCopy();
			wc.setAttribute(IProcess.ATTR_PROCESS_LABEL, command.getToolName() + command.getCommand());
			cfg = wc.doSave();
			return cfg;
		} catch (CoreException e) {
			CLIPlugin.logError(e);
		}
		return null;
	}
	
	protected void sendCLICommand(final IProcess process, final CLICommand command,
			final IProgressMonitor monitor) throws CoreException {
		Lock lock = projectLock();
		lock.lock();
		try {
			
			DebugPlugin.getDefault().addDebugEventListener(processTerminateListener);
			final IStreamsProxy streamProxy = process.getStreamsProxy();
			streamProxy.write(command.toString());
			while (!process.isTerminated()) {
				//exit the shell after sending the command
				streamProxy.write("exit\n"); //$NON-NLS-1$
				if (monitor.isCanceled()) {
					process.terminate();
					break;
				}
				Thread.sleep(100);
			}
		} catch (IOException | InterruptedException e) {
			throw new CoreException(new Status(IStatus.ERROR, CLIPlugin.PLUGIN_ID, Messages.Error_FatalInvokingCLI, e));
		} finally {
			lock.unlock();
		}
	}
	
	//public visibility to support testing
	public IProcess startShell(final IStreamListener listener, final IProgressMonitor monitor, 
			final ILaunchConfiguration launchConfiguration) throws CoreException{
		ArrayList<String> commandList = new ArrayList<String>();
		if(isWindows()){
			commandList.add("cmd"); //$NON-NLS-1$
		}else{
			commandList.add("/bin/bash"); //$NON-NLS-1$
			commandList.add("-l"); //$NON-NLS-1$
		}
		ExternalProcessUtility ep = new ExternalProcessUtility();
		IProcess process = ep.exec(commandList.toArray(new String[commandList.size()]), workingDir.toFile(), 
				monitor, null, launchConfiguration);
		 if(listener != null){
			 process.getStreamsProxy().getOutputStreamMonitor().addListener(listener);
			 process.getStreamsProxy().getErrorStreamMonitor().addListener(listener);
		 }
		 return process;
	}
	
	private boolean isWindows(){
		String OS = System.getProperty("os.name","unknown");  //$NON-NLS-1$//$NON-NLS-2$
		return OS.toLowerCase().indexOf("win")>-1; //$NON-NLS-1$
	}
	
	
	private Lock projectLock(){
		final String projectName = project.getProject().getName();
		Lock l = projectLock.get(project.getProject().getName());
		if(l == null){
			// Use reentrant locks
			l = new ReentrantLock();
			projectLock.put(projectName, l);
		}
		return l;
	}
	
	protected void throwExceptionIfError(CLIResult result) throws CoreException {
		if(result.hasError()){
			throw result.asCoreException();
		}
	}
	
	IDebugEventSetListener processTerminateListener = new IDebugEventSetListener() {

		@Override
		public void handleDebugEvents(DebugEvent[] events) {
			for (DebugEvent event : events) {
				if (event.getKind() == DebugEvent.TERMINATE) {
					Object source = event.getSource();
					if (source instanceof IProcess) {
						ILaunch launch = ((IProcess) source).getLaunch();
						if (launch != null) {
							ILaunchConfiguration lc = launch.getLaunchConfiguration();
							// TODO: need to write smarter conditions
							if (lc != null && project != null && project.exists()) {
								try {
									project.refreshLocal(IResource.DEPTH_INFINITE, null);
								} catch (CoreException e) {
									CLIPlugin.logError(e);
								} finally {
									DebugPlugin.getDefault().removeDebugEventListener(this);
								}
							}
						}
					}
				}
			}
		}
	};

}
