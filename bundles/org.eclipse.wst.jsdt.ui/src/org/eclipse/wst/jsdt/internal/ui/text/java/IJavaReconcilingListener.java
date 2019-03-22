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
package org.eclipse.wst.jsdt.internal.ui.text.java;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;


/**
 * Interface of an object listening to Java reconciling.
 *
 * 
 */
public interface IJavaReconcilingListener {

	/**
	 * Called before reconciling is started.
	 */
	void aboutToBeReconciled();

	/**
	 * Called after reconciling has been finished.
	 * @param ast				the compilation unit AST or <code>null</code> if
 * 								the working copy was consistent or reconciliation has been cancelled
	 * @param forced			<code>true</code> iff this reconciliation was forced
	 * @param progressMonitor	the progress monitor
	 */
	void reconciled(JavaScriptUnit ast, boolean forced, IProgressMonitor progressMonitor);
}
