/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.search.indexing;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.IProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.ISourceElementRequestor;
import org.eclipse.wst.jsdt.internal.compiler.SourceElementParser;
import org.eclipse.wst.jsdt.internal.compiler.ast.ImportReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

/*
 * A source element parser that avoids creating unnecessary nodes.
 */
public class IndexingParser extends SourceElementParser {
	SingleNameReference singleNameReference = new SingleNameReference(CharOperation.NO_CHAR, 0);
	QualifiedNameReference qualifiedNameReference = new QualifiedNameReference(CharOperation.NO_CHAR_CHAR, new long[0], 0, 0);
	ImportReference importReference = new ImportReference(CharOperation.NO_CHAR_CHAR, new long[1], false);

	public IndexingParser(ISourceElementRequestor requestor, IProblemFactory problemFactory, CompilerOptions options, boolean reportLocalDeclarations, boolean optimizeStringLiterals, boolean useSourceJavadocParser) {
		super(requestor, problemFactory, options, reportLocalDeclarations,
				optimizeStringLiterals, useSourceJavadocParser);
	}

//	protected ImportReference newImportReference(char[][] tokens, long[] sourcePositions, boolean onDemand, int mod) {
//		ImportReference ref = this.importReference;
//		ref.tokens = tokens;
//		ref.sourcePositions = sourcePositions;
//		if (onDemand) {
//			ref.bits |= ASTNode.OnDemand;
//		}
//		ref.sourceEnd = (int) (sourcePositions[sourcePositions.length-1] & 0x00000000FFFFFFFF);
//		ref.sourceStart = (int) (sourcePositions[0] >>> 32);
//		ref.modifiers = modifiers;
//		return ref;
//	}
//
//	protected SingleNameReference newSingleNameReference(char[] source, long positions) {
//		SingleNameReference ref = this.singleNameReference;
//		ref.token = source;
//		ref.sourceStart = (int) (positions >>> 32);
//		ref.sourceEnd = (int) positions;
//		return ref;
//	}
//
//	protected QualifiedNameReference newQualifiedNameReference(char[][] tokens, long[] positions, int sourceStart, int sourceEnd) {
//		QualifiedNameReference ref = this.qualifiedNameReference;
//		ref.tokens = tokens;
//		ref.sourcePositions = positions;
//		ref.sourceStart = sourceStart;
//		ref.sourceEnd = sourceEnd;
//		return ref;
//	}
}
