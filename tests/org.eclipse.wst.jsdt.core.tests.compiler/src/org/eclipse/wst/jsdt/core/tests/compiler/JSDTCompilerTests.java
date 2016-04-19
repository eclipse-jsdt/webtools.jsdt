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
package org.eclipse.wst.jsdt.core.tests.compiler;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.tests.closure.ClosureCompilerTests;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.DualParseSyntaxErrorTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.FieldAccessCompletionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.JavadocCompletionParserTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.NameReferenceCompletionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.ReferenceTypeCompletionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.SelectionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.SelectionTest3;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.StatementRecoveryTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.SyntaxErrorTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.ASTImplTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.BasicJsdocTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.BasicParserTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.CharOperationTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.CompilerInvocationTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.InternalScannerTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.ScannerTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.UtilTest;
import org.eclipse.wst.jsdt.core.tests.compiler.util.ExclusionTests;
import org.eclipse.wst.jsdt.core.tests.interpret.BasicInterpretTest;
import org.eclipse.wst.jsdt.core.tests.search.SearchTests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Run all compiler regression tests
 */
public class JSDTCompilerTests extends TestSuite {

static {
	JavaScriptCore.getPlugin().getPluginPreferences().setValue("semanticValidation", true);
}

public JSDTCompilerTests() {
	this("JavaScript Model Tests");
}

public JSDTCompilerTests(String testName) {
	super(testName);
}
public static Test suite() {

	ArrayList standardTests = new ArrayList();
	
	// regression tests
	standardTests.add(ASTImplTests.class);
	standardTests.add(BasicJsdocTests.class);
	standardTests.add(BasicParserTests.class);
	standardTests.add(CharOperationTest.class);
	standardTests.add(CompilerInvocationTests.class);
	standardTests.add(InternalScannerTest.class);
	standardTests.add(ScannerTest.class);
	standardTests.add(UtilTest.class);
	//Disabled until inference and linter is back
//	standardTests.add(AssignmentTest.class);
//	standardTests.add(BasicAnalyseTests.class);
//	standardTests.add(BasicResolveTests.class);
//	standardTests.add(InferTypesTests.class);
//	standardTests.add(SwitchTest.class);
	
	// parser tests
	
	standardTests.add(DualParseSyntaxErrorTest.class);
	standardTests.add(FieldAccessCompletionTest.class);
	standardTests.add(JavadocCompletionParserTest.class);
	standardTests.add(NameReferenceCompletionTest.class);
	
	//disabled as they are more like linter tests
//	standardTests.add(ParserTest.class); 
	standardTests.add(ReferenceTypeCompletionTest.class);
	standardTests.add(SelectionTest.class);
	standardTests.add(StatementRecoveryTest.class);
	standardTests.add(SyntaxErrorTest.class);
	
	
	// interpret tests
	standardTests.add(BasicInterpretTest.class);
	
	
	TestSuite all = new TestSuite("JSDT 'Compiler' Tests");
	all.addTest(ExclusionTests.suite());
	all.addTest(SearchTests.suite());
	all.addTest(new JUnit4TestAdapter(ClosureCompilerTests.class));
	//disabled until codeSelect is fixed
//	all.addTest(new JUnit4TestAdapter( SelectionTest3.class));

	for (Iterator iter = standardTests.iterator(); iter.hasNext();) {
		Class test = (Class) iter.next();
		all.addTestSuite(test); 
	}
	
	return all;
} 
}
