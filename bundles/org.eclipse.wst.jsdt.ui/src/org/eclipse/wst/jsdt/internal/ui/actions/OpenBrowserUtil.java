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
package org.eclipse.wst.jsdt.internal.ui.actions;

import java.net.URL;

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class OpenBrowserUtil {
	
	public static void open(final URL url, Display display, final String dialogTitle) {
		display.syncExec(new Runnable() {
			public void run() {
				internalOpen(url, dialogTitle);
			}
		});
	}
	
	private static void internalOpen(final URL url, String title) {
		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(url.toExternalForm() + "?noframes=true"); //$NON-NLS-1$
			}
		});			
	}
}
