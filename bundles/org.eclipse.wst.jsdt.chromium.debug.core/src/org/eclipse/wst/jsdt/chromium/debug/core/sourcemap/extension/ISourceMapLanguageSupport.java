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
package org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension;

import org.eclipse.core.runtime.IPath;

/**
 * API to implement to add SourceMap Language Support.
 * <pre>
 * 
1) Here a basic implementation for TypeScript:
----------------------------------------------------------
package ts.eclipse.ide.jsdt.debug.internal.support;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension.ISourceMapLanguageSupport;

public class TypeScriptSourceMapLanguageSupport implements ISourceMapLanguageSupport {

	&#64;Override
	public IPath getJsFile(IPath file) {
		// TODO: search js file in the well folder by using tsconfig.json
		return file.removeFileExtension().addFileExtension("js");
	}

	&#64;Override
	public IPath getSourceMapFile(IPath file) {
		// TODO: search js file in the well folder by using tsconfig.json
		return file.removeFileExtension().addFileExtension("js.map");
	}
}
----------------------------------------------------------

2) Declare the extension point:
----------------------------------------------------------
<extension
                point=
"org.eclipse.wst.jsdt.chromium.debug.core.sourceMapLanguageSupports">
             <support
                   class=
"ts.eclipse.ide.jsdt.debug.internal.support.TypeScriptSourceMapLanguageSupport"
                   fileExtensions="ts,tsx"
                   id="ts.eclipse.ide.jsdt.debug.support1"
                   name="ts.eclipse.ide.jsdt.debug.support1">
             </support>
          </extension>
----------------------------------------------------------
 * 
 * </pre>
 *
 */
public interface ISourceMapLanguageSupport {

	/**
	 * Returns the ".js" file which is linked to the given file (".ts", ".tsx",
	 * ".coffee", etc).
	 * 
	 * @param file
	 *            a ".ts", ".tsx", ".coffee", etc to debug.
	 * @return the "js" file which is linked to the given file (".ts", ".tsx",
	 *         ".coffee", etc).
	 */
	IPath getJsFile(IPath file);

	/**
	 * Returns the ".js.map" SourceMap file which is linked to the given file (".ts", ".tsx",
	 * ".coffee", etc).
	 * 
	 * @param file
	 *            a ".ts", ".tsx", ".coffee", etc to debug.
	 * @return the ".js.map" SourceMap file which is linked to the given file (".ts", ".tsx",
	 *         ".coffee", etc).
	 */
	IPath getSourceMapFile(IPath file);

}
