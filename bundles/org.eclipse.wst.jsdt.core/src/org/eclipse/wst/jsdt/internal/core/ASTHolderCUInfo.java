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
package org.eclipse.wst.jsdt.internal.core;

import java.util.HashMap;

import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;

public class ASTHolderCUInfo extends CompilationUnitElementInfo {
	int astLevel;
	boolean resolveBindings;
	int reconcileFlags;
	HashMap problems = null;
	JavaScriptUnit ast;
}
