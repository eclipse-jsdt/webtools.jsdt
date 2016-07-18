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
package org.eclipse.wst.jsdt.chromium.debug.js.launchers;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.wst.jsdt.chromium.debug.js.util.ChromiumUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ChromiumEditorLauncher implements IEditorLauncher{

	public static final String EDITOR_ID = "org.eclipse.wst.jsdt.chromium.debug.js.editor.chromium"; //$NON-NLS-1$

	@Override
	public void open(IPath path) {
		String fileURL = path.toFile().toURI().toASCIIString();
		fileURL = (fileURL != null) ? fileURL : ChromiumUtil.guessUrl(); 
		// FIXME Launch Chrome / Chromium
	}

}