/*******************************************************************************
 * Copyright (c) 2005, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IPageLayout;

public class ProductProperties {

	/**
	 * Default values for WTP level product
	 */
	public static final String ID_PERSPECTIVE_EXPLORER_VIEW = IPageLayout.ID_PROJECT_EXPLORER;
	/**
	 * Default values for WTP level product
	 */
	public static final String ID_DEFAULT_JS_EDITOR = "org.eclipse.ui.genericeditor.GenericEditor"; //$NON-NLS-1$

	/**
	 * Return the value for the associated key from the Platform Product registry or return the
	 * WTP default for the JavaScript cases, or <code>null</code> if none.
	 * 
	 * @param key
	 * @return String value of product's property
	 */
	public static String getProperty(String key) {
		if (key == null)
			return null;
		String value = null;
		if (Platform.getProduct()!=null)
			value = Platform.getProduct().getProperty(key);
		if (value == null) {
			if (key.equals(IProductConstants.PERSPECTIVE_EXPLORER_VIEW))
				return ID_PERSPECTIVE_EXPLORER_VIEW;
		}
		return value;
	}
	
}
