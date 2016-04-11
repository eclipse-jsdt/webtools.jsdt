/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.runtime;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeManager;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;
import org.eclipse.wst.jsdt.internal.core.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is a container for runtime definitions such as the runtime definitions that are
 * stored in the workbench preferences.  
 * <p>
 * An instance of this class may be obtained from an XML document by calling
 * <code>parseXMLIntoContainer</code>.
 * </p>
 * <p>
 * An instance of this class may be translated into an XML document by calling
 * <code>getAsXML</code>.
 * </p>
 * <p>
 * Clients may instantiate this class; it is not intended to be subclassed.
 * </p>
 * 
 */
public class JSRuntimesDefinitionsContainer {
	private Map<IJSRuntimeType, List<IJSRuntimeInstall>> fRuntimeTypeToInstallMap;
	
	private Map<String, String> fRuntimeTypeDefaultMap;
	
	/**
	 * Cached list of runtimes in this container
	 */
	private List<IJSRuntimeInstall> fRuntimeList;
	
	/**
	 * Constructs an empty container 
	 */
	public JSRuntimesDefinitionsContainer() {
		fRuntimeTypeToInstallMap = new HashMap<IJSRuntimeType, List<IJSRuntimeInstall>>(10);	
		fRuntimeTypeDefaultMap = new HashMap <String, String> (10);
		fRuntimeList = new ArrayList<IJSRuntimeInstall>(10);		
	}
		
	/**
	 * Add the specified runtime to the runtime definitions managed by this container.
	 *
	 * @param runtime the runtime to be added to this container
	 */
	public void addRuntime(IJSRuntimeInstall runtime) {
		if (!fRuntimeList.contains(runtime)) {	
			IJSRuntimeType runtimeType = runtime.getRuntimeType();
			List<IJSRuntimeInstall> runtimeList = fRuntimeTypeToInstallMap.get(runtimeType);
			if (runtimeList == null) {
				runtimeList = new ArrayList<IJSRuntimeInstall>(3);
				fRuntimeTypeToInstallMap.put(runtimeType, runtimeList);			
			}
			runtimeList.add(runtime);
			fRuntimeList.add(runtime);
		}
	}
	
	/**
	 * Return a list of all runtimes in this container.
	 * The order of the list is not specified.
	 * 
	 * @return List the data structure containing all runtimes managed by this container
	 */
	public List<IJSRuntimeInstall> getRuntimeList() {
		return fRuntimeList;
	}
	
	/**
	 * Return the runtime definitions contained in this object as a String of XML.  The String
	 * is suitable for storing in the workbench preferences.
	 * <p>
	 * The resulting XML is compatible with the static method <code>parseXMLIntoContainer</code>.
	 * </p>
	 * @return String the results of flattening this object into XML
	 * @throws CoreException if serialization of the XML document failed
	 */
	public String getAsXML() throws CoreException {
		
		// Create the Document and the top-level node
		Document doc = DebugPlugin.newDocument();
		Element config = doc.createElement("runtimeSettings");    //$NON-NLS-1$
		doc.appendChild(config);
				
		// Create a node for each install type represented in this container
		Set<IJSRuntimeType> runtimeTypeSet = fRuntimeTypeToInstallMap.keySet();
		Iterator<IJSRuntimeType> keyIterator = runtimeTypeSet.iterator();
		while (keyIterator.hasNext()) {
			IJSRuntimeType runtimeType = keyIterator.next();
			Element runtimeTypeElement = runtimeTypeAsElement(doc, runtimeType);
			config.appendChild(runtimeTypeElement);
		}
		
		// Serialize the Document and return the resulting String
		return DebugPlugin.serializeDocument(doc);
	}
	
	public void setDefaultRuntimeInstallId (String runtimeTypeId, String runtimeInstallId) {
		fRuntimeTypeDefaultMap.put(runtimeTypeId, runtimeInstallId);
	}
	
	public String getDefaultRuntimeInstallId (String runtimeTypeId) {
		return fRuntimeTypeDefaultMap.get(runtimeTypeId);
	}
	
	/**
	 * Create and return a node for the specified runtime install type 
	 * in the specified Document.
	 * 
	 * @param doc the backing {@link Document}
	 * @param runtimeType the {@link IJSRuntimeType} to create an {@link Element} for
	 * @return the new {@link Element}
	 */
	private Element runtimeTypeAsElement(Document doc, IJSRuntimeType runtimeType) {
		
		// Create a node for the runtime type and set its 'id' attribute
		Element element= doc.createElement("runtimeType");   //$NON-NLS-1$
		element.setAttribute("id", runtimeType.getId());     //$NON-NLS-1$
		
		if (getDefaultRuntimeInstallId(runtimeType.getId()) != null) {
			element.setAttribute("defaultRuntime", getDefaultRuntimeInstallId(runtimeType.getId())); //$NON-NLS-1$
		}
		
		// For each runtime install of the specified type, create a subordinate node for it
		List<IJSRuntimeInstall> runtimeList = fRuntimeTypeToInstallMap.get(runtimeType);
		Iterator<IJSRuntimeInstall> runtimesIterator = runtimeList.iterator();
		while (runtimesIterator.hasNext()) {
			IJSRuntimeInstall runtime = runtimesIterator.next();
			Element runtimeElement = runtimeInstallAsElement(doc, runtime);
			element.appendChild(runtimeElement);
		}
		
		return element;
	}
	
	/**
	 * Create and return a node for the specified runtime install in the specified Document.
	 * 
	 * @param doc the backing {@link Document}
	 * @param runtimeInstall the {@link IJSRuntimeInstall} to create an {@link Element} for
	 * @return the new {@link Element} representing the given {@link IJSRuntimeInstall}
	 */
	private Element runtimeInstallAsElement(Document doc, IJSRuntimeInstall runtimeInstall) {
		
		// Create the node for the runtime install and set its 'id' & 'name' attributes
		Element element= doc.createElement("runtime");        //$NON-NLS-1$
		element.setAttribute("id", runtimeInstall.getId());	         //$NON-NLS-1$
		element.setAttribute("name", runtimeInstall.getName());      //$NON-NLS-1$
		
		// Determine and set the 'path' attribute for the runtime install
		String installPath = "";                          //$NON-NLS-1$
		File installLocation = runtimeInstall.getInstallLocation();
		if (installLocation != null) {
			if (installLocation.exists()) {
				installPath = installLocation.getAbsolutePath();
			} else {
				// Store it as-is (usually for system-global command)
				installPath = installLocation.getPath();
			}
		}
		element.setAttribute("path", installPath);       //$NON-NLS-1$
		
		String runtimeArgs = runtimeInstall.getJSRuntimeArgumentsAsString();
		if (runtimeArgs != null && runtimeArgs.length() > 0) {
			element.setAttribute("runtimeArgs", runtimeArgs); //$NON-NLS-1$
		}
		
		return element;
	}
	
	public static JSRuntimesDefinitionsContainer parseXMLIntoContainer(InputStream inputStream) throws IOException {
		JSRuntimesDefinitionsContainer container = new JSRuntimesDefinitionsContainer();
		parseXMLIntoContainer(inputStream, container);
		return container;
	}
			
	/**
	 * Parse the runtime definitions contained in the specified InputStream into the
	 * specified container.
	 * <p>
	 * The Runtimes in the returned container are instances of <code>JSRuntimeWorkingCopy</code>.
	 * </p>
	 * <p>
	 * If the <code>getAsXML</code> method is called on the returned container object,
	 * the resulting XML will be semantically equivalent (though not necessarily syntactically equivalent) as
	 * the XML contained in <code>inputStream</code>.
	 * </p>
	 * @param inputStream the <code>InputStream</code> containing XML that declares a set of runtime types
	 * and a default runtime per-type
	 * @param container the container to add the runtime definitions to
	 * @throws IOException if this method fails. Reasons include:<ul>
	 * <li>the XML in <code>inputStream</code> was badly formatted</li>
	 * <li>the top-level node was not 'runtimeSettings'</li>
	 * </ul>
	 */
	public static void parseXMLIntoContainer(InputStream inputStream, JSRuntimesDefinitionsContainer container) throws IOException {
		// Do the parsing and obtain the top-level node
		Element config = null;		
		// Wrapper the stream for efficient parsing
		try (InputStream stream = new BufferedInputStream(inputStream)) {
			DocumentBuilder parser= DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			config = parser.parse(new InputSource(stream)).getDocumentElement();
		} catch (SAXException e) {
			throw new IOException(RuntimeMessages.JSRuntimeDefinitionsContainer_WrongXMLFormat_Error); 
		} catch (ParserConfigurationException e) {
			throw new IOException(RuntimeMessages.JSRuntimeDefinitionsContainer_WrongXMLFormat_Error); 
		}
		
		// If the top-level node wasn't what we expected, bail out
		if (!config.getNodeName().equalsIgnoreCase("runtimeSettings")) { //$NON-NLS-1$
			throw new IOException(RuntimeMessages.JSRuntimeDefinitionsContainer_WrongXMLFormat_Error); 
		}
		
		// Traverse the parsed structure and populate the runtime type to runimtes map
		NodeList list = config.getChildNodes();
		int length = list.getLength();
		for (int i = 0; i < length; ++i) {
			Node node = list.item(i);
			short type = node.getNodeType();
			if (type == Node.ELEMENT_NODE) {
				Element runtimeTypeElement = (Element) node;
				if (runtimeTypeElement.getNodeName().equalsIgnoreCase("runtimeType")) { //$NON-NLS-1$
					populateRuntimeType(runtimeTypeElement, container);
				}
			}
		}
	}
	
	/**
	 * For the specified runtime type node, parse all subordinate runtime definitions and add them
	 * to the specified container.
	 * 
	 * Also, parse the default install for the current runtime type.
	 * 
	 * @param runtimeTypeElement the {@link Element} to populate the {@link JSRuntimesDefinitionsContainer} from
	 * @param container the {@link JSRuntimesDefinitionsContainer} to populate from the {@link Element}
	 */
	private static void populateRuntimeType(Element runtimeTypeElement, JSRuntimesDefinitionsContainer container) {
		// Retrieve the 'id' attribute and the corresponding runtime type object
		String id = runtimeTypeElement.getAttribute("id");         //$NON-NLS-1$
		IJSRuntimeType runtimeType = JSRuntimeManager.getJSRuntimeType(id);
		if (runtimeType != null) {
			// Set the default runtime for this specific type
			String defaultRuntimeInstall = runtimeTypeElement.getAttribute("defaultRuntime"); //$NON-NLS-1$
			if (!defaultRuntimeInstall.isEmpty()) { 
				container.setDefaultRuntimeInstallId(id, defaultRuntimeInstall);
			}
			
			// For each runtime child node, populate the container with a subordinate node
			NodeList runtimeNodeList = runtimeTypeElement.getElementsByTagName("runtime"); //$NON-NLS-1$
			for (int i = 0; i < runtimeNodeList.getLength(); ++i) {
				populateRuntimeForType(runtimeType, (Element) runtimeNodeList.item(i), container);
			}
		} else {
			Logger.log(Logger.ERROR, "Runtime type with id " + id + " skipped because it is not "  //$NON-NLS-1$//$NON-NLS-2$
						+ "recognized by the runtime manager. " //$NON-NLS-1$
						+ "This will cause all children runtime installs will be skipped as well"); //$NON-NLS-1$
		}
	}

	/**
	 * Parse the specified rutime node, create a JSRuntimeWorkingCopy for it, and add this to the 
	 * specified container.
	 * 
	 * @param runtimeType runtime type
	 * @param runtimeElement XML element
	 * @param container container to add runtime to
	 */
	private static void populateRuntimeForType(IJSRuntimeType runtimeType, Element runtimeElement, JSRuntimesDefinitionsContainer container) {
		String id= runtimeElement.getAttribute("id"); //$NON-NLS-1$
		if (id != null) {
			String installPath= runtimeElement.getAttribute("path"); //$NON-NLS-1$
			String name = runtimeElement.getAttribute("name"); //$NON-NLS-1$
			if (name == null) {
				Logger.log(Logger.ERROR, "Runtime " + id + " skipped because it does not contain a name."); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			if (installPath == null) {
				Logger.log(Logger.ERROR, "Runtime " + id + " skipped because it does not contain an install path.");  //$NON-NLS-1$//$NON-NLS-2$
				return;
			}
						
			// Create a JSRuntimeWorkingCopy for the node and set its 'name' & 'installLocation' attributes
			JSRuntimeWorkingCopy wcRuntime = new JSRuntimeWorkingCopy(runtimeType, id);
			wcRuntime.setName(name);
			File installLocation = new File(installPath);
			wcRuntime.setInstallLocation(installLocation);
			String runtimeArgs = runtimeElement.getAttribute("runtimeArgs"); //$NON-NLS-1$
			if (runtimeArgs != null && runtimeArgs.length() > 0) {
				wcRuntime.setJSRuntimeArguments(runtimeArgs);
			}
			
			container.addRuntime(wcRuntime);
		} else {
			Logger.log(Logger.ERROR, "Runtime skipped because it does not contain an id."); //$NON-NLS-1$
		}
	}	
	
	/**
	 * Removes the runtime from this container.
	 * 
	 * @param runtimeInstall runtime install
	 */
	public void removeRuntime(IJSRuntimeInstall runtimeInstall) {
		fRuntimeList.remove(runtimeInstall);
		List<IJSRuntimeInstall> list = fRuntimeTypeToInstallMap.get(runtimeInstall.getRuntimeType());
		if (list != null) {
			list.remove(runtimeInstall);
		}
	}
}
