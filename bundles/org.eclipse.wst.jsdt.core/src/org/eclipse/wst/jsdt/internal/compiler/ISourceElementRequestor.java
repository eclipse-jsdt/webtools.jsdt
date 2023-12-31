/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler;

import org.eclipse.wst.jsdt.core.compiler.CategorizedProblem;

/*
 * Part of the source element parser responsible for building the output. It
 * gets notified of structural information as they are detected, relying on the
 * requestor to assemble them together, based on the notifications it got.
 *
 * The structural investigation includes: - package statement - import
 * statements - top-level types: package member, member types (member types of
 * member types...) - fields - methods
 *
 * If reference information is requested, then all source constructs are
 * investigated and type, field & method references are provided as well.
 *
 * Any (parsing) problem encountered is also provided.
 *
 * All positions are relative to the exact source fed to the parser.
 *
 * Elements which are complex are notified in two steps: - enter <Element> :
 * once the element header has been identified - exit <Element> : once the
 * element has been fully consumed
 *
 * other simpler elements (package, import) are read all at once: - accept
 * <Element>
 */

public interface ISourceElementRequestor {

	public static class TypeInfo {
		public int declarationStart;
		public int modifiers;
		public char[] name;
		public int nameSourceStart;
		public int nameSourceEnd;
		public char[] superclass;
		public char[][] categories;
		public boolean secondary;
		public boolean anonymousMember;
		public char[][] synonyms;
		
		/**
		 * <p>
		 * <code>true</code> if this type is a globally visible type,
		 * <code>false</code> otherwise.
		 * </p>
		 * 
		 * <p>
		 * EX: The anonymous type for a global variable would be globally
		 * visible, the anonymous type for a local would not.
		 * </p>
		 * 
		 * @since 1.1
		 */
		public boolean isIndexed;
	}

	public static class MethodInfo {
		public boolean isConstructor;
		public int declarationStart;
		public int modifiers;
		public char[] returnType;
		public char[] name;
		public int nameSourceStart;
		public int nameSourceEnd;
		public char[][] parameterTypes;
		public char[][] parameterNames;
		public char[] declaringType;
		public char[][] categories;
	}

	public static class FieldInfo {
		public int declarationStart;
		public int modifiers;
		public char[] type;
		public char[] name;
		public char[] declaringType;
		public int nameSourceStart;
		public int nameSourceEnd;
		public char[][] categories;
	}

	void acceptConstructorReference(char[] typeName, int argCount, int sourcePosition);

	void acceptFieldReference(char[] fieldName, int sourcePosition);
	/**
	 * @param declarationStart
	 *                   This is the position of the first character of the import
	 *                   keyword.
	 * @param declarationEnd
	 *                   This is the position of the ';' ending the import statement or
	 *                   the end of the comment following the import.
	 * @param tokens
	 *                   This are the tokens of the import like specified in the source.
	 * @param onDemand
	 *                   set to true if the import is an import on demand (e.g. import
	 *                   java.io.*). False otherwise.
	 */
	void acceptImport(int declarationStart, int declarationEnd, char[][] tokens, boolean onDemand);

	/**
	 * @param declarationStart
	 *                   This is the position of the first character of the export
	 *                   keyword.
	 * @param declarationEnd
	 *                   This is the position of the ';' ending the export statement or
	 *                   the end of the comment following the export.
	 * @param tokens
	 *                   This are the tokens of the export like specified in the source.
	 */
	void acceptExport(int declarationStart, int declarationEnd, char[][] tokens);	
	
	/*
	 * Table of line separator position. This table is passed once at the end of
	 * the parse action, so as to allow computation of normalized ranges.
	 *
	 * A line separator might corresponds to several characters in the source,
	 *
	 */
	void acceptLineSeparatorPositions(int[] positions);

	void acceptMethodReference(char[] methodName, int sourcePosition);

	void acceptProblem(CategorizedProblem problem);

	void acceptTypeReference(char[][] typeName, int sourceStart, int sourceEnd);

	void acceptTypeReference(char[] typeName, int sourcePosition);

	void acceptUnknownReference(char[][] name, int sourceStart, int sourceEnd);

	void acceptUnknownReference(char[] name, int sourcePosition);

	void enterCompilationUnit();

	void enterConstructor(MethodInfo methodInfo);

	void enterField(FieldInfo fieldInfo);

	void enterInitializer(int declarationStart, int modifiers);

	void enterMethod(MethodInfo methodInfo);

	void enterType(TypeInfo typeInfo);

	void exitCompilationUnit(int declarationEnd);

	void exitConstructor(int declarationEnd);

	/*
	 * initializationStart denotes the source start of the expression used for
	 * initializing the field if any (-1 if no initialization).
	 */
	void exitField(int initializationStart, int declarationEnd, int declarationSourceEnd);

	void exitInitializer(int declarationEnd);

	void exitMethod(int declarationEnd, int defaultValueStart, int defaultValueEnd);

	void exitType(int declarationEnd);
}
