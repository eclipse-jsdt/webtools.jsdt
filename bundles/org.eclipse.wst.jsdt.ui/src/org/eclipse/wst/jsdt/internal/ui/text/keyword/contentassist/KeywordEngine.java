/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ********************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.keyword.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.internal.corext.template.java.CompilationUnitContext;
import org.eclipse.wst.jsdt.internal.corext.template.java.CompilationUnitContextType;

public class KeywordEngine {

	private CompilationUnitContextType fContextType;
	private ArrayList<KeywordProposal> fProposals = new ArrayList<KeywordProposal>();

	public KeywordEngine(CompilationUnitContextType contextType) {
		Assert.isNotNull(contextType);
		fContextType = contextType;
	}

	public void complete(ITextViewer viewer, int completionPosition, IJavaScriptUnit compilationUnit) {
		reset();

		IDocument document = viewer.getDocument();
		Point selection = viewer.getSelectedRange();
		Position position = new Position(completionPosition, selection.y);

		CompilationUnitContext context = fContextType.createContext(document, position, compilationUnit);
		int start = context.getStart();
		int end = context.getEnd();
		IRegion region = new Region(start, end - start);

		KeywordUtilities keywordUtil = KeywordUtilities.getInstance();
		List<String> matchingKeywords = keywordUtil.getMatchingKeywords(context.getKey());

		for (String keywordName : matchingKeywords) {
			KeywordProposal keywordProposal = new KeywordProposal(keywordName, region);
			fProposals.add(keywordProposal);
		}
	}

	public void reset() {
		fProposals.clear();
	}

	/**
	 * Returns matching keywords.
	 */
	public KeywordProposal[] getResults() {
		return fProposals.toArray(new KeywordProposal[fProposals.size()]);
	}

}
