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
package org.eclipse.wst.jsdt.internal.corext.template.java;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.internal.corext.util.Strings;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.ui.PreferenceConstants;


/**
 * A context for javadoc.
 */
public class JavaDocContext extends CompilationUnitContext {

	// tags
	private static final char HTML_TAG_BEGIN= '<';
	private static final char HTML_TAG_END= '>';
	private static final char JAVADOC_TAG_BEGIN= '@';	

	/**
	 * Creates a javadoc template context.
	 * 
	 * @param type the context type.
	 * @param document the document.
	 * @param completionOffset the completion offset within the document.
	 * @param completionLength the completion length within the document.
	 * @param compilationUnit the compilation unit (may be <code>null</code>).
	 */
	public JavaDocContext(TemplateContextType type, IDocument document, int completionOffset, int completionLength, IJavaScriptUnit compilationUnit) {
		super(type, document, completionOffset, completionLength, compilationUnit);
	}

	/**
	 * Creates a javadoc template context.
	 * 
	 * @param type the context type.
	 * @param document the document.
	 * @param completionPosition the position defining the completion offset and length 
	 * @param compilationUnit the compilation unit (may be <code>null</code>).
	 * 
	 */
	public JavaDocContext(TemplateContextType type, IDocument document, Position completionPosition, IJavaScriptUnit compilationUnit) {
		super(type, document, completionPosition, compilationUnit);
	}
	
	/*
	 * @see TemplateContext#canEvaluate(Template templates)
	 */
	public boolean canEvaluate(Template template) {
		String key= getKey();
		
		if (fForceEvaluation)
			return true;

		return
			template.matches(key, getContextType().getId()) &&
			(key.length() != 0) && template.getName().toLowerCase().startsWith(key.toLowerCase());
	}

	/*
	 * @see DocumentTemplateContext#getStart()
	 */ 
	public int getStart() {
		if (fIsManaged && getCompletionLength() > 0)
			return super.getStart();
		
		try {
			IDocument document= getDocument();

			if (getCompletionLength() == 0) {
				int start= getCompletionOffset();
		
				if ((start != 0) && (document.getChar(start - 1) == HTML_TAG_END))
					start--;
		
				while ((start != 0) && !Character.isWhitespace(document.getChar(start - 1)))
					start--;
				
				if ((start != 0) && !Character.isWhitespace(document.getChar(start - 1)))
					start--;
		
				// include html and javadoc tags
				if ((start != 0) && (
					(document.getChar(start - 1) == HTML_TAG_BEGIN) ||
					(document.getChar(start - 1) == JAVADOC_TAG_BEGIN)))
				{
					start--;
				}	
		
				return start;
				
			} 

			int start= getCompletionOffset();
			int end= getCompletionOffset() + getCompletionLength();
			
			while (start != 0 && !Character.isWhitespace(document.getChar(start - 1)))
				start--;
			
			while (start != end && Character.isWhitespace(document.getChar(start)))
				start++;
			
			if (start == end)
				start= getCompletionOffset();	
			
			return start;					
			

		} catch (BadLocationException e) {
			return getCompletionOffset();	
		}
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.corext.template.DocumentTemplateContext#getEnd()
	 */
	public int getEnd() {
		
		if (fIsManaged || getCompletionLength() == 0)		
			return super.getEnd();

		try {			
			IDocument document= getDocument();

			int start= getCompletionOffset();
			int end= getCompletionOffset() + getCompletionLength();
			
			while (start != end && Character.isWhitespace(document.getChar(end - 1)))
				end--;
			
			return end;	

		} catch (BadLocationException e) {
			return super.getEnd();
		}		
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.corext.template.DocumentTemplateContext#getKey()
	 */
	public String getKey() {

		if (getCompletionLength() == 0)		
			return super.getKey();

		try {
			IDocument document= getDocument();

			int start= getStart();
			int end= getCompletionOffset();
			return start <= end
				? document.get(start, end - start)
				: ""; //$NON-NLS-1$
			
		} catch (BadLocationException e) {
			return super.getKey();			
		}
	}

	/*
	 * @see TemplateContext#evaluate(Template)
	 */
	public TemplateBuffer evaluate(Template template) throws BadLocationException, TemplateException {
		TemplateTranslator translator= new TemplateTranslator();
		TemplateBuffer buffer= translator.translate(template);

		getContextType().resolve(buffer, this);
		
		IPreferenceStore prefs= JavaScriptPlugin.getDefault().getPreferenceStore();
		boolean useCodeFormatter= prefs.getBoolean(PreferenceConstants.TEMPLATES_USE_CODEFORMATTER);

		IJavaScriptProject project= getJavaProject();
		JavaFormatter formatter= new JavaFormatter(TextUtilities.getDefaultLineDelimiter(getDocument()), getIndentation(), useCodeFormatter, project);
		formatter.format(buffer, this);
			
		return buffer;
	}

	/**
	 * Returns the indentation level at the position of code completion.
	 * 
	 * @return the indentation level at the position of the code completion
	 */
	private int getIndentation() {
		int start= getStart();
		IDocument document= getDocument();
		try {
			IRegion region= document.getLineInformationOfOffset(start);
			String lineContent= document.get(region.getOffset(), region.getLength());
			IJavaScriptProject project= getJavaProject();
			return Strings.computeIndentUnits(lineContent, project);
		} catch (BadLocationException e) {
			return 0;
		}
	}	
}

