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
package org.eclipse.wst.jsdt.js.common.tests.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ResourceUtil {
	
	/**
	 * Get {@link File} from bundle if exists
	 * @param {@link String} bundle name
	 * @param {@link String} relative path to resource 
	 * @return {@link File} according to the path from the specified bundle if exists
	 */
	public static File getFileFromBundle(String bundleName, String resourcePath)
			throws IOException, URISyntaxException {
		Bundle bundle = Platform.getBundle(bundleName);
		URL fileURL = bundle.getEntry(resourcePath);
		URL resolvedFileURL = FileLocator.toFileURL(fileURL);
		URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
		File file = new File(resolvedURI);
		return file;
	}
	
}
