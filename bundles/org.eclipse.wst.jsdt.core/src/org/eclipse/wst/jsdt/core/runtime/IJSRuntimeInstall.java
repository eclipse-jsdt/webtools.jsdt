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

import java.io.File;

/**
 * Models an instance of {@link IBaseJSRuntimeInstall} but
 * extended with the read-only classification given by {@link IJSRuntimeType}.
 * 
 * @since 2.0
 */
public interface IJSRuntimeInstall {
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
	 * Since this is a required field and usually to be presented to 
	 * users, it can not be null
	 * 
	 * @param name the display name of this runtime install.
	 */
	void setName(String name);
	 
	/**
	 * Returns the binary file location of this runtime install.
	 * 
	 * @return the binary file of this runtime install. May
	 * 			return <code>null</code>.
	 */
	File getInstallLocation();
	
	/**
	 * Sets the binary file location of this runtime install.
	 * 
	 * @param installLocation binary file of this runtime install.
	 */
	void setInstallLocation(File installLocation);
	
	/**
	 * Returns JS runtime arguments to be used with this install whenever this
	 * runtime is launched, parsed as an array of strings, or
	 * <code>null</code> if none.
	 * 
	 * @return JS runtime arguments to be used with this install whenever this
	 * runtime is launched or <code>null</code> if none
	 * 
	 */
	public String [] getJSRuntimeArguments();
		
	/**
	 * Returns JS runtime arguments to be used with this install whenever this
	 * runtime is launched as they should be passed to the command line, or
	 * <code>null</code> if none.
	 * 
	 * @return JS runtime arguments to be used with this install whenever this
	 * runtime is launched as they should be passed to the command line, or
	 * <code>null</code> if none
	 */
	public String getJSRuntimeArgumentsAsString ();
	
	/**
	 * Sets JS runtime arguments to be used with this install whenever this
	 * runtime is launched, possibly <code>null</code>. 
	 * 
	 * @param runtimeArgs arguments to be used with this runtime install whenever this
	 * runtime is launched, possibly <code>null</code>
	 * 
	 */
	public void setJSRuntimeArguments(String runtimeArgs);
	
	/**
	 * Returns an instance of runtime type, so this IJSRuntimeInstall can
	 * be classified.
	 * @return a valid runtime type for this runtime install contribution.
	 */
	public IJSRuntimeType getRuntimeType ();
}
