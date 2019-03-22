/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.wst.jsdt.js.npm.internal.launch.shortcut;

import org.eclipse.wst.jsdt.js.npm.NpmCommands;

/**
 *
 * @author Shane Bryzak
 */
public class NpmRunScript extends GenericNpmLaunch {

	@Override
	protected String getCommandName() {
		return NpmCommands.RUN_SCRIPT.getValue();
	}

}
