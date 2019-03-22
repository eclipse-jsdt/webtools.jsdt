/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.compare;

/**
 * Provides "Replace from local history" for Java elements.
 */
public class JavaCompareWithEditionAction extends JavaHistoryAction {
	
	public JavaCompareWithEditionAction() {
	}
	
	protected JavaHistoryActionImpl createDelegate() {
		return new JavaCompareWithEditionActionImpl();
	}
}

