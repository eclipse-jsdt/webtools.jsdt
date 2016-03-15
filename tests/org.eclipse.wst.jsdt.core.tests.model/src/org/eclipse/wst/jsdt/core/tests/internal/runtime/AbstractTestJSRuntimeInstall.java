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

package org.eclipse.wst.jsdt.core.tests.internal.runtime;

import java.io.File;

import org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.launching.ExecutionArguments;

public abstract class AbstractTestJSRuntimeInstall implements IBaseJSRuntimeInstall {
	protected String name;
	protected File installLocation;
	protected String runtimeArgs;
	protected IJSRunner runner;
	
	public String[] getJSRuntimeArguments() {
		if (runtimeArgs == null) {
		    return null;
		}
		
		ExecutionArguments ex = new ExecutionArguments(runtimeArgs, ""); //$NON-NLS-1$
		return ex.getVMArgumentsArray();
	}

	public void setJSRuntimeArguments(String nodeArgs) {
		this.runtimeArgs = nodeArgs;
	}	

	public IJSRunner getJSRunner(String mode) {
		if (runner == null) {
			runner = new TestJSRunner(this);
		}
		return runner;
	}

	public String getName() {
		return name;
	}
	
	public void setName(@SuppressWarnings("hiding") String name) {
		this.name = name;
	}

	public File getInstallLocation() {
		return this.installLocation;
	}

	public void setInstallLocation(@SuppressWarnings("hiding") File installLocation) {
		this.installLocation = installLocation;
	}

}
