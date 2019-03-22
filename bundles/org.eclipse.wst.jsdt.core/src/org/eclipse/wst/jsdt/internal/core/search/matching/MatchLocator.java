/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Update to use new parsing engine.
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.core.search.matching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchDocument;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.SearchRequestor;
import org.eclipse.wst.jsdt.internal.core.CompilationUnit;
import org.eclipse.wst.jsdt.internal.core.LibraryFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.Openable;
import org.eclipse.wst.jsdt.internal.core.PackageFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.search.IndexSelector;
import org.eclipse.wst.jsdt.internal.core.search.JavaSearchDocument;
import org.eclipse.wst.jsdt.internal.core.util.HandleFactory;
import org.eclipse.wst.jsdt.internal.core.util.Messages;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class MatchLocator {

	private SearchPattern pattern;
	private SearchRequestor requestor;
	private IJavaScriptSearchScope scope;
	private SubMonitor progressMonitor;
	
	/**
	 * Toggle for displaying timing information for search.
	 */
	private static boolean PERF_STATS = false;
	
	public MatchLocator(
			SearchPattern pattern,
			SearchRequestor requestor,
			IJavaScriptSearchScope scope,
			IProgressMonitor progressMonitor) {

		this.pattern = pattern;
		this.requestor = requestor;
		this.scope = scope;
		this.progressMonitor = SubMonitor.convert(progressMonitor);
	}
	
	/**
	 * Locates IJavaScriptElements matching the pattern provided in
	 * the array of documents, and reports them through 
	 * {@link SearchRequestor#acceptSearchMatch(SearchMatch)}
	 * 
	 * @param documents The array of documents to search through
	 * @throws CoreException
	 */
	public void locateMatches(SearchDocument[] documents) throws CoreException {

		progressMonitor.beginTask(Messages.engine_searching_matching_progress, documents.length);
		long start = 0;
		if (PERF_STATS) start = System.currentTimeMillis();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(false);
		HandleFactory handleFactory = new HandleFactory();
		for (int i = 0; i < documents.length; i++) {
			
			SearchDocument currentDocument = documents[i];
			
			if (progressMonitor.isCanceled()) throw new OperationCanceledException();
			progressMonitor.subTask(Messages.bind(Messages.engine_searching_matching_progress, 
						currentDocument.getPath().toString()));
			
			IPath path = new Path(currentDocument.getPath());
			Openable openable = handleFactory.createOpenable(path.toString(), scope);

			ITypeRoot element = (ITypeRoot) openable;
			parser.setSource(element);

			ASTMatchingVisitor visitor = new ASTMatchingVisitor(
						pattern, currentDocument.getParticipant(), element);
			
			try {
				ASTNode root = parser.createAST(null);
				root.accept(visitor);
			} catch (ClassCastException e) {
				Util.verbose("ClassCastException during matching -- " + e.getMessage() //$NON-NLS-1$
							+ "\n\t in file:  " + currentDocument.getPath()); //$NON-NLS-1$
			} catch (IllegalArgumentException e) {
				Util.verbose("IllegalArgumentException during matching-- " + e.getMessage() //$NON-NLS-1$
							+ "\n\t in file:  " + currentDocument.getPath()); //$NON-NLS-1$
			}
			
			reportMatches(visitor.getMatches());
			visitor.clearMatches();
			
			progressMonitor.worked(1);
		}
		if (PERF_STATS) System.out.println("Search Complete -- time : " + (System.currentTimeMillis() - start)); //$NON-NLS-1$
	}

	private void reportMatches(List<SearchMatch> matches) throws CoreException {
		for (SearchMatch match : matches) {
			requestor.acceptSearchMatch(match);
		}
	}
	
	public static SearchDocument[] addWorkingCopies(InternalSearchPattern pattern, SearchDocument[] indexMatches, org.eclipse.wst.jsdt.core.IJavaScriptUnit[] copies, SearchParticipant participant) {
		// working copies take precedence over corresponding compilation units
		HashMap workingCopyDocuments = workingCopiesThatCanSeeFocus(copies, pattern.focus, pattern.isPolymorphicSearch(), participant);
		SearchDocument[] matches = null;
		int length = indexMatches.length;
		for (int i = 0; i < length; i++) {
			SearchDocument searchDocument = indexMatches[i];
			if (searchDocument.getParticipant() == participant) {
				SearchDocument workingCopyDocument = (SearchDocument) workingCopyDocuments.remove(searchDocument.getPath());
				if (workingCopyDocument != null) {
					if (matches == null) {
						System.arraycopy(indexMatches, 0, matches = new SearchDocument[length], 0, length);
					}
					matches[i] = workingCopyDocument;
				}
			}
		}
		if (matches == null) { // no working copy
			matches = indexMatches;
		}
		int remainingWorkingCopiesSize = workingCopyDocuments.size();
		if (remainingWorkingCopiesSize != 0) {
			System.arraycopy(matches, 0, matches = new SearchDocument[length+remainingWorkingCopiesSize], 0, length);
			Iterator iterator = workingCopyDocuments.values().iterator();
			int index = length;
			while (iterator.hasNext()) {
				matches[index++] = (SearchDocument) iterator.next();
			}
		}
		return matches;
	}
	
	public static IJavaScriptElement projectOrJarFocus(InternalSearchPattern pattern) {
		return pattern == null || pattern.focus == null ? null : getProjectOrJar(pattern.focus);
	}
	
	public static IJavaScriptElement getProjectOrJar(IJavaScriptElement element) {
		while (!(element instanceof IJavaScriptProject) &&
				!( element instanceof LibraryFragmentRoot) &&
				!( element instanceof PackageFragmentRoot)) {
			element = element.getParent();
		}
		return element;
	}
	
	/**
	 * Returns the working copies that can see the given focus.
	 */
	private static HashMap workingCopiesThatCanSeeFocus(org.eclipse.wst.jsdt.core.IJavaScriptUnit[] copies, IJavaScriptElement focus, boolean isPolymorphicSearch, SearchParticipant participant) {
		if (copies == null) return new HashMap();
		if (focus != null) {
			while (!(focus instanceof IJavaScriptProject)) {
				focus = focus.getParent();
			}
		}
		HashMap result = new HashMap();
		for (int i=0, length = copies.length; i<length; i++) {
			org.eclipse.wst.jsdt.core.IJavaScriptUnit workingCopy = copies[i];
			IPath projectOrJar = MatchLocator.getProjectOrJar(workingCopy).getPath();
			if (focus == null || IndexSelector.canSeeFocus(focus, isPolymorphicSearch, projectOrJar)) {
				result.put(
					workingCopy.getPath().toString(),
					new WorkingCopyDocument(workingCopy, participant)
				);
			}
		}
		return result;
	}
	
	public static class WorkingCopyDocument extends JavaSearchDocument {
		public org.eclipse.wst.jsdt.core.IJavaScriptUnit workingCopy;
		WorkingCopyDocument(org.eclipse.wst.jsdt.core.IJavaScriptUnit workingCopy, SearchParticipant participant) {
			super(workingCopy.getPath().toString(), participant);
			this.charContents = ((CompilationUnit)workingCopy).getContents();
			this.workingCopy = workingCopy;
		}
		public String toString() {
			return "WorkingCopyDocument for " + getPath(); //$NON-NLS-1$
		}
	}
}
