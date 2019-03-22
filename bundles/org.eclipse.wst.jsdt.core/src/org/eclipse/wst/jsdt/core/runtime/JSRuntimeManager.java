/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.wst.jsdt.core.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.core.Logger;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimeInstallRegistryReader;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimeTypeRegistryReader;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimesDefinitionsContainer;
import org.eclipse.wst.jsdt.internal.core.runtime.RuntimeMessages;
import org.osgi.service.prefs.BackingStoreException;

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

	/**
	 * Preference key for the String of XML that defines all user defined runtime types.
	 */
	private static final String PREF_JS_RUNTIME_INSTALLS_XML = 
				JavaScriptCore.PLUGIN_ID + ".PREF_JS_RUNTIME_INSTALLS_XML"; //$NON-NLS-1$
	
	private static Map <String, IJSRuntimeInstall> jsRuntimes = new HashMap<String, IJSRuntimeInstall> ();
	private static Set <String> contributedRuntimeInstallIds = new HashSet <String> ();
	private static Map <String, String> defaultRuntimeInstalls = new HashMap<String, String> ();
	private static boolean runtimeManagerInitializing;
	private static boolean runtimeManagerInitialized;
	
	// Private constructor so no one can directly instantiate this.
	private JSRuntimeManager () {
	}
	
	/**
	 * Perform the internal data structure pre-filling by reading
	 * the extension points that contribute JS runtime installs, then
	 * switch the extPointsInitialized to the right value
	 */
	private static void initializeRuntimeManager () {
		runtimeManagerInitializing = true;
		synchronized (jsRuntimes) {
			Collection <IJSRuntimeInstall> extRuntimeInstalls = JSRuntimeInstallRegistryReader.getJSRuntimeInstalls();
			for (IJSRuntimeInstall extRuntimeInstall : extRuntimeInstalls) {
				jsRuntimes.put(extRuntimeInstall.getId(), extRuntimeInstall);
				contributedRuntimeInstallIds.add(extRuntimeInstall.getId());
			}
			
			// Try retrieving the user runtime installs from the preference store
			String runtimeInstallsXMLString = InstanceScope.INSTANCE.getNode(JavaScriptCore.PLUGIN_ID).
						get(PREF_JS_RUNTIME_INSTALLS_XML, ""); //$NON-NLS-1$

			JSRuntimesDefinitionsContainer container = null;
			if (runtimeInstallsXMLString.length() > 0) {
				try {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(runtimeInstallsXMLString.getBytes("UTF8")); //$NON-NLS-1$
					container = JSRuntimesDefinitionsContainer.parseXMLIntoContainer(inputStream);
				}
				catch (IOException ioe) {
					Logger.logException(ioe);
				}
			}
			
			if (container != null) {
				List <IJSRuntimeInstall> runtimeInstalls = container.getRuntimeList();
				for (IJSRuntimeInstall runtimeInstall : runtimeInstalls) {
					if (runtimeInstall instanceof JSRuntimeWorkingCopy) {
						((JSRuntimeWorkingCopy) runtimeInstall).convertToRealRuntime();
					}
				}
				
				// End up setting the default runtime types
				Collection <IJSRuntimeType> runtimeTypes = getJSRuntimeTypes();
				for (IJSRuntimeType runtimeType : runtimeTypes) {
					String defaultRuntimeId = container.getDefaultRuntimeInstallId(runtimeType.getId());
					if (defaultRuntimeId != null) {
						setDefaultRuntimeInstall(runtimeType.getId(), defaultRuntimeId);
						continue;
					}
				}
			}	
		}
		
		runtimeManagerInitializing = false;
		runtimeManagerInitialized = true;
	}
	
	/**
	 * Return the collection of known JSRuntimeTypes registered on the
	 * platform.
	 * 
	 * @return the collection of known JSRuntimeTypes registered on the
	 * platform.
	 */
	public static Collection<IJSRuntimeType> getJSRuntimeTypes () {
		return JSRuntimeTypeRegistryReader.getJSRuntimeTypes();
	}
	
	/**
	 * Return the collection of known JSRuntimeTypesIds registered on the
	 * platform.
	 * 
	 * @return the collection of known JSRuntimeTypesIds registered on the
	 * platform.
	 */
	public static Collection<String> getJSRuntimeTypesIds () {
		return JSRuntimeTypeRegistryReader.getJSRuntimeTypesIds();
	}
	
	/**
	 * Returns the runtime install looking for its id, or null if it does
	 * not exist.
	 * 
	 * @param jsRuntimeInstallId the runtime install id to look for
	 * @return the IJSRuntimeInstall the runtime install instance of registered
	 * on this manager, or null if it does not exist.
	 */
	public static IJSRuntimeInstall getJSRuntimeInstall(String jsRuntimeInstallId) {
		if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
			initializeRuntimeManager();
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
	public static IJSRuntimeInstall[] getJSRuntimeInstallsByType(String jsRuntimeInstallTypeId) {
		synchronized (jsRuntimes) {
			if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
				initializeRuntimeManager();
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
	 * @throws IllegalArgumentException if the given runtime install already
	 * exists in the manager.
	 */
	public static void addJSRuntimeInstall(IJSRuntimeInstall runtimeInstall) throws IllegalArgumentException {
		synchronized (jsRuntimes) {
			if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
				initializeRuntimeManager();
			}
			String id = runtimeInstall.getId();
			if (jsRuntimes.containsKey(id)) {
				throw new IllegalArgumentException(RuntimeMessages.JSRuntimeManager_DuplicatedRuntimeException);
			}
			
			jsRuntimes.put(runtimeInstall.getId(), runtimeInstall);	
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
	public static void updateJSRuntimeInstall(IJSRuntimeInstall runtimeInstall) throws IllegalArgumentException {
		synchronized (jsRuntimes) {
			if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
				initializeRuntimeManager();
			}
			
			IJSRuntimeInstall storedRuntimeInstall = jsRuntimes.get(runtimeInstall.getId());
			if (storedRuntimeInstall == null) {
				throw new IllegalArgumentException(RuntimeMessages.JSRuntimeManager_UnexistingRuntimeException);
			}
			
			// Update the item in the manager. If it was here it is safe to just replace it.
			jsRuntimes.put(runtimeInstall.getId(), runtimeInstall);	
		}
	}

	/**
	 * Removes an existing runtime install from this manager.
	 * 
	 * @param runtimeInstallId the id of the runtime install to remove.
	 * @throws IllegalArgumentException if this runtime id does not match
	 * any of the runtime installs registered in this manager.
	 */
	public static void removeJSRuntimeInstall(String runtimeInstallId) throws IllegalArgumentException {
		synchronized (jsRuntimes) {
			if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
				initializeRuntimeManager();
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
	public static void clear () {
		synchronized (jsRuntimes) {
			jsRuntimes.clear();
		}
	}
	
	/**
	 * Resets the current (and only) instance of the manager
	 * to its original state, to force a re-load of the contributions
	 */
	public static void reset () {
		clear ();
		runtimeManagerInitializing = false;
		runtimeManagerInitialized = false;
	}

	/**
	 * Returns the default runtime install for a given runtime type id.
	 * 
	 * This default element could come from any source (i.e. extension
	 * point contribution or user defined runtime), so take care
	 * on handling those possible cases when consuming this API
	 * 
	 * @param runtimeTypeId a valid runtime type id
	 * @return a valid runtime install if there is one associated with
	 * the received runtime type id. If no runtime install is found
	 * as default, this will return the first one in the collection
	 * that happens to be of the given type. If no runtime install is
	 * found associated with this runtime type, then null will be
	 * returned.
	 * @throws IllegalArgumentException if the runtime type is invalid,
	 * i.e. has not been registered through 
	 * org.eclipse.wst.jsdt.core.JSRuntimeType extension point.
	 */
	public static IJSRuntimeInstall getDefaultRuntimeInstall(String runtimeTypeId) throws IllegalArgumentException {
		synchronized (jsRuntimes) {
			if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
				initializeRuntimeManager();
			}
			
			IJSRuntimeType type = JSRuntimeTypeRegistryReader.getJSRuntimeType(runtimeTypeId);
			if (type == null) {
				throw new IllegalArgumentException(
							NLS.bind(RuntimeMessages.JSRuntimeManager_UnexistingRuntimeTypeException, runtimeTypeId));
			}
			
			String defaultInstallId = defaultRuntimeInstalls.get(runtimeTypeId);
			
			IJSRuntimeInstall defaultRuntimeInstall = getJSRuntimeInstall(defaultInstallId);
			
			// If for some reason there is no default for this install then try to re-calculate it
			if (defaultRuntimeInstall == null) {
				IJSRuntimeInstall[] runtimeInstalls = getJSRuntimeInstallsByType(runtimeTypeId);
				if (runtimeInstalls.length > 0) {
					defaultRuntimeInstall = runtimeInstalls[0];
					defaultRuntimeInstalls.put (runtimeTypeId, defaultRuntimeInstall.getId());
				}
			}
			
			return defaultRuntimeInstall;
		}
	}
	
	/**
	 * Set the default runtime install for a given runtime type on the manager
	 * 
	 * @param runtimeTypeId
	 * @param runtimeInstallId
	 */
	public static void setDefaultRuntimeInstall (String runtimeTypeId, String runtimeInstallId) {
		IJSRuntimeType type = JSRuntimeTypeRegistryReader.getJSRuntimeType(runtimeTypeId);
		if (type == null) {
			throw new IllegalArgumentException(
						NLS.bind(RuntimeMessages.JSRuntimeManager_UnexistingRuntimeTypeException, runtimeTypeId));
		}
		
		defaultRuntimeInstalls.put (runtimeTypeId, runtimeInstallId);
	}

	/**
	 * Returns whether the runtime install with the specified id was contributed via
     * the runtimeInstalls extension point.
	 * 
	 * @param runtimeInstallId
	 * @return true if the indicated runtime install happens to be contributed through 
	 * ext-point.
	 */
	public static boolean isContributedRuntimeInstall(String runtimeInstallId) {
		synchronized (jsRuntimes) {
			if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
				initializeRuntimeManager();
			}
		}
		
		return contributedRuntimeInstallIds.contains(runtimeInstallId);
	}

	/**
	 * Returns the runtime type based on its id (or null if there is no 
	 * such runtime type)
	 * 
	 * @param runtimeTypeId
	 * @return a valid runtime type given its id or null if there is 
	 * runtime type with that identifier.
	 */
	public static IJSRuntimeType getJSRuntimeType(String runtimeTypeId) {
		return JSRuntimeTypeRegistryReader.getJSRuntimeType(runtimeTypeId);
	}

	/**
	 * Force user runtime store on the preferences.
	 * 
	 * @throws CoreException when the XML in the preferences
	 * is invalid.
	 * 
	 */
	public static void saveRuntimesConfiguration() throws CoreException {
		if (!runtimeManagerInitializing && !runtimeManagerInitialized) {
			// Manager has not bee initialized, so really nothing to save
			return;
		}
		String xml = getUserRuntimesAsXML();
		InstanceScope.INSTANCE.getNode(JavaScriptCore.PLUGIN_ID).put(PREF_JS_RUNTIME_INSTALLS_XML, xml);
		savePreferences();
	}
	
	/**
	 * Returns the listing of currently installed user runtimes as a single XML file
	 * @return an XML representation of all of the currently installed runtimes
	 * @throws CoreException if trying to compute the XML for the runtime state encounters a problem
	 */
	private static String getUserRuntimesAsXML() throws CoreException {
		JSRuntimesDefinitionsContainer container = new JSRuntimesDefinitionsContainer();
		Collection<IJSRuntimeType> runtimeTypes = getJSRuntimeTypes(); 
		IJSRuntimeInstall[] runtimes = null;
		for (IJSRuntimeType runtimeType : runtimeTypes) {
			runtimes = getJSRuntimeInstallsByType(runtimeType.getId());
			for (int i = 0; i < runtimes.length; ++i) {
				// We must only provide the user installs
				if (!isContributedRuntimeInstall(runtimes[i].getId())) {
					container.addRuntime(runtimes[i]);	
				}
			}
			
			IJSRuntimeInstall defaultInstall = getDefaultRuntimeInstall(runtimeType.getId());
			if (defaultInstall != null) {
				container.setDefaultRuntimeInstallId(runtimeType.getId(), defaultInstall.getId());	
			}
		}
		return container.getAsXML();
	}
	
	/**
	 * Force preference save for the current plug-in.
	 */
	private static void savePreferences() {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(JavaScriptCore.PLUGIN_ID);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logException(e);
		}
	}

}
