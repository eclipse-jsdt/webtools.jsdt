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
package org.eclipse.wst.jsdt.js.bower.internal.launch.shortcut;

import org.eclipse.wst.jsdt.js.bower.BowerCommands;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerInstall extends GenericBowerLaunch {

	@Override
	protected String getCommandName() {
		return BowerCommands.INSTALL.getValue();
	}
	
}
