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
package org.eclipse.wst.jsdt.internal.core;

public interface INamingRequestor {
	void acceptNameWithPrefixAndSuffix(char[] name, boolean isFirstPrefix, boolean isFirstSuffix, int reusedCharacters);
	void acceptNameWithPrefix(char[] name, boolean isFirstPrefix, int reusedCharacters);
	void acceptNameWithSuffix(char[] name, boolean isFirstSuffix, int reusedCharacters);
	void acceptNameWithoutPrefixAndSuffix(char[] name, int reusedCharacters);
}
