/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.npm.internal.launch.shortcut;

import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
import org.eclipse.wst.jsdt.js.npm.NpmCommands;
import org.eclipse.wst.jsdt.js.npm.internal.NpmConstants;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NpmUpdate extends GenericNpmLaunch {
	private static final String LAUNCH_NAME = "npm Update"; //$NON-NLS-1$
	private static final CLICommand COMMAND = new CLICommand(NpmConstants.NPM, NpmCommands.UPDATE.getValue(),
			null, null);
	
	@Override
	protected  String getLaunchName() {
		return LAUNCH_NAME;
	}
	
	@Override
	protected String getCommandName() {
		return NpmCommands.UPDATE.getValue();
	}

	@Override
	protected CLICommand getCLICommand() {
		return COMMAND;
	}

}
