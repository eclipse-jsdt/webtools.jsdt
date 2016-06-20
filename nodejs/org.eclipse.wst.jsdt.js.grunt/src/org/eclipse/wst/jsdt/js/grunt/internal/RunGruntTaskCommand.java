/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.grunt.internal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;
import org.eclipse.wst.jsdt.js.grunt.internal.launch.shortcut.GruntLaunch;

/**
 * A command that executes the "Run as Grunt" task for the currently selected
 * project, if it is a Grunt project.
 *
 * @author Shane Bryzak
 */
public class RunGruntTaskCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = WorkbenchResourceUtil.getNamedFileOrTaskSelection(GruntConstants.GRUNT_FILE_JS,
				GruntTask.class);
		if (selection != null) {
			new GruntLaunch().launch(selection, GruntConstants.RUN_COMMAND);
		}

		return null;
	}
}
