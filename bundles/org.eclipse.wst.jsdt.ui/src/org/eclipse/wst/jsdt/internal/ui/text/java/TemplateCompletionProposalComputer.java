/*******************************************************************************
 * Copyright (c) 2005, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - refactoring
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.text.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.internal.corext.template.java.JavaContextType;
import org.eclipse.wst.jsdt.internal.corext.template.java.JavaDocContextType;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.text.keyword.contentassist.KeywordUtilities;
import org.eclipse.wst.jsdt.internal.ui.text.template.contentassist.TemplateEngine;
import org.eclipse.wst.jsdt.internal.ui.text.template.contentassist.TemplateProposal;
import org.eclipse.wst.jsdt.ui.text.IJavaScriptPartitions;
import org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.wst.jsdt.ui.text.java.JavaContentAssistInvocationContext;

public final class TemplateCompletionProposalComputer implements IJavaCompletionProposalComputer {

	private final TemplateEngine fJavaTemplateEngine;
	private final TemplateEngine fJavadocTemplateEngine;

	public TemplateCompletionProposalComputer() {
		TemplateContextType contextType = JavaScriptPlugin.getDefault().getTemplateContextRegistry().getContextType(JavaContextType.NAME);

		if (contextType == null) {
			contextType = new JavaContextType();
			JavaScriptPlugin.getDefault().getTemplateContextRegistry().addContextType(contextType);
		}

		fJavaTemplateEngine = new TemplateEngine(contextType);
		contextType = JavaScriptPlugin.getDefault().getTemplateContextRegistry().getContextType("javadoc"); //$NON-NLS-1$

		if (contextType == null) {
			contextType = new JavaDocContextType();
			JavaScriptPlugin.getDefault().getTemplateContextRegistry().addContextType(contextType);
		}

		fJavadocTemplateEngine = new TemplateEngine(contextType);
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalComputer#computeCompletionProposals(org.eclipse.jface.text.contentassist.TextContentAssistInvocationContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		TemplateEngine engine;
		try {
			String partition = TextUtilities.getContentType(context.getDocument(), IJavaScriptPartitions.JAVA_PARTITIONING, context.getInvocationOffset(), true);
			if (partition.equals(IJavaScriptPartitions.JAVA_DOC)) {
				engine = fJavadocTemplateEngine;
			} else {
				engine = fJavaTemplateEngine;
			}
		} catch (BadLocationException x) {
			return Collections.emptyList();
		}

		if (engine != null) {
			if (!(context instanceof JavaContentAssistInvocationContext)) {
				return Collections.emptyList();
			}

			JavaContentAssistInvocationContext javaContext = (JavaContentAssistInvocationContext) context;
			IJavaScriptUnit unit = javaContext.getCompilationUnit();
			if (unit == null) {
				return Collections.emptyList();
			}

			engine.reset();
			engine.complete(javaContext.getViewer(), javaContext.getInvocationOffset(), unit);

			TemplateProposal[] templateProposals = engine.getResults();
			List<ICompletionProposal> result = new ArrayList<ICompletionProposal>(Arrays.asList(templateProposals));

			IJavaCompletionProposal[] keyWordResults = javaContext.getKeywordProposals();
			if (keyWordResults.length > 0) {
				List<TemplateProposal> removals = new ArrayList<TemplateProposal>();

				// update relevance of template proposals that match with a keyword
				// give those templates slightly more relevance than the keyword to
				// sort them first
				// remove keyword templates that don't have an equivalent
				// keyword proposal
				if (keyWordResults.length > 0) {
					outer: for (int k = 0; k < templateProposals.length; k++) {
						TemplateProposal curr = templateProposals[k];
						String name = curr.getTemplate().getName();

						for (int i = 0; i < keyWordResults.length; i++) {
							String keyword = keyWordResults[i].getDisplayString();
							if (name.startsWith(keyword)) {
								curr.setRelevance(keyWordResults[i].getRelevance() + 1);
								continue outer;
							}
						}

						if (isKeyword(name)) {
							removals.add(curr);
						}
					}
				}

				result.removeAll(removals);
			}
			return result;
		}

		return Collections.emptyList();
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalComputer#computeContextInformation(org.eclipse.jface.text.contentassist.TextContentAssistInvocationContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return Collections.emptyList();
	}

	private boolean isKeyword(String name) {
		return KeywordUtilities.getInstance().isKeyword(name);
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalComputer#getErrorMessage()
	 */
	public String getErrorMessage() {
		return null;
	}

	/*
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer#sessionStarted()
	 */
	public void sessionStarted() {
	}

	/*
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer#sessionEnded()
	 */
	public void sessionEnded() {
		fJavadocTemplateEngine.reset();
		fJavaTemplateEngine.reset();
	}

}
