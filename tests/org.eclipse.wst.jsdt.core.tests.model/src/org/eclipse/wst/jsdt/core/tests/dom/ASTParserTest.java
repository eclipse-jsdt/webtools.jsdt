/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.tests.dom;

import static org.junit.Assert.*;
import java.util.HashMap;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ASTParserTest { 

	AST ast;
	ASTParser parser;
	int API_LEVEL;

	public ASTParserTest() {
		this.API_LEVEL = AST.JLS3;
	}
	
	@Before
	public void setUp() throws Exception {
		ast = AST.newAST(this.API_LEVEL);
		parser = ASTParser.newParser(this.API_LEVEL);
	}
	
	@After
	public void tearDown() throws Exception {
		ast = null;
	}
	
	@Test
	public void testKConstants() {
		assertTrue(ASTParser.K_EXPRESSION == 1);
		assertTrue(ASTParser.K_STATEMENTS == 2);
		assertTrue(ASTParser.K_CLASS_BODY_DECLARATIONS == 4);
		assertTrue(ASTParser.K_COMPILATION_UNIT == 8);
	}

	@Test
	public void testSetting() {
		// for now, just slam some values in
	    parser.setKind(ASTParser.K_COMPILATION_UNIT);
	    parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
	    parser.setKind(ASTParser.K_EXPRESSION);
	    parser.setKind(ASTParser.K_STATEMENTS);
	    
	    parser.setSource(new char[0]);
	    parser.setSource((char[]) null);
	    parser.setSource((IJavaScriptUnit) null);
	    parser.setSource((IClassFile) null);
	    
	    parser.setResolveBindings(false);
	    parser.setResolveBindings(true);
	    
	    parser.setSourceRange(0, -1);
	    parser.setSourceRange(0, 1);
	    parser.setSourceRange(1, 0);
	    parser.setSourceRange(1, -1);
	    
	    parser.setWorkingCopyOwner(null);

	    parser.setUnitName(null);
	    parser.setUnitName("Foo.js"); //$NON-NLS-1$

	    parser.setProject(null);

	    parser.setFocalPosition(-1);
	    parser.setFocalPosition(0);

	    parser.setCompilerOptions(null);
	    parser.setCompilerOptions(new HashMap());
	}
}
