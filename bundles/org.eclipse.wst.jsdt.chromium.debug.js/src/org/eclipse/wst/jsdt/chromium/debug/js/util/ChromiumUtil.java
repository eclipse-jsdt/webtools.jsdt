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

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IURLProvider;
import org.eclipse.wst.server.ui.IServerModule;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public final class ChromiumUtil {
	private static final String USER_HOME = System.getProperty("user.home"); //$NON-NLS-1$
	private static final String JSDT_CHROMIUM = ".jsdt-chromium"; //$NON-NLS-1$
	
	private ChromiumUtil(){
	}

	/**
	 * Creates '.jsdt-chromium' directory in user.home for using it as
	 * '--user-data-dir' parameter witch force Chrome / Chromium to start a new
	 * browser instance, not just a new window
	 * 
	 * @see <a href=
	 *      "https://www.chromium.org/user-experience/user-data-directory">--user-data-dir</a>
	 * @return Absolute path to '.jsdt-chromium' folder that will be used as
	 *         --user-data-dir parameter
	 */
	public static String getChromiumUserDataDir() {
		File chromiumDir = new File(USER_HOME, JSDT_CHROMIUM);
		chromiumDir.mkdir();
		return chromiumDir.getAbsolutePath();
	}

	public static int getRandomOpenPort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
	}

	public static String guessUrl() {
		String url = null;
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {			
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IWorkbenchPart part = page.getActivePart();
				IFile activeEditorFile = getEditorFile(part);
				url = toUrl(activeEditorFile);
			}
			
			if (url == null) {
				ISelection selection = window.getSelectionService().getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ssel = (IStructuredSelection) selection;
					Object firstSelectedElement = ssel.getFirstElement();
					if (firstSelectedElement != null) {
						url = getUrlFromSelection(firstSelectedElement);
					}
				}
			}
		}

		return url;
	}
	
	public static String toUrl(IResource resource) {
		String url = null;

		if (resource != null) {
			IPath location = resource.getLocation();
			if (location != null) {
				url = location.toFile().toURI().toASCIIString();
			}
		}
		return url;
	}
		
	public static IProject guessProject() {
		IProject project = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			Object firstSelectedElement = ssel.getFirstElement();
			if (firstSelectedElement != null) {
				@SuppressWarnings("deprecation")
				IServerModule serverModule = (IServerModule) ResourceUtil.getAdapter(firstSelectedElement, IServerModule.class, false);
				IModule module = serverModule.getModule()[0];
				project = module.getProject();
			}
		}
		return project;
	}

	
	/**
	 * If {@code part} is Internal Web IBrowser, returns opened URL. Otherwise
	 * returns {@code null}. 
	 */
	private static IFile getEditorFile(IWorkbenchPart part) {
		IFile file = null;
		
		if (part instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) part;

			if (editorPart.getEditorInput() instanceof IFileEditorInput) {
				IFileEditorInput fileEditorInput = (IFileEditorInput) editorPart.getEditorInput();
				file = fileEditorInput.getFile();
			}
		}
		
		return file;
	}

	/**
	 * Returns selected file, if a file is contained in the {@code selection}.
	 * Otherwise returns {@code null}.
	 */
	private static IFile getSelectedFile(Object firstSelectedElement) {
		IFile file = null;
		file = (IFile) Platform.getAdapterManager().getAdapter(firstSelectedElement, IFile.class);
		if (file == null) {
			if (firstSelectedElement instanceof IAdaptable) {
				file = (IFile) ((IAdaptable) firstSelectedElement).getAdapter(IFile.class);
			}
		}
		return file;
	}
	
	private static String getUrlFromSelection(Object firstSelectedElement) {
		String url = null;

		if (firstSelectedElement instanceof IFile) {
			IFile selectedFile = getSelectedFile(firstSelectedElement);
			url = toUrl(selectedFile);
		// org.eclipse.wst.server.* bundles are optional 
		} else if (Platform.getBundle("org.eclipse.wst.server.core") != null && Platform.getBundle("org.eclipse.wst.server.ui") != null) {  //$NON-NLS-1$//$NON-NLS-2$
			@SuppressWarnings("deprecation")
			IServerModule serverModule = (IServerModule) ResourceUtil.getAdapter(firstSelectedElement, IServerModule.class, false);
			if (serverModule != null) {
				url = toUrl(serverModule);
			}
		}
		return url;
	}

	private static String toUrl(IServerModule serverModule) {
		String url = null;

		IServer server = serverModule.getServer();
		IModule[] module = serverModule.getModule();
		if (server.getServerState() == IServer.STATE_STARTED && module.length == 1) {
			IModule selectedModule = module[0];
			Object serverAdapter = server.loadAdapter(IURLProvider.class, null);
			if (serverAdapter != null && selectedModule != null) {
				URL moduleRootUrl = ((IURLProvider) serverAdapter).getModuleRootURL(selectedModule);
				url = moduleRootUrl.toString();
			}
		}
		return url;
	}

}

