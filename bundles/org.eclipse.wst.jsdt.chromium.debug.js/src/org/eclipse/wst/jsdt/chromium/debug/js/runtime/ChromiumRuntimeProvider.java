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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.wst.jsdt.chromium.debug.js.Messages;
import org.eclipse.wst.jsdt.chromium.debug.js.util.ChromiumDetector;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstallProvider;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ChromiumRuntimeProvider implements IJSRuntimeInstallProvider {
	private static final String RUNTIME_ID = "GlobalChromiumRuntime"; //$NON-NLS-1$

	@Override
	public Collection<IJSRuntimeInstall> getJSRuntimeInstallContributions(IJSRuntimeType runtimeType) {
		ArrayList<IJSRuntimeInstall> array = new ArrayList<IJSRuntimeInstall>();
		File chromiumSystemPath = ChromiumDetector.findChromiumSystemPath();
		// Return a system path install if there is one, otherwise return nothing
		if (chromiumSystemPath != null) {
			IJSRuntimeInstall contributedRuntimeInstall = runtimeType.createRuntimeInstall(RUNTIME_ID);
			contributedRuntimeInstall.setName (Messages.CHROMIUM_RUNTIME_PROVIDER_NAME);
			contributedRuntimeInstall.setInstallLocation(chromiumSystemPath);
			array.add(contributedRuntimeInstall);
		}
		return array;
	}

}
