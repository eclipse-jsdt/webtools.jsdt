/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.codeassist.select;

import java.util.List;

import org.eclipse.wst.jsdt.core.compiler.InvalidInputException;
import org.eclipse.wst.jsdt.internal.codeassist.SelectionEngine;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.Expression;
import org.eclipse.wst.jsdt.internal.compiler.ast.JavadocQualifiedTypeReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference;
import org.eclipse.wst.jsdt.internal.compiler.parser.JavadocParser;

/**
 * Parser specialized for decoding javadoc comments which includes code selection.
 */
public class SelectionJavadocParser extends JavadocParser {

	int selectionStart;
	int selectionEnd;
	ASTNode selectedNode;

	public SelectionJavadocParser(SelectionParser sourceParser) {
		super(sourceParser);
		this.kind = SELECTION_PARSER | TEXT_PARSE;
	}

	/*
	 * Do not parse comment if selection is not included.
	 */
	public boolean checkDeprecation(int commentPtr) {
		this.selectionStart = ((SelectionParser)sourceParser).selectionStart;
		this.selectionEnd = ((SelectionParser)sourceParser).selectionEnd;
		this.javadocStart = this.sourceParser.scanner.commentStarts[commentPtr];
		this.javadocEnd = this.sourceParser.scanner.commentStops[commentPtr];
		if (SelectionEngine.DEBUG) {
			System.out.println("SELECTION in Javadoc:"); //$NON-NLS-1$
		}
		super.checkDeprecation(commentPtr);
		return false;
	}

	/*
	 * Replace stored Javadoc node with specific selection one.
	 */
	protected boolean commentParse() {
		this.docComment = new SelectionJavadoc(this.javadocStart, this.javadocEnd);
		return super.commentParse();
	}

	/*
	 * Create argument expression and store it if it includes selection.
	 */
	protected Object createArgumentReference(char[] name, int dim, boolean isVarargs, Object typeRef, long[] dimPositions, long argNamePos) throws InvalidInputException {
		// Create argument as we may need it after
		Expression expression = (Expression) super.createArgumentReference(name, dim, isVarargs, typeRef, dimPositions, argNamePos);
		// See if selection is in argument
		int start = ((TypeReference)typeRef).sourceStart;
		int end = ((TypeReference)typeRef).sourceEnd;
		if (start <= this.selectionStart && this.selectionEnd <= end) {
			selectedNode = expression;
			this.abort = true;
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected argument="+selectedNode); //$NON-NLS-1$
			}
		}
		return expression;
	}

	/*
	 * Verify if field identifier positions include selection.
	 * If so, create field reference, store it and abort comment parse.
	 * Otherwise return null as we do not need this reference.
	 */
	protected Object createFieldReference(Object receiver) throws InvalidInputException {
		int start = (int) (this.identifierPositionStack[0] >>> 32);
		int end = (int) this.identifierPositionStack[0];
		if (start <= this.selectionStart && this.selectionEnd <= end) {
			selectedNode = (ASTNode) super.createFieldReference(receiver);
			this.abort = true;
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected field="+selectedNode); //$NON-NLS-1$
			}
		}
		return null;
	}

	/*
	 * Verify if method identifier positions include selection.
	 * If so, create field reference, store it and abort comment parse.
	 * Otherwise return null as we do not need this reference.
	 */
	protected Object createMethodReference(Object receiver, List arguments) throws InvalidInputException {
		int memberPtr = this.identifierLengthStack[0] - 1;	// may be > 0 for inner class constructor reference
		int start = (int) (this.identifierPositionStack[memberPtr] >>> 32);
		int end = (int) this.identifierPositionStack[memberPtr];
		if (start <= this.selectionStart && this.selectionEnd <= end) {
			selectedNode = (ASTNode) super.createMethodReference(receiver, arguments);
			this.abort = true;
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected method="+selectedNode); //$NON-NLS-1$
			}
		}
		return null;
	}

	/*
	 * Create type reference and verify if it includes selection.
	 * If so, store it and abort comment parse.
	 * Otherwise return null as we do not need this reference.
	 */
	protected Object createTypeReference(int primitiveToken) {
		// Need to create type ref in case it was needed by members
		TypeReference typeRef = (TypeReference) super.createTypeReference(primitiveToken);

		// See if node is concerned by selection
		if (typeRef.sourceStart <= this.selectionStart && this.selectionEnd <= typeRef.sourceEnd) {
			// See if selection is in one of tokens of qualification
			if (typeRef instanceof JavadocQualifiedTypeReference) {
				JavadocQualifiedTypeReference qualifiedTypeRef = (JavadocQualifiedTypeReference) typeRef;
				int size = qualifiedTypeRef.tokens.length - 1;
				for (int i=0; i<size; i++) {
					int start = (int) (qualifiedTypeRef.sourcePositions[i] >>> 32);
					int end = (int) qualifiedTypeRef.sourcePositions[i];
					if (start <= this.selectionStart && this.selectionEnd <= end) {
						int pos = i + 1;
						char[][] tokens = new char[pos][];
						int ptr = this.identifierPtr - size;
						System.arraycopy(this.identifierStack, ptr, tokens, 0, pos);
						long[] positions = new long[pos];
						System.arraycopy(this.identifierPositionStack, ptr, positions, 0, pos);
						selectedNode = new JavadocQualifiedTypeReference(tokens, positions, this.tagSourceStart, this.tagSourceEnd);
						this.abort = true; // we got selected node => cancel parse
						if (SelectionEngine.DEBUG) {
							System.out.println("	selected partial qualified type="+selectedNode); //$NON-NLS-1$
						}
						return typeRef;
					}
				}
				// Selection is in last token => we'll store type ref as this
			}
			// Store type ref as selected node
			selectedNode = typeRef;
			this.abort = true; // we got selected node => cancel parse
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected type="+selectedNode); //$NON-NLS-1$
			}
		}
		return typeRef;
	}

	/*
	 * Push param reference and verify if it includes selection.
	 * If so, store it and abort comment parse.
	 */
	protected boolean pushParamName(boolean isTypeParam) {
		if (super.pushParamName(isTypeParam)) {
			Expression expression = (Expression) astStack[astPtr];
			// See if expression is concerned by selection
			if (expression.sourceStart <= this.selectionStart && this.selectionEnd <= expression.sourceEnd) {
				selectedNode = expression;
				this.abort = true; // we got selected node => cancel parse
				if (SelectionEngine.DEBUG) {
					System.out.println("	selected param="+selectedNode); //$NON-NLS-1$
				}
			}
		}
		return false;
	}

	/*
	 * Store selected node into doc comment.
	 */
	protected void updateDocComment() {
		super.updateDocComment();
		if (selectedNode instanceof Expression) {
			((SelectionJavadoc) this.docComment).selectedNode = (Expression) selectedNode;
		}
	}
}
