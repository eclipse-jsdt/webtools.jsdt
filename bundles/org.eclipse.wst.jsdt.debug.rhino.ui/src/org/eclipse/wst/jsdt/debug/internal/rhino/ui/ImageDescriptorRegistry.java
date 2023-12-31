/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.ui;

 
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * A registry that maps <code>ImageDescriptors</code> to <code>Image</code>.
 * 
 * @since 1.0
 */
public class ImageDescriptorRegistry {

	private HashMap fRegistry= new HashMap(10);
	private Display fDisplay;
	
	/**
	 * Creates a new image descriptor registry for the current or default display,
	 * respectively.
	 */
	public ImageDescriptorRegistry() {
		this(PlatformUI.getWorkbench().getDisplay());
	}
	
	/**
	 * Creates a new image descriptor registry for the given display. All images
	 * managed by this registry will be disposed when the display gets disposed.
	 * 
	 * @param display the display the images managed by this registry are allocated for 
	 */
	public ImageDescriptorRegistry(Display display) {
		fDisplay= display;
		Assert.isNotNull(fDisplay);
		hookDisplay();
	}
	
	/**
	 * Returns the image associated with the given image descriptor.
	 * 
	 * @param descriptor the image descriptor for which the registry manages an image
	 * @return the image associated with the image descriptor or <code>null</code>
	 *  if the image descriptor can't create the requested image.
	 */
	public Image get(ImageDescriptor descriptor) {
		ImageDescriptor desc = descriptor;
		if (desc == null) {
			desc = ImageDescriptor.getMissingImageDescriptor();
		}
		Image result = (Image)fRegistry.get(desc);
		if (result != null) {
			return result; 
		}
		result = desc.createImage();
		if (result != null) {
			fRegistry.put(desc, result);
		}
		return result;
	}

	/**
	 * Disposes all images managed by this registry.
	 */	
	public void dispose() {
		for (Iterator iter= fRegistry.values().iterator(); iter.hasNext(); ) {
			Image image= (Image)iter.next();
			image.dispose();
		}
		fRegistry.clear();
	}
	
	private void hookDisplay() {
		fDisplay.asyncExec(new Runnable() {
			public void run() {
			fDisplay.disposeExec(new Runnable() {
				public void run() {
					dispose();
				}	
			});			
			}
		});

	}
}

