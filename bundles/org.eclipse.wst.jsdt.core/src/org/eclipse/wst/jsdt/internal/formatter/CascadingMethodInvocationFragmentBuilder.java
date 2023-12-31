/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.formatter;

import java.util.ArrayList;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;

class CascadingMethodInvocationFragmentBuilder
	extends ASTVisitor {

	ArrayList fragmentsList;

	CascadingMethodInvocationFragmentBuilder() {
		this.fragmentsList = new ArrayList();
	}

	public MessageSend[] fragments() {
		MessageSend[] fragments = new MessageSend[this.fragmentsList.size()];
		this.fragmentsList.toArray(fragments);
		return fragments;
	}

	public int size() {
		return this.fragmentsList.size();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ASTVisitor#visit(org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend, org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope)
	 */
	public boolean visit(MessageSend messageSend, BlockScope scope) {
		if ( messageSend.receiver==null || (messageSend.receiver.bits & ASTNode.ParenthesizedMASK) >> ASTNode.ParenthesizedSHIFT == 0) {

			if (messageSend.receiver!=null &&messageSend.receiver instanceof MessageSend) {
				this.fragmentsList.add(0, messageSend);
				messageSend.receiver.traverse(this, scope);
				return false;
			}
			this.fragmentsList.add(0, messageSend);
			this.fragmentsList.add(1, messageSend);
		} else {
			this.fragmentsList.add(0, messageSend);
			this.fragmentsList.add(1, messageSend);
		}
		return false;
	}
}
