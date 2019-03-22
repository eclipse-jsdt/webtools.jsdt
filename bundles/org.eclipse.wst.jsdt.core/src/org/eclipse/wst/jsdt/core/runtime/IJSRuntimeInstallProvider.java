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

import java.util.Collection;

/**
 * Interface that all contributors of 
 * <code>org.eclipse.wst.jsdt.core.JSRuntimeInstallProvider</code>
 * must implement.
 * 
 * @since 2.0
 */
public interface IJSRuntimeInstallProvider {

	/**
	 * Return a collection of runtime install contributions for
	 * the runtime type provided by this contributor.
	 * 
	 * NOTE: All runtime installs MUST be created using a call to
	 * the received runtimeType#createRuntimeInstall(String)
	 * and never instantiating a runtime type itself.
	 * @param runtimeType 
	 * 
	 * @return a collection of {@link IBaseJSRuntimeInstall} which represent
	 * valid runtime installations of the given runtime type.
	 */
	Collection<IJSRuntimeInstall> getJSRuntimeInstallContributions(IJSRuntimeType runtimeType);

}
