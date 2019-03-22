/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/

/**
 * An Adapter factory for providing a FileExtensionFilterAdapter instance.
 * 
 * @author Shane Bryzak
 */
package org.eclipse.wst.jsdt.chromium.debug.ui.actions;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

public class FileExtensionFilterAdapterFactory implements IAdapterFactory {

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IActionFilter.class)
			return FileExtensionFilterAdapter.getInstance();

		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IActionFilter.class };
	}

}
