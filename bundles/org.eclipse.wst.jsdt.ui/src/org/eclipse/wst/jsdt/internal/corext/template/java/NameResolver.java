/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sebastian Davids: sdavids@gmx.de - see bug 25376
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.template.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.jface.text.templates.TemplateVariableResolver;
import org.eclipse.wst.jsdt.internal.ui.text.template.contentassist.MultiVariable;

/**
 * Resolves template variables to non-conflicting names that adhere to the naming conventions and
 * match the parameter (fully qualified name).
 * 
 * 
 */
public class NameResolver extends TemplateVariableResolver {
	
	private final String fDefaultType;

	/**
	 * Default ctor for instantiation by the extension point.
	 */
	public NameResolver() {
		this("java.lang.Object"); //$NON-NLS-1$
	}
	
	NameResolver(String defaultType) {
		fDefaultType= defaultType;
	}

	/*
	 * @see org.eclipse.jface.text.templates.TemplateVariableResolver#resolve(org.eclipse.jface.text.templates.TemplateVariable, org.eclipse.jface.text.templates.TemplateContext)
	 */
	public void resolve(TemplateVariable variable, TemplateContext context) {
		List params= variable.getVariableType().getParams();
		String param;
		if (params.size() == 0)
			param= fDefaultType;
		else
			param= (String) params.get(0);
		JavaContext jc= (JavaContext) context;
		TemplateVariable ref= jc.getTemplateVariable(param);
		MultiVariable mv= (MultiVariable) variable;
		if (ref instanceof MultiVariable) {
			// reference is another variable
			MultiVariable refVar= (MultiVariable) ref;
			jc.addDependency(refVar, mv);
			
			refVar.getAllChoices();
			Object[] types= flatten(refVar.getAllChoices());
			for (int i= 0; i < types.length; i++) {
				String[] names= jc.suggestVariableNames(mv.toString(types[i]));
				mv.setChoices(types[i], names);
			}
			
			mv.setKey(refVar.getCurrentChoice());
			jc.markAsUsed(mv.getDefaultValue());
		} else {
			// reference is a Java type name
			jc.addImport(param);
			String[] names= jc.suggestVariableNames(param);
			mv.setChoices(names);
			jc.markAsUsed(names[0]);
		}
	}

	private Object[] flatten(Object[][] allValues) {
		List flattened= new ArrayList(allValues.length);
		for (int i= 0; i < allValues.length; i++) {
			flattened.addAll(Arrays.asList(allValues[i]));
		}
		return flattened.toArray(new Object[flattened.size()]);
	}
}
