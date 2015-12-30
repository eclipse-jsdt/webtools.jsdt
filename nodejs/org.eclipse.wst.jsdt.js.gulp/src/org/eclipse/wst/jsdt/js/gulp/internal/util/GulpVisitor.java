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
package org.eclipse.wst.jsdt.js.gulp.internal.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.js.common.build.system.BuildSystemVisitor;
import org.eclipse.wst.jsdt.js.common.build.system.util.ASTUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GulpVisitor extends BuildSystemVisitor {
	private Set<String> tasks;
	
	private static final String GULP = "gulp"; //$NON-NLS-1$
	private static final String TASK= "task"; //$NON-NLS-1$
	
	public GulpVisitor() {
		super();
		this.tasks = new HashSet<String>();
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(FunctionInvocation node) {
		SimpleName functionName = node.getName();
		Expression expression = node.getExpression();
		List<Expression> arguments = node.arguments();

		if (TASK.equals(functionName.toString()) && GULP.equals(expression.toString())) {
			if (arguments.size() > 0) {
				Expression task = arguments.get(0);
				tasks.add(ASTUtil.beautify(task));
			}
		}

		return true;
	}
	
	public Set<String> getTasks() {
		return tasks;
	}
	
	
}
