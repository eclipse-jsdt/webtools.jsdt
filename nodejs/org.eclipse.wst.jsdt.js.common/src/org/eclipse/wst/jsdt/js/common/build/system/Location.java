/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.jsdt.js.common.build.system;

public class Location {
	private int start;
	private int length;

	public Location(int start, int length) {
		this.start = start;
		this.length = length;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}
}
