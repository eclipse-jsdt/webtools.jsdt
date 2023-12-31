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
package org.eclipse.wst.jsdt.internal.formatter;

/**
 * Unchecked exception wrapping invalid input checked exception which may occur
 * when scanning original formatted source.
 *
 * @since 2.1
 */
public class AbortFormatting extends RuntimeException {

	Throwable nestedException;
	private static final long serialVersionUID = -5796507276311428526L; // backward compatible

	public AbortFormatting(String message) {
		super(message);
	}
	public AbortFormatting(Throwable nestedException) {
		super(nestedException.getMessage());
		this.nestedException = nestedException;
	}
}
