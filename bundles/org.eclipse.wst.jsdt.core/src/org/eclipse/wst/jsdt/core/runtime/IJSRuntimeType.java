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
	 * @return runtime type name.
	 */
	public String getName ();
}
