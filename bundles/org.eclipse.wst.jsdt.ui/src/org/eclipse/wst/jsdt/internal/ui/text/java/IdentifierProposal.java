/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.text.java.ScopedCodeAssistVisitor.Scope;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposal;

public class IdentifierProposal implements IJavaCompletionProposal, ICompletionProposalExtension2, ICompletionProposalExtension3, ICompletionProposalExtension4 {

	private IRegion fRegion;
	private List<IdentifierProposal> fields = new ArrayList<IdentifierProposal>();
	private IdentifierType type;
	private List<String> parameterNames = new ArrayList<String>();
	private String name;
	private boolean isGlobal = false;
	private IdentifierProposal parent;

	public IdentifierProposal(String name) {
		this.name = name;
	}

	public void setRegion(IRegion fRegion) {
		this.fRegion = fRegion;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#apply(org.eclipse.jface.text.IDocument)
	 */
	public void apply(IDocument document) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getSelection(org.eclipse.jface.text.IDocument)
	 */
	public Point getSelection(IDocument document) {
		// TODO Auto-generated method stub
		return new Point(fRegion.getOffset() + this.name.length(), 0);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getAdditionalProposalInfo()
	 */
	public String getAdditionalProposalInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDisplayString() {
		if (this.isGlobal) {
			return String.format("%s - Global", getProposalString()); //$NON-NLS-1$
		}
		return getProposalString();
	}

	public String getProposalString() {
		if (this.type == IdentifierType.FUNCTION) {
			return this.name + String.format("(%s)", getParameterString()); //$NON-NLS-1$
		} else {
			return this.name;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getImage()
	 */
	public Image getImage() {
		if (this.type == IdentifierType.FUNCTION) {
			return JavaPluginImages.get(JavaPluginImages.IMG_MISC_PUBLIC);
		}
		else {
			return JavaPluginImages.get(JavaPluginImages.IMG_FIELD_PUBLIC);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getContextInformation()
	 */
	public IContextInformation getContextInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension4#isAutoInsertable()
	 */
	public boolean isAutoInsertable() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension3#getInformationControlCreator()
	 */
	public IInformationControlCreator getInformationControlCreator() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension3#getPrefixCompletionText(org.eclipse.jface.text.IDocument, int)
	 */
	public CharSequence getPrefixCompletionText(IDocument document, int completionOffset) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension3#getPrefixCompletionStart(org.eclipse.jface.text.IDocument, int)
	 */
	public int getPrefixCompletionStart(IDocument document, int completionOffset) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension2#apply(org.eclipse.jface.text.ITextViewer, char, int, int)
	 */
	public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
		IDocument document = viewer.getDocument();
		try {
			document.replace(fRegion.getOffset(), offset - fRegion.getOffset(), getProposalString());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension2#selected(org.eclipse.jface.text.ITextViewer, boolean)
	 */
	public void selected(ITextViewer viewer, boolean smartToggle) {
		// TODO Auto-generated method stub

	}

	private String getParameterString() {
		if (parameterNames.isEmpty()) {
			return ""; //$NON-NLS-1$
		}
		StringBuilder str = new StringBuilder();
		for (String param : parameterNames) {
			str.append(param + ", "); //$NON-NLS-1$
		}
		return str.substring(0, str.length() - 2);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension2#unselected(org.eclipse.jface.text.ITextViewer)
	 */
	public void unselected(ITextViewer viewer) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension2#validate(org.eclipse.jface.text.IDocument, int, org.eclipse.jface.text.DocumentEvent)
	 */
	public boolean validate(IDocument document, int offset, DocumentEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposal#getRelevance()
	 */
	public int getRelevance() {
		// Set to be higher than keyword/template proposals
		return 1;
	}

	/**
	 * Adds an identifier to this identifier's fields.
	 * @param field The field to be added.
	 */
	public void addField(IdentifierProposal field) {
		this.fields.add(field);
	}

	/**
	 * Gets this identifier's name.
	 * @return This identifier's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets this identifier's fields.
	 * @return This identifier's fields.
	 */
	public List<IdentifierProposal> getFields() {
		return this.fields;
	}

	/**
	 * Gets this identifier's type.
	 * @param type This identifier's type.
	 */
	public void setType(IdentifierType type) {
		this.type = type;
	}

	/**
	 * Sets this identifier's parameter names.
	 * @param parameterNames This identifier's parameter names.
	 */
	public void setParameters(List<String> parameterNames) {
		this.parameterNames = parameterNames;
	}

	/**
	 * Sets the JSDoc for this identifier.
	 * @param jsdoc The JSDoc.
	 */
	public void setJSdoc(JSdoc jsdoc) {
		// TODO: Add Jsdoc
	}

	/**
	 * Updates the scope of this identifier.
	 * @param scopes
	 */
	public void updateScope(Stack<Scope> scopes) {
		this.isGlobal = scopes.size() == 1;
	}

	/**
	 * Adds a parent to this identifier should this identifier be a field of an object.
	 * @param parent The parent.
	 */
	public void addParent(IdentifierProposal parent) {
		this.parent = parent;
	}

	public IdentifierProposal getParent() {
		return parent;
	}

	public void setIsGlobal(boolean b) {
		isGlobal = b;
	}

	/**
	 * Returns this identifier's camelcase name. For instance, camelCaseName would return cCN.
	 * @return This identifier's camel case name.
	 */
	public String getCamelCaseName() {
		return this.getName().charAt(0) + getCaptialLetters(this.getName().substring(1));
	}

	/**
	 * Returns the input string's capital letters, in order.
	 */
	private String getCaptialLetters(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (Character.isUpperCase(s.charAt(i))) {
				sb.append(s.charAt(i));
			}
		}
		return sb.toString();
	}
}