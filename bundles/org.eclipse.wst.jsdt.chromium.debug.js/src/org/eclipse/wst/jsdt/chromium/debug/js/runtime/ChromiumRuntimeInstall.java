/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.js.runtime;

import org.eclipse.wst.jsdt.core.runtime.AbstractJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ChromiumRuntimeInstall extends AbstractJSRuntimeInstall {

	public ChromiumRuntimeInstall(IJSRuntimeType runtimeType, String id) {
		super(runtimeType, id);
	}

	private IJSRunner runner;

	@Override
	public IJSRunner getJSRunner(String mode) {
		if (runner == null) {
			runner = new ChromiumRunner(this);
		}
		return runner;
	}
}
