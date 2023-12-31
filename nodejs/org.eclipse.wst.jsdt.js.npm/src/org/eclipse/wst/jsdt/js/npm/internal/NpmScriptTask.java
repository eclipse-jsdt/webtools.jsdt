/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.npm.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.jsdt.js.common.build.system.AbstractTask;
import org.eclipse.wst.jsdt.js.common.build.system.Location;

/**
 *
 * @author Shane Bryzak
 *
 */
public class NpmScriptTask extends AbstractTask {

	private IResource parent;

	public NpmScriptTask(IResource parent, String name, IFile buildFile, boolean isDefault,
			Location location) {
		super(name, buildFile, isDefault, location);
		this.parent = parent;
	}

	public IResource getParent() {
		return parent;
	}
}
