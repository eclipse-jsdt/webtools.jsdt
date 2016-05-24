/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.internal.corext.template.java.CompilationUnitContextType;
import org.eclipse.wst.jsdt.internal.corext.template.java.JavaContextType;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.ui.text.identifier.contentassist.IdentifierEngine;
import org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.wst.jsdt.ui.text.java.JavaContentAssistInvocationContext;

public class IdentifierCompletionProposalComputer implements IJavaCompletionProposalComputer {

	private IdentifierEngine engine;

	public IdentifierCompletionProposalComputer() {
		CompilationUnitContextType contextType = (CompilationUnitContextType) JavaScriptPlugin.getDefault().getTemplateContextRegistry().getContextType(JavaContextType.NAME);
		engine = new IdentifierEngine(contextType);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer#sessionStarted()
	 */
	public void sessionStarted() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer#computeCompletionProposals(org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		List<ICompletionProposal> result = Collections.emptyList();
		if (context instanceof JavaContentAssistInvocationContext) {
			JavaContentAssistInvocationContext javaContext = (JavaContentAssistInvocationContext) context;
			IJavaScriptUnit unit = javaContext.getCompilationUnit();
	
			engine.reset();
			engine.complete(javaContext.getProject(), javaContext.getViewer(), javaContext.getInvocationOffset(), unit);
	
			ICompletionProposal[] identifierProposals =  engine.getResults();
			result = new ArrayList<ICompletionProposal>(Arrays.asList(identifierProposals));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer#computeContextInformation(org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer#getErrorMessage()
	 */
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer#sessionEnded()
	 */
	public void sessionEnded() {
		// TODO Auto-generated method stub

	}

}