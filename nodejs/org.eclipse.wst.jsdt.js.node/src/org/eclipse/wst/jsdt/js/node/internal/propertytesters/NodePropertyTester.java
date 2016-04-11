/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.internal.propertytesters;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;

/**
 * Property tester for Node Launch Shortcut
 *
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodePropertyTester extends PropertyTester {
	// Properties
	private static final String HAS_PACKAGE_JSON = "hasPackageJson"; //$NON-NLS-1$
	private static final String IS_VALID_FILE = "isValidFile"; //$NON-NLS-1$

	// File extensions
	private static final String JS_EXT = "js"; //$NON-NLS-1$

	// Invalid parent folders for js files
	private static final String NODE_MODULES_FOLDER = "node_modules"; //$NON-NLS-1$
	private static final String BOWER_COMPONENTS_FOLDER = "bower_components"; //$NON-NLS-1$

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (HAS_PACKAGE_JSON.equals(property)) {
			return hasPackageJson((IProject) receiver);
		}
		else if (IS_VALID_FILE.equals(property)) {
			return isValidFile((IFile) receiver);
		}
		return false;
	}

	/**
	 *
	 * @param project
	 * @return
	 */
	public static boolean hasPackageJson(IProject project){
		IFile packageJsonFile = project.getFile(NodeConstants.PACKAGE_JSON);
		if (packageJsonFile != null && packageJsonFile.isAccessible()) {
			return true;
		}
		return false;
	}

	public static boolean isValidFile(IFile file) {
		// File must be a js file
		String fileExtension = file.getFileExtension();
		if (!fileExtension.equals(JS_EXT)) {
			return false;
		}

		// File must exists and be accessible
		if (file.exists() && file.isAccessible()) {
			String filePath = file.getFullPath().toOSString();

			// Js file cannot be inside node_modules or bower_components
			if (filePath != null
					&& !filePath.contains(NODE_MODULES_FOLDER) && !filePath.contains(BOWER_COMPONENTS_FOLDER)) {
				return true;
			}
		}
		return false;
	}

}

