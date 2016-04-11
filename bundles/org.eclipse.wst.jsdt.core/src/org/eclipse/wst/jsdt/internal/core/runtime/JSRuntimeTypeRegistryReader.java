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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.internal.core.Logger;
import org.eclipse.wst.jsdt.internal.core.util.Util;

/**
 * Reads the <code>org.eclipse.wst.jsdt.core.JSRuntimeType</code>
 * contributors which will let the framework know about different
 * specific kinds of runtime types (to name a few: node.js, rhino,
 * an specific browser, etc.)
 *
 */
public class JSRuntimeTypeRegistryReader {
	private final static String EXTENSION_NAME = "org.eclipse.wst.jsdt.core.JSRuntimeType"; //$NON-NLS-1$
	private final static String RUNTIME_TYPE = "runtimeType"; //$NON-NLS-1$
	private final static String RUNTIME_TYPE_ID_ATTR = "id"; //$NON-NLS-1$
	private final static String RUNTIME_TYPE_CLASS_ATTR = "class"; //$NON-NLS-1$
	
	private static Map <String, IJSRuntimeType> runtimeTypesMap = null;
	
	private static void initRuntimeTypesMap () {
		runtimeTypesMap = new HashMap<String, IJSRuntimeType> ();
		
		IExtensionPoint exp = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_NAME);

		if (exp == null) {
			return;
		}

		for (IExtension extension : exp.getExtensions()) {
			for (IConfigurationElement typeConfigElements : extension.getConfigurationElements()) {
				if (typeConfigElements.getName().equals(RUNTIME_TYPE)) {
					if (typeConfigElements.getAttribute(RUNTIME_TYPE_ID_ATTR) != null) {
						// Id is required, while name is just optional
						String id = typeConfigElements.getAttribute(RUNTIME_TYPE_ID_ATTR);
						
						// There is a possibility that the runtime type is actually duplicated
						// and might be overriden by this implementation, but since currently
						// the runtime type is only holding id and name it does not look that
						// would be a really bad case, so just let that happen with no additional
						// complain.
						if (runtimeTypesMap.containsKey(id)) {
							Logger.log(Logger.WARNING, "Duplicated runtime type with id = " + id); //$NON-NLS-1$	
						}
						
						try {
							IJSRuntimeType runtimeType = 
										(IJSRuntimeType) typeConfigElements.createExecutableExtension(RUNTIME_TYPE_CLASS_ATTR);
							runtimeTypesMap.put(id, runtimeType);
						} catch (Exception e) {
							Util.log(e, "Error instantiating class " + typeConfigElements.getAttribute(RUNTIME_TYPE_CLASS_ATTR)); //$NON-NLS-1$
						}
					}
				} else {
					Logger.log(Logger.WARNING, "Unexpected tag " + typeConfigElements.getName() +  //$NON-NLS-1$
						". Expected " + RUNTIME_TYPE + " instead."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	public static Collection <IJSRuntimeType> getJSRuntimeTypes() {
		// If this method was previously visited, just return the cached
		// values from the last read.
		if (runtimeTypesMap != null) {
			return runtimeTypesMap.values();
		}
		
		initRuntimeTypesMap();

		return runtimeTypesMap.values();
	}
	
	public static Collection <String> getJSRuntimeTypesIds () {
		// If this method was previously visited, just return the cached
		// values from the last read.
		if (runtimeTypesMap != null) {
			return runtimeTypesMap.keySet();
		}
				
		initRuntimeTypesMap();

		return runtimeTypesMap.keySet();
	}
	
	public static IJSRuntimeType getJSRuntimeType (String typeId) {
		if (runtimeTypesMap == null) {
			initRuntimeTypesMap();
		}
				
		return runtimeTypesMap.get(typeId);
		
	}
}
