/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class IEMessages extends NLS {
	private static final String BUNDLE_NAME= "org.eclipse.wst.jsdt.internal.ui.wizards.IEMessages";//$NON-NLS-1$

	private IEMessages() {
		// Do not instantiate
	}
	
	public static String IELibraryWizardPage_title;
	public static String IELibraryWizardPage_IELibraryAdded;
	public static String IELibraryWizardPage_BrowserSupport;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, IEMessages.class);
	}

}
