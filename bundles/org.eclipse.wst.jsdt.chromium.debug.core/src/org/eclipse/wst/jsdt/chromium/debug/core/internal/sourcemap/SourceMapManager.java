/*******************************************************************************
 * Copyright (c) 2016, 2017 Angelo Zerr.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.core.internal.sourcemap;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.chromium.debug.core.ChromiumDebugPlugin;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension.ISourceMapLanguageSupport;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension.ISourceMapManager;

/**
 * Manager which loads the "org.eclipse.wst.jsdt.chromium.debug.core.sourceMapLanguageSupports" extensions point.
 *
 */
public class SourceMapManager implements ISourceMapManager, IRegistryChangeListener {

	private static final SourceMapManager INSTANCE = new SourceMapManager();
	private static final String EXTENSION_SOURCEMAP_LANGUAGE_SUPPORTS = "sourceMapLanguageSupports";

	public static SourceMapManager getInstance() {
		return INSTANCE;
	}

	private List<SourceMapLanguageSupportType> sourceMapLanguageSupports;

	private SourceMapManager() {
	}

	@Override
	public boolean canSupportSourceMap(String fileExtension) {
		return getSourceMapLanguageSupport(fileExtension) != null;
	}

	@Override
	public String getJsFile(IPath file) throws CoreException {
		ISourceMapLanguageSupport support = getSourceMapLanguageSupport(file.getFileExtension());
		if (support == null) {
			return null;
		}
		IPath jsFile = support.getJsFile(file);
		return jsFile != null ? jsFile.toString() : null;
	}

	@Override
	public ISourceMapLanguageSupport getSourceMapLanguageSupport(String fileExtension) {
		loadSourceMapLanguageSupportsIfNeeded();
		for (SourceMapLanguageSupportType type : sourceMapLanguageSupports) {
			if (type.canSupportSourceMap(fileExtension)) {
				return type.getSupport();
			}
		}
		return null;
	}

	@Override
	public void registryChanged(final IRegistryChangeEvent event) {
		IExtensionDelta[] deltas = event.getExtensionDeltas(ChromiumDebugPlugin.PLUGIN_ID,
				EXTENSION_SOURCEMAP_LANGUAGE_SUPPORTS);
		if (deltas != null) {
			for (IExtensionDelta delta : deltas)
				handleSourceMapLanguageSupportsDelta(delta);
		}
	}

	private void loadSourceMapLanguageSupportsIfNeeded() {
		if (sourceMapLanguageSupports != null) {
			return;
		}
		loadSourceMapLanguageSupports();
	}

	/**
	 * Load the SourceMap language supports.
	 */
	private synchronized void loadSourceMapLanguageSupports() {
		if (sourceMapLanguageSupports != null) {
			return;
		}

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(ChromiumDebugPlugin.PLUGIN_ID,
				EXTENSION_SOURCEMAP_LANGUAGE_SUPPORTS);
		List<SourceMapLanguageSupportType> list = new ArrayList<SourceMapLanguageSupportType>(cf.length);
		addSourceMapLanguageSupports(cf, list);
		sourceMapLanguageSupports = list;
	}

	/**
	 * Add the SourceMap language supports.
	 */
	private synchronized void addSourceMapLanguageSupports(IConfigurationElement[] cf,
			List<SourceMapLanguageSupportType> list) {
		for (IConfigurationElement ce : cf) {
			try {
				list.add(new SourceMapLanguageSupportType(ce));
			} catch (Throwable e) {
				ChromiumDebugPlugin.log(e);
			}
		}
	}

	protected void handleSourceMapLanguageSupportsDelta(IExtensionDelta delta) {
		if (sourceMapLanguageSupports == null) // not loaded yet
			return;

		IConfigurationElement[] cf = delta.getExtension().getConfigurationElements();

		List<SourceMapLanguageSupportType> list = new ArrayList<SourceMapLanguageSupportType>(
				sourceMapLanguageSupports);
		if (delta.getKind() == IExtensionDelta.ADDED) {
			addSourceMapLanguageSupports(cf, list);
		} else {
			int size = list.size();
			SourceMapLanguageSupportType[] st = new SourceMapLanguageSupportType[size];
			list.toArray(st);
			int size2 = cf.length;

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size2; j++) {
					if (st[i].getId().equals(cf[j].getAttribute("id"))) {
						list.remove(st[i]);
					}
				}
			}
		}
		sourceMapLanguageSupports = list;
	}

	public void initialize() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this, ChromiumDebugPlugin.PLUGIN_ID);
	}

	public void destroy() {
		Platform.getExtensionRegistry().removeRegistryChangeListener(this);
	}

}
