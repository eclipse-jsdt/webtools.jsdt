/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.compiler.libraries.SystemLibraryLocation;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.Requestor;
import org.eclipse.wst.jsdt.internal.compiler.Compiler;
import org.eclipse.wst.jsdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.wst.jsdt.internal.compiler.IProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.batch.FileSystem;
import org.eclipse.wst.jsdt.internal.compiler.env.INameEnvironment;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblemFactory;

public class Util {
	private static String TARGET_PLUGIN="org.eclipse.wst.jsdt.core"; //$NON-NLS-1$
	// Trace for delete operation
	/*
	 * Maximum time wasted repeating delete operations while running JDT/Core tests.
	 */
	private static int DELETE_MAX_TIME = 0;
	/**
	 * Trace deletion operations while running JDT/Core tests.
	 */
	public static boolean DELETE_DEBUG = false;
	/**
	 * Maximum of time in ms to wait in deletion operation while running JDT/Core tests.
	 * Default is 10 seconds. This number cannot exceed 1 minute (ie. 60000).
	 * <br>
	 * To avoid too many loops while waiting, the ten first ones are done waiting
	 * 10ms before repeating, the ten loops after are done waiting 100ms and
	 * the other loops are done waiting 1s...
	 */
	public static int DELETE_MAX_WAIT = 10000;

	private static final boolean DEBUG = false;
	/**
	 * Initially, output directory was located in System.getProperty("user.dir")+"\comptest".
	 * To allow user to run several compiler tests at the same time, main output directory
	 * is now located in a sub-directory of "comptest" which name is "run."+<code>System.currentMilliseconds</code>.
	 * 
	 * @see #DELAY_BEFORE_CLEAN_PREVIOUS
	 */
	private final static String OUTPUT_DIRECTORY;
	/**
	 * Let user specify the delay in hours before output directories are removed from file system
	 * while starting a new test run. Default value is 2 hours.
	 * <p>
	 * Note that this value may be a float and so have time less than one hour.
	 * If value is 0 or negative, then all previous run directories will be removed...
	 * 
	 * @see #OUTPUT_DIRECTORY
	 */
	private final static String DELAY_BEFORE_CLEAN_PREVIOUS = System.getProperty("delay");
	/*
	 * Static initializer to clean directories created while running previous test suites.
	 */
	static {
		// Get delay for cleaning sub-directories
		long millisecondsPerHour = 1000L * 3600L;
		long delay = millisecondsPerHour * 2; // default is to keep previous run directories for 2 hours
		try {
			if (DELAY_BEFORE_CLEAN_PREVIOUS != null) {
				float hours = Float.parseFloat(DELAY_BEFORE_CLEAN_PREVIOUS);
				delay = (int) (millisecondsPerHour * hours);
			}
		}
		catch (NumberFormatException nfe) {
			// use default
		}

		// Get output directory root from system properties
		String container = System.getProperty("jdt.test.output_directory");
		if (container == null){
			container = System.getProperty("user.dir");
		}
		if (container == null) {
			container = ".";	// use current directory
		}
		
		// Get file for root directory
		if (Character.isLowerCase(container.charAt(0)) && container.charAt(1) == ':') {
			container = Character.toUpperCase(container.charAt(0)) + container.substring(1);
		}
		
		// Moving the comptest into ./target/work folder due not confuse Git
		// ./target/work folder is created and removed by maven/sure
		File dir = new File(new File(new File(new File(container), "target"), "work"), "comptest");

		// If root directory already exists, clean it
		if (dir.exists()) {
			long now = System.currentTimeMillis();
			if ((now - dir.lastModified()) > delay) {
				// remove all directory content
				flushDirectoryContent(dir);
			} else {
				// remove only old sub-dirs
				File[] testDirs = dir.listFiles();
				for (int i=0,l=testDirs.length; i<l; i++) {
					if (testDirs[i].isDirectory()) {
						if ((now - testDirs[i].lastModified()) > delay) {
							delete(testDirs[i]);
						}
					}
				}
			}
		}

		// Computed test run directory name based on current time
		File dateDir = new File(dir, "run."+System.currentTimeMillis());
		OUTPUT_DIRECTORY = dateDir.getPath();
	}

public static void appendProblem(StringBuffer problems, IProblem problem, char[] source, int problemCount) {
	problems.append(problemCount + (problem.isError() ? ". ERROR" : ". WARNING"));
	problems.append(" in " + new String(problem.getOriginatingFileName()));
	if (source != null) {
		problems.append(((DefaultProblem)problem).errorReportSource(source));
	}
	problems.append("\n");
	problems.append(problem.getMessage());
	problems.append("\n");
}

public static CompilationUnit[] compilationUnits(String[] testFiles) {
	int length = testFiles.length / 2;
	CompilationUnit[] result = new CompilationUnit[length];
	int index = 0;
	for (int i = 0; i < length; i++) {
		String fileName = testFiles[index];
		result[i] = new CompilationUnit(testFiles[index + 1].toCharArray(), fileName, null);
		char [] fileNameChars=fileName.toCharArray();
		int lastIndexOf = CharOperation.lastIndexOf('/', fileNameChars);
		if (lastIndexOf>=0)
		{
			char[] subarray = CharOperation.subarray(fileNameChars, 0, lastIndexOf);
			result[i].packageName=CharOperation.splitOn('/', subarray);
		}
		index += 2;
	}
	return result;
}
public static void compile(String[] pathsAndContents, Map options, String outputPath) {
		IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());
		Requestor requestor = 
			new Requestor(
				problemFactory, 
				outputPath.endsWith(File.separator) ? outputPath : outputPath + File.separator, 
				false,
				null/*no custom requestor*/,
				false, /* show category */
				false /* show warning token*/);
		
		INameEnvironment nameEnvironment = new FileSystem(getJavaClassLibs(), new String[] {}, null);
		IErrorHandlingPolicy errorHandlingPolicy = 
			new IErrorHandlingPolicy() {
				public boolean proceedOnErrors() {
					return true;
				}
				public boolean stopOnFirstError() {
					return false;
				}
			};
		CompilerOptions compilerOptions = new CompilerOptions(options);
		compilerOptions.performMethodsFullRecovery = false;
		compilerOptions.performStatementsRecovery = false;
		Compiler batchCompiler = 
			new Compiler(
				nameEnvironment, 
				errorHandlingPolicy, 
				compilerOptions,
				requestor, 
				problemFactory);
		batchCompiler.options.produceReferenceInfo = true;
		batchCompiler.compile(compilationUnits(pathsAndContents)); // compile all files together
		System.err.print(requestor.problemLog); // problem log empty if no problems
}
public static String[] concatWithClassLibs(String[] classpaths, boolean inFront) {
	String[] classLibs = getJavaClassLibs();
	if (classpaths == null) return classLibs;
	final int classLibsLength = classLibs.length;
	final int classpathsLength = classpaths.length;
	String[] defaultClassPaths = new String[classLibsLength + classpathsLength];
	if (inFront) {
		System.arraycopy(classLibs, 0, defaultClassPaths, classpathsLength, classLibsLength);
		System.arraycopy(classpaths, 0, defaultClassPaths, 0, classpathsLength);
	} else {
		System.arraycopy(classLibs, 0, defaultClassPaths, 0, classLibsLength);
		System.arraycopy(classpaths, 0, defaultClassPaths, classLibsLength, classpathsLength);
	}
	for (int i = 0; i < classpathsLength; i++) {
		File file = new File(classpaths[i]);
		if (!file.exists()) {
			file.mkdirs();
		} 
	}
	return defaultClassPaths;
}
public static String[] concatWithClassLibs(String classpath, boolean inFront) {
	String[] classLibs = getJavaClassLibs();
	final int length = classLibs.length;
	File dir = new File(classpath);
	if (!dir.exists())
		dir.mkdirs();
	String[] defaultClassPaths = new String[length + 1];
	if (inFront) {
		System.arraycopy(classLibs, 0, defaultClassPaths, 1, length);
		defaultClassPaths[0] = classpath;
	} else {
		System.arraycopy(classLibs, 0, defaultClassPaths, 0, length);
		defaultClassPaths[length] = classpath;
	} 
	return defaultClassPaths;
}
public static String convertToIndependantLineDelimiter(String source) {
	if (source.indexOf('\n') == -1 && source.indexOf('\r') == -1) return source;
	StringBuffer buffer = new StringBuffer();
	for (int i = 0, length = source.length(); i < length; i++) {
		char car = source.charAt(i);
		if (car == '\r') {
			buffer.append('\n');
			if (i < length-1 && source.charAt(i+1) == '\n') {
				i++; // skip \n after \r
			}
		} else {
			buffer.append(car);
		}
	}
	return buffer.toString();
}
/**
 * Copy the given source (a file or a directory that must exists) to the given destination (a directory that must exists).
 */
public static void copy(String sourcePath, String destPath) {
	sourcePath = toNativePath(sourcePath);
	destPath = toNativePath(destPath);
	File source = new File(sourcePath);
	if (!source.exists()) return;
	File dest = new File(destPath);
	if (!dest.exists()) return;
	if (source.isDirectory()) {
		String[] files = source.list();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String file = files[i];
				File sourceFile = new File(source, file);
				if (sourceFile.isDirectory()) {
					File destSubDir = new File(dest, file);
					destSubDir.mkdir();
					copy(sourceFile.getPath(), destSubDir.getPath());
				} else {
					copy(sourceFile.getPath(), dest.getPath());
				}
			}
		}
	} else {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(source);
			File destFile = new File(dest, source.getName());
			if (destFile.exists()) {
				if (!Util.delete(destFile)) {
					throw new IOException(destFile + " is in use");
				}
			}
		 	out = new FileOutputStream(destFile);
			int bufferLength = 1024;
			byte[] buffer = new byte[bufferLength];
			int read = 0;
			while (read != -1) {
				read = in.read(buffer, 0, bufferLength);
				if (read != -1) {
					out.write(buffer, 0, read);
				}
			}
		} catch (IOException e) {
			throw new Error(e.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
public static void createFile(String path, String contents) throws IOException {
	FileOutputStream output = new FileOutputStream(path);
	try {
		output.write(contents.getBytes());
	} finally {
		output.close();
	}
}
public static void createJar(String[] pathsAndContents, Map options, String jarPath) throws IOException {
	String classesPath = getOutputDirectory() + File.separator + "classes";
	File classesDir = new File(classesPath);
	flushDirectoryContent(classesDir);
	compile(pathsAndContents, options, classesPath);
	zip(classesDir, jarPath);
}
public static void createJar(String[] pathsAndContents, String jarPath, String compliance) throws IOException {
	Map options = new HashMap();
	options.put(CompilerOptions.OPTION_Compliance, compliance);
	options.put(CompilerOptions.OPTION_Source, compliance);
	options.put(CompilerOptions.OPTION_TargetPlatform, compliance);
	// Ignore options with new defaults (since bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=76530)
	options.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.IGNORE);
	options.put(CompilerOptions.OPTION_ReportUnusedPrivateMember, CompilerOptions.IGNORE);
	options.put(CompilerOptions.OPTION_ReportFieldHiding, CompilerOptions.IGNORE);
	options.put(CompilerOptions.OPTION_ReportLocalVariableHiding, CompilerOptions.IGNORE);
	options.put(CompilerOptions.OPTION_ReportTypeParameterHiding, CompilerOptions.IGNORE);
	options.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
	options.put(CompilerOptions.OPTION_ReportRawTypeReference, CompilerOptions.IGNORE);
	createJar(pathsAndContents, options, jarPath);
}
public static void createSourceZip(String[] pathsAndContents, String zipPath) throws IOException {
	String sourcesPath = getOutputDirectory() + File.separator + "sources";
	File sourcesDir = new File(sourcesPath);
	flushDirectoryContent(sourcesDir);
	for (int i = 0, length = pathsAndContents.length; i < length; i+=2) {
		String sourcePath = sourcesPath + File.separator + pathsAndContents[i];
		File sourceFile = new File(sourcePath);
		sourceFile.getParentFile().mkdirs();
		createFile(sourcePath, pathsAndContents[i+1]);
	}
	zip(sourcesDir, zipPath);
}
/**
 * Delete a file or directory and insure that the file is no longer present
 * on file system. In case of directory, delete all the hierarchy underneath.
 * 
 * @param file The file or directory to delete
 * @return true iff the file was really delete, false otherwise
 */
public static boolean delete(File file) {
	// flush all directory content
	if (file.isDirectory()) {
		flushDirectoryContent(file);
	}
	// remove file
	file.delete();
	if (isFileDeleted(file)) {
		return true;
	}
	return waitUntilFileDeleted(file);
}
/**
 * Delete a file or directory and insure that the file is no longer present
 * on file system. In case of directory, delete all the hierarchy underneath.
 * 
 * @param resource The resource to delete
 * @return true iff the file was really delete, false otherwise
 */
public static boolean delete(IResource resource) {
	try {
		resource.delete(true, null);
		if (isResourceDeleted(resource)) {
			return true;
		}
	}
	catch (CoreException e) {
		//	skip
	}
	return waitUntilResourceDeleted(resource);
}
/**
 * Delete a file or directory and insure that the file is no longer present
 * on file system. In case of directory, delete all the hierarchy underneath.
 * 
 * @param path The path of the file or directory to delete
 * @return true iff the file was really delete, false otherwise
 */
public static boolean delete(String path) {
	return delete(new File(path));
}
/**
 * Generate a display string from the given String.
 * @param inputString the given input string
 *
 * Example of use: [org.eclipse.wst.jsdt.core.tests.util.Util.displayString("abc\ndef\tghi")]
*/
public static String displayString(String inputString){
	return displayString(inputString, 0);
}
/**
 * Generate a display string from the given String.
 * It converts:
 * <ul>
 * <li>\t to \t</li>
 * <li>\r to \\r</li>
 * <li>\n to \n</li>
 * <li>\b to \\b</li>
 * <li>\f to \\f</li>
 * <li>\" to \\\"</li>
 * <li>\' to \\'</li>
 * <li>\\ to \\\\</li>
 * <li>All other characters are unchanged.</li>
 * </ul>
 * This method doesn't convert \r\n to \n. 
 * <p>
 * Example of use:
 * <o>
 * <li>
 * <pre>
 * input string = "abc\ndef\tghi",
 * indent = 3
 * result = "\"\t\t\tabc\\n" +
 * 			"\t\t\tdef\tghi\""
 * </pre>
 * </li>
 * <li>
 * <pre>
 * input string = "abc\ndef\tghi\n",
 * indent = 3
 * result = "\"\t\t\tabc\\n" +
 * 			"\t\t\tdef\tghi\\n\""
 * </pre>
 * </li>
 * <li>
 * <pre>
 * input string = "abc\r\ndef\tghi\r\n",
 * indent = 3
 * result = "\"\t\t\tabc\\r\\n" +
 * 			"\t\t\tdef\tghi\\r\\n\""
 * </pre>
 * </li>
 * </ol>
 * </p>
 * 
 * @param inputString the given input string
 * @param indent number of tabs are added at the begining of each line.
 *
 * @return the displayed string
*/
public static String displayString(String inputString, int indent) {
	return displayString(inputString, indent, false);
}
public static String displayString(String inputString, int indent, boolean shift) {
	if (inputString == null)
		return "null";
	int length = inputString.length();
	StringBuffer buffer = new StringBuffer(length);
	java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(inputString, "\n\r", true);
	for (int i = 0; i < indent; i++) buffer.append("\t");
	if (shift) indent++;
	buffer.append("\"");
	while (tokenizer.hasMoreTokens()){

		String token = tokenizer.nextToken();
		if (token.equals("\r")) {
			buffer.append("\\r");
			if (tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken();
				if (token.equals("\n")) {
					buffer.append("\\n");
					if (tokenizer.hasMoreTokens()) {
						buffer.append("\" + \n");
						for (int i = 0; i < indent; i++) buffer.append("\t");
						buffer.append("\"");
					}
					continue;
				}
				buffer.append("\" + \n");
				for (int i = 0; i < indent; i++) buffer.append("\t");
				buffer.append("\"");
			} else {
				continue;
			}
		} else if (token.equals("\n")) {
			buffer.append("\\n");
			if (tokenizer.hasMoreTokens()) {
				buffer.append("\" + \n");
				for (int i = 0; i < indent; i++) buffer.append("\t");
				buffer.append("\"");
			}
			continue;
		}	

		StringBuffer tokenBuffer = new StringBuffer();
		for (int i = 0; i < token.length(); i++){ 
			char c = token.charAt(i);
			switch (c) {
				case '\r' :
					tokenBuffer.append("\\r");
					break;
				case '\n' :
					tokenBuffer.append("\\n");
					break;
				case '\b' :
					tokenBuffer.append("\\b");
					break;
				case '\t' :
					tokenBuffer.append("\t");
					break;
				case '\f' :
					tokenBuffer.append("\\f");
					break;
				case '\"' :
					tokenBuffer.append("\\\"");
					break;
				case '\'' :
					tokenBuffer.append("\\'");
					break;
				case '\\' :
					tokenBuffer.append("\\\\");
					break;
				default :
					tokenBuffer.append(c);
			}
		}
		buffer.append(tokenBuffer.toString());
	}
	buffer.append("\"");
	return buffer.toString();
}
/**
 * Reads the content of the given source file.
 * Returns null if enable to read given source file.
 *
 * Example of use: [org.eclipse.wst.jsdt.core.tests.util.Util.fileContent("c:/temp/X.java")]
*/
public static String fileContent(String sourceFilePath) {
	File sourceFile = new File(sourceFilePath);
	if (!sourceFile.exists()) {
		if (DEBUG) System.out.println("File " + sourceFilePath + " does not exists.");
		return null;
	}
	if (!sourceFile.isFile()) {
		if (DEBUG) System.out.println(sourceFilePath + " is not a file.");
		return null;
	}
	StringBuffer sourceContentBuffer = new StringBuffer();
	FileInputStream input = null;
	try {
		input = new FileInputStream(sourceFile);
	} catch (FileNotFoundException e) {
		return null;
	}
	try { 
		int read;
		do {
			read = input.read();
			if (read != -1) {
				sourceContentBuffer.append((char)read);
			}
		} while (read != -1);
		input.close();
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	} finally {
		try {
			input.close();
		} catch (IOException e2) {
		}
	}
	return sourceContentBuffer.toString();
}

/**
 * Reads the content of the given source file and converts it to a display string.
 *
 * Example of use: [org.eclipse.wst.jsdt.core.tests.util.Util.fileContentToDisplayString("c:/temp/X.java", 0)]
*/
public static String fileContentToDisplayString(String sourceFilePath, int indent, boolean independantLineDelimiter) {
	String sourceString = fileContent(sourceFilePath);
	if (independantLineDelimiter) {
		sourceString = convertToIndependantLineDelimiter(sourceString);
	}
	return displayString(sourceString, indent);
}
/**
 * Reads the content of the given source file, converts it to a display string.
 * If the destination file path is not null, writes the result to this file.
 * Otherwise writes it to the console.
 *
 * Example of use: [org.eclipse.wst.jsdt.core.tests.util.Util.fileContentToDisplayString("c:/temp/X.java", 0, null)]
*/
public static void fileContentToDisplayString(String sourceFilePath, int indent, String destinationFilePath, boolean independantLineDelimiter) {
	String displayString = fileContentToDisplayString(sourceFilePath, indent, independantLineDelimiter);
	if (destinationFilePath == null) {
		System.out.println(displayString);
		return;
	}
	writeToFile(displayString, destinationFilePath);
}
/**
 * Flush content of a given directory (leaving it empty),
 * no-op if not a directory.
 */
public static void flushDirectoryContent(File dir) {
	File[] files = dir.listFiles();
	if (files == null) return;
	for (int i = 0, max = files.length; i < max; i++) {
		delete(files[i]);
	}
}
/**
 * Returns the next available port number on the local host.
 */
public static int getFreePort() {
	ServerSocket socket = null;
	try {
		socket = new ServerSocket(0);
		return socket.getLocalPort();
	} catch (IOException e) {
		// ignore
	} finally {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
	return -1;
}
/**
 * Search the user hard-drive for a Java class library.
 * Returns null if none could be found.
 *
 * Example of use: [org.eclipse.wst.jsdt.core.tests.util.Util.getJavaClassLib()]
*/
public static String[] getJavaClassLibs() {
	
	IPath targetRoot =  (new Path(System.getProperty("user.dir"))).removeLastSegments(1);
	IPath pluginDir = targetRoot.append(new Path(TARGET_PLUGIN));
	IPath libDir = pluginDir.append(new Path(new String(SystemLibraryLocation.LIBRARY_PLUGIN_DIRECTORY)));
	IPath fullDir = libDir.append(new Path(new String(SystemLibraryLocation.SYSTEM_LIBARAY_NAME)));
	File libFile=new File(fullDir.toOSString());
	if (!libFile.exists())
	{
	targetRoot =  new Path(OUTPUT_DIRECTORY);
	libDir = targetRoot.append(new Path(new String(SystemLibraryLocation.LIBRARY_PLUGIN_DIRECTORY)));
	
	 libFile=new File(libDir.toOSString());
	libFile.mkdirs();
	
	 fullDir = libDir.append(new Path(new String(SystemLibraryLocation.SYSTEM_LIBARAY_NAME)));

	Class clazz=SystemLibraryLocation.class;
 
	String inputName=/*"../../../../../../../"+*/
	new String(SystemLibraryLocation.LIBRARY_PLUGIN_DIRECTORY)+"/"+new String(SystemLibraryLocation.SYSTEM_LIBARAY_NAME);

	URL resource = clazz.getClassLoader().getResource(inputName);

	InputStream inputStream=clazz.getClassLoader().getResourceAsStream(inputName);
	
	try {
		copyFile(inputStream, new File(fullDir.toOSString()));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
	return new String[]
	                  {
			fullDir.toOSString()
	                  };
//	String jreDir = getJREDirectory();
//	final String osName = System.getProperty("os.name");
//	if (jreDir == null)  {
//		return new String[] {};
//	}
//	if (osName.startsWith("Mac")) {
//		return new String[] { toNativePath(jreDir + "/../Classes/classes.jar")};
//	}
//	final String vmName = System.getProperty("java.vm.name");
//	if ("J9".equals(vmName)) {
//		return new String[] { toNativePath(jreDir + "/lib/jclMax/classes.zip")};
//	}
//	File file = new File(jreDir + "/lib/rt.jar");
//	if (file.exists()) {
//		return new String[] {
//			toNativePath(jreDir + "/lib/rt.jar")
//		};				
//	}
//	return new String[] { 
//		toNativePath(jreDir + "/lib/core.jar"),
//		toNativePath(jreDir + "/lib/security.jar"),
//		toNativePath(jreDir + "/lib/graphics.jar")
//	};
}

public static void copyFile(InputStream src, File dst) throws IOException {
	InputStream in=null;
	OutputStream out=null;
	try {
		in = new BufferedInputStream(src);
		out = new BufferedOutputStream(new FileOutputStream(dst));
		byte[] buffer = new byte[4096];
		int len;
		while ((len=in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
	} finally {
		if (in != null)
			try {
				in.close();
			} catch (IOException e) {
			}
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
			}
	}
}

public static String copyToOutput(String fileName, String contents) {
	
	Path targetRoot =  new Path(OUTPUT_DIRECTORY);
	Path filePath=new Path(fileName);
	
	IPath libDir = targetRoot.append(filePath.removeLastSegments(1));
	String fullFileName=libDir.toOSString();
	 File libFile=new File(fullFileName);
	libFile.mkdirs();
	
	 IPath fullDir = libDir.append(filePath.lastSegment());


	InputStream inputStream=new StringBufferInputStream(contents);
	
	try {
		copyFile(inputStream, new File(fullDir.toOSString()));
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
	
	return 
			fullDir.toOSString();
	                  
}



public static String getJavaClassLibsAsString() {
	String[] classLibs = getJavaClassLibs();
	StringBuffer buffer = new StringBuffer();
	for (int i = 0, max = classLibs.length; i < max; i++) {
		buffer
			.append(classLibs[i])
			.append(File.pathSeparatorChar);
		
	}
	return buffer.toString();
}
/**
 * Returns the JRE directory this tests are running on.
 * Returns null if none could be found.
 * 
 * Example of use: [org.eclipse.wst.jsdt.core.tests.util.Util.getJREDirectory()]
 */
public static String getJREDirectory() {
	return System.getProperty("java.home");
}
/**
 * Search the user hard-drive for a possible output directory.
 * Returns null if none could be found.
 * 
 * Example of use: [org.eclipse.wst.jsdt.core.tests.util.Util.getOutputDirectory()]
 */
public static String getOutputDirectory() {
	return OUTPUT_DIRECTORY;
}
/**
 * Returns the parent's child file matching the given file or null if not found.
 * 
 * @param file The searched file in parent
 * @return The parent's child matching the given file or null if not found.
 */
private static File getParentChildFile(File file) {
	File parent = file.getParentFile();
	if (parent == null || !parent.exists()) return null;
	File[] files = parent.listFiles();
	int length = files==null ? 0 : files.length;
	if (length > 0) {
		for (int i=0; i<length; i++) {
			if (files[i] == file) {
				return files[i];
			} else if (files[i].equals(file)) {
				return files[i];
			} else if (files[i].getPath().equals(file.getPath())) {
				return files[i];
			}
		}
	}
	return null;
}
/**
 * Returns parent's child resource matching the given resource or null if not found.
 * 
 * @param resource The searched file in parent
 * @return The parent's child matching the given file or null if not found.
 */
private static IResource getParentChildResource(IResource resource) {
	IContainer parent = resource.getParent();
	if (parent == null || !parent.exists()) return null;
	try {
		IResource[] members = parent.members();
		int length = members ==null ? 0 : members.length;
		if (length > 0) {
			for (int i=0; i<length; i++) {
				if (members[i] == resource) {
					return members[i];
				} else if (members[i].equals(resource)) {
					return members[i];
				} else if (members[i].getFullPath().equals(resource.getFullPath())) {
					return members[i];
				}
			}
		}
	}
	catch (CoreException ce) {
		// skip
	}
	return null;
}
/**
 * Returns the test name from stack elements info.
 * 
 * @return The name of the test currently running
 */
private static String getTestName() {
	StackTraceElement[] elements = new Exception().getStackTrace();
	int idx = 0, length=elements.length;
	while (idx<length && !elements[idx++].getClassName().startsWith("org.eclipse.wst.jsdt")) {
		// loop until JDT/Core class appears in the stack
	}
	if (idx<length) {
		StackTraceElement testElement = null;
		while (idx<length && elements[idx].getClassName().startsWith("org.eclipse.wst.jsdt")) {
			testElement = elements[idx++];
		}
		if (testElement != null) {
			return testElement.getClassName() + " - " + testElement.getMethodName();
		}
	}
	return "?";
}
public static String indentString(String inputString, int indent) {
	if (inputString == null)
		return "";
	int length = inputString.length();
	StringBuffer buffer = new StringBuffer(length);
	java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(inputString, "\n\r", true);
	StringBuffer indentStr = new StringBuffer(indent);
	for (int i = 0; i < indent; i++) indentStr.append("\t");
	buffer.append(indentStr);
	while (tokenizer.hasMoreTokens()){
		String token = tokenizer.nextToken();
		buffer.append(token);
		if (token.equals("\r") || token.equals("\n")) {
			buffer.append(indentStr);
		}
	}
	return buffer.toString();
}
/**
 * Returns whether a file is really deleted or not.
 * Does not only rely on {@link File#exists()} method but also
 * look if it's not in its parent children {@link #getParentChildFile(File)}.
 * 
 * @param file The file to test if deleted
 * @return true if the file does not exist and was not found in its parent children.
 */
public static boolean isFileDeleted(File file) {
	return !file.exists() && getParentChildFile(file) == null;
}
public static boolean isMacOS() {
	return System.getProperty("os.name").indexOf("Mac") != -1;
}
/**
 * Returns whether a resource is really deleted or not.
 * Does not only rely on {@link IResource#isAccessible()} method but also
 * look if it's not in its parent children {@link #getParentChildResource(IResource)}.
 * 
 * @param resource The resource to test if deleted
 * @return true if the resource is not accessible and was not found in its parent children.
 */
public static boolean isResourceDeleted(IResource resource) {
	return !resource.isAccessible() && getParentChildResource(resource) == null;
}
/**
 * Print given file information with specified indentation.
 * These information are:<ul>
 * 	<li>read {@link File#canRead()}</li>
 * 	<li>write {@link File#canWrite()}</li>
 * 	<li>exists {@link File#exists()}</li>
 * 	<li>is file {@link File#isFile()}</li>
 * 	<li>is directory {@link File#isDirectory()}</li>
 * 	<li>is hidden {@link File#isHidden()}</li>
 * </ul>
 * May recurse several level in parents hierarchy.
 * May also display children, but then will not recusre in parent
 * hierarchy to avoid infinite loop...
 * 
 * @param file The file to display information
 * @param indent Number of tab to print before the information
 * @param recurse Display also information on <code>recurse</code>th parents in hierarchy.
 * 	If negative then display children information instead.
 */
private static void printFileInfo(File file, int indent, int recurse) {
	StringBuilder tab = new StringBuilder();
	for (int i=0; i<indent; i++) {
		tab.append('\t');
	}
	System.out.print(tab+"- "+file.getName()+" file info: ");
	String sep = "";
	if (file.canRead()) {
		System.out.print("read");
		sep = ", ";
	}
	if (file.canWrite()) {
		System.out.print(sep+"write");
		sep = ", ";
	}
	if (file.exists()) {
		System.out.print(sep+"exist");
		sep = ", ";
	}
	if (file.isDirectory()) {
		System.out.print(sep+"dir");
		sep = ", ";
	}
	if (file.isFile()) {
		System.out.print(sep+"file");
		sep = ", ";
	}
	if (file.isHidden()) {
		System.out.print(sep+"hidden");
		sep = ", ";
	}
	System.out.println();
	File[] files = file.listFiles();
	int length = files==null ? 0 : files.length;
	if (length > 0) {
		boolean children = recurse < 0;
		System.out.print(tab+"	+ children: ");
		if (children) System.out.println();
		for (int i=0; i<length; i++) {
			if (children) { // display children
				printFileInfo(files[i], indent+2, -1);
			} else {
				if (i>0) System.out.print(", ");
				System.out.print(files[i].getName());
				if (files[i].isDirectory()) System.out.print("[dir]");
				else if (files[i].isFile()) System.out.print("[file]");
				else System.out.print("[?]");
			}
		}
		if (!children) System.out.println();
	}
	if (recurse > 0) {
		File parent = file.getParentFile();
		if (parent != null) printFileInfo(parent, indent+1, recurse-1);
	}
}
/**
 * Print stack trace with only JDT/Core elements.
 * 
 * @param exception Exception of the stack trace. May be null, then a fake exception is used.
 * @param indent Number of tab to display before the stack elements to display.
 */
private static void printJdtCoreStackTrace(Exception exception, int indent) {
	StringBuilder tab = new StringBuilder();
	for (int i=0; i<indent; i++) {
		tab.append('\t');
	}
	StackTraceElement[] elements = (exception==null?new Exception():exception).getStackTrace();
	int idx = 0, length=elements.length;
	while (idx<length && !elements[idx++].getClassName().startsWith("org.eclipse.wst.jsdt")) {
		// loop until JDT/Core class appears in the stack
	}
	if (idx<length) {
		System.out.print(tab+"- stack trace");
		if (exception == null)
			System.out.println(":");
		else
			System.out.println(" for exception "+exception+":");
		while (idx<length && elements[idx].getClassName().startsWith("org.eclipse.wst.jsdt")) {
			StackTraceElement testElement = elements[idx++];
			System.out.println(tab+"	-> "+testElement);
		}
	} else {
		exception.printStackTrace(System.out);
	}
}
/**
 * Makes the given path a path using native path separators as returned by File.getPath()
 * and trimming any extra slash.
 */
public static String toNativePath(String path) {
	String nativePath = path.replace('\\', File.separatorChar).replace('/', File.separatorChar);
	return
		nativePath.endsWith("/") || nativePath.endsWith("\\") ?
			nativePath.substring(0, nativePath.length() - 1) :
			nativePath;
}
/**
 * Unzip the contents of the given zip in the given directory (create it if it doesn't exist)
 */
public static void unzip(String zipPath, String destDirPath) throws IOException {

	InputStream zipIn = new FileInputStream(zipPath);
	byte[] buf = new byte[8192];
	File destDir = new File(destDirPath);
	ZipInputStream zis = new ZipInputStream(zipIn);
	FileOutputStream fos = null;
	try {
		ZipEntry zEntry;
		while ((zEntry = zis.getNextEntry()) != null) {
			// if it is empty directory, create it
			if (zEntry.isDirectory()) {
				new File(destDir, zEntry.getName()).mkdirs();
				continue;
			}
			// if it is a file, extract it
			String filePath = zEntry.getName();
			int lastSeparator = filePath.lastIndexOf("/"); //$NON-NLS-1$
			String fileDir = ""; //$NON-NLS-1$
			if (lastSeparator >= 0) {
				fileDir = filePath.substring(0, lastSeparator);
			}
			//create directory for a file
			new File(destDir, fileDir).mkdirs();
			//write file
			File outFile = new File(destDir, filePath);
			fos = new FileOutputStream(outFile);
			int n = 0;
			while ((n = zis.read(buf)) >= 0) {
				fos.write(buf, 0, n);
			}
			fos.close();
		}
	} catch (IOException ioe) {
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException ioe2) {
			}
		}
	} finally {
		try {
			zipIn.close();
			if (zis != null)
				zis.close();
		} catch (IOException ioe) {
		}
	}
}
/**
 * Wait until the file is _really_ deleted on file system.
 * 
 * @param file Deleted file
 * @return true if the file was finally deleted, false otherwise
 */
private static boolean waitUntilFileDeleted(File file) {
	if (DELETE_DEBUG) {
		System.out.println();
		System.out.println("WARNING in test: "+getTestName());
		System.out.println("	- problems occured while deleting "+file);
		printJdtCoreStackTrace(null, 1);
		printFileInfo(file.getParentFile(), 1, -1); // display parent with its children
		System.out.print("	- wait for ("+DELETE_MAX_WAIT+"ms max): ");
	}
	int count = 0;
	int delay = 10; // ms
	int maxRetry = DELETE_MAX_WAIT / delay;
	int time = 0;
	while (count < maxRetry) {
		try {
			count++;
			Thread.sleep(delay);
			time += delay;
			if (time > DELETE_MAX_TIME) DELETE_MAX_TIME = time;
			if (DELETE_DEBUG) System.out.print('.');
			if (file.exists()) {
				if (file.delete()) {
					// SUCCESS
					if (DELETE_DEBUG) {
						System.out.println();
						System.out.println("	=> file really removed after "+time+"ms (max="+DELETE_MAX_TIME+"ms)");
						System.out.println();
					}
					return true;
				}
			}
			if (isFileDeleted(file)) {
				// SUCCESS
				if (DELETE_DEBUG) {
					System.out.println();
					System.out.println("	=> file disappeared after "+time+"ms (max="+DELETE_MAX_TIME+"ms)");
					System.out.println();
				}
				return true;
			}
			// Increment waiting delay exponentially
			if (count >= 10 && delay <= 100) {
				count = 1;
				delay *= 10;
				maxRetry = DELETE_MAX_WAIT / delay;
				if ((DELETE_MAX_WAIT%delay) != 0) {
					maxRetry++;
				}
			}
		}
		catch (InterruptedException ie) {
			break; // end loop
		}
	}
	if (!DELETE_DEBUG) {
		System.out.println();
		System.out.println("WARNING in test: "+getTestName());
		System.out.println("	- problems occured while deleting "+file);
		printJdtCoreStackTrace(null, 1);
		printFileInfo(file.getParentFile(), 1, -1); // display parent with its children
	}
	System.out.println();
	System.out.println("	!!! ERROR: "+file+" was never deleted even after having waited "+DELETE_MAX_TIME+"ms!!!");
	System.out.println();
	return false;
}
/**
 * Wait until a resource is _really_ deleted on file system.
 * 
 * @param resource Deleted resource
 * @return true if the file was finally deleted, false otherwise
 */
private static boolean waitUntilResourceDeleted(IResource resource) {
	File file = resource.getLocation().toFile();
	if (DELETE_DEBUG) {
		System.out.println();
		System.out.println("WARNING in test: "+getTestName());
		System.out.println("	- problems occured while deleting resource "+resource);
		printJdtCoreStackTrace(null, 1);
		printFileInfo(file.getParentFile(), 1, -1); // display parent with its children
		System.out.print("	- wait for ("+DELETE_MAX_WAIT+"ms max): ");
	}
	int count = 0;
	int delay = 10; // ms
	int maxRetry = DELETE_MAX_WAIT / delay;
	int time = 0;
	while (count < maxRetry) {
		try {
			count++;
			Thread.sleep(delay);
			time += delay;
			if (time > DELETE_MAX_TIME) DELETE_MAX_TIME = time;
			if (DELETE_DEBUG) System.out.print('.');
			if (resource.isAccessible()) {
				try {
					resource.delete(true, null);
					if (isResourceDeleted(resource) && isFileDeleted(file)) {
						// SUCCESS
						if (DELETE_DEBUG) {
							System.out.println();
							System.out.println("	=> resource really removed after "+time+"ms (max="+DELETE_MAX_TIME+"ms)");
							System.out.println();
						}
						return true;
					}
				}
				catch (CoreException e) {
					//	skip
				}
			}
			if (isResourceDeleted(resource) && isFileDeleted(file)) {
				// SUCCESS
				if (DELETE_DEBUG) {
					System.out.println();
					System.out.println("	=> resource disappeared after "+time+"ms (max="+DELETE_MAX_TIME+"ms)");
					System.out.println();
				}
				return true;
			}
			// Increment waiting delay exponentially
			if (count >= 10 && delay <= 100) {
				count = 1;
				delay *= 10;
				maxRetry = DELETE_MAX_WAIT / delay;
				if ((DELETE_MAX_WAIT%delay) != 0) {
					maxRetry++;
				}
			}
		}
		catch (InterruptedException ie) {
			break; // end loop
		}
	}
	if (!DELETE_DEBUG) {
		System.out.println();
		System.out.println("WARNING in test: "+getTestName());
		System.out.println("	- problems occured while deleting resource "+resource);
		printJdtCoreStackTrace(null, 1);
		printFileInfo(file.getParentFile(), 1, -1); // display parent with its children
	}
	System.out.println();
	System.out.println("	!!! ERROR: "+resource+" was never deleted even after having waited "+DELETE_MAX_TIME+"ms!!!");
	System.out.println();
	return false;
}
public static void writeToFile(String contents, String destinationFilePath) {
	File destFile = new File(destinationFilePath);
	PrintWriter writer = null;
	try {
		writer = new PrintWriter(new FileOutputStream(destFile));
		writer.print(contents);
		writer.flush();
	} catch (IOException e) {
		e.printStackTrace();
		return;
	} finally {
		if (writer != null) {
			writer.close();
		}
	}
}
public static void zip(File rootDir, String zipPath) throws IOException {
	ZipOutputStream zip = null;
	try {
		File zipFile = new File(zipPath);
		if (zipFile.exists()) delete(zipFile);
		zip = new ZipOutputStream(new FileOutputStream(zipFile));
		zip(rootDir, zip, rootDir.getPath().length()+1); // 1 for last slash
	} finally {
		if (zip != null) {
			zip.close();
		}
	}
}
private static void zip(File dir, ZipOutputStream zip, int rootPathLength) throws IOException {
	File[] files = dir.listFiles();
	if (files != null) {
		for (int i = 0, length = files.length; i < length; i++) {
			File file = files[i];
			if (file.isFile()) {
				String path = file.getPath();
				path = path.substring(rootPathLength);
				ZipEntry entry = new ZipEntry(path.replace('\\', '/'));
				zip.putNextEntry(entry);
				zip.write(org.eclipse.wst.jsdt.internal.compiler.util.Util.getFileByteContent(file));
				zip.closeEntry();
			} else {
				zip(file, zip, rootPathLength);
			}
		}
	}
}
}
