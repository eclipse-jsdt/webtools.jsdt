/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.text.keyword.contentassist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class KeywordUtilities {

	private static KeywordUtilities instance;
	private final Set<String> keywords;

	private KeywordUtilities() {
		keywords = new HashSet<String>(27);
		keywords.add("break");      //$NON-NLS-1$
		keywords.add("case");       //$NON-NLS-1$
		keywords.add("catch");      //$NON-NLS-1$
		keywords.add("class");      //$NON-NLS-1$
		keywords.add("const");      //$NON-NLS-1$
		keywords.add("continue");   //$NON-NLS-1$
		keywords.add("debugger");   //$NON-NLS-1$
		keywords.add("default");    //$NON-NLS-1$
		keywords.add("delete");     //$NON-NLS-1$
		keywords.add("do");         //$NON-NLS-1$
		keywords.add("else");       //$NON-NLS-1$
		keywords.add("elseif");     //$NON-NLS-1$
		keywords.add("export");     //$NON-NLS-1$
		keywords.add("extends");    //$NON-NLS-1$
		keywords.add("finally");    //$NON-NLS-1$
		keywords.add("for");        //$NON-NLS-1$
		keywords.add("function");   //$NON-NLS-1$
		keywords.add("if");         //$NON-NLS-1$
		keywords.add("import");     //$NON-NLS-1$
		keywords.add("in");         //$NON-NLS-1$
		keywords.add("instanceof"); //$NON-NLS-1$
		keywords.add("let");        //$NON-NLS-1$
		keywords.add("new");        //$NON-NLS-1$
		keywords.add("return");     //$NON-NLS-1$
		keywords.add("super");      //$NON-NLS-1$
		keywords.add("static");     //$NON-NLS-1$
		keywords.add("switch");     //$NON-NLS-1$
		keywords.add("this");       //$NON-NLS-1$
		keywords.add("throw");      //$NON-NLS-1$
		keywords.add("try");        //$NON-NLS-1$
		keywords.add("typeof");     //$NON-NLS-1$
		keywords.add("var");        //$NON-NLS-1$
		keywords.add("void");       //$NON-NLS-1$
		keywords.add("while");      //$NON-NLS-1$
		keywords.add("with");       //$NON-NLS-1$
		keywords.add("yield");      //$NON-NLS-1$
	}

	public static KeywordUtilities getInstance() {
		if (instance == null) {
			instance = new KeywordUtilities();
		}
		return instance;
	}

	/**
	 * Returns whether string should be recognized as a keyword, regardless of case.
	 * @param string The string being tested.
	 * @return Whether string is a keyword.
	 */
	public boolean isKeyword(String string) {
		return keywords.contains(string.toLowerCase());
	}

	/**
	 * Returns the keywords whose prefix is string, regardless of case.
	 * @param string The string being tested.
	 * @return The keywords whose prefix is string.
	 */
	public List<String> getMatchingKeywords(String string) {
		return keywords.stream().filter(k -> k.startsWith(string.toLowerCase())).collect(Collectors.toList());
	}

	public Set<String> getKeywords() {
		return keywords;
	}

}
