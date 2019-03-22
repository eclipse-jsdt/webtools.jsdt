/*******************************************************************************
 * Copyright (c) 2016, 2017 Angelo Zerr.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * SourceMap manager API.
 *
 */
public interface ISourceMapManager {

	/**
	 * Returns the {@link ISourceMapLanguageSupport} for the given file
	 * extension and null otherwise.
	 * 
	 * @param fileExtension
	 *            "ts", "tsx", "coffee", etc
	 * @return the {@link ISourceMapLanguageSupport} for the given file
	 *         extension and null otherwise.
	 */
	ISourceMapLanguageSupport getSourceMapLanguageSupport(String fileExtension);

	/**
	 * Returns true if the given file extension can support SourceMap and false
	 * otherwise.
	 * 
	 * @param fileExtension
	 *            "ts", "tsx", "coffee", etc
	 * @return true if the given file extension can support SourceMap and false
	 *         otherwise.
	 */
	boolean canSupportSourceMap(String fileExtension);

	/**
	 * Returns the ".js" file which is linked to the given file (".ts", ".tsx",
	 * ".coffee", etc).
	 * 
	 * @param file
	 *            a ".ts", ".tsx", ".coffee", etc to debug.
	 * @return the "js" file which is linked to the given file (".ts", ".tsx",
	 *         ".coffee", etc).
	 */
	String getJsFile(IPath file) throws CoreException;

}
