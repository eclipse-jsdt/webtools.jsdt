/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     bug 242694 -  Michael Spector <spektom@gmail.com>     
 *     Red Hat Inc - Rewrite to use ASTParser
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.search.indexing;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.search.SearchDocument;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.internal.compiler.util.SuffixConstants;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.index.Index;
import org.eclipse.wst.jsdt.internal.core.search.JavaSearchDocument;
import org.eclipse.wst.jsdt.internal.core.util.Util;
import org.eclipse.wst.jsdt.internal.oaametadata.LibraryAPIs;
import org.eclipse.wst.jsdt.internal.oaametadata.MetadataReader;
import org.eclipse.wst.jsdt.internal.oaametadata.MetadataSourceElementNotifier;

/**
 * Indexes JavaScript files using an {@link ASTParser}. Currently indexes
 * (both references and declarations) <br>
 * - Classes (ES6) <br>
 * - Methods/Functions <br>
 * - Variables/Fields <br>
 */
public class SourceIndexer extends AbstractIndexer implements SuffixConstants {

	/**
	 * Toggle for displaying time taken to index files.
	 */
	private static boolean PERF_STATS = false;

	public SourceIndexer(SearchDocument document) {
		super(document);
	}

	/**
	 * Generates the documents AST using an {@link ASTParser} and then
	 * traverses it using an {@link ASTIndexerVisitor}, adding index entries
	 * where appropriate.
	 *
	 * Note that {@link ClassCastException} and {@link IllegalArgumentExceptions}
	 * due to parsing AST are caught and logged, but not handled any further --
	 * meaning the file in which they are thrown is not indexed properly. This
	 * is necessary since indexes are stored per project and so any Exceptions that
	 * propagate up will prevent the entire index from being marked consistent.
	 */
	public void indexDocument() {
		long start = 0;
		if (PERF_STATS) {
			start = System.currentTimeMillis();
		}
		ASTIndexerVisitor visitor = new ASTIndexerVisitor(this);

		// Create ASTParser
		char[] source = document.getCharContents();
		IPath path = new Path(document.getPath());
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(path.segment(0));
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(false);
		parser.setSource(source);
		parser.setUnitName(document.getPath());
		parser.setProject(JavaScriptCore.create(project));
		
		// Exceptions during indexing have to be caught and ignored -- otherwise the index
		// for the current project is never completed and starts from the beginning on every
		// file save. For large projects this can be a significant performance impediment.
		try {
			ASTNode root = parser.createAST(null);
			root.accept(visitor);
		} catch (ClassCastException e) {
			Util.verbose("ClassCastException during indexing -- " + e.getMessage() //$NON-NLS-1$
						+ "\n\t in file:  " + this.document.getPath()); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			Util.verbose("IllegalArgumentException during indexing -- " + e.getMessage() //$NON-NLS-1$
						+ "\n\t in file:  " + this.document.getPath()); //$NON-NLS-1$
		}

		if (PERF_STATS) {
			System.out.println((System.currentTimeMillis() - start) + "  \ttaken to index file: " + this.document.getPath()); //$NON-NLS-1$
		}
	}

	public void indexMetadata() {
		// Create a new Parser
		SourceIndexerRequestor requestor = new SourceIndexerRequestor(this);
		String documentPath = this.document.getPath();

		
		// Launch the parser
		char[] source = null;
		char[] name = null;
		try {
			source = document.getCharContents();
			name = documentPath.toCharArray();
		} catch(Exception e){
			// ignore
		}
		if (source == null || name == null) return; // could not retrieve document info (e.g. resource was discarded)
		String pkgName=((JavaSearchDocument)document).getPackageName();
		char [][]packageName=null;
		if (pkgName!=null)
		{
			packageName=new char[1][];
			packageName[0]=pkgName.toCharArray();
		}
		
		LibraryAPIs apis = MetadataReader.readAPIsFromString(new String(source),documentPath);
		new MetadataSourceElementNotifier(apis,requestor).notifyRequestor();
		
	}
	public void indexArchive() {
		/*
		 * index the individual documents in the archive into the single index
		 * file for the archive's path
		 */
		IPath jarPath = new Path(this.document.getPath());

		File file = new File(jarPath.toOSString());

		if (file.isFile()) {
			IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
			Index index = indexManager.getIndexForUpdate(jarPath, false /*don't reuse index file*/, true /*create if none*/);
			SearchParticipant participant = SearchEngine.getDefaultSearchParticipant();
			ZipFile zip = null;
			try {
				zip = new ZipFile(file);
				for (Enumeration e = zip.entries(); e.hasMoreElements();) {
					// iterate each entry to index it
					ZipEntry ze = (ZipEntry) e.nextElement();
					if (org.eclipse.wst.jsdt.internal.compiler.util.Util.isClassFileName(ze.getName())) {
						final byte[] classFileBytes = org.eclipse.wst.jsdt.internal.compiler.util.Util.getZipEntryByteContent(ze, zip);
						JavaSearchDocument entryDocument = new JavaSearchDocument(ze, jarPath, ByteBuffer.wrap(classFileBytes).asCharBuffer().array(), participant);
						indexManager.indexDocument(entryDocument, participant, index, jarPath);
					}
				}
				indexManager.saveIndex(index);
			}
			catch (ZipException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally {
				if (zip != null) {
					try {
						zip.close();
					}
					catch (IOException e) {
					}
					if(index != null) {
					}
				}
			}
		}
	}
}
