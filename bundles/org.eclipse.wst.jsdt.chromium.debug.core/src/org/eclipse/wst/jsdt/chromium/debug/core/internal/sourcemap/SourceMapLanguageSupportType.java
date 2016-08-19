/*******************************************************************************
 * Copyright (c) 2016, 2017 Angelo Zerr.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.core.internal.sourcemap;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension.ISourceMapLanguageSupport;

public class SourceMapLanguageSupportType {

	private final String id;
	private final ISourceMapLanguageSupport support;
	private final Set<String> fileExtensions;

	public SourceMapLanguageSupportType(IConfigurationElement ce) throws CoreException {
		this.id = ce.getAttribute("id");
		this.support = (ISourceMapLanguageSupport) ce.createExecutableExtension("class");
		this.fileExtensions = createFileExtensions(ce);
	}

	private Set<String> createFileExtensions(IConfigurationElement ce) {
		String[] fileExtensions = ce.getAttribute("fileExtensions").split(",");
		Set<String> set = new HashSet<String>();
		for (String fileExtension : fileExtensions) {
			set.add(fileExtension.trim());
		}
		return set;
	}

	public ISourceMapLanguageSupport getSupport() {
		return support;
	}

	public String getId() {
		return id;
	}

	public boolean canSupportSourceMap(String fileExtension) {
		return fileExtensions.contains(fileExtension);
	}

}
