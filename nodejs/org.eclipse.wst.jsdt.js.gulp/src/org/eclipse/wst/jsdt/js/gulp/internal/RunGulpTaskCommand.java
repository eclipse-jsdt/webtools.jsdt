/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.gulp.internal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;
import org.eclipse.wst.jsdt.js.gulp.internal.launch.shortcut.GulpLaunch;

/**
 * Provides a command to the quick start that allows either a selected Gulp
 * task, or the default Gulp task in the selected project to be run.
 *
 * @author Shane Bryzak
 */
public class RunGulpTaskCommand extends AbstractHandler {

	public static final String GULP_FILE_NAME = "gulpfile.js";
	public static final String GULP_TASK_RUN = "run";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = WorkbenchResourceUtil.getNamedFileOrTaskSelection(GulpConstants.GULP_FILE_JS,
				GulpTask.class);
		if (selection != null) {
			new GulpLaunch().launch(selection, GulpConstants.RUN_COMMAND);
		}

		return null;
	}

}
