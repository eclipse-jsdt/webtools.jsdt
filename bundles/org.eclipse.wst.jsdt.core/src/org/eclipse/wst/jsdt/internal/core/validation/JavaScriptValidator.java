/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.wst.jsdt.internal.core.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.compiler.CategorizedProblem;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.internal.compiler.env.INameEnvironment;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.jsdt.internal.core.builder.ClasspathMultiDirectory;
import org.eclipse.wst.jsdt.internal.core.builder.NameEnvironment;
import org.eclipse.wst.jsdt.internal.core.builder.SourceFile;
import org.eclipse.wst.jsdt.internal.core.util.Util;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.ValidatorMessage;

/**
 * @author orlandor@mx1.ibm.com
 * 
 * JavaScript V2 Validator
 *
 */
public class JavaScriptValidator extends AbstractValidator {
	private ASTParser parser = ASTParser.newParser(AST.JLS3);
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.validation.AbstractValidator#validate(org.eclipse.core.resources.IResource, int, org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ValidationResult validate(IResource resource, int kind, ValidationState state, IProgressMonitor monitor) {
		ValidationResult result = new ValidationResult();

		JavaProject project = (JavaProject) JavaScriptCore.create(resource.getProject());
		INameEnvironment nameEnvironment = new NameEnvironment(project);
		
		// Just care about javascript valid files
		if (resource instanceof IFile && Util.isJavaLikeFileName(resource.getName())) {
			SourceFile sf = findSourceFile((IFile) resource, true, nameEnvironment);
			if (sf == null) {
				return result;
			}
			
			// Does not worth keep analyzing
			parser.setSource(sf.getContents());
			JavaScriptUnit unit = (JavaScriptUnit) parser.createAST(monitor);
			
			if(unit.getProblems().length > 0){
				CategorizedProblem[] resourceProblems = (CategorizedProblem[]) unit.getProblems();
				for (CategorizedProblem problem : resourceProblems) {
					ValidatorMessage vm = ValidatorMessage.create(problem.getMessage(), resource);
					vm.setAttribute(IMarker.SEVERITY, problem.isError() ? IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING);
					vm.setType(problem.getMarkerType());
					//Esprima only provides line numbers 
					vm.setAttribute(IMarker.LINE_NUMBER, problem.getSourceLineNumber());
					result.add(vm);
				}
			}
		}
		return result;
	}
	
	/*
	 * Pretty much a copy of org.eclipse.wst.jsdt.internal.core.builder.AbstractImageBuilder.findSourceFile(IFile, boolean)
	 */
	private SourceFile findSourceFile(IFile file, boolean mustExist, INameEnvironment nameEnvironment) {
		if (mustExist && !file.exists()) return null;
		if (file.isDerived()) return null;

		ClasspathMultiDirectory [] sourceLocations = ((NameEnvironment)nameEnvironment).getSourceLocations();
		if (sourceLocations == null) // A project might not have source locations set
			return null; 
		
		// assumes the file exists in at least one of the source folders & is not excluded
		ClasspathMultiDirectory md = sourceLocations[0];
		if (sourceLocations.length > 1) {
			IPath sourceFileFullPath = file.getFullPath();
			for (int j = 0, m = sourceLocations.length; j < m; j++) {
				if (sourceLocations[j].getSourceFolder().getFullPath().isPrefixOf(sourceFileFullPath)) {
					md = sourceLocations[j];
					if (md.getExclusionPatterns() == null && md.getInclusionPatterns() == null)
						break;
					if (!Util.isExcluded(file, md.getInclusionPatterns(), md.getExclusionPatterns()))
						break;
				}
			}
		}
		
		if (!Util.isExcluded(file, md.getInclusionPatterns(), md.getExclusionPatterns())) {
			return new SourceFile(file, md);
		}
		
		return null;
	}	
}