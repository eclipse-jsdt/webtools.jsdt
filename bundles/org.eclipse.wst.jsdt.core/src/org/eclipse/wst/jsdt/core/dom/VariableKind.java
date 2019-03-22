/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom;

/**
 * The different variable declaration kinds as defined on ES 2015
 * specification.
 *
 * @author Gorkem Ercan
 * @since 2.0
 *
 */
public enum VariableKind {
	VAR, LET, CONST;
}