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
package org.eclipse.wst.jsdt.internal.ui.text.java;




/**
 * Interface of an object participating in reconciling.
 *
 * @deprecated as of 3.0 use {@link IJavaReconcilingListener}
 */
public interface IReconcilingParticipant {

	/**
	 * Called after reconciling has been finished.
	 */
	void reconciled();
}
