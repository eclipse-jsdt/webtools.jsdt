/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.internal.genericeditor.ContentAssistProcessorRegistry;
import org.eclipse.ui.internal.genericeditor.GenericEditorPlugin;
import org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext;

public class GenericCompletionProposalComputer extends JavaCompletionProposalComputer {
	List<IContentAssistProcessor> fContentAssistProcessors = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.
	 * JavaCompletionProposalComputer#computeCompletionProposals(org.eclipse.
	 * wst.jsdt.ui.text.java.ContentAssistInvocationContext,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		ISourceViewer viewer = null;
		if (context.getViewer() instanceof ISourceViewer) {
			viewer = (ISourceViewer) context.getViewer();
		}
		if (fContentAssistProcessors == null && viewer != null) {
			IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
			ContentAssistProcessorRegistry contentAssistProcessorRegistry = GenericEditorPlugin.getDefault().getContentAssistProcessorRegistry();
			Set<IContentType> types = new HashSet<>();
			IContentType contentType = contentTypeManager.getContentType("org.eclipse.wst.jsdt.core.jsSource");
			types.add(contentType);
			contentType = contentTypeManager.getContentType("org.eclipse.core.runtime.text");
			types.add(contentType);
			fContentAssistProcessors = contentAssistProcessorRegistry.getContentAssistProcessors(viewer, null, types);
		}
		List<ICompletionProposal> proposals = new ArrayList<>();
		for (IContentAssistProcessor processor : fContentAssistProcessors) {
			ICompletionProposal[] computed = processor.computeCompletionProposals(context.getViewer(), context.getInvocationOffset());
			proposals.addAll(Arrays.asList(computed));
		}
		return proposals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.
	 * JavaCompletionProposalComputer#sessionEnded()
	 */
	public void sessionEnded() {
		fContentAssistProcessors = null;
		super.sessionEnded();
	}
}
