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
 * Property tester to verify project contains package.json file
 * 
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class PackageJsonPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("hasPackageJson".equals(property)) { //$NON-NLS-1$
			return hasPackageJson((IProject) receiver);
		}
		return false;
	}

	public static boolean hasPackageJson(IProject project){
		IFile packageJsonFile = project.getFile(NodeConstants.PACKAGE_JSON);
		if (packageJsonFile != null && packageJsonFile.isAccessible()) {
			return true;
		}
		return false;
	}

}

