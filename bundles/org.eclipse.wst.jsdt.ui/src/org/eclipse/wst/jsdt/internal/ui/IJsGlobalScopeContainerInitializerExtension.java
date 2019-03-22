/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.wst.jsdt.core.IJavaScriptProject;

/**
 * @author childsb
 *
 */
public interface IJsGlobalScopeContainerInitializerExtension{
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaScriptProject project);
}
