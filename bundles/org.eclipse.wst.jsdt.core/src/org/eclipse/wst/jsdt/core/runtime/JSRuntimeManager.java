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

package org.eclipse.wst.jsdt.core.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimeInstall;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimeInstallRegistryReader;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimeTypeRegistryReader;
import org.eclipse.wst.jsdt.internal.core.runtime.RuntimeMessages;

/**
 * A singleton JavaScript runtime manager that knows about *ALL* JavaScript
 * runtime installs (of all kinds), every time a new runtime install is added,
 * edited or removed from the system (usually through the user interface) this
 * manager must know about it.
 * 
 * This class is final, so it can not be extended.
 * 
 * @since 2.0
 * 
 */
public final class JSRuntimeManager {
	private Map <String, IJSRuntimeInstall> jsRuntimes;
	private static JSRuntimeManager fJSRuntimeManager;
	private static boolean extPointsInitialized;
	
	// Private constructor so no one can directly instantiate this.
	private JSRuntimeManager () {
		jsRuntimes = new HashMap<String, IJSRuntimeInstall> ();
	}
	
	/**
	 * @return a one and only instance of a runtime manager.
	 */
	public static JSRuntimeManager getDefault() {
		if (fJSRuntimeManager == null) {
			fJSRuntimeManager = new JSRuntimeManager();
		}
		
		return fJSRuntimeManager;
	}
	
	/**
	 * Perform the internal data structure pre-filling by reading
	 * the extension points that contribute JS runtime installs, then
	 * switch the extPointsInitialized to the right value
	 */
	private void initializeRuntimeManagerFromExtPts () {
		Collection <IJSRuntimeInstall> extRuntimeInstalls = JSRuntimeInstallRegistryReader.getJSRuntimeInstalls();
		for (IJSRuntimeInstall extRuntimeInstall : extRuntimeInstalls) {
			jsRuntimes.put(extRuntimeInstall.getId(), extRuntimeInstall);
		}
		extPointsInitialized = true;
	}
	
	/**
	 * Returns the runtime install looking for its id, or null if it does
	 * not exist.
	 * 
	 * @param jsRuntimeInstallId the runtime install id to look for
	 * @return the IJSRuntimeInstall the runtime install instance of registered
	 * on this manager, or null if it does not exist.
	 */
	public IJSRuntimeInstall getJSRuntimeInstall(String jsRuntimeInstallId) {
		if (!extPointsInitialized) {
			initializeRuntimeManagerFromExtPts();
		}
		return jsRuntimes.get(jsRuntimeInstallId);
	}
	
	/**
	 * Finds all runtime installs of the same type, given the type's id.
	 * 
	 * @param jsRuntimeInstallTypeId the JavaScript runtime install type that
	 * should be matched.
	 * @return an array of JavaScript runtime installs that matches the given install
	 * type.
	 * 
	 * @see IJSRuntimeInstall#getRuntimeType()
	 */
	public IJSRuntimeInstall[] getJSRuntimeInstallsByType(String jsRuntimeInstallTypeId) {
		synchronized (jsRuntimes) {
			if (!extPointsInitialized) {
				initializeRuntimeManagerFromExtPts();
			}
			Collection<IJSRuntimeInstall> allRuntimes = jsRuntimes.values();
			Collection<IJSRuntimeInstall> returnRuntimes = new ArrayList<IJSRuntimeInstall>();
			for (IJSRuntimeInstall runtime : allRuntimes) {
				if (runtime.getRuntimeType().getId().equals(jsRuntimeInstallTypeId)) {
					returnRuntimes.add(runtime);
				}
			}
			return returnRuntimes.toArray(new IJSRuntimeInstall[0]);	
		}
	}
	
	/**
	 * Registers a runtime install on this manager.
	 * 
	 * @param runtimeInstall valid (and unique) runtime install to register
	 * into this manager.
	 * @param runtimeTypeId valid runtime type id, which means, registered 
	 * properly through org.eclipse.wst.jsdt.core.JSRuntimeType ext point.
	 * @throws IllegalArgumentException if the given runtime install already
	 * exists in the manager or if the runtime type is invalid, i.e. has not
	 * been registered through org.eclipse.wst.jsdt.core.JSRuntimeType extension
	 * point.
	 */
	public void addJSRuntimeInstall(IBaseJSRuntimeInstall runtimeInstall, String runtimeTypeId) throws IllegalArgumentException {
		synchronized (jsRuntimes) {
			if (!extPointsInitialized) {
				initializeRuntimeManagerFromExtPts();
			}
			String id = runtimeInstall.getId();
			if (jsRuntimes.containsKey(id)) {
				throw new IllegalArgumentException(RuntimeMessages.JSRuntimeManager_DuplicatedRuntimeException);
			}
			
			IJSRuntimeType type = JSRuntimeTypeRegistryReader.getJSRuntimeType(runtimeTypeId);
			if (type == null) {
				throw new IllegalArgumentException(
							NLS.bind(RuntimeMessages.JSRuntimeManager_UnexistingRuntimeTypeException, runtimeTypeId));
			}
			
			jsRuntimes.put(runtimeInstall.getId(), new JSRuntimeInstall(runtimeInstall, runtimeTypeId));	
		}
	}

	/**
	 * Updates an existing runtime install on this manager with the provided instance.
	 * 
	 * @param runtimeInstall a valid runtime install to update, to match the existing
	 * instance to update, the given runtime install should have the same id. 
	 * @throws IllegalArgumentException if the given runtime install does not match 
	 * any of the existing runtime installs registered on this manager.
	 */
	public void updateJSRuntimeInstall(IBaseJSRuntimeInstall runtimeInstall) throws IllegalArgumentException {
		synchronized (jsRuntimes) {
			if (!extPointsInitialized) {
				initializeRuntimeManagerFromExtPts();
			}
			
			IJSRuntimeInstall storedRuntimeInstall = jsRuntimes.get(runtimeInstall.getId());
			if (storedRuntimeInstall == null) {
				throw new IllegalArgumentException(RuntimeMessages.JSRuntimeManager_UnexistingRuntimeException);
			}
			
			// Update the item in the manager. If it was here it is safe to just replace it.
			jsRuntimes.put(runtimeInstall.getId(), new JSRuntimeInstall(runtimeInstall, 
						storedRuntimeInstall.getRuntimeType().getId()));	
		}
	}

	/**
	 * Removes an existing runtime install from this manager.
	 * 
	 * @param runtimeInstallId the id of the runtime install to remove.
	 * @throws IllegalArgumentException if this runtime id does not match
	 * any of the runtime installs registered in this manager.
	 */
	public void removeJSRuntimeInstall(String runtimeInstallId) throws IllegalArgumentException {
		synchronized (jsRuntimes) {
			if (!extPointsInitialized) {
				initializeRuntimeManagerFromExtPts();
			}
			if (!jsRuntimes.containsKey(runtimeInstallId)) {
				throw new IllegalArgumentException(RuntimeMessages.JSRuntimeManager_UnexistingRuntimeException);
			}
			
			jsRuntimes.remove(runtimeInstallId);	
		}
	}
	
	/**
	 * Removes all existing runtime install from this manager.
	 */
	public void clear () {
		synchronized (jsRuntimes) {
			jsRuntimes.clear();
		}
	}
	
	/**
	 * Resets the current (and only) instance of the manager
	 * to its original state, to force a re-load of the contributions
	 */
	public void reset () {
		clear ();
		extPointsInitialized = false;
	}

}
