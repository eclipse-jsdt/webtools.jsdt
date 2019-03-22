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
package org.eclipse.wst.jsdt.core.dom.flatten;

/**
 * StringBuilder based implementation if JsCodeOutputStream
 *
 * @author Eugene Melekhov
 *
 */
public class JsCodeStringBuilderOutputStream extends JsCodeOutputStream {

	/**
	 * <code>StringBuilder</code> to write output to
	 */
	private StringBuilder sb;

	/**
	 * Create new <code>JsCodeStringBuilderOutputStream</code>
	 *
	 * @param sb
	 */
	public JsCodeStringBuilderOutputStream(StringBuilder sb) {
		this.sb = sb;
	}

	@Override
	protected void put(String s) {
		sb.append(s);
	}

}
