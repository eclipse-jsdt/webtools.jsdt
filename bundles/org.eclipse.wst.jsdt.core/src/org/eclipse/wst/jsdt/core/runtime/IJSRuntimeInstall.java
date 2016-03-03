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
 * Models an instance of {@link IBaseJSRuntimeInstall} but
 * extended with the read-only classification given by {@link IJSRuntimeType}.
 * 
 * @since 2.0
 */
public interface IJSRuntimeInstall extends IBaseJSRuntimeInstall {
	
	/**
	 * Returns an instance of runtime type, so this IJSRuntimeInstall can
	 * be classified.
	 * @return a valid runtime type for this runtime install contribution.
	 */
	public IJSRuntimeType getRuntimeType ();
}
