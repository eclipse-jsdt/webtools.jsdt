/******************************************************************************* 
 * Copyright (c) 2016-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.wst.jsdt.integration.tests.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.core.resources.ExplorerItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.dialogs.ExplorerItemPropertyDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.wst.jsdt.ui.wizards.JavaProjectWizardFirstPage;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.eclipse.wst.jsdt.integration.tests.internal.wizard.js.NewJSFileWizardDialog;
import org.eclipse.wst.jsdt.integration.tests.internal.wizard.js.NewJSFileWizardPage;
import org.eclipse.wst.jsdt.integration.tests.internal.condition.TreeContainsItem;
import org.eclipse.wst.jsdt.integration.tests.internal.wizard.npm.NpmInitDialog;
import org.eclipse.wst.jsdt.integration.tests.internal.wizard.bower.BowerInitDialog;
import org.junit.runner.RunWith;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener;
import org.eclipse.debug.core.model.IProcess;

/**
 * TestBase Class for JST tests
 * 
 * @author Pavol Srna
 */
@RunWith(RedDeerSuite.class)
public class JSTTestBase {

	protected static final String PROJECT_NAME = "testProject";
	protected static final String JS_FILE = "main.js";
	protected static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	protected static final String BASE_DIRECTORY = WORKSPACE + "/" + PROJECT_NAME;

	protected void createJSProject() {
		createJSProject(PROJECT_NAME);
	}

	protected void createJSProject(String name) {
		JavaProjectWizardDialog jsDialog = new JavaProjectWizardDialog();
		jsDialog.open();
		JavaProjectWizardFirstPage jsPage = new JavaProjectWizardFirstPage();
		jsPage.setName(name);
		jsDialog.finish();
		assertTrue("Project not found", new ProjectExplorer().containsProject(name));
	}

	protected void cleanEditor() {
		cleanEditor(JS_FILE);
	}

	protected static void importExistingProject(String path) {

		ExternalProjectImportWizardDialog importDialog = new ExternalProjectImportWizardDialog();
		importDialog.open();
		WizardProjectsImportPage importPage = new WizardProjectsImportPage();
		try {
			importPage.setRootDirectory((new File(path).getCanonicalPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		importPage.copyProjectsIntoWorkspace(true);
		importDialog.finish();
	}

	protected void cleanEditor(String name) {
		DefaultEditor editor = new DefaultEditor(name);
		editor.activate();
		DefaultStyledText text = new DefaultStyledText();
		text.setText("");
		editor.save();
	}

	protected void createJSFile(String filename) {
		NewWizardDialog d = new NewJSFileWizardDialog();
		d.open();
		NewJSFileWizardPage p = new NewJSFileWizardPage();
		new LabeledText("Enter or select the parent folder:").setText(PROJECT_NAME);
		p.setFileName(filename);
		d.finish();
		assertTrue(filename + " not found!", new ProjectExplorer().getProject(PROJECT_NAME).containsItem(filename));
	}

	protected ExplorerItemPropertyDialog openProjectProperties() {
		return openProjectProperties(PROJECT_NAME);
	}

	protected ExplorerItemPropertyDialog openProjectProperties(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
		ExplorerItemPropertyDialog dialog = new ExplorerItemPropertyDialog(pe.getProject(projectName));
		dialog.open();
		Shell shell = new DefaultShell();
		assertThat(shell.getText(), is(dialog.getTitle()));

		return dialog;
	}

	protected static String getMisingString(List<String> current, List<String> expected) {
		StringBuffer sbMissing = new StringBuffer("");
		for (String expectedItem : expected) {
			if (!current.contains(expectedItem)) {
				if (sbMissing.length() != 0) {
					sbMissing.append(",");
				}
				sbMissing.append(expectedItem);
			}
		}
		return sbMissing.toString();
	}

	protected void npmInit() {
		npmInit(PROJECT_NAME);
	}

	protected static void npmInit(String projectName) {
		NpmInitDialog dialog = new NpmInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		dialog.finish();
		assertPackageJsonExists();
	}

	protected static void assertPackageJsonExists() {
		File packageJson = new File(BASE_DIRECTORY + "/package.json");
		assertTrue("package.json file does not exist", packageJson.exists());
	}

	protected static void npmIntall(String projectName) {
		npmInstall(projectName, null);
	}

	@SuppressWarnings("unchecked")
	protected ContextMenu runAsNodeJSAppMenu() {
		ContextMenu menu = new ContextMenu(new WithTextMatcher("Run As"),
				new RegexMatcher("(\\d+)( Node.js Application)"));
		return menu;
	}

	@SuppressWarnings("unchecked")
	protected ContextMenu debugAsNodeJSAppMenu(ExplorerItem item) {
		item.select();
		ContextMenu menu = new ContextMenu(new WithTextMatcher("Debug As"),
				new RegexMatcher("(\\d+)( Node.js Application)"));
		return menu;
	}

	@SuppressWarnings("unchecked")
	protected static void npmInstall(String projectName, String... dir) {
		new ProjectExplorer().getProject(projectName).select();
		if (dir != null) {
			new ProjectExplorer().getProject(projectName).getProjectItem(dir).select();
		}
		new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( npm Install)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected static void assertBowerJsonExists() {
		File bowerJson = new File(BASE_DIRECTORY + "/bower.json");
		assertTrue("bower.json file does not exist", bowerJson.exists());
	}
	
	protected static void bowerInit() {
		bowerInit(PROJECT_NAME);
	}

	protected static void bowerInit(String projectName) {
		BowerInitDialog dialog = new BowerInitDialog();
		dialog.open();
		new LabeledText("Base directory:").setText(BASE_DIRECTORY);
		dialog.finish();
		assertBowerJsonExists();
	}

	@SuppressWarnings("unchecked")
	protected static void bowerUpdate(String projectName) {
		new ProjectExplorer().getProject(projectName).select();
		new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Bower Update)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected static void bowerInstall(String projectName) {
		bowerInstall(projectName, null);
	}

	@SuppressWarnings("unchecked")
	protected static void bowerInstall(String projectName, String... dir) {
		new ProjectExplorer().getProject(projectName).select();
		if (dir != null) {
			new ProjectExplorer().getProject(projectName).getProjectItem(dir).select();
		}
		new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Bower Install)")).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected void setLineBreakpoint(TextEditor editor, int lineNumber) throws CoreException {
		editor.activate();
		IResource resource = (IResource) editor.getEditorPart().getEditorInput().getAdapter(IResource.class);
		JavaScriptDebugModel.createLineBreakpoint(resource, lineNumber, -1, -1, new HashMap<String, Object>(), true);
	}

	protected void doubleClickTreeItem(Tree tree, Matcher matcher) {
		List<TreeItem> items = tree.getAllItems();
		for (TreeItem i : items) {
			if (matcher.matches(i.getText())) {
				i.doubleClick();
			}
		}
	}

	protected void resume(Tree tree, Matcher matcher) {
		List<TreeItem> items = tree.getAllItems();
		for (TreeItem i : items) {
			if (matcher.matches(i.getText())) {
				i.select();
				new ContextMenu("Resume").select();
			}
		}
	}

	protected TreeItem getVariable(String name) {

		WorkbenchView variables = new WorkbenchView("Variables");
		variables.activate();
		DefaultTree variablesTree = new DefaultTree();

		TreeItem var = null;
		try {
			new WaitUntil(new TreeContainsItem(variablesTree, new WithTextMatcher(name), false));
		} catch (WaitTimeoutExpiredException e) {
			// not found
			return null;
		}
		List<TreeItem> vars = variablesTree.getItems();
		for (TreeItem i : vars) {
			if (i.getText().equals(name)) {
				var = i;
			}
		}
		return var;
	}

	public void terminatePrcs(IProcess[] prcs) {

		for (IProcess p : prcs) {
			try {
				p.terminate();
			} catch (DebugException e) {
				fail(e.getMessage());
			}
			assertTrue("Proces:" + p.getLabel() + " not terminated!", p.isTerminated());
		}
	}

	public static boolean portAvailable(int port) {
		try (Socket ignored = new Socket("localhost", port)) {
			return false;
		} catch (IOException ignored) {
			return true;
		}
	}

	public class NodeJSLaunchListener implements ILaunchesListener {

		private ILaunch launch;

		public ILaunch getNodeJSLaunch() {
			return launch;
		}

		@Override
		public void launchesAdded(ILaunch[] arg0) {
			this.launch = arg0[0];
		}

		@Override
		public void launchesChanged(ILaunch[] arg0) {

		}

		@Override
		public void launchesRemoved(ILaunch[] arg0) {

		}

	}
}
