/*
 * Copyright (c) 2013, 2017 the Dart project authors.
 * 
 * Licensed under the Eclipse Public License v2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.eclipse.wst.jsdt.chromium.debug.core.internal.sourcemap;

/**
 * A reference to a file, line, column, and (optionally) name.
 */
public class SourceMapInfo {
	private String file;
	private int line;
	private int column;
	private String name;

	public SourceMapInfo() {

	}

	public SourceMapInfo(String file, int line, int column) {
		this.file = file;
		this.line = line;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public String getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}

	/**
	 * @return the (optional) name reference
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return file + "," + line + "," + column;
	}

	void setName(String name) {
		this.name = name;
	}

}