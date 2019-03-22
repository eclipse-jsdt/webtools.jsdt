/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.wst.jsdt.core.runtime;

import org.eclipse.wst.jsdt.internal.core.runtime.RuntimeMessages;

/**
 * Holder for various arguments passed to a JavaScript runner.
 * Mandatory parameters are passed in the constructor; optional arguments, via setters.
 * <p>
 * Clients may instantiate this class; it is not intended to be subclassed.
 * </p>
 * 
 * @since 2.0
 */
public class JSRunnerConfiguration {
	private String fFileToLaunch;
	private String[] fJavaScriptRuntimeArgs;
	private String[] fProgramArgs;
	private String[] fEnvironment;
	private String fWorkingDirectory;
	
	private static final String[] fgEmpty= new String[0];
	
	/**
	 * Creates a new configuration for launching a JS runtime to run the given main file.
	 *
	 * @param fileToLaunch The full path to the file to be launched. Must not be null.
	 */
	public JSRunnerConfiguration(String fileToLaunch) {
		if (fileToLaunch == null) {
			throw new IllegalArgumentException(RuntimeMessages.JSRunnerConfiguration_NullFile_Error); 
		}
		fFileToLaunch= fileToLaunch;
	}

	/**
	 * Sets the custom runtime arguments. Typically, these arguments
	 * are set by the user.
	 * These arguments will not be interpreted by a JSRunner, the client is responsible for
	 * passing arguments compatible with a particular runner.
	 *
	 * @param args the list of runtime arguments
	 */
	public void setJSRuntimeArguments(String[] args) {
		if (args == null) {
			throw new IllegalArgumentException(RuntimeMessages.JSRunnerConfiguration_NullRuntimeArgs_Error); 
		}
		fJavaScriptRuntimeArgs= args;
	}
	
	/**
	 * Sets the custom program arguments. These arguments will be appended to the list of 
	 * program arguments that a JS runner uses when launching a VM (in general: none). 
	 * Typically, these arguments are set by the user.
	 * These arguments will not be interpreted by a JSRunner, the client is responsible for
	 * passing arguments compatible with a particular JSRunner.
	 *
	 * @param args the list of arguments	
	 */
	public void setProgramArguments(String[] args) {
		if (args == null) {
			throw new IllegalArgumentException(RuntimeMessages.JSRunnerConfiguration_NullProgramArgs_Error); 
		}
		fProgramArgs= args;
	}
	
	/**
	 * Sets the environment for the JavaScript program. The JavaScript VM will be
	 * launched in the given environment.
	 * 
	 * @param environment the environment for the JavaScript program specified as an array
	 *  of strings, each element specifying an environment variable setting in the
	 *  format <i>name</i>=<i>value</i>
	 *  
	 */
	public void setEnvironment(String[] environment) {
		fEnvironment= environment;
	}
	
	/**
	 * Returns the file path to launch.
	 *
	 * @return The full path for the file to launch. Will not be <code>null</code>.
	 */
	public String getFileToLaunch() {
		return fFileToLaunch;
	}
	
	/**
	 * Returns the arguments to the runtime itself.
	 *
	 * @return The runner arguments. Default is an empty array. Will not be <code>null</code>.
	 * @see #setJSRuntimeArguments(String[])
	 */
	public String[] getJSRuntimeArguments() {
		if (fJavaScriptRuntimeArgs == null) {
			return fgEmpty;
		}
		return fJavaScriptRuntimeArgs;
	}
	
	/**
	 * Returns the arguments to the JavaScript program.
	 *
	 * @return The JavaScript program arguments. Default is an empty array. Will not be <code>null</code>.
	 * @see #setProgramArguments(String[])
	 */
	public String[] getProgramArguments() {
		if (fProgramArgs == null) {
			return fgEmpty;
		}
		return fProgramArgs;
	}
	
	/**
	 * Returns the environment for the JavaScript program or <code>null</code>
	 * 
	 * @return The JavaScript program environment. Default is <code>null</code>
	 *  
	 */
	public String[] getEnvironment() {
		return fEnvironment;
	}
	
	/**
	 * Sets the working directory for a launched runtime.
	 * 
	 * @param path the absolute path to the working directory
	 *  to be used by a launched VM, or <code>null</code> if
	 *  the default working directory is to be inherited from the
	 *  current process
	 *  
	 */
	public void setWorkingDirectory(String path) {
		fWorkingDirectory = path;
	}
	
	/**
	 * Returns the working directory of a launched VM.
	 * 
	 * @return the absolute path to the working directory
	 *  of a launched VM, or <code>null</code> if the working
	 *  directory is inherited from the current process
	 *  
	 */
	public String getWorkingDirectory() {
		return fWorkingDirectory;
	}
}
