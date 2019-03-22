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
package org.eclipse.wst.jsdt.internal.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.ui.ISharedImages;

/**
 * Default implementation of ISharedImages
 */
public class SharedImages implements ISharedImages {
	
	public SharedImages() {
	}
		
	/* (Non-Javadoc)
	 * Method declared in ISharedImages
	 */
	public Image getImage(String key) {
		return JavaPluginImages.get(key);
	}
	
	/* (Non-Javadoc)
	 * Method declared in ISharedImages
	 */
	public ImageDescriptor getImageDescriptor(String key) {
		return JavaPluginImages.getDescriptor(key);
	}

}
