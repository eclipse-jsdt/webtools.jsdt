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

package org.eclipse.wst.jsdt.ui.text.identifier.contentassist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.internal.corext.template.java.CompilationUnitContext;
import org.eclipse.wst.jsdt.internal.corext.template.java.CompilationUnitContextType;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.ASTProvider;
import org.eclipse.wst.jsdt.internal.ui.text.java.IdentifierProposal;
import org.eclipse.wst.jsdt.internal.ui.text.java.ScopedCodeAssistVisitor;

public class IdentifierEngine {

	/** The context type. */
	private CompilationUnitContextType fContextType;

	/** The result proposals. */
	private ArrayList<ICompletionProposal> fProposals = new ArrayList<ICompletionProposal>();
	private ScopedCodeAssistVisitor visitor;

	public IdentifierEngine(CompilationUnitContextType contextType) {
		Assert.isNotNull(contextType);
		fContextType = contextType;
	}

	public void complete(IJavaScriptProject project, ITextViewer viewer, int completionPosition, IJavaScriptUnit compilationUnit) {
	    IDocument document = viewer.getDocument();
		Point selection = viewer.getSelectedRange();
		Position position = new Position(completionPosition, selection.y);

		CompilationUnitContext context = fContextType.createContext(document, position, compilationUnit);
		int start = context.getStart();
		int end = context.getEnd();
		IRegion region = new Region(start, end - start);

		JavaScriptUnit ast = JavaScriptPlugin.getDefault().getASTProvider().getAST(compilationUnit, ASTProvider.WAIT_YES, new NullProgressMonitor());

		visitor = new ScopedCodeAssistVisitor(completionPosition);
		ast.accept(visitor);

		if (context.getKey().contains(".")) {
			int dot = getMostRecentDot(context.getKey());
			region = new Region(start + dot + 1, end - start);
		}

		List<IdentifierProposal> identifierProposals = visitor.getIdentifiers(context.getKey());
		for (IdentifierProposal idenProp : identifierProposals) {
			idenProp.setRegion(region);
		}
		fProposals.addAll(identifierProposals);
	}

	private int getMostRecentDot(String s)  {
		for (int i = s.length() - 1; i > 0; i--) {
			if (s.charAt(i) == '.') {
				return i;
			}
		}
		return -1;
	}

	public void reset() {
		fProposals.clear();
	}

	/**
	 * Returns the array of matching keywords.
	 */
	public ICompletionProposal[] getResults() {
		return fProposals.toArray(new ICompletionProposal[fProposals.size()]);
	}

	public List<String> getMatchingIdentifiers(List<String> identifiers, String string) {
		return identifiers.stream().filter(k -> k.startsWith(string)).collect(Collectors.toList());
	}
}