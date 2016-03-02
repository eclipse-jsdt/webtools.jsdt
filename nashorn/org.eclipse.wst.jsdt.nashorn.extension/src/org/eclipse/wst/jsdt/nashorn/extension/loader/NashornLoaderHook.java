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
package org.eclipse.wst.jsdt.nashorn.extension.loader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.osgi.internal.debug.Debug;
import org.eclipse.osgi.internal.framework.EquinoxConfiguration;
import org.eclipse.osgi.internal.hookregistry.ClassLoaderHook;
import org.eclipse.osgi.internal.loader.BundleLoader;
import org.eclipse.osgi.internal.loader.ModuleClassLoader;
import org.eclipse.osgi.internal.loader.classpath.ClasspathManager;
import org.eclipse.osgi.storage.BundleInfo.Generation;
/**
 * A classloader hook that creates a Module classloader for 
 * <i>org.eclipse.wst.jsdt.nashorn.api</i> bundle and resolves the nashorn classes from
 * <i>java.home/lib/ext/nashorn.jar</i> for that bundle
 * 
 * @author Gorkem Ercan
 *
 */
public class NashornLoaderHook extends ClassLoaderHook {
	private static final String BUNDLE_ID_NASHORN_API = "org.eclipse.wst.jsdt.nashorn.api";
	
	static final class NashornClassLoader extends ModuleClassLoader{
		
		private EquinoxConfiguration configuration;
		private BundleLoader delegate;
		private Generation generation;
		private ClasspathManager classpathManager;
		private URLClassLoader nashornLoader;

		public NashornClassLoader(ClassLoader parent, EquinoxConfiguration configuration, BundleLoader delegate, Generation generation) {
			super(parent);
			this.configuration = configuration;
			this.delegate = delegate;
			this.generation = generation;
			this.classpathManager = new ClasspathManager(this.generation, this);
		}

		@Override
		protected Generation getGeneration() {
			return this.generation;
		}

		@Override
		protected Debug getDebug() {
			return this.configuration.getDebug();
		}

		@Override
		public ClasspathManager getClasspathManager() {
			return this.classpathManager;
		}

		@Override
		protected EquinoxConfiguration getConfiguration() {
			return this.configuration;
		}

		@Override
		public BundleLoader getBundleLoader() {
			return this.delegate;
		}

		@Override
		public boolean isRegisteredAsParallel() {
			return false;
		}
		
		@Override
		public Class<?> findLocalClass(String classname) throws ClassNotFoundException {
			if(classname.startsWith("jdk.nashorn") || classname.startsWith("jdk.internal.dynalink")){
				URLClassLoader loader = getNashornClassLoader();
				Class<?> clazz =  loader.loadClass(classname);
				if(clazz != null){
					return clazz;
				}
			}
			return super.findLocalClass(classname);
		}
		
		private URLClassLoader getNashornClassLoader(){
			if (nashornLoader == null) {
				try {
					File javaHomeFile = new File(System.getProperty("java.home")).getCanonicalFile();
					if (!javaHomeFile.isDirectory()) {
						throw new IllegalStateException(
								"java.home " + javaHomeFile.getAbsolutePath() + " does not exist");
					}
					File nashornJar = new File(javaHomeFile, "lib/ext/nashorn.jar");
					URI nashornUri = nashornJar.toURI();
					nashornLoader = URLClassLoader.newInstance(new URL[] { nashornUri.toURL() });
					return nashornLoader;
				} catch (IOException e) {
					throw new IllegalStateException("Failire when creating the class loader for nashorn.jar", e);
				}
			}
			return nashornLoader;
		}
	}

	@Override
	public ModuleClassLoader createClassLoader(ClassLoader parent, EquinoxConfiguration configuration,
			BundleLoader delegate, Generation generation) {
		if(BUNDLE_ID_NASHORN_API.equals(generation.getRevision().getBundle().getSymbolicName())){
			return new NashornClassLoader(parent, configuration, delegate, generation);
		}
		return super.createClassLoader(parent, configuration, delegate, generation);
	}
}
