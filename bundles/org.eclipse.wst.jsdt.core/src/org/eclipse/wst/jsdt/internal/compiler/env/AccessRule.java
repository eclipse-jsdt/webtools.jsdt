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
package org.eclipse.wst.jsdt.internal.compiler.env;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.compiler.IProblem;

public class AccessRule {

	public static final int IgnoreIfBetter = 0x02000000; // value must be greater than IProblem#ForbiddenReference and DiscouragedReference

	public char[] pattern;
	public int problemId;

	public AccessRule(char[] pattern, int problemId) {
		this(pattern, problemId, false);
	}

	public AccessRule(char[] pattern, int problemId, boolean keepLooking) {
		this.pattern = pattern;
		this.problemId = keepLooking ? problemId | IgnoreIfBetter : problemId;
	}

	public int hashCode() {
		return this.problemId * 17 + CharOperation.hashCode(this.pattern);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AccessRule)) return false;
		AccessRule other = (AccessRule) obj;
		if (this.problemId != other.problemId) return false;
		return CharOperation.equals(this.pattern, other.pattern);
	}

	public int getProblemId() {
		return this.problemId & ~IgnoreIfBetter;
	}

	public boolean ignoreIfBetter() {
		return (this.problemId & IgnoreIfBetter) != 0;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("pattern="); //$NON-NLS-1$
		buffer.append(this.pattern);
		switch (getProblemId()) {
			case IProblem.ForbiddenReference:
				buffer.append(" (NON ACCESSIBLE"); //$NON-NLS-1$
				break;
			case IProblem.DiscouragedReference:
				buffer.append(" (DISCOURAGED"); //$NON-NLS-1$
				break;
			default:
				buffer.append(" (ACCESSIBLE"); //$NON-NLS-1$
				break;
		}
		if (ignoreIfBetter())
			buffer.append(" | IGNORE IF BETTER"); //$NON-NLS-1$
		buffer.append(')');
		return buffer.toString();
	}
}
