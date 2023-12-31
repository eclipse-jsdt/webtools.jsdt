/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.core.model;

import java.net.URI;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.ITerminate;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.model.IScript;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;

/**
 * Wrapper for a {@link ScriptReference} to display it in the debug view
 * 
 * @since 1.0
 */
public class Script extends JavaScriptDebugElement implements IScript, Comparable, ITerminate {

	private ScriptReference script = null;
	
	/**
	 * Constructor
	 */
	public Script(JavaScriptDebugTarget target, ScriptReference script) {
		super(target);
		this.script  = script;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScript#source()
	 */
	public String source() {
		return this.script.source();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScript#sourceURI()
	 */
	public URI sourceURI() {
		return this.script.sourceURI();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if(o instanceof Script) {
			Script s2 = (Script) o;
			return sourceURI().toString().compareTo(s2.sourceURI().toString());
		}
		return 0;
	}
	
	/**
	 * Helper method to compute a usable name in the event the source URI has a path of "/"
	 * 
	 * @param sourceuri
	 * @return the name to use from the URI
	 */
	public static String resolveName(URI sourceuri) {
		String name = URIUtil.lastSegment(sourceuri);
		if(name == null) {
			name = new Path(sourceuri.getSchemeSpecificPart()).lastSegment();
		}
		if(name != null && !name.toLowerCase().endsWith(Constants.JS_EXTENSION)) {
			StringBuffer newname = new StringBuffer(name).append('.').append(Constants.JS_EXTENSION);
			name = newname.toString();
		}
		return name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		return getJavaScriptDebugTarget().canTerminate();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated() {
		return getJavaScriptDebugTarget().isTerminated();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public void terminate() throws DebugException {
		getJavaScriptDebugTarget().terminate();
	}
}
