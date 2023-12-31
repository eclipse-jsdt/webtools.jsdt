/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.wst.jsdt.integration.tests.internal.condition;

import org.eclipse.swt.graphics.Point;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

/**
 * 
 * @author Pavol Srna
 *
 */
public class CursorPositionIsOnLine extends AbstractWaitCondition {

	private TextEditor editor;
	private int line;

	/**
	 * Constructs CursorPositionIsOnLine wait condition. Condition is met when the
	 * specified editor has cursor set on a line specified in param.
	 * 
	 * @param editor editor where to look for the cursor position
	 * @param line - line counting starts from 1
	 */
	public CursorPositionIsOnLine(TextEditor editor, int line) {
		this.editor = editor;
		this.line = line;
	}
	


	@Override
	public boolean test() {
		Point p = editor.getCursorPosition();
		if(p.x + 1 == line){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String description() {
		return "editor cursor position is on line '" + line;
	}

}