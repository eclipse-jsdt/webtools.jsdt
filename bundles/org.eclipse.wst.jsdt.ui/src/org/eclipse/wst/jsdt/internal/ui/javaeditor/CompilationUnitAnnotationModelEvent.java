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
package org.eclipse.wst.jsdt.internal.ui.javaeditor;



import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;

/**
 * Event sent out by changes of the compilation unit annotation model.
 */
public class CompilationUnitAnnotationModelEvent  extends AnnotationModelEvent {

	private boolean fIncludesProblemMarkerAnnotations;
	private IResource fUnderlyingResource;

	/**
	 * Constructor for CompilationUnitAnnotationModelEvent.
	 * @param model
	 * @param underlyingResource The annotation model's underlying resource
	 */
	public CompilationUnitAnnotationModelEvent(IAnnotationModel model, IResource underlyingResource) {
		super(model);
		fUnderlyingResource= underlyingResource;
		fIncludesProblemMarkerAnnotations= false;
	}

	private void testIfProblemMarker(Annotation annotation) {
		if (fIncludesProblemMarkerAnnotations) {
			return;
		}
		if (annotation instanceof JavaMarkerAnnotation) {
			fIncludesProblemMarkerAnnotations= ((JavaMarkerAnnotation) annotation).isProblem();
		} else if (annotation instanceof MarkerAnnotation) {
			try {
				IMarker marker= ((MarkerAnnotation) annotation).getMarker();
				if (!marker.exists() || marker.isSubtypeOf(IMarker.PROBLEM)) {
					fIncludesProblemMarkerAnnotations= true;
				}
			} catch (CoreException e) {
				JavaScriptPlugin.log(e);
			}
		}
	}

	/*
	 * @see org.eclipse.jface.text.source.AnnotationModelEvent#annotationAdded(org.eclipse.jface.text.source.Annotation)
	 */
	public void annotationAdded(Annotation annotation) {
		super.annotationAdded(annotation);
		testIfProblemMarker(annotation);
	}


	/*
	 * @see org.eclipse.jface.text.source.AnnotationModelEvent#annotationRemoved(org.eclipse.jface.text.source.Annotation)
	 */
	public void annotationRemoved(Annotation annotation) {
		super.annotationRemoved(annotation);
		testIfProblemMarker(annotation);
	}

	/*
	 * @see org.eclipse.jface.text.source.AnnotationModelEvent#annotationRemoved(org.eclipse.jface.text.source.Annotation, org.eclipse.jface.text.Position)
	 */
	public void annotationRemoved(Annotation annotation, Position position) {
		super.annotationRemoved(annotation, position);
		testIfProblemMarker(annotation);
	}

	/*
	 * @see org.eclipse.jface.text.source.AnnotationModelEvent#annotationChanged(org.eclipse.jface.text.source.Annotation)
	 */
	public void annotationChanged(Annotation annotation) {
		testIfProblemMarker(annotation);
		super.annotationChanged(annotation);
	}

	/**
	 * Returns whether the change included problem marker annotations.
	 *
	 * @return <code>true</code> if the change included marker annotations
	 */
	public boolean includesProblemMarkerAnnotationChanges() {
		return fIncludesProblemMarkerAnnotations;
	}

	/**
	 * Returns the annotation model's underlying resource
	 */
	public IResource getUnderlyingResource() {
		return fUnderlyingResource;
	}

}
