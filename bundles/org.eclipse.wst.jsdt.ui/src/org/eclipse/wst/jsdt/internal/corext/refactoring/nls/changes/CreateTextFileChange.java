/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.nls.changes;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.corext.refactoring.nls.NLSUtil;

public class CreateTextFileChange extends CreateFileChange {
	
	private final String fTextType;
	
	public CreateTextFileChange(IPath path, String source, String encoding, String textType) {
		super(path, source, encoding);
		fTextType= textType;
	}
	
	public String getTextType() {
		return fTextType;
	}
	
	public String getCurrentContent() throws JavaScriptModelException {
		IFile file= getOldFile(new NullProgressMonitor());
		if (! file.exists())
			return ""; //$NON-NLS-1$
		InputStream stream= null;
		try{
			stream= file.getContents();
			String encoding= file.getCharset();
			String c= NLSUtil.readString(stream, encoding);
			return (c == null) ? "": c; //$NON-NLS-1$
		} catch (CoreException e){
			throw new JavaScriptModelException(e, IJavaScriptModelStatusConstants.CORE_EXCEPTION);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException x) {
			}
		}
	}
	
	public String getPreview() {
		return getSource();
	}
}

