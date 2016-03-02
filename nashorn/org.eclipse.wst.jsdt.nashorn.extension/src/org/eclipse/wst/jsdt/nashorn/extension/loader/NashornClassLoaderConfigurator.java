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
package org.eclipse.wst.jsdt.nashorn.extension.loader;


import org.eclipse.osgi.internal.hookregistry.HookConfigurator;
import org.eclipse.osgi.internal.hookregistry.HookRegistry;
/**
 * Registers {@link NashornLoaderHook}
 * 
 */
public class NashornClassLoaderConfigurator implements HookConfigurator {

	@Override
	public void addHooks(HookRegistry hookRegistry) {
		hookRegistry.addClassLoaderHook(new NashornLoaderHook());
	}
}
