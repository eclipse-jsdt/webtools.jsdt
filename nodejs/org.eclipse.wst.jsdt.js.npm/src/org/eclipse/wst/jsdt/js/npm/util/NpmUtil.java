/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.npm.util;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;
import org.eclipse.wst.jsdt.js.npm.PackageJson;
import org.eclipse.wst.jsdt.js.npm.internal.NpmConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class NpmUtil {

	private NpmUtil() {
	}

	public static boolean isPackageJsonExist(final IProject project) throws CoreException {
		IFile packageJson = null;
		if (project != null && project.isAccessible()) {
			packageJson = WorkbenchResourceUtil.findFileRecursively(project, NpmConstants.PACKAGE_JSON);
		}
		return (packageJson != null && packageJson.exists());
	}

	public static boolean hasPackageJson(final IFolder folder) throws CoreException {
		IResource packageJson = folder.findMember(NpmConstants.PACKAGE_JSON);
		return (packageJson != null && packageJson.exists());
	}

	public static boolean isPackageJson(final IResource resource) {
		return (resource != null && NpmConstants.PACKAGE_JSON.equals(resource.getName()) && resource.exists());
	}

	public static String generateJson(PackageJson packageJson) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		return gson.toJson(packageJson);
	}

	public static PackageJson parsePackageJsonFile(final IFile packageJsonFile)
			throws UnsupportedEncodingException, CoreException {
		JsonReader reader = new JsonReader(new InputStreamReader(packageJsonFile.getContents(), NpmConstants.UTF_8));

		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		PackageJson pj = gson.fromJson(reader, PackageJson.class);

		return pj;
	}

	/**
	 * @return path to directory in which native npm call must be performed. Basically, the method scans
	 * project for package.json file and returns it's parent, ignoring "node_modules"
	 * @throws CoreException
	 */
	public static IPath getNpmWorkingDir(IProject project, final String... ignores) throws CoreException {
		IPath workingDir = null;
		final List<IFile> foundFiles = new ArrayList<>();
		if (project != null && project.exists()) {
			project.accept(new IResourceVisitor() {

				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (!foundFiles.isEmpty()) {
						return false;
					} else if (resource.getType() == IResource.FOLDER && ignores != null) {
						for (String ignore : ignores) {
							if (resource.getName().equals(ignore)) {
								return false;
							}
						}
					} else if (resource.getType() == IResource.FILE
							&& NpmConstants.PACKAGE_JSON.equals(resource.getName())) {
						foundFiles.add((IFile) resource);
					}
					return true;
				}
			});
		}
		if (!foundFiles.isEmpty()) {
			workingDir = foundFiles.get(0).getParent().getLocation();
		}
		return workingDir;
	}
}