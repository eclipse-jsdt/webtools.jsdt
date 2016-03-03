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

package org.eclipse.wst.jsdt.internal.core.runtime;

import java.io.File;

import org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;

/**
 * Mostly a wrapper that joins a base runtime install with a given
 * JS runtime type and builds a real full instance of a JavaScript 
 * runtime install
 */
public class JSRuntimeInstall implements IJSRuntimeInstall {
	IBaseJSRuntimeInstall baseRuntimeInstall;
	IJSRuntimeType runtimeType;
	
	public JSRuntimeInstall (IBaseJSRuntimeInstall baseRuntimeInstall, String runtimeTypeId) {
		this.baseRuntimeInstall = baseRuntimeInstall;
		this.runtimeType = JSRuntimeTypeRegistryReader.getJSRuntimeType(runtimeTypeId);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#getId()
	 */
	public String getId() {
		return baseRuntimeInstall.getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#getJSRunner(java.lang.String)
	 */
	public IJSRunner getJSRunner(String mode) {
		return baseRuntimeInstall.getJSRunner(mode);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#getName()
	 */
	public String getName() {
		return baseRuntimeInstall.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#setName(java.lang.String)
	 */
	public void setName(String name) {
		baseRuntimeInstall.setName(name);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#getInstallLocation()
	 */
	public File getInstallLocation() {
		return baseRuntimeInstall.getInstallLocation();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#setInstallLocation(java.io.File)
	 */
	public void setInstallLocation(File installLocation) {
		baseRuntimeInstall.setInstallLocation(installLocation);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#getJSRuntimeArguments()
	 */
	public String[] getJSRuntimeArguments() {
		return baseRuntimeInstall.getJSRuntimeArguments();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall#setJSRuntimeArguments(java.lang.String)
	 */
	public void setJSRuntimeArguments(String runtimeArgs) {
		baseRuntimeInstall.setJSRuntimeArguments(runtimeArgs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall#getRuntimeType()
	 */
	public IJSRuntimeType getRuntimeType() {
		return runtimeType;
	}

}
