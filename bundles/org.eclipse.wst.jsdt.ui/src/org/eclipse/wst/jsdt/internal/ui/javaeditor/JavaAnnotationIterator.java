/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.javaeditor;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.jface.text.quickassist.IQuickFixableAnnotation;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;


/**
 * Filters problems based on their types.
 */
public class JavaAnnotationIterator implements Iterator {

	private Iterator fIterator;
	private Annotation fNext;
	private boolean fSkipIrrelevants;
	private boolean fReturnAllAnnotations;

	/**
	 * Equivalent to <code>JavaAnnotationIterator(model, skipIrrelevants, false)</code>.
	 */
	public JavaAnnotationIterator(IAnnotationModel model, boolean skipIrrelevants) {
		this(model, skipIrrelevants, false);
	}

	/**
	 * Returns a new JavaAnnotationIterator.
	 * @param model the annotation model
	 * @param skipIrrelevants whether to skip irrelevant annotations
	 * @param returnAllAnnotations Whether to return non IJavaAnnotations as well
	 */
	public JavaAnnotationIterator(IAnnotationModel model, boolean skipIrrelevants, boolean returnAllAnnotations) {
		fReturnAllAnnotations= returnAllAnnotations;
		if (model != null)
			fIterator= model.getAnnotationIterator();
		else
			fIterator= Collections.EMPTY_LIST.iterator();
		fSkipIrrelevants= skipIrrelevants;
		skip();
	}

	private void skip() {
		while (fIterator.hasNext()) {
			Annotation next= (Annotation) fIterator.next();
			if (next instanceof IJavaAnnotation || next instanceof IQuickFixableAnnotation) {
				if (fSkipIrrelevants) {
					if (!next.isMarkedDeleted()) {
						fNext= next;
						return;
					}
				} else {
					fNext= next;
					return;
				}
			} else if (fReturnAllAnnotations) {
				fNext= next;
				return;
			}
		}
		fNext= null;
	}

	/*
	 * @see Iterator#hasNext()
	 */
	public boolean hasNext() {
		return fNext != null;
	}

	/*
	 * @see Iterator#next()
	 */
	public Object next() {
		try {
			return fNext;
		} finally {
			skip();
		}
	}

	/*
	 * @see Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
