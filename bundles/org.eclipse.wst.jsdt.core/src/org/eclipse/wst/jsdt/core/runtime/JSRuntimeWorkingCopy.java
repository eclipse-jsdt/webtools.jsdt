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

/**
 * An implementation of IJSRuntimeInstall that is used for manipulating runtimes without necessarily 
 * committing changes.
 * <p>
 * Instances of this class act like wrappers.  All other instances of IJSRuntimeInstall represent 
 * 'real live' runtimes that may be used for building or launching. Instances of this class
 * behave like 'temporary' runtimes that are not visible and not available for building or launching.
 * </p>
 * <p>
 * Instances of this class may be constructed as a preliminary step to creating a 'live' runtime
 * or as a preliminary step to making changes to a 'real' runtime.
 * </p>
 * When <code>convertToRealRuntime</code> is called, a corresponding 'real' runtime is created
 * if one did not previously exist, or the corresponding 'real' runtime is updated.
 * </p>
 * <p>
 * Clients may instantiate this class.
 * </p>
 * 
 * @since 2.0
 * @noextend This class is not intended to be sub-classed by clients.
 */
public class JSRuntimeWorkingCopy extends AbstractJSRuntimeInstall {
    
	public JSRuntimeWorkingCopy(IJSRuntimeType type, String id) {
		super(type, id);
	}
	
	/**
	 * Constructs a copy of the specified runtime with the given identifier.
	 * 
	 * @param sourceRuntime the original runtime install
	 * @param id the new ID to use
	 */
	public JSRuntimeWorkingCopy(IJSRuntimeInstall sourceRuntime, String id) {
		super(sourceRuntime.getRuntimeType(), id);
		init(sourceRuntime);
	}
	
	/**
	 * Construct a <code>JSRuntimeWorkingCopy</code> instance based on the specified 
	 * <code>IJSRuntimeInstall</code>.
	 * Changes to this working copy will not be reflected in the 'real' runtime until 
	 * <code>convertToRealRuntime</code> is called.
	 * 
	 * @param realRuntime the 'real' runtime from which to construct this
	 * working copy
	 */
	public JSRuntimeWorkingCopy(IJSRuntimeInstall realRuntime) {
		this (realRuntime.getRuntimeType(), realRuntime.getId());
		init(realRuntime);
	}

	/**
	 * Initializes the settings of this runtime working copy based on the 
	 * settings in the given runtime install.
	 * 
	 * @param realRuntime runtime to copy settings from
	 */
	private void init(IJSRuntimeInstall realRuntime) {
		setName(realRuntime.getName());
		setInstallLocation(realRuntime.getInstallLocation());
		setJSRuntimeArguments(realRuntime.getJSRuntimeArgumentsAsString());
	}
	
	/**
	 * If no corresponding 'real' runtime exists, create one and populate it from this 
	 * working copy instance. 
	 * If a corresponding runtime exists, update its attributes from this working copy instance.
	 * 
	 * @return IJSRuntimeInstall the 'real' corresponding to this working copy
	 */
	public IJSRuntimeInstall convertToRealRuntime() {
		IJSRuntimeType runtimeType = getRuntimeType();
		IJSRuntimeInstall realRuntime = JSRuntimeManager.getJSRuntimeInstall(getId());
		boolean existingInstall = true;
		
		if (realRuntime == null) {
			existingInstall = false;
			realRuntime = runtimeType.createRuntimeInstall(getId());
		}

		realRuntime.setName(getName());
		realRuntime.setInstallLocation(getInstallLocation());
		realRuntime.setJSRuntimeArguments(getJSRuntimeArgumentsAsString());
		
		// Last and most important, let the JSRuntimeManager to know
		// about this runtime install
		if (existingInstall) {
			JSRuntimeManager.updateJSRuntimeInstall(realRuntime);	
		} else {
			JSRuntimeManager.addJSRuntimeInstall(realRuntime);
		}
		
		return realRuntime;
	}
}
