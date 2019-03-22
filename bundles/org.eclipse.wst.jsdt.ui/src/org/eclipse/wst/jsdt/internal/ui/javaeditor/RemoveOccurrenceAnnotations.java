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

package org.eclipse.wst.jsdt.internal.ui.javaeditor;

import org.eclipse.jface.action.Action;

/**
 * Remove occurrence annotations action.
 *
 * 
 */
class RemoveOccurrenceAnnotations extends Action {

	/** The Java editor to which this actions belongs. */
	private final JavaEditor fEditor;

	/**
	 * Creates this action.
	 *
	 * @param editor the Java editor for which to remove the occurrence annotations
	 */
	RemoveOccurrenceAnnotations(JavaEditor editor) {
		fEditor = editor;
	}

	/*
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		fEditor.removeOccurrenceAnnotations();
	}
}
