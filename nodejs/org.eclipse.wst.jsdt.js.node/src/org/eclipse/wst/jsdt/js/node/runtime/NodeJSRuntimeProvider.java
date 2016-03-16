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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstallProvider;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeJSRuntimeProvider implements IJSRuntimeInstallProvider {

	public NodeJSRuntimeProvider() {
	}

	@Override
	public Collection<IBaseJSRuntimeInstall> getJSRuntimeInstallContributions() {
		ArrayList<IBaseJSRuntimeInstall> array = new ArrayList<IBaseJSRuntimeInstall>();
		array.add(new NodeJsInstall());
		return array;
	}

}
