/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.common.tests.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class ResourceUtils {
	public static final String PLUGIN_ID = "org.eclipse.wst.jsdt.js.node.common.tests"; //$NON-NLS-1$
	public static File getResource(String relativePath)
			throws IOException {
		if (Platform.isRunning()) {
			URL fileURL = Platform.getBundle(PLUGIN_ID).getEntry(relativePath);
			File file = null;
			try {
				if (FileLocator.resolve(fileURL).toString().indexOf(" ") > -1) {
					URL location = FileLocator.resolve(fileURL);
					URI resolvedUri = new URI(location.getProtocol(),
							location.getPath(), null);
					file = new File(resolvedUri);
				} else {
					file = new File(FileLocator.resolve(fileURL).toURI());
				}

			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return file;
		}

		throw new IllegalStateException("Platform not running"); //$NON-NLS-1$
	}
}
