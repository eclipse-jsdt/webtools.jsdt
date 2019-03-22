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
package org.eclipse.wst.jsdt.internal.core.builder;

/**
 * Exception thrown when an incremental builder cannot find a .class file.
 * Its possible the type can no longer be found because it was renamed inside its existing
 * source file.
 */
public class AbortIncrementalBuildException extends RuntimeException {

protected String qualifiedTypeName;
private static final long serialVersionUID = -8874662133883858502L; // backward compatible

public AbortIncrementalBuildException(String qualifiedTypeName) {
	this.qualifiedTypeName = qualifiedTypeName;
}
}
