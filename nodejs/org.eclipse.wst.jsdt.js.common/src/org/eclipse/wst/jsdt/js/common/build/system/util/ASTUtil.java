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
package org.eclipse.wst.jsdt.js.common.build.system.util;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.js.common.build.system.BuildSystemVisitor;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ASTUtil {
	public static JavaScriptUnit getJavaScriptUnit(IFile file) throws JavaScriptModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(getCompilationUnit(file));
		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		return (JavaScriptUnit) parser.createAST(null);
	}
	
	public static Set<String> getTasks(String pathToFile, BuildSystemVisitor visitor) throws JavaScriptModelException {
		File file = WorkbenchResourceUtil.getFile(pathToFile);
		if (file != null) {
			IFile ifile = WorkbenchResourceUtil.getFileForLocation(file.getAbsolutePath());
			if (ifile != null) {
				JavaScriptUnit unit = ASTUtil.getJavaScriptUnit(ifile);
				unit.accept(visitor);
				return visitor.getTasks();
			}
		}
		return Collections.emptySet();
	}
	
	public static String beautify(Expression e) {
		if (e != null) {
			return e.toString().replaceAll("'", "").replaceAll("\"", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return null;
	}
	
	private static IJavaScriptUnit getCompilationUnit(IFile file) {
		return (IJavaScriptUnit) JavaScriptCore.create(file);
	}
	
	
}
