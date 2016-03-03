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

import java.io.File;

/**
 * Represents a particular installation of a runtime environment. 
 * An instance of this class holds all parameters specific to a 
 * runtime installation.
 * 
 * @since 2.0
 */
public interface IBaseJSRuntimeInstall {
	/**
	 * Returns the id for this runtime. Runtime IDs MUST BE UNIQUE, so 
	 * JSRuntimeManager can differentiate from all existing JS runtime
	 * installations.
	 * 
	 * This id is not intended to be presented to users.
	 * 
	 * @return the JSRuntimeInstall identifier. 
	 * Must not return <code>null</code>.
	 */
	String getId();
	
	/**
	 * Returns a JavaScript runner that runs this installed runtime in the given mode.
	 * 
	 * @param mode the mode the JSRuntime should be launched in; one of the constants
	 *   declared in <code>org.eclipse.debug.core.ILaunchManager</code>
	 * @return 	a IJSRunner for a given mode May return <code>null</code> if the given mode
	 * 			is not supported.
	 * @see org.eclipse.debug.core.ILaunchManager
	 */
	IJSRunner getJSRunner(String mode);
	
	/**
	 * Returns the display name of this runtime install.
	 * The runtime install name is intended to be presented to users.
	 * 
	 * @return the display name of this runtime install. 
	 * May return <code>null</code>.
	 */
	String getName();
	
	/**
	 * Sets the display name of this runtime install.
	 * The runtime install name is intended to be presented to users.
	 * 
	 * @param name the display name of this runtime install.
	 */
	void setName(String name);
	
	/**
	 * Returns the root directory of the install location of this 
	 * runtime install.
	 * 
	 * @return the root directory of this runtime installation. May
	 * 			return <code>null</code>.
	 */
	File getInstallLocation();
	
	/**
	 * Sets the root directory of the install location of this runtime install.
	 * 
	 * @param installLocation the root directory of this runtime installation
	 */
	void setInstallLocation(File installLocation);
		
	/**
	 * Returns JS runtime arguments to be used with this install whenever this
	 * runtime is launched as they should be passed to the command line, or
	 * <code>null</code> if none.
	 * 
	 * @return JS runtime arguments to be used with this install whenever this
	 * runtime is launched as they should be passed to the command line, or
	 * <code>null</code> if none
	 */
	public String [] getJSRuntimeArguments();
	
	/**
	 * Sets JS runtime arguments to be used with this install whenever this
	 * runtime is launched, possibly <code>null</code>. This is equivalent
	 * to <code>setVMArgs(String)</code> with whitespace character delimited
	 * arguments.  
	 * 
	 * @param runtimeArgs arguments to be used with this runtime install whenever this
	 * runtime is launched, possibly <code>null</code>
	 * 
	 */
	public void setJSRuntimeArguments(String runtimeArgs);
	    
}
