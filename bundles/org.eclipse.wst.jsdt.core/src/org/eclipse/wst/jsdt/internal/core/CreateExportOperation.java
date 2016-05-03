/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.core;

import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.jsdt.core.IExportDeclaration;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatus;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptConventions;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ExportDeclaration;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.internal.core.util.Messages;

/**
 * <p>
 * This operation adds an export declaration to an existing compilation unit.
 * If the compilation unit already includes the specified export declaration,
 * the export is not generated (it does not generate duplicates).
 * 
 * <p>
 * Required Attributes:
 * <ul>
 * <li>Compilation unit
 * <li>Export name - the name of the export to add to the compilation unit.
 * </ul>
 */
public class CreateExportOperation extends CreateElementInCUOperation {
	/*
	 * The name of the export to be created.
	 */
	protected String exportName;

	/*
	 * The flags of the export to be created (either Flags#AccDefault or
	 * Flags#AccStatic)
	 */
	protected int flags;

	/**
	 * This operation will add an export to the specified compilation unit.
	 */
	public CreateExportOperation(String exportName, IJavaScriptUnit parentElement, int flags) {
		super(parentElement);
		this.exportName = exportName;
		this.flags = flags;
	}

	protected StructuralPropertyDescriptor getChildPropertyDescriptor(ASTNode parent) {
		return JavaScriptUnit.EXPORTS_PROPERTY;
	}

	protected ASTNode generateElementAST(ASTRewrite rewriter, IDocument document, IJavaScriptUnit cu) throws JavaScriptModelException {
		// ensure no duplicate
		Iterator exports = this.cuAST.exports().iterator();
		//boolean onDemand = this.exportName.endsWith(".*"); //$NON-NLS-1$
		String exportActualName = this.exportName;
		//if (onDemand) {
//			importActualName = this.importName.substring(0, this.importName.length() - 2);
	//	}
		while (exports.hasNext()) {
			ExportDeclaration exportDeclaration = (ExportDeclaration) exports.next();
			//ModuleSpecifier specifiers = exportDeclaration.specifiers();
			//if (exportActualName.equals(exportDeclaration.getName().getFullyQualifiedName()) && 
						//(onDemand == importDeclaration.isOnDemand()) && 
						//(Flags.isStatic(this.flags) == exportDeclaration.isStatic())) {
				//this.creationOccurred = false;
				//return null;
			//}
		}

		AST ast = this.cuAST.getAST();
		ExportDeclaration exportDeclaration = ast.newExportDeclaration();

		// split export name into individual fragments, checking for on demand
		// imports
		char[][] charFragments = CharOperation.splitOn('.', exportActualName.toCharArray(), 0, exportActualName.length());
		int length = charFragments.length;
		String[] strFragments = new String[length];
		for (int i = 0; i < length; i++) {
			strFragments[i] = String.valueOf(charFragments[i]);
		}
		Name name = ast.newName(strFragments);
		//exportDeclaration.setName(name);
		//if (onDemand)
		//	importDeclaration.setOnDemand(true);
		return exportDeclaration;
	}

	/**
	 * @see CreateElementInCUOperation#generateResultHandle
	 */
	protected IJavaScriptElement generateResultHandle() {
		return getCompilationUnit().getExport(this.exportName);
	}

	/**
	 * @see CreateElementInCUOperation#getMainTaskName()
	 */
	public String getMainTaskName() {
		return Messages.operation_createImportsProgress;
	}

	/**
	 * Sets the correct position for the new export:
	 * <ul>
	 * <li>after the last export
	 * <li>if no exports, before the first type
	 * <li>if no type, after the package statement
	 * <li>and if no package statement - first thing in the CU
	 */
	protected void initializeDefaultPosition() {
		try {
			IJavaScriptUnit cu = getCompilationUnit();
			IExportDeclaration[] exports = cu.getExports();
			if (exports.length > 0) {
				createAfter(exports[exports.length - 1]);
				return;
			}
			IType[] types = cu.getTypes();
			if (types.length > 0) {
				createBefore(types[0]);
				return;
			}
		}
		catch (JavaScriptModelException e) {
			// cu doesn't exit: ignore
		}
	}

	/**
	 * Possible failures:
	 * <ul>
	 * <li>NO_ELEMENTS_TO_PROCESS - the compilation unit supplied to the
	 * operation is <code>null</code>.
	 * <li>INVALID_NAME - not a valid import declaration name.
	 * </ul>
	 * 
	 * @see IJavaScriptModelStatus
	 * @see JavaScriptConventions
	 */
	public IJavaScriptModelStatus verify() {
		IJavaScriptModelStatus status = super.verify();
		if (!status.isOK()) {
			return status;
		}
		IJavaScriptProject project = getParentElement().getJavaScriptProject();
		if (JavaScriptConventions.validateImportDeclaration(this.exportName, 
					project.getOption(JavaScriptCore.COMPILER_SOURCE, true), 
					project.getOption(JavaScriptCore.COMPILER_COMPLIANCE, true)).getSeverity() == IStatus.ERROR) {
			return new JavaModelStatus(IJavaScriptModelStatusConstants.INVALID_NAME, this.exportName);
		}
		return JavaModelStatus.VERIFIED_OK;
	}
}
