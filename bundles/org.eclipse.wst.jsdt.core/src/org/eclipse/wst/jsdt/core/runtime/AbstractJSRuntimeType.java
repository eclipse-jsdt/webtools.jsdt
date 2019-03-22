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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.core.runtime.RuntimeMessages;

/**
 * Provides a basic pre-implementation of a runtime type.
 * 
 * @author orlandor@mx1.ibm.com
 * 
 * @since 2.0
 *
 */
public abstract class AbstractJSRuntimeType implements IJSRuntimeType, IExecutableExtension {
	protected String fId;
	
	/**
	 * Initializes the id parameter from the "id" attribute in the configuration markup.
	 * 
	 */
	public final void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		fId = config.getAttribute("id"); //$NON-NLS-1$
	}

	public String getId() {
		return fId;
	}

	public final IJSRuntimeInstall createRuntimeInstall(String id) {
		if (JSRuntimeManager.getJSRuntimeInstall(id) != null) {
			throw new IllegalArgumentException(
						NLS.bind(RuntimeMessages.AbstractJSRuntimeType_DuplicateRuntimeInstall_Error, id));
		}
		IJSRuntimeInstall runtimeInstall = null;
		runtimeInstall = doCreateRuntimeInstall(id);
		return runtimeInstall;
	}
	
	/**
	 * Subclasses should return a new instance of the appropriate
	 * <code>IJSRuntimeInstall</code> subclass from this method.
	 * @param	id	The runtime's id. The <code>IJSRuntimeInstall</code> instance that is created must
	 * 				return <code>id</code> from its <code>getId()</code> method.
	 * 				Must not be <code>null</code>.
	 * @return	the newly created IVMInstall instance. Must not return <code>null</code>.
	 */
	protected abstract IJSRuntimeInstall doCreateRuntimeInstall(String id);

	public final IStatus validateInstallLocation(File tempFile) {
		if (tempFile == null) {
			return new Status (IStatus.ERROR, JavaScriptCore.PLUGIN_ID, 
						RuntimeMessages.AbstractJSRuntimeType_NullPath_Error);
		}
		if (!tempFile.exists()) {
			return new Status (IStatus.ERROR, JavaScriptCore.PLUGIN_ID, 
						NLS.bind (RuntimeMessages.AbstractJSRuntimeType_UnexistingInstallLocation_Error, 
									tempFile.getAbsolutePath()));	
		}
		
		return doValidateInstallLocation(tempFile);
	}
	
	/**
	 * Subclasses should return a validation additional to path existence
	 * @param tempFile
	 * @return
	 */
	protected abstract IStatus doValidateInstallLocation (File tempFile);

}
