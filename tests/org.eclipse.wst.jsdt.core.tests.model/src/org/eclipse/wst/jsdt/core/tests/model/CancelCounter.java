/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.core.runtime.IProgressMonitor;

/*
 * Counts the number of time isCancelled() is called.
 */
public class CancelCounter implements IProgressMonitor {
	public int count = 0;
	public void beginTask(String name, int totalWork) {}
	public void done() {}
	public void internalWorked(double work) {}
	public boolean isCanceled() {
		count++;
		return false;
	}
	public void setCanceled(boolean value) {}
	public void setTaskName(String name) {}
	public void subTask(String name) {}
	public void worked(int work) {}
}
