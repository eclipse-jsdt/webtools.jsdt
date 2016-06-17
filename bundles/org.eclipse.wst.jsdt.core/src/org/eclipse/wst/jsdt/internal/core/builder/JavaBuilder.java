/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.builder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptModelMarker;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.compiler.ValidationParticipant;
import org.eclipse.wst.jsdt.internal.compiler.util.SimpleLookupTable;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.validation.internal.operations.ValidationBuilder;

@SuppressWarnings("restriction")
public class JavaBuilder extends ValidationBuilder {

IProject currentProject;
JavaProject javaProject;
IWorkspaceRoot workspaceRoot;
ValidationParticipant[] participants;
NameEnvironment nameEnvironment;
SimpleLookupTable binaryLocationsPerProject; // maps a project to its binary resources (output folders, class folders, zip/jar files)
public State lastState;
BuildNotifier notifier;
char[][] extraResourceFileFilters;
String[] extraResourceFolderFilters;
public static final String SOURCE_ID = "JSDT"; //$NON-NLS-1$

public static boolean DEBUG = false;

public static IMarker[] getProblemsFor(IResource resource) {
	try {
		if (resource != null && resource.exists()) {
			IMarker[] markers = resource.findMarkers(IJavaScriptModelMarker.JAVASCRIPT_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
			Set markerTypes = JavaModelManager.getJavaModelManager().validationParticipants.managedMarkerTypes();
			if (markerTypes.isEmpty()) return markers;
			ArrayList markerList = new ArrayList(5);
			for (int i = 0, length = markers.length; i < length; i++) {
				markerList.add(markers[i]);
			}
			Iterator iterator = markerTypes.iterator();
			while (iterator.hasNext()) {
				markers = resource.findMarkers((String) iterator.next(), false, IResource.DEPTH_INFINITE);
				for (int i = 0, length = markers.length; i < length; i++) {
					markerList.add(markers[i]);
				}
			}
			IMarker[] result;
			markerList.toArray(result = new IMarker[markerList.size()]);
			return result;
		}
	} catch (CoreException e) {
		// assume there are no problems
	}
	return new IMarker[0];
}

public static IMarker[] getTasksFor(IResource resource) {
	try {
		if (resource != null && resource.exists())
			return resource.findMarkers(IJavaScriptModelMarker.TASK_MARKER, false, IResource.DEPTH_INFINITE);
	} catch (CoreException e) {
		// assume there are no tasks
	}
	return new IMarker[0];
}

/**
 * Hook allowing to initialize some static state before a complete build iteration.
 * This hook is invoked during PRE_AUTO_BUILD notification
 */
public static void buildStarting() {
	// build is about to start
}

/**
 * Hook allowing to reset some static state after a complete build iteration.
 * This hook is invoked during POST_AUTO_BUILD notification
 */
public static void buildFinished() {
	BuildNotifier.resetProblemCounters();
}

public static void removeProblemsFor(IResource resource) {
	try {
		if (resource != null && resource.exists()) {
			resource.deleteMarkers(IJavaScriptModelMarker.JAVASCRIPT_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);

			// delete managed markers
			Set markerTypes = JavaModelManager.getJavaModelManager().validationParticipants.managedMarkerTypes();
			if (markerTypes.size() == 0) return;
			Iterator iterator = markerTypes.iterator();
			while (iterator.hasNext())
				resource.deleteMarkers((String) iterator.next(), false, IResource.DEPTH_INFINITE);
		}
	} catch (CoreException e) {
		// assume there were no problems
	}
}

public static void removeTasksFor(IResource resource) {
	try {
		if (resource != null && resource.exists())
			resource.deleteMarkers(IJavaScriptModelMarker.TASK_MARKER, false, IResource.DEPTH_INFINITE);
	} catch (CoreException e) {
		// assume there were no problems
	}
}

public static void removeProblemsAndTasksFor(IResource resource) {
	try {
		if (resource != null && resource.exists()) {
			resource.deleteMarkers(IJavaScriptModelMarker.JAVASCRIPT_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
			resource.deleteMarkers(IJavaScriptModelMarker.TASK_MARKER, false, IResource.DEPTH_INFINITE);

			// delete managed markers
			Set markerTypes = JavaModelManager.getJavaModelManager().validationParticipants.managedMarkerTypes();
			if (markerTypes.size() == 0) return;
			Iterator iterator = markerTypes.iterator();
			while (iterator.hasNext())
				resource.deleteMarkers((String) iterator.next(), false, IResource.DEPTH_INFINITE);
		}
	} catch (CoreException e) {
		// assume there were no problems
	}
}

public static State readState(IProject project, DataInputStream in) throws IOException {
	return State.read(project, in);
}

public static void writeState(Object state, DataOutputStream out) throws IOException {
	((State) state).write(out);
}

private static final IProject[] EMPTY_PROJECTS = new IProject[0];
public IProject[] build(int kind, Map ignored, IProgressMonitor monitor) {
	// We shouldn't run the validation builder again if validation builder exists on a project.
	if (!isValidationBuilderDefined()) {
		// Simply rely on the ValidationBuilder behavior
		return super.build(kind, ignored, monitor);
	}
	return EMPTY_PROJECTS;
}

private static final String VALIDATION_BUILDER = "org.eclipse.wst.validation.validationbuilder"; //$NON-NLS-1$
private boolean isValidationBuilderDefined() {
	try {
		ICommand[] spec = getProject().getDescription().getBuildSpec();
		if (spec != null) {
			for (ICommand cmd : spec) {
				if(VALIDATION_BUILDER.equals(cmd.getBuilderName())) {
					return true;
				}
			}
		}
	} catch (CoreException e) {
		// Cannot obtain description for the project - skip it
		return true;
	}
	return false;
}


protected void clean(IProgressMonitor monitor) throws CoreException {
	super.clean(monitor);
}

boolean filterExtraResource(IResource resource) {
	if (extraResourceFileFilters != null) {
		char[] name = resource.getName().toCharArray();
		for (int i = 0, l = extraResourceFileFilters.length; i < l; i++)
			if (CharOperation.match(extraResourceFileFilters[i], name, true))
				return true;
	}
	if (extraResourceFolderFilters != null) {
		IPath path = resource.getProjectRelativePath();
		String pathName = path.toString();
		int count = path.segmentCount();
		if (resource.getType() == IResource.FILE) count--;
		for (int i = 0, l = extraResourceFolderFilters.length; i < l; i++)
			if (pathName.indexOf(extraResourceFolderFilters[i]) != -1)
				for (int j = 0; j < count; j++)
					if (extraResourceFolderFilters[i].equals(path.segment(j)))
						return true;
	}
	return false;
}

public State getLastState(IProject project) {
	return (State) JavaModelManager.getJavaModelManager().getLastBuiltState(project, notifier.monitor);
}

boolean hasBuildpathErrors() throws CoreException {
//	IMarker[] markers = this.currentProject.findMarkers(IJavaScriptModelMarker.JAVASCRIPT_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_ZERO);
//	for (int i = 0, l = markers.length; i < l; i++)
//		if (markers[i].getAttribute(IJavaScriptModelMarker.CATEGORY_ID, -1) == CategorizedProblem.CAT_BUILDPATH)
//			return true;
	return false;
}

/*
 * Instruct the build manager that this project is involved in a cycle and
 * needs to propagate structural changes to the other projects in the cycle.
 */
void mustPropagateStructuralChanges() {
	HashSet cycleParticipants = new HashSet(3);
	javaProject.updateCycleParticipants(new ArrayList(), cycleParticipants, workspaceRoot, new HashSet(3), null);
	IPath currentPath = javaProject.getPath();
	Iterator i= cycleParticipants.iterator();
	while (i.hasNext()) {
		IPath participantPath = (IPath) i.next();
		if (participantPath != currentPath) {
			IProject project = workspaceRoot.getProject(participantPath.segment(0));
			if (hasBeenBuilt(project)) {
				if (DEBUG)
					System.out.println("Requesting another build iteration since cycle participant " + project.getName() //$NON-NLS-1$
						+ " has not yet seen some structural changes"); //$NON-NLS-1$
				needRebuild();
				return;
			}
		}
	}
}

/**
 * String representation for debugging purposes
 */
public String toString() {
	return currentProject == null
		? "JavaBuilder for unknown project" //$NON-NLS-1$
		: "JavaBuilder for " + currentProject.getName(); //$NON-NLS-1$
}
}
