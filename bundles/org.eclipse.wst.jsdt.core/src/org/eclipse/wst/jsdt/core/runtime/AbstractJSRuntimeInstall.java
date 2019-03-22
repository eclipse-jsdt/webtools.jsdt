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

import org.eclipse.wst.jsdt.internal.core.runtime.RuntimeMessages;
import org.eclipse.wst.jsdt.launching.ExecutionArguments;

/**
 * Provides a basic pre-implementation for a runtime install.
 * 
 * @author orlandor@mx1.ibm.com
 * 
 * @since 2.0
 *
 */
public abstract class AbstractJSRuntimeInstall implements IJSRuntimeInstall {
	protected String fId;
	protected String fName;
	protected File fInstallLocation;
	protected String fRuntimeArgs;
	private IJSRuntimeType fType;
	
	/**
	 * Constructs a new runtime install.
	 * 
	 * @param	type	The type of this runtime install.
	 * 					Must not be <code>null</code>
	 * @param	id		The unique identifier of this runtime install instance
	 * 					Must not be <code>null</code>.
	 * @throws	IllegalArgumentException	if any of the required
	 * 					parameters are <code>null</code>.
	 */
	public AbstractJSRuntimeInstall(IJSRuntimeType type, String id) {
		if (id == null) {
			throw new IllegalArgumentException(RuntimeMessages.AbstractJSRuntimeInstall_MissingId_Error);
		}
		if (type == null) {
			throw new IllegalArgumentException(RuntimeMessages.AbstractJSRuntimeInstall_MissingType_Error);
		}
		fId = id;
		fType = type;
	}
	
	public String getId() {
		return fId;
	}
	
	public String getName() {
		return fName;
	}
	
	/**
	 * @throws	IllegalArgumentException if the required parameter is <code>null</code>
	 */
	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException(RuntimeMessages.AbstractJSRuntimeInstall_MissingName_Error);
		}
		fName = name;
	}
	
	public File getInstallLocation() {
		return fInstallLocation;
	}
	
	public void setInstallLocation(File installLocation) {
		fInstallLocation = installLocation;
	}

	public IJSRunner getJSRunner(String mode) {
		return null;
	}

	public String[] getJSRuntimeArguments() {
		String args = fRuntimeArgs;
		if (args == null) {
		    return null;
		}
		ExecutionArguments ex = new ExecutionArguments(args, ""); //$NON-NLS-1$
		return ex.getVMArgumentsArray();
	}
	
	/**
	 * Convenience method to get the runtime arguments without
	 * being parsed as an array of strings.
	 * 
	 * @return the raw runtime arguments as stored by {@link #setJSRuntimeArguments(String)}
	 */
	public String getJSRuntimeArgumentsAsString () {
		return fRuntimeArgs;
	}

	public void setJSRuntimeArguments(String runtimeArgs) {
        fRuntimeArgs = runtimeArgs;
	}

	public IJSRuntimeType getRuntimeType() {
		return fType;
	}
	
	public boolean equals(Object object) {
		if (object instanceof IJSRuntimeInstall) {
			IJSRuntimeInstall runtime = (IJSRuntimeInstall)object;
			return getRuntimeType().equals(runtime.getRuntimeType()) &&
				getId().equals(runtime.getId());
		}
		return false;
	}

	public int hashCode() {
		return getRuntimeType().hashCode() + getId().hashCode();
	}
}
