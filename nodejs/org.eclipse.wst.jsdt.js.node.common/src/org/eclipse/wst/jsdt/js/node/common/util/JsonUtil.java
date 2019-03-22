/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON Utils for Gson 
 *
 * @see <a href="https://github.com/google/gson/blob/master/UserGuide.md">https://github.com/google/gson/blob/master/UserGuide.md</a>
 * @author "Adalberto Lopez Venegas (adalbert)"
 */

public class JsonUtil {	
	/**
	 * Reads a JSON file at the specified location and returns an Object of the class type
	 * specified representing the JSON file attributes and values.
	 * 
	 * This method will always return an Object of the type specified,
	 * 
	 * @param fileLocation The absolute path of the JSON file
	 * @param type Class type to be use to represent the JSON format file 
	 * @return 
	 * @return An Object of type T representing the JSON format file.
	 * @throws FileNotFoundException
	 */
	public static<T> T readJsonFromFile(String fileLocation, Class<T> type) throws FileNotFoundException{
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		BufferedReader br = new BufferedReader(new FileReader(fileLocation));
		return gson.fromJson(br, type);
	}
	
	/**
	 * Reads a JSON file specified in the IFile and returns an Object of the class type
	 * specified representing the JSON file attributes and values.
	 * 
	 * This method will always return an Object of the type specified.
	 *  
	 * @param json The IFIle containing the JSON file
	 * @param type Class type to be use to map the json attribute:value pairs 
	 * @return An Object of type T representing the JSON format string, null otherwise.
	 */
	public static <T> T readJsonFromIFile(IFile json, Class<T> type){
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		InputStreamReader isr = null;
		
		try {
			isr = new InputStreamReader(json.getContents());
			return gson.fromJson(isr, type);
		} catch (CoreException e) {
			e.printStackTrace();
		} finally {
			if (isr != null){
				try {
					isr.close();
				} catch (IOException e){
				}
			}
		}
		return null;
	}

	/**
	 * Writes a JSON file representation of the Object received at the specified destination.
	 * 
	 * @param fileDestination Absolute path where the JSON file will be written.
	 * @param jsonJavaObject The object to be represented as a JSON.
	 * @throws IOException  if the named file exists but is a directory rather than a regular file, 
	 * does not exist but cannot be created, or cannot be opened for any other reason.
	 */
	public static void writeJsonToFile(String fileDestination, Object jsonJavaObject) throws IOException{
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String json = gson.toJson(jsonJavaObject);
		FileWriter writer = new FileWriter(fileDestination);
		writer.write(json);
		writer.close();
	}
	
	public static void writeJsonToFile(File fileDestination, Object jsonJavaObject) throws IOException{
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String json = gson.toJson(jsonJavaObject);
		FileWriter writer = new FileWriter(fileDestination);
		writer.write(json);
		writer.close();
	}
	
	/**
	 * Converts an Object to a JSON interpretation of that object.
	 * 
	 * @param jsonJavaObject The object to be converted.
	 * @return JSON object as string.
	 */
	public static String convertJavaObjectToJson(Object jsonJavaObject){
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		return gson.toJson(jsonJavaObject);
	}
	
	/**
	 * Converts a JSON to an Object of type T, which is an interpretation of that JSON string.
	 * 
	 * @param jsonString JSON format string.
	 * @param type Class type to be use to represent the JSON format string
	 * @return An Object of type T representing the JSON input string.
	 */
	public static <T> T convertJsonToJavaObject(String jsonString, Class<T> type){
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		return gson.fromJson(jsonString, type);
	}
}
