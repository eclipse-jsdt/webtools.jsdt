/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.tests.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

import org.eclipse.wst.jsdt.core.util.SequenceReader;

public class SequenceReaderTests extends TestCase {
	public void testSingleReaderSequence() throws IOException {
		Reader[] readers = new Reader[]{new StringReader("Ready")};
		Reader sequenceReader = new SequenceReader(readers);
		StringBuffer buff = new StringBuffer();
		try {
			int c = sequenceReader.read();
			while (c != -1) {
				buff.append((char) c);
				c = sequenceReader.read();
			}
		} finally {
			sequenceReader.close();
		}
		assertEquals("Ready", buff.toString());
	}

	public void testTripleReaderSequence() throws IOException {
		Reader[] readers = new Reader[]{new StringReader("Ready"), new StringReader("Set"), new StringReader("Go")};
		Reader sequenceReader = new SequenceReader(readers);
		StringBuffer buff = new StringBuffer();
		try {
			int c = sequenceReader.read();
			while (c != -1) {
				buff.append((char) c);
				c = sequenceReader.read();
			}
		} finally {
			sequenceReader.close();
		}
		assertEquals("ReadySetGo", buff.toString());
	}

	public void testEmptyReader() throws IOException {
		Reader[] readers = new Reader[]{new StringReader("")};
		Reader sequenceReader = new SequenceReader(readers);
		StringBuffer buff = new StringBuffer();
		try {
			int c = sequenceReader.read();
			while (c != -1) {
				buff.append((char) c);
				c = sequenceReader.read();
			}
		} finally {
			sequenceReader.close();
		}
		assertEquals("something was read from an empty reader", 0, buff.length());
	}

	public void testEmptySequence() throws IOException {
		Reader[] readers = new Reader[0];
		Reader sequenceReader = new SequenceReader(readers);
		StringBuffer buff = new StringBuffer();
		try {
			int c = sequenceReader.read();
			while (c != -1) {
				buff.append((char) c);
				c = sequenceReader.read();
			}
		} finally {
			sequenceReader.close();
		}
		assertEquals("something was read from nothing", 0, buff.length());
	}

	public void testBufferedTripleReaderSequence() throws IOException {
		Reader[] readers = new Reader[]{new StringReader("Ready"), new StringReader("Set"), new StringReader("Go")};
		Reader sequenceReader = new BufferedReader(new SequenceReader(readers));
		StringBuffer buff = new StringBuffer();
		try {
			int c = sequenceReader.read();
			while (c != -1) {
				buff.append((char) c);
				c = sequenceReader.read();
			}
		} finally {
			sequenceReader.close();
		}
		assertEquals("ReadySetGo", buff.toString());
	}

}
