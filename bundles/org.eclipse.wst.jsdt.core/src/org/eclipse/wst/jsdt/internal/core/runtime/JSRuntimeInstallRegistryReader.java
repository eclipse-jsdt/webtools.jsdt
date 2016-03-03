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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstallProvider;
import org.eclipse.wst.jsdt.internal.core.Logger;
import org.eclipse.wst.jsdt.internal.core.util.Util;

/**
 * Reads the <code>org.eclipse.wst.jsdt.core.JSRuntimeInstallProvider</code>
 * contributors, and get all <code>IBaseJSRuntimeInstall</code> out of them.
 *
 */
public class JSRuntimeInstallRegistryReader {
	private final static String EXTENSION_NAME = "org.eclipse.wst.jsdt.core.JSRuntimeInstallProvider"; //$NON-NLS-1$
	private final static String RUNTIME_PROVIDER = "runtimeProvider"; //$NON-NLS-1$
	private final static String RUNTIME_TYPE_ID_ATTR = "runtimeTypeId"; //$NON-NLS-1$
	private final static String PROVIDER_CLASS_ATTR = "class"; //$NON-NLS-1$
	
	private static Collection <IJSRuntimeInstall> runtimeInstalls = null;
	
	private static boolean isValidJSRuntimeType (String runtimeTypeId) {
		return JSRuntimeTypeRegistryReader.getJSRuntimeTypesIds().contains(runtimeTypeId);
	}
	
	private static void initRuntimeInstallsCollection() {
		runtimeInstalls = new ArrayList <IJSRuntimeInstall> ();
		
		IExtensionPoint exp = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_NAME);

		if (exp == null) {
			return;
		}

		for (IExtension extension : exp.getExtensions()) {
			for (IConfigurationElement providerConfigElements : extension.getConfigurationElements()) {
				if (providerConfigElements.getName().equals(RUNTIME_PROVIDER)) {
					if (providerConfigElements.getAttribute(PROVIDER_CLASS_ATTR) != null && 
								providerConfigElements.getAttribute(RUNTIME_TYPE_ID_ATTR) != null) {
						// This contribution is trying to register runtime installs
						// for an unknown runtime type, avoid that to happen!
						String runtimeTypeId = providerConfigElements.getAttribute(RUNTIME_TYPE_ID_ATTR);
						if (!isValidJSRuntimeType(runtimeTypeId)) {
							Logger.log(Logger.WARNING, "Trying to register a JS runtime install using an " //$NON-NLS-1$
										+ "invalid runtime type id." + runtimeTypeId + " Runtime types must " + //$NON-NLS-1$ //$NON-NLS-2$
										"be registered using extension point org.eclipse.wst.jsdt.core.JSRuntimeType"); //$NON-NLS-1$
							break;
						}
						
						IJSRuntimeInstallProvider provider = null;
						try {
							provider = 
										(IJSRuntimeInstallProvider) providerConfigElements.createExecutableExtension(PROVIDER_CLASS_ATTR); 
						} catch (Exception e) {
							Util.log(e, "Error instantiating class " + providerConfigElements.getAttribute(PROVIDER_CLASS_ATTR)); //$NON-NLS-1$
						}
						
						if (provider != null) {
							// Provider will return a list of IBaseJSRuntimeInstall, but it is our 
							// responsibility to build valid managed runtime install from there.
							Collection <IBaseJSRuntimeInstall> contributions = 
										provider.getJSRuntimeInstallContributions ();
							for (IBaseJSRuntimeInstall contribution : contributions) {
								runtimeInstalls.add(new JSRuntimeInstall(contribution, runtimeTypeId));
							}
						}
					}
				} else {
					Logger.log(Logger.WARNING, "Unexpected tag " + providerConfigElements.getName() +  //$NON-NLS-1$
								". Expected " + RUNTIME_PROVIDER + " instead."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	public static Collection <IJSRuntimeInstall> getJSRuntimeInstalls() {
		// We read it previously, no need to make it again
		if (runtimeInstalls != null) {
			return runtimeInstalls;
		}
		
		initRuntimeInstallsCollection ();

		return runtimeInstalls;
	}
}
