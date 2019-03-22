/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.runtime;

import org.eclipse.wst.jsdt.core.runtime.AbstractJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeJsInstall extends AbstractJSRuntimeInstall{
	public NodeJsInstall(IJSRuntimeType runtimeType, String id) {
		super(runtimeType, id);
	}

	private IJSRunner runner;

	@Override
	public IJSRunner getJSRunner(String mode) {
		if (runner == null) {
			runner = new NodeJsRunner(this);
		}
		return runner;
	}
}
