/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.dom.binding;

import org.eclipse.wst.jsdt.core.dom.ASTNode;

/**
 * Utility class to query various information from ASTNode
 *
 * @since 2.0
 *
 */
public class NodeUtil {

	/**
	 * Return <code>true</code> if given node creates block in ES6
	 *
	 * @param n
	 *            node to check
	 * @return <code>true</code> if given node creates block in ES6
	 */
	public static boolean createsBlockScope(ASTNode n) {
		switch (n.getNodeType()) {
		case ASTNode.BLOCK: {
			// Don't create block scope for one contained in a CATCH.
			if (n.getParent() == null || n.getParent().getParent() == null
					|| n.getParent().getNodeType() == ASTNode.CATCH_CLAUSE) {
				return false;
			}
		}
			return true;
		case ASTNode.FOR_STATEMENT:
		case ASTNode.FOR_OF_STATEMENT:
		case ASTNode.SWITCH_STATEMENT:
		case ASTNode.TYPE_DECLARATION:
		case ASTNode.TYPE_DECLARATION_EXPRESSION:
		case ASTNode.TYPE_DECLARATION_STATEMENT:
			return true;
		}
		return false;
	}

	public static ASTNode grandParent(ASTNode n) {
		if (n != null && n.getParent() != null) {
			return n.getParent().getParent();
		}
		return null;
	}
}
