/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.runtime;

import java.io.File;

import org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeJsInstall implements IBaseJSRuntimeInstall{

	private IJSRunner runner;

	@Override
	public String getId() {
		return "nodejsGlobal";
	}

	@Override
	public IJSRunner getJSRunner(String mode) {
		if (runner == null) {
			runner = new NodeJsRunner(this);
		}
		return runner;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public File getInstallLocation() {
		return new File ("node");
	}

	@Override
	public void setInstallLocation(File installLocation) {
		
	}

	@Override
	public String[] getJSRuntimeArguments() {
		return null;
	}

	@Override
	public void setJSRuntimeArguments(String runtimeArgs) {
		
	}

}
