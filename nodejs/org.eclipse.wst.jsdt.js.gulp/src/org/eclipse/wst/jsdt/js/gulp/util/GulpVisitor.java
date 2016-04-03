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
package org.eclipse.wst.jsdt.js.gulp.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.js.common.build.system.BuildSystemVisitor;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.Location;
import org.eclipse.wst.jsdt.js.common.build.system.util.ASTUtil;
import org.eclipse.wst.jsdt.js.gulp.internal.GulpTask;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GulpVisitor extends BuildSystemVisitor {
	private static final String GULP_TASK= "gulp.task"; //$NON-NLS-1$
	
	private Set<ITask> tasks;
	private IFile file;

	public GulpVisitor(IFile file) {
		super();
		this.file = file;
		this.tasks = new HashSet<ITask>();
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(FunctionInvocation node) {
		Expression expression = node.getExpression();
		List<Expression> arguments = node.arguments();

		if (expression != null && arguments != null && GULP_TASK.equals(expression.toString())) {
			if (arguments.size() > 0) {
				Expression e = arguments.get(0);
				tasks.add(new GulpTask((ASTUtil.beautify(e)), file, false,
						new Location(e.getStartPosition(), e.getLength())));
			}
		}

		return true;
	}
	
	public Set<ITask> getTasks() {
		return tasks;
	}
		
}
