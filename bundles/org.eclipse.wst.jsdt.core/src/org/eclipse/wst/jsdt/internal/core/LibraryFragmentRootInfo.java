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

/**
 * The element info for <code>LibraryFragmentRootInfo</code>s.
 */
class LibraryFragmentRootInfo extends PackageFragmentRootInfo {
/**
 * Returns an array of non-java resources contained in the receiver.
 */
public Object[] getNonJavaResources() {
	fNonJavaResources = NO_NON_JAVA_RESOURCES;
	return fNonJavaResources;
}
}
