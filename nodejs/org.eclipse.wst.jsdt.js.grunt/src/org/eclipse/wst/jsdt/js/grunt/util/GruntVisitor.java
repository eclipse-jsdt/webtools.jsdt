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
package org.eclipse.wst.jsdt.js.grunt.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.js.common.build.system.BuildSystemVisitor;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.Location;
import org.eclipse.wst.jsdt.js.common.build.system.util.ASTUtil;
import org.eclipse.wst.jsdt.js.grunt.internal.GruntTask;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GruntVisitor extends BuildSystemVisitor {
	private static final String GRUNT_INIT_CONFIG = "grunt.initConfig"; //$NON-NLS-1$
	private static final String GRUNT_REGISTER_TASK = "grunt.registerTask"; //$NON-NLS-1$
	private static final String GRUNT_REGISTER_MULTI_TASK = "grunt.registerMultiTask"; //$NON-NLS-1$
	
	private Set<ITask> tasks;
	private IFile file;
	
	public GruntVisitor(IFile file) {
		super();
		this.file = file;
		this.tasks = new HashSet<ITask>();
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(FunctionInvocation node) {
		Expression expression = node.getExpression();
		List<Expression> arguments = node.arguments();
		
		if (expression != null && arguments != null) {
			int argSize = arguments.size();
			
			// http://gruntjs.com/api/grunt.task#grunt.task.registertask
			if (GRUNT_REGISTER_TASK.equals(expression.toString())) {
				if (argSize == 2) {
					Expression task = arguments.get(0);
					tasks.add(new GruntTask(ASTUtil.beautify(task), file, false, new Location(task.getStartPosition(), task.getLength()))); 
				}
				return false;
			
			// http://gruntjs.com/api/grunt.task#grunt.task.registermultitask
			} else if (GRUNT_REGISTER_MULTI_TASK.equals(expression)) {
				if (argSize == 3) {
					Expression task = arguments.get(0);
					tasks.add(new GruntTask(ASTUtil.beautify(task), file, false, new Location(task.getStartPosition(), task.getLength()))); 
				}
				return false;
				
			// http://gruntjs.com/api/grunt#grunt.initconfig
			} else if (GRUNT_INIT_CONFIG.equals(expression.toString())) {
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
	
	
	public Set<ITask> getTasks() {
		return this.tasks;
	}
	
}
