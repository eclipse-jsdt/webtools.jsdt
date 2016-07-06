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
package org.eclipse.wst.jsdt.js.npm.internal.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.js.npm.NpmPlugin;

/**
 * Utility class to handle image resources.
 */
public class ImageResource {
	private static ImageRegistry imageRegistry;
	private static Map<String, ImageDescriptor> imageDescriptors;
	private static URL ICON_BASE_URL;

	public static final String IMG_NPMFILE = "npm_16.png"; //$NON-NLS-1$

	static {
		try {
			String pathSuffix = "icons/"; //$NON-NLS-1$
			ICON_BASE_URL = NpmPlugin.getDefault().getBundle()
					.getEntry(pathSuffix);
		} catch (Exception e) {
			NpmPlugin.logError(e, "Images error: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Return the image with the given key.
	 *
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.swt.graphics.Image
	 */
	public static Image getImage(String key) {
		return getImage(key, null);
	}

	/**
	 * Return the image with the given key.
	 *
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.swt.graphics.Image
	 */
	public static Image getImage(String key, String keyIfImageNull) {
		if (imageRegistry == null)
			initializeImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null) {
			if (keyIfImageNull != null) {
				return getImage(keyIfImageNull, null);
			}
			imageRegistry.put(key, ImageDescriptor.getMissingImageDescriptor());
			image = imageRegistry.get(key);
		}
		return image;
	}

	/**
	 * Return the image descriptor with the given key.
	 *
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.jface.resource.ImageDescriptor
	 */
	public static ImageDescriptor getImageDescriptor(String key) {
		if (imageRegistry == null)
			initializeImageRegistry();
		ImageDescriptor id = imageDescriptors.get(key);
		if (id != null)
			return id;

		return ImageDescriptor.getMissingImageDescriptor();
	}

	/**
	 * Initialize the image resources.
	 */
	protected static void initializeImageRegistry() {
		imageRegistry = NpmPlugin.getDefault().getImageRegistry();
		imageDescriptors = new HashMap<String, ImageDescriptor>();
		registerImage(IMG_NPMFILE, IMG_NPMFILE);
	}

	/**
	 * Register an image with the registry.
	 *
	 * @param key
	 *            java.lang.String
	 * @param partialURL
	 *            java.lang.String
	 */
	public static void registerImage(String key, String partialURL) {
		try {
			ImageDescriptor id = ImageDescriptor.createFromURL(new URL(
					ICON_BASE_URL, partialURL));
			registerImageDescriptor(key, id);
		} catch (Exception e) {
			NpmPlugin.logError(e, "Error registering image" + e.getMessage()); //$NON-NLS-1$
		}
	}

	public static void registerImageDescriptor(String key, ImageDescriptor id) {
		if (imageRegistry == null)
			initializeImageRegistry();
		imageRegistry.put(key, id);
		imageDescriptors.put(key, id);
	}

}