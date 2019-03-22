/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
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
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.internal.corext.template.java.CompilationUnitContextType;
import org.eclipse.wst.jsdt.internal.corext.template.java.JavaContextType;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.text.keyword.contentassist.KeywordEngine;
import org.eclipse.wst.jsdt.internal.ui.text.keyword.contentassist.KeywordProposal;
import org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.wst.jsdt.ui.text.java.JavaContentAssistInvocationContext;

public class KeywordCompletionProposalComputer implements IJavaCompletionProposalComputer {

	private KeywordEngine engine;
	
	public KeywordCompletionProposalComputer() {
		// TODO: Is TemplateContextType the type we want?
		CompilationUnitContextType contextType = (CompilationUnitContextType) JavaScriptPlugin.getDefault().getTemplateContextRegistry().getContextType(JavaContextType.NAME);
		engine = new KeywordEngine(contextType);
	}
	
	public void sessionStarted() {
		// do nothing
	}

	public List computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		List<KeywordProposal> result = Collections.emptyList();
		if (context instanceof JavaContentAssistInvocationContext) {
			JavaContentAssistInvocationContext javaContext = (JavaContentAssistInvocationContext) context;
			IJavaScriptUnit unit = javaContext.getCompilationUnit();
			engine.complete(javaContext.getViewer(), javaContext.getInvocationOffset(), unit);
			
			KeywordProposal[] keywordProposals =  engine.getResults();
			result = new ArrayList<KeywordProposal>(Arrays.asList(keywordProposals));
		}		
		return result;
	}

	public List computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Collections.emptyList();
	}

	public String getErrorMessage() {
		return null;
	}

	public void sessionEnded() {
		engine.reset();
	}

}
