/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *    mkaufman@bea.com - initial API and implementation
 *    IBM - renamed from PreReconcileCompilationResult to ReconcileContext
 *    IBM - rewrote spec
 *
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.compiler;

import java.util.HashMap;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptElementDelta;
import org.eclipse.wst.jsdt.core.IJavaScriptModelMarker;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.internal.core.CompilationUnit;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.jsdt.internal.core.ReconcileWorkingCopyOperation;

/**
 * The context of a reconcile event that is notified to interested validation
 * participants while a reconcile operation is running.
 * <p>
 * A reconcile participant can get the AST for the reconcile-operation using
 * {@link #getAST3()}. If the participant modifies in any way the AST
 * (either by modifying the source of the working copy, or modifying another entity
 * that would result in different bindings for the AST), it is expected to reset the
 * AST in the context using {@link #resetAST()}.
 * </p><p>
 * A reconcile participant can also create and return problems using
 * {@link #putProblems(String, CategorizedProblem[])}. These problems are then reported
 * to the problem requestor of the reconcile operation.
 * </p><p>
 * This class is not intended to be instanciated or subclassed by clients.
 * </p>
 *
 * @see ValidationParticipant#reconcile(ReconcileContext)
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class ReconcileContext {

	private ReconcileWorkingCopyOperation operation;
	private CompilationUnit workingCopy;

/**
 * Creates a reconcile context for the given reconcile operation.
 * <p>
 * This constructor is not intended to be called by clients.
 * </p>
 *
 * @param operation the reconcile operation
 */
public ReconcileContext(ReconcileWorkingCopyOperation operation, CompilationUnit workingCopy) {
	this.operation = operation;
	this.workingCopy = workingCopy;
}

/**
 * Returns a resolved AST with {@link AST#JLS3 JLS3} level.
 * It is created from the current state of the working copy.
 * Creates one if none exists yet.
 * Returns <code>null</code> if the current state of the working copy
 * doesn't allow the AST to be created (e.g. if the working copy's content
 * cannot be parsed).
 * <p>
 * If the AST level requested during reconciling is not {@link AST#JLS3}
 * or if binding resolutions was not requested, then a different AST is created.
 * Note that this AST does not become the current AST and it is only valid for
 * the requestor.
 * </p>
 *
 * @return the AST created from the current state of the working copy,
 *   or <code>null</code> if none could be created
 * @exception JavaScriptModelException  if the contents of the working copy
 *		cannot be accessed. Reasons include:
 * <ul>
 * <li> The working copy does not exist (ELEMENT_DOES_NOT_EXIST)</li>
 * </ul>
 */
public org.eclipse.wst.jsdt.core.dom.JavaScriptUnit getAST3() throws JavaScriptModelException {
	if (this.operation.astLevel != AST.JLS3 || !this.operation.resolveBindings) {
		// create AST (optionally resolving bindings)
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setCompilerOptions(workingCopy.getJavaScriptProject().getOptions(true));
		if (JavaProject.hasJavaNature(workingCopy.getJavaScriptProject().getProject()))
			parser.setResolveBindings(true);
		parser.setStatementsRecovery((this.operation.reconcileFlags & IJavaScriptUnit.ENABLE_STATEMENTS_RECOVERY) != 0);
		parser.setBindingsRecovery((this.operation.reconcileFlags & IJavaScriptUnit.ENABLE_BINDINGS_RECOVERY) != 0);
		parser.setSource(workingCopy);
		return (org.eclipse.wst.jsdt.core.dom.JavaScriptUnit) parser.createAST(this.operation.progressMonitor);
	}
	return this.operation.makeConsistent(this.workingCopy);
}

/**
 * Returns the AST level requested by the reconcile operation.
 * It is either {@link IJavaScriptUnit#NO_AST}, or one of the JLS constants defined on {@link AST}.
 *
 * @return the AST level requested by the reconcile operation
 */
public int getASTLevel() {
	return this.operation.astLevel;
}

/**
 * Returns whether the reconcile operation is resolving bindings.
 *
 * @return whether the reconcile operation is resolving bindings
 */
public boolean isResolvingBindings() {
	return this.operation.resolveBindings;
}

/**
 * Returns the delta describing the change to the working copy being reconciled.
 * Returns <code>null</code> if there is no change.
 * Note that the delta's AST is not yet positioned at this stage. Use {@link #getAST3()}
 * to get the current AST.
 *
 * @return the delta describing the change, or <code>null</code> if none
 */
public IJavaScriptElementDelta getDelta() {
	return this.operation.deltaBuilder.delta;
}

/**
 * Returns the problems to be reported to the problem requester of the reconcile operation
 * for the given marker type.
 * Returns <code>null</code> if no problems need to be reported for this marker type.
 *
 * @param markerType the given marker type
 * @return problems to be reported to the problem requester
 */
public CategorizedProblem[] getProblems(String markerType) {
	if (this.operation.problems == null) return null;
	return (CategorizedProblem[]) this.operation.problems.get(markerType);
}

/**
 * Returns the working copy this context refers to.
 *
 * @return the working copy this context refers to
 */
public IJavaScriptUnit getWorkingCopy() {
	return this.workingCopy;
}

/**
 * Resets the AST carried by this context.
 * A validation participant that modifies the environment that would result in different
 * bindings for the AST is expected to reset the AST on this context, so that other
 * participants don't get a stale AST.
 * <p>
 * Note that resetting the AST will not restart the reconcile process. Only further
 * participants will see the new AST. Thus participants running before the one that
 * resets the AST will have a stale view of the AST and its problems. Use
 * the validation participant extension point to order the participants.
 * </p>
 */
public void resetAST() {
	this.operation.ast = null;
	putProblems(IJavaScriptModelMarker.JAVASCRIPT_MODEL_PROBLEM_MARKER, null);
	putProblems(IJavaScriptModelMarker.TASK_MARKER, null);
}

/**
 * Sets the problems to be reported to the problem requester of the reconcile operation
 * for the given marker type.
 * <code>null</code> indicates that no problems need to be reported.
 * <p>
 * Using this functionality, a participant that resolves problems for a given marker type
 * can hide those problems since they don't exist any longer.
 * </p>
 *
 * @param markerType the marker type of the given problems
 * @param problems  the problems to be reported to the problem requester of the reconcile operation,
 *   or <code>null</code> if none
 */
public void putProblems(String markerType, CategorizedProblem[] problems) {
	if (this.operation.problems == null)
		this.operation.problems = new HashMap();
	this.operation.problems.put(markerType, problems);
}

}
