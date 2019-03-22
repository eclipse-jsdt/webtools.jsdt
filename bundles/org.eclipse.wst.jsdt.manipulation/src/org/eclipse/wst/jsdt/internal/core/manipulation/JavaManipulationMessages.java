/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.manipulation;

import org.eclipse.osgi.util.NLS;

public class JavaManipulationMessages extends NLS {
	
	private static final String BUNDLE_NAME= "org.eclipse.wst.jsdt.internal.core.manipulation.JavaManipulationMessages"; //$NON-NLS-1$

	private JavaManipulationMessages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, JavaManipulationMessages.class);
	}
	
	public static String JavaManipulationMessages_internalError;
}
