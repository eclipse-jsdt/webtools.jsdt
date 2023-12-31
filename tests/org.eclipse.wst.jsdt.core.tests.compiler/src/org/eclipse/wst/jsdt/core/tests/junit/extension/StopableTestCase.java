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
package org.eclipse.wst.jsdt.core.tests.junit.extension;

/**
 * A test case that is being sent stop() when the user presses 'Stop' or 'Exit'.
 */
public interface StopableTestCase {
	/**
	 * Invoked when this test needs to be stoped.
	 */
	public void stop();
}
