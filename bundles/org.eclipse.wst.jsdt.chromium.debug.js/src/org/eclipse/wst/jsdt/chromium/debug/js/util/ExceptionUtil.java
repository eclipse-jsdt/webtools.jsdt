/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.js.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.jsdt.chromium.debug.js.JSDebuggerPlugin;
import org.eclipse.wst.jsdt.chromium.debug.js.Messages;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class ExceptionUtil {

	private ExceptionUtil() {
	}

	public static void showErrorDialog(Exception e) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				IStatus status = new Status(IStatus.ERROR, JSDebuggerPlugin.PLUGIN_ID, e.getMessage(), new Exception(sw.toString()));
				ErrorDialog.openError(Display.getDefault().getActiveShell(), Messages.ERROR_DIALOG_TITLE, null, status);
			}
		});
	}

}
