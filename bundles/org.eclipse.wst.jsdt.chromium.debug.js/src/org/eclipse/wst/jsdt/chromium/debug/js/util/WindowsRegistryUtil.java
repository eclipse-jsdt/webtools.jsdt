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

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple util class that uses the "reg" command in Windows to read a registry key value
 * 
 * @author "Shane Bryzak"   
 */
public class WindowsRegistryUtil {
	
	private static final Pattern REGISTRY_PATTERN = Pattern.compile("HKEY_.*\\r\\n\\s*\\S+\\s+REG_SZ\\s+(.+)"); //$NON-NLS-1$

    public static String readKeyValue(String location, String key){
        try {
            Process process = Runtime.getRuntime().exec("reg query \"" + location + "\" /v " + key); //$NON-NLS-1$ //$NON-NLS-2$

            InputStream is = process.getInputStream();
            StringBuilder sb = new StringBuilder();
            
            byte[] buffer = new byte[512];
            int i = is.read(buffer);
            while (i != -1) {
            	sb.append(new String(buffer));
            	i = is.read(buffer);
            }
            
            String output = sb.toString().trim();

            // Output should look something like this:
            //
            // HKEY_LOCAL_MACHINE\Software\Microsoft\Windows\CurrentVersion\App Paths\chrome.exe
            //     Path    REG_SZ    C:\Program Files (x86)\Google\Chrome\Application
           
            Matcher m = REGISTRY_PATTERN.matcher(output);
            if (m.matches()) {
            	return m.group(1);
            }
            
            return null;
        }
        catch (Exception e) {
            return null;
        }

    }

}