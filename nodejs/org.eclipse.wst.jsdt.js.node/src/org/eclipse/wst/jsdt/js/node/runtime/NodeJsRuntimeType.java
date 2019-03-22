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

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.core.runtime.AbstractJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;

public class NodeJsRuntimeType extends AbstractJSRuntimeType {
	public static final String NODE_JS_RUNTIME_TYPE_ID = "org.eclipse.wst.jsdt.js.node.runtimeType";	
	
	@Override
	public String getName() {
		return "Node.js Runtime";
	}

	@Override
	protected IJSRuntimeInstall doCreateRuntimeInstall(String id) {
		return new NodeJsInstall(this,  id);
	}

	@Override
	protected IStatus doValidateInstallLocation(File tempFile) {
		// FIXME: Add real Node.js path validation
		return Status.OK_STATUS;
	}
}
