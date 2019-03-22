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

import java.io.File;

import org.eclipse.core.runtime.IStatus;

/**
 * Models a JavaScript Runtime Type. Exposes the public methods
 * that helps to identify a given runtime type from another.
 * 
 * All contributors of <code>org.eclipse.wst.jsdt.core.JSRuntimeInstallTypes</code>
 * MUST implement this interface.
 * 
 * @since 2.0
 *
 */
public interface IJSRuntimeType {
	/**
	 * Returns the runtime type id for the current instance.
	 * @return runtime type id.
	 */
	public String getId ();
	
	/**
	 * Returns the runtime type name for the current instance.
	 * 
	 * This is a required attribute because value returned here
	 * will be presented to user in the UI.
	 * @return runtime type name.
	 */
	public String getName ();
	
	/**
	 * Creates a new instance of this runtime type.
	 *  
	 * @param id An id String that must be unique.
	 * @return the newly created runtime install instance.
	 */
	public IJSRuntimeInstall createRuntimeInstall(String id);

	/**
	 * Validates install location.
	 * 
	 * @param tempFile
	 * @return an status indicating whether this install location is
	 * fine or not.
	 */
	public IStatus validateInstallLocation(File tempFile);
}
