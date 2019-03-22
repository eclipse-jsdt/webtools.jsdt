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
package org.eclipse.wst.jsdt.js.node.common.util;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.jsdt.js.node.common.CommonPlugin;
import org.eclipse.wst.jsdt.js.node.common.internal.CommonConstants;
import org.eclipse.wst.jsdt.js.node.common.json.objects.PackageJson;

/**
 * package.json utils 
 *
 * @see <a href="https://docs.npmjs.com/files/package.json">https://docs.npmjs.com/files/package.json</a>
 * @author "Adalberto Lopez Venegas (adalbert)"
 */

public class PackageJsonUtil {
	/**
	 * Reads a package.json file at the specified location and returns a PackageJson object 
	 * representing the package.json attributes and values.
	 * 
	 * @param fileLocation Absolute path of the package.json file
	 * @return PackageJson object representing the package.json file
	 * @throws FileNotFoundException
	 */
	public static PackageJson readPackageJsonFromFile(String fileLocation) throws FileNotFoundException{
		return JsonUtil.readJsonFromFile(fileLocation, PackageJson.class);
	}
	
	/**
	 * Reads a package.json file specified in the IFile and returns a PackageJson object 
	 * representing the package.json attributes and values.
	 * 
	 * @param file containing the package.json file
	 * @return PackageJson object representing the package.json file
	 * @throws FileNotFoundException
	 */
	public static PackageJson readPackageJsonFromIFile(IFile file) throws FileNotFoundException{
		return JsonUtil.readJsonFromIFile(file, PackageJson.class);
	}
	
	/**
	 * Reads a package.json contained in the IResource given and returns a PackageJson object 
	 * representing the package.json attributes and values.
	 * 
	 * @param resource IResource containing a package.json file
	 * @return PackageJson object representing the package.json file
	 */
	public static PackageJson readPackageJsonFromIResource(IResource resource) {
		IFile packageJsonFile = resource.getProject().getFile(CommonConstants.PACKAGE_JSON);
		if(packageJsonFile != null && packageJsonFile.isAccessible()){
			try {
				return readPackageJsonFromIFile(packageJsonFile);
			} catch (FileNotFoundException e) {
				CommonPlugin.logError(e);
			}
		}
		return null;
	}
}
