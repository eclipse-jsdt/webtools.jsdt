/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * Implementation of IJavaScriptModel. A Java Model is specific to a
 * workspace.
 *
 * @see org.eclipse.wst.jsdt.core.IJavaScriptModel
 */
public class JavaModelInfo extends OpenableElementInfo {

	/**
	 * A array with all the non-java projects contained by this model
	 */
	Object[] nonJavaResources;

/**
 * Compute the non-java resources contained in this java project.
 */
private Object[] computeNonJavaResources() {
	IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
	int length = projects.length;
	Object[] resources = null;
	int index = 0;
	for (int i = 0; i < length; i++) {
		IProject project = projects[i];
		if (!JavaProject.hasJavaNature(project)) {
			if (resources == null) {
				resources = new Object[length];
			}
			resources[index++] = project;
		}
	}
	if (index == 0) return NO_NON_JAVA_RESOURCES;
	if (index < length) {
		System.arraycopy(resources, 0, resources = new Object[index], 0, index);
	}
	return resources;
}

/**
 * Returns an array of non-java resources contained in the receiver.
 */
Object[] getNonJavaResources() {

	if (this.nonJavaResources == null) {
		this.nonJavaResources = computeNonJavaResources();
	}
	return this.nonJavaResources;
}
}
