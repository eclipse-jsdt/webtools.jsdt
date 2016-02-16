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
package org.eclipse.wst.jsdt.js.grunt.internal.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.js.common.build.system.BuildSystemVisitor;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.Location;
import org.eclipse.wst.jsdt.js.common.build.system.util.ASTUtil;
import org.eclipse.wst.jsdt.js.grunt.internal.GruntTask;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GruntVisitor extends BuildSystemVisitor {
	private Set<ITask> tasks;
	private IFile file;
	private static final String GRUNT = "grunt"; //$NON-NLS-1$
	private static final String REGISTER_TASK = "registerTask"; //$NON-NLS-1$
	private static final String REGISTER_MULTI_TASK = "registerMultiTask"; //$NON-NLS-1$
	private static final String INIT_CONFIG = "initConfig"; //$NON-NLS-1$
	
	public GruntVisitor(IFile file) {
		super();
		this.file = file;
		this.tasks = new HashSet<ITask>();
	}
	
	public Set<ITask> getTasks() {
		return this.tasks;
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(FunctionInvocation node) {
		SimpleName functionName = node.getName();
		Expression expression = node.getExpression();
		List<Expression> arguments = node.arguments();
		
		node.getStartPosition();
		node.getLength();
		if (functionName != null && expression != null && arguments != null) {
			int argSize = arguments.size();
			
			// http://gruntjs.com/api/grunt.task#grunt.task.registertask
			if (REGISTER_TASK.equals(functionName.toString()) && GRUNT.equals(expression.toString())) {
				if (argSize == 2) {
					Expression task = arguments.get(0);
					tasks.add(new GruntTask(ASTUtil.beautify(task), file, false, new Location(task.getStartPosition(), task.getLength()))); 
				}
				return false;
			
			// http://gruntjs.com/api/grunt.task#grunt.task.registermultitask
			} else if (REGISTER_MULTI_TASK.equals(functionName.toString()) && GRUNT.equals(expression.toString())) {
				if (argSize == 3) {
					Expression task = arguments.get(0);
					tasks.add(new GruntTask(ASTUtil.beautify(task), file, false, new Location(task.getStartPosition(), task.getLength()))); 
				}
				return false;
				
			// http://gruntjs.com/api/grunt#grunt.initconfig
			} else if (INIT_CONFIG.equals(functionName.toString()) && GRUNT.equals(expression.toString())) {
				if (argSize == 1 && arguments.get(0) instanceof ObjectLiteral) {
					ObjectLiteral jsObject = (ObjectLiteral) arguments.get(0);
					List<ObjectLiteralField> fields = jsObject.fields();
					for (ObjectLiteralField f : fields) {
						Expression field = f.getFieldName();
						tasks.add(new GruntTask((field.toString()), file, false, new Location(field.getStartPosition(), field.getLength())));
					}
				}
				return false;
			}
		}
		
		return true;
	}
	
}
