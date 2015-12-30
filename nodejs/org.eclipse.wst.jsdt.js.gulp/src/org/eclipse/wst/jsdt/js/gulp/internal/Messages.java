/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.gulp.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();

	public static String GulpLaunchError_Title;
	public static String GulpLaunchError_Message;
	public static String GulpLaunchTab_Main;
	public static String GulpLaunchTab_ErrorNotExist;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
