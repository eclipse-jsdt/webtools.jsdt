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
package org.eclipse.wst.jsdt.core.tests.formatter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DecodeCodeFormatterPreferences extends DefaultHandler {
	
	private boolean record;
	private Map entries;
	private String profileName;

	public static Map decodeCodeFormatterOptions(String fileName) {
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(fileName));
			Map options = new HashMap();
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				StringTokenizer stringTokenizer = new StringTokenizer(line, "=");
				options.put(stringTokenizer.nextElement(), stringTokenizer.nextElement());
			}
			reader.close();
			return options;
		} catch (IOException e) {
			// ignore
		}
		return null;
	}

	public static Map decodeCodeFormatterOptions(String fileName, String profileName) {
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			final DecodeCodeFormatterPreferences preferences = new DecodeCodeFormatterPreferences(profileName);
			saxParser.parse(new File(fileName), preferences);
			return preferences.getEntries();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map decodeCodeFormatterOptions(String zipFileName, String zipEntryName, String profileName) {
		ZipFile zipFile = null;
		BufferedInputStream inputStream = null;
		try {
			zipFile = new ZipFile(zipFileName);
			ZipEntry zipEntry = zipFile.getEntry(zipEntryName);
			if (zipEntry == null) {
				return null;
			}
			inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			final DecodeCodeFormatterPreferences preferences = new DecodeCodeFormatterPreferences(profileName);
			saxParser.parse(inputStream, preferences);
			return preferences.getEntries();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e1) {
				// Do nothing
			}
		}
		return null;
	}
	
	DecodeCodeFormatterPreferences(String profileName) {
		this.profileName = profileName;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		int attributesLength = attributes.getLength();
		if ("profile".equals(qName)) {
			for (int i = 0; i < attributesLength; i++) {
				if ("name".equals(attributes.getQName(i)) && profileName.equals(attributes.getValue(i))) {
					record = true;
					entries = new HashMap();
					break;
				}
			}
		} else if ("setting".equals(qName) && record) {
			if (attributesLength == 2) {
				entries.put(attributes.getValue(0), attributes.getValue(1));
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("profile".equals(qName) && record) {
			record = false;
		}
	}
	/**
	 * @return Returns the entries.
	 */
	public Map getEntries() {
		return entries;
	}
}
