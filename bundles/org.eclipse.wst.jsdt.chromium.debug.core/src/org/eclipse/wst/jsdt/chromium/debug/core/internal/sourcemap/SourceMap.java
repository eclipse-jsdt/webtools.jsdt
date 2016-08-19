/*
 * Copyright (c) 2013, 2017 the Dart project authors.
 * 
 * Licensed under the Eclipse Public License v1.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors:
 *  Dart project authors
 *  Angelo Zerr <angelo.zerr@gmail.com> - adapt original class https://github.com/sdbg/sdbg/blob/master/com.github.sdbg.debug.core/src/com/github/sdbg/debug/core/internal/sourcemaps/SourceMap.java to use Gson/
 */
package org.eclipse.wst.jsdt.chromium.debug.core.internal.sourcemap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.TextSectionMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

////@ sourceMappingURL=/path/to/file.js.map

/**
* This maps from a generated file back to the original source files. It also supports the reverse
* mapping; from locations in the source files to locations in the generated file.
* 
* @see http://www.html5rocks.com/en/tutorials/developertools/sourcemaps/
*/
public class SourceMap {

	// Which version of the source map spec this map is following.
	private String version;

	// An array of URLs to the original source files.
	private String[] sources;

	// An array of identifiers which can be referenced by individual mappings.
	private String[] names;

	// Optional. The URL root from which all sources are relative.
	private String sourceRoot;

	// Optional. An array of contents of the original source files.
	private String[] sourcesContent;
	// A string of base64 VLQs which contain the actual mappings.

	private String mappings;

	// Optional. The generated filename this source map is associated with.
	private String file;

	private SourceMapInfoEntry[] entries;

	private Map<String, TextSectionMapping> maps = new HashMap<String, TextSectionMapping>();

	public static SourceMap load(Reader reader) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		SourceMap sourceMap = gson.fromJson(reader, SourceMap.class);
		if (sourceMap == null) {
			throw new JsonSyntaxException("JSON Syntax error");
		}
		sourceMap.update();
		return sourceMap;
	}

	private void update() {
		// Prepend sourceRoot to the sources entries.
		if (sourceRoot != null && sourceRoot.length() > 0) {
			for (int i = 0; i < sources.length; i++) {
				sources[i] = sourceRoot + sources[i];
			}
		}

		List<SourceMapInfoEntry> result = SourceMapDecoder.decode(sources, names, mappings);
		entries = result.toArray(new SourceMapInfoEntry[result.size()]);
	}

	public static SourceMap load(InputStream in) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(in);
			return load(reader);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public TextSectionMapping getMapping(String file) {
		TextSectionMapping mapping = maps.get(file);
		if (mapping == null) {
			mapping = new SourceMapTextSectionMapping(file);
			maps.put(file, mapping);
		}
		return mapping;
	}

	private class SourceMapTextSectionMapping implements TextSectionMapping {

		private String file;

		public SourceMapTextSectionMapping(String file) {
			this.file = file;
		}

		@Override
		public TextPoint transform(TextPoint point, Direction direction) {
			if (direction == Direction.DIRECT) {
				return getReverseMappingsFor(file, point.getLine(), point.getColumn());
			}

			SourceMapInfo info = getMappingFor(point.getLine(), point.getColumn());
			if (info != null) {
				return new TextPoint(info.getLine(), info.getColumn());
			}

			return getReverseMappingsFor(file, point.getLine(), point.getColumn());
		}
	}

	/**
	 * Map from a location in the generated file back to the original source.
	 * 
	 * @param line
	 *            the line in the generated source
	 * @param column
	 *            the column in the generated source; -1 means the column is not
	 *            interesting
	 * @return the corresponding location in the original source
	 */
	public SourceMapInfo getMappingFor(int line, int column) {
		int index = findIndexForLine(line);

		if (index == -1) {
			return null;
		}

		SourceMapInfoEntry entry = entries[index];

		// If column == -1, return the first mapping for that line.
		if (column == -1) {
			return entry.getInfo();
		}

		// Search for a matching mapping.
		while (index < entries.length) {
			entry = entries[index];

			if (entry.column <= column) {
				if (entry.endColumn == -1) {
					return entry.getInfo();
				}

				if (column < entry.endColumn) {
					return entry.getInfo();
				}
			}

			index++;
		}

		// no mapping found
		return null;
	}

	private int findIndexForLine(int line) {
		// TODO(devoncarew): test this binary search

		int location = Arrays.binarySearch(entries, SourceMapInfoEntry.forLine(line),
				SourceMapInfoEntry.lineComparator());

		if (location < 0) {
			return -1;
		}

		while (location > 0 && entries[location - 1].line == line) {
			location--;
		}

		return location;
	}

	/**
	 * Map from a location in a source file to a location in the generated
	 * source file.
	 * 
	 * @param file
	 * @param line
	 * @param column
	 * @return
	 */
	public TextSectionMapping.TextPoint getReverseMappingsFor(String file, int line, int column) {
		// TODO(devoncarew): calculate this information once for O(1) lookup
		for (SourceMapInfoEntry entry : entries) {
			SourceMapInfo info = entry.getInfo();

			if (info == null) {
				continue;
			}

			if (line == info.getLine()) {
				if (file.equals(info.getFile())) {
					// TODO(devoncarew): there will be several entries on this
					// line
					// We need to choose one that has a non-zero range, or is a
					// catch-all entry
					return new TextSectionMapping.TextPoint(entry.line, entry.column);
					// return Collections.singletonList(new SourceMapInfo(null,
					// entry.line, entry.column));
				}
			}
		}

		return null;
	}
}
