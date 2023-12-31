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

package org.eclipse.wst.jsdt.internal.ui.text.java;


import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.ui.text.java.JavaContentAssistInvocationContext;


public class JavaCompletionProposal extends AbstractJavaCompletionProposal {

	/**
	 * Creates a new completion proposal. All fields are initialized based on the provided
	 * information.
	 * 
	 * @param replacementString the actual string to be inserted into the document
	 * @param replacementOffset the offset of the text to be replaced
	 * @param replacementLength the length of the text to be replaced
	 * @param image the image to display for this proposal
	 * @param displayString the string to be displayed for the proposal If set to <code>null</code>,
	 *        the replacement string will be taken as display string.
	 */
	public JavaCompletionProposal(String replacementString, int replacementOffset, int replacementLength, Image image, String displayString, int relevance) {
		this(replacementString, replacementOffset, replacementLength, image, displayString, relevance, false);
	}
	
	/**
	 * Creates a new completion proposal. All fields are initialized based on the provided
	 * information.
	 * 
	 * @param replacementString the actual string to be inserted into the document
	 * @param replacementOffset the offset of the text to be replaced
	 * @param replacementLength the length of the text to be replaced
	 * @param image the image to display for this proposal
	 * @param displayString the string to be displayed for the proposal If set to <code>null</code>,
	 *        the replacement string will be taken as display string.
	 * @param relevance the relevance
	 * @param inJavadoc <code>true</code> for a javadoc proposal
	 * 
	 */
	public JavaCompletionProposal(String replacementString, int replacementOffset, int replacementLength, Image image, String displayString, int relevance, boolean inJavadoc) {
		this(replacementString, replacementOffset, replacementLength, image, displayString, relevance, inJavadoc, null);
	}
	
	/**
	 * Creates a new completion proposal. All fields are initialized based on the provided
	 * information.
	 * 
	 * @param replacementString the actual string to be inserted into the document
	 * @param replacementOffset the offset of the text to be replaced
	 * @param replacementLength the length of the text to be replaced
	 * @param image the image to display for this proposal
	 * @param displayString the string to be displayed for the proposal If set to <code>null</code>,
	 *        the replacement string will be taken as display string.
	 * @param relevance the relevance
	 * @param inJavadoc <code>true</code> for a javadoc proposal
	 * @param invocationContext the invocation context of this completion proposal or <code>null</code> not available
	 * 
	 */
	public JavaCompletionProposal(String replacementString, int replacementOffset, int replacementLength, Image image, String displayString, int relevance, boolean inJavadoc, JavaContentAssistInvocationContext invocationContext) {
		super(invocationContext);
		Assert.isNotNull(replacementString);
		Assert.isTrue(replacementOffset >= 0);
		Assert.isTrue(replacementLength >= 0);

		setReplacementString(replacementString);
		setReplacementOffset(replacementOffset);
		setReplacementLength(replacementLength);
		setImage(image);
		setDisplayString(displayString == null ? replacementString : displayString);
		setRelevance(relevance);
		setCursorPosition(replacementString.length());
		setInJavadoc(inJavadoc);
		setSortString(displayString == null ? replacementString : displayString);
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.AbstractJavaCompletionProposal#isValidPrefix(java.lang.String)
	 */
	public boolean isValidPrefix(String prefix) {
		String word= getDisplayString();
		if (isInJavadoc()) {
			int idx = word.indexOf("{@link "); //$NON-NLS-1$
			if (idx==0) {
				word = word.substring(7);
			} else {
				idx = word.indexOf("{@value "); //$NON-NLS-1$
				if (idx==0) {
					word = word.substring(8);
				}
			}
		} else if (word.indexOf("this.") != -1) { //$NON-NLS-1$
			word= word.substring(word.indexOf("this.") + 5); //$NON-NLS-1$
		}
		return isPrefix(prefix, word);
	}
	
	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension3#getReplacementText()
	 */
	public CharSequence getPrefixCompletionText(IDocument document, int completionOffset) {
		String string= getReplacementString();
		int pos= string.indexOf('(');
		if (pos > 0)
			return string.subSequence(0, pos);
		else if (string.startsWith("this.")) //$NON-NLS-1$
			return string.substring(5);
		else
			return string;
	}
}
