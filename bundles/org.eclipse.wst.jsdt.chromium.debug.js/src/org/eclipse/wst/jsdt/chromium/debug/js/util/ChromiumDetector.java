/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.js.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class ChromiumDetector {
	private static final String EMPTY = ""; //$NON-NLS-1$
	private static final String WHERE_COMMAND = "where"; //$NON-NLS-1$
	private static final String WHICH_LOCATION_1 = "/usr/bin/which"; //$NON-NLS-1$
	public static final String WHICH_LOCATION_2 = "/bin/which"; //$NON-NLS-1$
	public static final String WHICH_LOCATION_3 = "/usr/local/bin/which"; //$NON-NLS-1$
	public static final String SYSTEM_PATH_ENV_VAR = "PATH"; //$NON-NLS-1$
	public static final String CHROMIUM_EXTRA_LOCATION = "/usr/local/bin"; //$NON-NLS-1$
	private static final String CHROMIUM_BROWSER = "chromium-browser"; //$NON-NLS-1$
	private static final String CHROME_WINDOWS = "chrome.exe"; //$NON-NLS-1$

	private ChromiumDetector() {
	}

	/**
	 * Tries to find Chrome / Chromium on all possible system path
	 * installations.
	 *
	 * @return File pointing to the chrome / chromium binary in system path if
	 *         any was found, or null otherwise
	 */
	public static File findChromiumSystemPath() {
		// TODO in future browsers from General -> Web Browser preferences should be used 
		// TODO need to test for all OS - tested only on Ubuntu 16.04 / only chromium-browser is processed correctly
		File chromiumFile = null;
		boolean isWindows = true;
		List<String> cmdLine = new ArrayList<String>();

		// Is windows?
		if (Platform.getOS().startsWith("win")) { //$NON-NLS-1$
			// Handle command in a different way
			cmdLine.add("cmd"); //$NON-NLS-1$
			cmdLine.add("/c"); //$NON-NLS-1$
			cmdLine.add(WHERE_COMMAND);
		} else {
			// Assume all other OS behave as *nix systems.
			isWindows = false;
			cmdLine.add(WHICH_LOCATION_1);
		}

		cmdLine.add(CHROMIUM_BROWSER);

		String output = executeCmd(cmdLine);

		if (!isWindows) {
			if (output.equals(EMPTY)) {
				cmdLine.remove(0);
				cmdLine.add(0, WHICH_LOCATION_2);
				output = executeCmd(cmdLine);
				if (output.equals(EMPTY)) {
					cmdLine.remove(0);
					cmdLine.add(0, WHICH_LOCATION_3);
					output = executeCmd(cmdLine);
				}
			}
		}

		if (output.equals(EMPTY)) {
			String chromeFileName = CHROMIUM_BROWSER;
			if (isWindows) {
				chromeFileName = CHROME_WINDOWS;
			}

			String path = System.getenv(SYSTEM_PATH_ENV_VAR);
			String[] paths = path.split(File.pathSeparator, 0);
			List<String> directories = new ArrayList<String>();
			for (String p : paths) {
				directories.add(p);
			}

			if (!isWindows) {
				directories.add(CHROMIUM_EXTRA_LOCATION);
			}
			boolean notFound = true;
			Iterator<String> it = directories.iterator();
			while (notFound && it.hasNext()) {
				String directory = it.next();
				File tempFile = new File(directory, chromeFileName);
				if (tempFile.exists()) {
					notFound = false;
					chromiumFile = tempFile;
				}
			}
			return chromiumFile;
		}
		chromiumFile = new File(output);
		return chromiumFile;
	}

	/**
	 * Executes a command on the CLI and return the execution output.
	 * 
	 * Will return an empty String if command failed to execute.
	 */
	private static String executeCmd(List<String> commands) {
		String output = EMPTY;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(commands);
			Process process = processBuilder.start();
			InputStream inputStream = process.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
			int exit = process.waitFor();
			if (exit == 0) {
				output = buffer.readLine();
			}
		} catch (Exception e) {
			// Do nothing, this only means this command was unable to execute
			// which in our context is harmless
		}
		return output;
	}

}
