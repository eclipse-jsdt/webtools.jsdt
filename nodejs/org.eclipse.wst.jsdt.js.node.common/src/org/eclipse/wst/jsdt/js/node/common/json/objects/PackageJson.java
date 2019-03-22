/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 * 		 IBM Corporation - updated PackageJson object
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.common.json.objects;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * Specifics of npm's package.json 
 *
 * @see <a href="https://docs.npmjs.com/files/package.json">https://docs.npmjs.com/files/package.json</a>
 * @author "Ilya Buziuk (ibuziuk)"
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class PackageJson {
	private String name;
	private String version;
	private String description;
	private String author;
	private String homepage;
	private List<Map<String, String>> contributors;
	private Map<String, String> bin;
	private String man;
	private Map<String, String> scripts;
	private String main;
	private Map<String, String> repository;
	private Map<String, String> bugs;
	private List<String> keywords;
	private Map<String, String> dependencies;
	private Map<String, String> devDependencies;
	private Boolean preferGlobal;
	private Map<String, String> publishConfig;
	private String license;
	private Map<String, String> engines;
	private String os;
	private String cpu;
	private List<String> bundledDependencies;
	private Map<String, String> peerDependencies;
	private Map<String, String> optionalDependencies;
	private Map<String, String> config;
	private Map<String, String> directories;
	private List<String> files;
	private List<Map<String, String>> licenses;
	
	@SerializedName("private") private Boolean isPrivate;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, String> getScripts() {
		return scripts;
	}

	public void setScripts(Map<String, String> scripts) {
		this.scripts = scripts;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public Map<String, String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Map<String, String> dependencies) {
		this.dependencies = dependencies;
	}
	
	public List<Map<String, String>> getContributors() {
		return contributors;
	}

	public void setContributors(List<Map<String, String>> contributors) {
		this.contributors = contributors;
	}

	public Map<String, String> getBin() {
		return bin;
	}

	public void setBin(Map<String, String> bin) {
		this.bin = bin;
	}

	public String getMan() {
		return man;
	}

	public void setMan(String man) {
		this.man = man;
	}

	public Map<String, String> getRepository() {
		return repository;
	}

	public void setRepository(Map<String, String> repository) {
		this.repository = repository;
	}

	public Map<String, String> getBugs() {
		return bugs;
	}

	public void setBugs(Map<String, String> bugs) {
		this.bugs = bugs;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Map<String, String> getDevDependencies() {
		return devDependencies;
	}

	public void setDevDependencies(Map<String, String> devDependencies) {
		this.devDependencies = devDependencies;
	}

	public Boolean getPreferGlobal() {
		return preferGlobal;
	}

	public void setPreferGlobal(Boolean preferGlobal) {
		this.preferGlobal = preferGlobal;
	}

	public Map<String, String> getPublishConfig() {
		return publishConfig;
	}

	public void setPublishConfig(Map<String, String> publishConfig) {
		this.publishConfig = publishConfig;
	}

	public Map<String, String> getEngines() {
		return engines;
	}

	public void setEngines(Map<String, String> engines) {
		this.engines = engines;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public List<String> getBundledDependencies() {
		return bundledDependencies;
	}

	public void setBundledDependencies(List<String> bundledDependencies) {
		this.bundledDependencies = bundledDependencies;
	}

	public Map<String, String> getPeerDependencies() {
		return peerDependencies;
	}

	public void setPeerDependencies(Map<String, String> peerDependencies) {
		this.peerDependencies = peerDependencies;
	}

	public Map<String, String> getOptionalDependencies() {
		return optionalDependencies;
	}

	public void setOptionalDependencies(Map<String, String> optionalDependencies) {
		this.optionalDependencies = optionalDependencies;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	public Map<String, String> getDirectories() {
		return directories;
	}

	public void setDirectories(Map<String, String> directories) {
		this.directories = directories;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public List<Map<String, String>> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<Map<String, String>> licenses) {
		this.licenses = licenses;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}	
	
	public static class Builder {
		private String name;
		private String version;
		private String description;
		private String author;
		private String homepage;
		private List<Map<String, String>> contributors;
		private Map<String, String> bin;
		private String man;
		private Map<String, String> scripts;
		private String main;
		private Map<String, String> repository;
		private Map<String, String> bugs;
		private List<String> keywords;
		private Map<String, String> dependencies;
		private Map<String, String> devDependencies;
		private Boolean preferGlobal;
		private Map<String, String> publishConfig;
		private String license;
		private Map<String, String> engines;
		private String os;
		private String cpu;
		private List<String> bundledDependencies;
		private Map<String, String> peerDependencies;
		private Map<String, String> optionalDependencies;
		private Map<String, String> config;
		private Map<String, String> directories;
		private List<String> files;
		private List<Map<String, String>> licenses;
		
		@SerializedName("private") private Boolean isPrivate;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder version(String version) {
			this.version = version;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder author(String author) {
			this.author = author;
			return this;
		}
		
		public Builder homepage(String homepage) {
			this.homepage = homepage;
			return this;
		}
		
		public Builder contributors(List<Map<String, String>> contributors) {
			this.contributors = contributors;
			return this;
		}
		
		public Builder bin(Map<String, String> bin) {
			this.bin = bin;
			return this;
		}
		
		public Builder man(String man) {
			this.man = man;
			return this;
		}
		
		public Builder scripts(Map<String, String> scripts) {
			this.scripts = scripts;
			return this;
		}
		
		public Builder main(String main) {
			this.main = main;
			return this;
		}
		
		public Builder repository(Map<String, String> repository) {
			this.repository = repository;
			return this;
		}
		
		public Builder bugs(Map<String, String> bugs) {
			this.bugs = bugs;
			return this;
		}
		
		public Builder keywords(List<String> keywords) {
			this.keywords = keywords;
			return this;
		}
		
		public Builder dependencies(Map<String, String> dependencies) {
			this.dependencies = dependencies;
			return this;
		}
		
		public Builder devDependencies(Map<String, String> devDependencies) {
			this.devDependencies = devDependencies;
			return this;
		}
		
		public Builder preferGlobal(Boolean preferGlobal) {
			this.preferGlobal = preferGlobal;
			return this;
		}
		
		public Builder publishConfig(Map<String, String> publishConfig) {
			this.publishConfig = publishConfig;
			return this;
		}
		
		public Builder license(String license) {
			this.license = license;
			return this;
		}
		
		public Builder engines(Map<String, String> engines) {
			this.engines = engines;
			return this;
		}
		
		public Builder os(String os) {
			this.os = os;
			return this;
		}
		
		public Builder cpu(String cpu) {
			this.cpu = cpu;
			return this;
		}
		
		public Builder bundledDependencies(List<String> bundledDependencies) {
			this.bundledDependencies = bundledDependencies;
			return this;
		}
		
		public Builder peerDependencies(Map<String, String> peerDependencies) {
			this.peerDependencies = peerDependencies;
			return this;
		}
		
		public Builder optionalDependencies(Map<String, String> optionalDependencies) {
			this.optionalDependencies = optionalDependencies;
			return this;
		}
		
		public Builder config(Map<String, String> config) {
			this.config = config;
			return this;
		}
		
		public Builder directories(Map<String, String> directories) {
			this.directories = directories;
			return this;
		}
		
		public Builder files(List<String> files) {
			this.files = files;
			return this;
		}
		
		public Builder licenses(List<Map<String, String>> licenses) {
			this.licenses = licenses;
			return this;
		}
		
		public Builder isPrivate(Boolean isPrivate) {
			this.isPrivate = isPrivate;
			return this;
		}
				
		public PackageJson build() {
			return new PackageJson(this);
		}
		
	}
	
	private PackageJson(Builder builder) {
		this.name = builder.name;
		this.version = builder.version;
		this.description = builder.description;
		this.homepage = builder.homepage;
		this.author = builder.author;
		this.contributors = builder.contributors;
		this.bin = builder.bin;
		this.man = builder.man;
		this.scripts = builder.scripts;
		this.main = builder.main;
		this.repository = builder.repository;
		this.bugs = builder.bugs;
		this.keywords = builder.keywords;
		this.dependencies = builder.dependencies;
		this.devDependencies = builder.devDependencies;
		this.preferGlobal = builder.preferGlobal;
		this.publishConfig = builder.publishConfig;
		this.license = builder.license;
		this.engines = builder.engines;
		this.os = builder.os;
		this.cpu = builder.cpu;
		this.bundledDependencies = builder.bundledDependencies;
		this.peerDependencies = builder.peerDependencies;
		this.optionalDependencies = builder.optionalDependencies;
		this.config = builder.config;
		this.directories = builder.directories;
		this.files = builder.files;
		this.licenses = builder.licenses;
		this.isPrivate = builder.isPrivate;
	}

	@Override
	public String toString() {
		return "PackageJson [name=" + name + " , version=" + version + " , description=" + description + " , homepage=" + homepage + " , author=" + author + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				" , contributors=" + contributors + " , bin=" + bin + " , scripts=" + scripts + " , main=" + main + " , repository=" + repository + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				" , bugs=" + bugs + " , keywords=" + keywords + " , dependencies=" + dependencies + " , devDependencies=" + devDependencies + " , preferGlobal=" + preferGlobal + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				" , publishConfig=" + publishConfig + " , license=" + license + " , engines=" + engines +  " , os=" + os +  " , cpu=" + cpu + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				" , bundledDependencies=" + bundledDependencies + " , peerDependencies=" + peerDependencies + " , optionalDependencies=" + optionalDependencies + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				" , config=" + config + " , directories=" + directories + " , files=" + files + " , licenses=" + licenses + " , isPrivate=" + isPrivate //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				+ "]";//$NON-NLS-1$

	}
}
