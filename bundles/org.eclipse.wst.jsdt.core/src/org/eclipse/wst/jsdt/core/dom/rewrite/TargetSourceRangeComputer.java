/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom.rewrite;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;

/**
 * An object for computing adjusted source ranges for AST nodes
 * that are being replaced or deleted.
 * <p>
 * For example, a refactoring like inline method may choose to replace
 * calls to the method but leave intact any comments immediately preceding
 * the calls. On the other hand, a refactoring like extract method may choose
 * to extract not only the nodes for the selected code but also any
 * comments preceding or following them.
 * </p>
 * <p>
 * Clients should subclass if they need to influence the
 * the source range to be affected when replacing or deleting a particular node.
 * An instance of the subclass should be registered with
 * {@link ASTRewrite#setTargetSourceRangeComputer(TargetSourceRangeComputer)}.
 * During a call to {@link ASTRewrite#rewriteAST(org.eclipse.jface.text.IDocument, java.util.Map)},
 * the {@link #computeSourceRange(ASTNode)} method on this object will be
 * used to compute the source range for a node being deleted or replaced.
 * </p>
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class TargetSourceRangeComputer {

	/**
	 * Reified source range. Instances are &quot;value&quot; object
	 * (cannot be modified).
	 *
	 */
	public static final class SourceRange {
		/**
		 * 0-based character index, or <code>-1</code>
		 * if no source position information is known.
		 */
		private int startPosition;

		/**
		 * (possibly 0) length, or <code>0</code>
		 * if no source position information is known.
		 */
		private int length;

		/**
		 * Creates a new source range.
		 *
		 * @param startPosition the 0-based character index, or <code>-1</code>
		 *    if no source position information is known
		 * @param length the (possibly 0) length, or <code>0</code>
		 *    if no source position information is known
		 */
		public SourceRange(int startPosition, int length) {
			this.startPosition = startPosition;
			this.length = length;
		}

		/**
		 * Returns the start position.
		 *
		 * @return the 0-based character index, or <code>-1</code>
		 *    if no source position information is known
		 */
		public int getStartPosition() {
			return this.startPosition;
		}

		/**
		 * Returns the source length.
		 *
		 * @return a (possibly 0) length, or <code>0</code>
		 *    if no source position information is known
		 */
		public int getLength() {
			return this.length;
		}
	}

	/**
	 * Creates a new target source range computer.
	 */
	public TargetSourceRangeComputer() {
		// do nothing
	}

	/**
	 * Returns the target source range of the given node. Unlike
	 * {@link ASTNode#getStartPosition()} and {@link ASTNode#getLength()},
	 * the extended source range may include comments and whitespace
	 * immediately before or after the normal source range for the node.
	 * <p>
	 * The returned source ranges must satisfy the following conditions:
	 * <dl>
	 * <li>no two source ranges in an AST may be overlapping</li>
	 * <li>a source range of a parent node must fully cover the source ranges of its children</li>
	 * 	</dl>
	 * 	</p>
	 * <p>
	 * The default implementation uses
	 * {@link JavaScriptUnit#getExtendedStartPosition(ASTNode)}
	 * and {@link JavaScriptUnit#getExtendedLength(ASTNode)}
	 * to compute the target source range. Clients may override or
	 * extend this method to expand or contract the source range of the
	 * given node. The resulting source range must cover at least the
	 * original source range of the node.
	 * </p>
	 *
	 * @param node the node with a known source range in the javaScript unit
	 * being rewritten
	 * @return the exact source range in the javaScript unit being rewritten
	 * that should be replaced (or deleted)
	 */
	public SourceRange computeSourceRange(ASTNode node) {
		ASTNode root= node.getRoot();
		if (root instanceof JavaScriptUnit) {
			JavaScriptUnit cu= (JavaScriptUnit) root;
			return new SourceRange(cu.getExtendedStartPosition(node), cu.getExtendedLength(node));
		}
		return new SourceRange(node.getStartPosition(), node.getLength());
	}
}
