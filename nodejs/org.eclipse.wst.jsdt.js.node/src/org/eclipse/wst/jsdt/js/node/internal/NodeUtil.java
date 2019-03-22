/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 
package org.eclipse.wst.jsdt.js.node.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;

public class NodeUtil {
	/**
	 * Tries to find Node.js on all possible system path
	 * installations.
	 *
	 * @return File pointing to the node.js binary in
	 * system path if any was found, or null otherwise
	 */
	public static File findNodeSystemPath() {
		File nodeFile = null;
		boolean isWindows = true;
		List<String> cmdLine = new ArrayList<String>();
		
		// Is windows?
		if (Platform.getOS().startsWith("win")) { //$NON-NLS-1$
			// Handle command in a different way
			cmdLine.add("cmd"); //$NON-NLS-1$
			cmdLine.add("/c"); //$NON-NLS-1$
			cmdLine.add(NodeConstants.FIND_NODE_SYSTEM_PATH_CONSTANTS_WHERE_COMMAND);
		} else {
			// Assume all other OS behave as *nix systems.
			isWindows = false;
			cmdLine.add(NodeConstants.FIND_NODE_SYSTEM_PATH_CONSTANTS_WHICH_LOCATION_1);
		}
		
		cmdLine.add(NodeConstants.NODE);
		
		String output = executeCmd(cmdLine);
		
		if(!isWindows) {
			if (output.equals(NodeConstants.EMPTY)) {
				cmdLine.remove(0);
				cmdLine.add(0, NodeConstants.FIND_NODE_SYSTEM_PATH_CONSTANTS_WHICH_LOCATION_2);
				output = executeCmd(cmdLine);
				if(output.equals(NodeConstants.EMPTY)) {
					cmdLine.remove(0);
					cmdLine.add(0, NodeConstants.FIND_NODE_SYSTEM_PATH_CONSTANTS_WHICH_LOCATION_3);
					output = executeCmd(cmdLine);
				}
			}
		} 
		
		if(output.equals(NodeConstants.EMPTY)) {
			String nodeFileName = NodeConstants.NODE;
			if (isWindows) {
				nodeFileName = NodeConstants.NODE_WINDOWS;
			}

			String path = System.getenv(NodeConstants.FIND_NODE_SYSTEM_PATH_ENV_VAR);
			String[] paths = path.split(File.pathSeparator, 0);
			List<String> directories = new ArrayList<String>();
			for (String p : paths) {
				directories.add(p);
			}

			if (!isWindows) {
				directories.add(NodeConstants.FIND_NODE_SYSTEM_PATH_CONSTANTS_NODE_EXTRA_LOCATION);
			}
			boolean notFound = true;
			Iterator<String> it = directories.iterator();
			while (notFound && it.hasNext()) {
				String directory = it.next();
				File tempFile = new File(directory, nodeFileName);
				if (tempFile.exists()) {
					notFound = false;
					nodeFile = tempFile;
				}
			}
			return nodeFile;
		}
		nodeFile = new File(output);
		return nodeFile;
	}
	
	/**
	 * Executes a command on the CLI and return the execution output.
	 * 
	 * Will return the NodeConstants.EMPTY String if command failed to execute.
	 */
	private static String executeCmd(List<String> commands) {
		String output = NodeConstants.EMPTY;
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
