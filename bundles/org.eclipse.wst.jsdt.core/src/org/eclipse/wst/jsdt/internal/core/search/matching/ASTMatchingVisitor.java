/*******************************************************************************
* Copyright (c) 2016 Red Hat, Inc.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* 	Contributors:
* 		 Red Hat Inc. - initial API and implementation and/or initial documentation
*******************************************************************************/

package org.eclipse.wst.jsdt.internal.core.search.matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.search.FieldDeclarationMatch;
import org.eclipse.wst.jsdt.core.search.FieldReferenceMatch;
import org.eclipse.wst.jsdt.core.search.LocalVariableDeclarationMatch;
import org.eclipse.wst.jsdt.core.search.MethodDeclarationMatch;
import org.eclipse.wst.jsdt.core.search.MethodReferenceMatch;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.TypeDeclarationMatch;
import org.eclipse.wst.jsdt.core.search.TypeReferenceMatch;
import org.eclipse.wst.jsdt.internal.core.search.indexing.IIndexConstants;
import org.eclipse.wst.jsdt.internal.core.util.Util;

/**
 * Given a {@link SearchPattern}, traverses an AST and compares
 * the pattern against its nodes, distinguishing the type of
 * JavaScript element being searched for, and whether the search is
 * for references or declarations. Supports matching ES6-style
 * class declarations. <p>
 *
 * For each search match found, finds the enclosing
 * {@link IJavaScriptElement} and creates a {@link SearchMatch}
 * that is stored in an internal list. This list can be accessed
 * via the {@link ASTMatchingVisitor#getMatches()} function, and
 * is stored until {@link ASTMatchingVisitor#clearMatches()} is
 * called. <p>
 *
 * Note that enclosing {@link IJavaScriptElement}s are reported
 * as children of the root {@link JavaScriptUnit} (i.e. no tree
 * structure is built), and elements that are at the top level
 * of the JavaScript file are reported as children of a fictitious
 * "(top level)" element (due to the way Eclipse displays search
 * results). These details mean that decorations on search results
 * shown in the search view are occasionally incorrect.
 */
public class ASTMatchingVisitor extends ASTVisitor {

	/**
	 * The pattern defining the current search. Used to find
	 * the term searched for, as well as element type and whether
	 * the search is for references or declarations.
	 */
	private SearchPattern pattern;

	/**
	 * The search participant for the current search. Required for
	 * constructing SearchMatches.
	 */
	private SearchParticipant participant;

	/**
	 * The root JavaScriptUnit for the entire file. Used to generate
	 * new IJavaScriptElements when reporting matches.
	 */
	private ITypeRoot rootJSUnit;

	/**
	 * The type of element being searched for. Stored mostly for
	 * convenience and readability, and used to end visits early
	 * when we know that a match is impossible.
	 */
	private int searchKind;

	private boolean isCaseSensitive;
	private boolean isCamelCase;

	/**
	 * An internal cache of matches found thus far. The creator of this
	 * class can obtain this list for reporting matches after the visiting
	 * is complete.
	 */
	private List<SearchMatch> matches;

	/**
	 * Stores the name of parent nodes to the current node. When
	 * e.g. a VariableDeclarationFragment is encountered, its name
	 * is pushed onto the stack, enabling elements lower in the AST
	 * to be correctly named. <p>
	 *
	 * It is used both to name functions assigned to variables and to
	 * find the name of the object that owns a field.
	 */
	private Stack<char[]> contextNames;

	/**
	 * Since it is not always possible to push a name on to contextNames,
	 * this int tracks the expected size of the stack. This lets us
	 * determine when it is appropriate to pop off of the contextNames
	 * stack, and when it is not.
	 */
	private int stackDepth;

	/**
	 * A set of field names (in the format "rootName.fieldName") that
	 * have been encountered thus far in parsing. This is used to
	 * try and report field references vs. declarations correctly.
	 */
	private Set<String> fieldNames;

	/**
	 * Maps names to superTypes to enable checking for ClassInstanceCreations.
	 */
	private Map<String, char[]> superTypeNames;

	/**
	 * String used to define top level elements in search results.
	 */
	private String ROOT_ELEMENT_NAME = "(top level)"; //$NON-NLS-1$

	/**
	 * String used to name anonymous functions in search results.
	 */
	private String ANONYMOUS_FUNCTION_NAME = "___anonymous"; //$NON-NLS-1$

	public ASTMatchingVisitor(SearchPattern pattern, SearchParticipant participant,
				ITypeRoot rootElement) {
		super();
		this.pattern = pattern;
		this.participant = participant;
		this.searchKind = ((InternalSearchPattern) pattern).kind;
		this.rootJSUnit = rootElement;
		this.matches = new ArrayList<>();

		if (pattern instanceof JavaSearchPattern) {
			this.isCaseSensitive = ((JavaSearchPattern) pattern).isCaseSensitive;
			this.isCamelCase = ((JavaSearchPattern) pattern).isCamelCase;
		} else {
			this.isCaseSensitive = false;
			this.isCamelCase = false;
		}
	}

	/**
	 * Returns the List of matches discovered so far. Does not clear the
	 * list of matches, and can be called repeatedly. <p>
	 *
	 * To clear the list, use {@link ASTMatchingVisitor#clearMatches()}
	 * @return the list of SearchMatches found.
	 */
	public List<SearchMatch> getMatches() {
		return matches;
	}

	/**
	 * Clears the internal list of matches found thus far.
	 */
	public void clearMatches() {
		this.matches = new ArrayList<>();
	}

	@Override
	public boolean visit(JavaScriptUnit node) {
		this.contextNames = new Stack<>();
		stackDepth = 0;
		fieldNames = new HashSet<>();
		superTypeNames = new HashMap<>();
		return true;
	}

	@Override
	public void endVisit(JavaScriptUnit node) {
		this.contextNames = null;
		this.fieldNames = null;
		this.superTypeNames = null;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		stackDepth++;

		char[] nodeName = node.getName().getIdentifier().toCharArray();
		char[] contextSuper = contextNames.isEmpty() ? new char[0] : contextNames.peek();
		contextNames.push(nodeName);

		if (searchKind != IIndexConstants.FIELD_PATTERN) {
			return true;
		}
		FieldPattern fieldPattern = (FieldPattern) pattern;
		if (!fieldPattern.findDeclarations) {
			return true;
		}
		char[] patternName = fieldPattern.name;
		char[] patternSuper = fieldPattern.getDeclaringSimpleName();

		if (matchesName(patternName, nodeName)
					&& (patternSuper == null || CharOperation.equals(patternSuper, contextSuper))) {
			IJavaScriptElement enclosingElement = getEnclosingElement(node);
			LocalVariableDeclarationMatch match = new LocalVariableDeclarationMatch(
						enclosingElement,
						SearchMatch.A_ACCURATE,
						node.getName().getStartPosition(),
						node.getName().getLength(),
						participant, rootJSUnit.getResource());
			matches.add(match);
		}
		if(node.getInitializer() != null ){
			node.getInitializer().accept(this);
		}
		return false;
	}

	@Override
	public void endVisit(VariableDeclarationFragment node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(ObjectLiteralField node) {

		stackDepth++;

		char[] nodeName = null;
		Expression name = node.getFieldName();
		if (name != null) {
			switch (name.getNodeType()) {
				case ASTNode.SIMPLE_NAME :
					nodeName = ((SimpleName) name).getIdentifier().toCharArray();
					break;
				case ASTNode.STRING_LITERAL :
					nodeName = ((StringLiteral) name).getEscapedValue().toCharArray();
					break;
			}
		} else {
			return true;
		}

		char[] nodeSuperName = contextNames.isEmpty() ? new char[0] : contextNames.peek();
		if (nodeName != null) {
			contextNames.push(nodeName);
		}
		if (searchKind != IIndexConstants.FIELD_PATTERN) {
			return true;
		}
		FieldPattern fieldPattern = (FieldPattern) pattern;
		if (!fieldPattern.findDeclarations) {
			return true;
		}

		if (fieldPattern.getDeclaringSimpleName() != null && !contextNames.isEmpty()
					&& !CharOperation.equals(nodeSuperName,
								fieldPattern.getDeclaringSimpleName(), false)) {
			return true;
		}

		char[] patternName = fieldPattern.name;
		if (matchesName(patternName, nodeName)) {
			IJavaScriptElement enclosingElement = getEnclosingElement(node);
			FieldDeclarationMatch match = new FieldDeclarationMatch(
						enclosingElement,
						SearchMatch.A_ACCURATE,
						node.getStartPosition(), node.getLength(),
						participant, rootJSUnit.getResource());
			matches.add(match);
		}

		isFieldDeclaration(nodeSuperName, nodeName);

		if (node.getInitializer() != null) {
			node.getInitializer().accept(this);
		}
		return false;
	}

	@Override
	public void endVisit(ObjectLiteralField node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(FunctionDeclaration node) {

		Expression name = node.getMethodName();
		char[] nodeName, contextName, patternName;
		if (name != null) {
			stackDepth++;
			if (name.getNodeType() == ASTNode.STRING_LITERAL) {
				// This is due to a bug in ASTParser
				Util.verbose("Encountered FunctionDeclaration node with " //$NON-NLS-1$
							+ "StringLiteral getMethodName(); this should not happen"); //$NON-NLS-1$
				return true;
			}
			nodeName = ((SimpleName) name).getIdentifier().toCharArray();
		} else {
			if (contextNames.isEmpty()) {
				return true; // Anonymous function; can't be matched.
			}
			nodeName = contextNames.pop();
		}
		contextName = !contextNames.isEmpty() ? contextNames.peek() : new char[0];
		contextNames.push(nodeName);

		switch (searchKind) {
			case IIndexConstants.METHOD_PATTERN :
				MethodPattern methodPattern = (MethodPattern) pattern;
				if (!methodPattern.findDeclarations) {
					return true;
				}

				if (methodPattern.getDeclaringSimpleName() != null
							&& !CharOperation.equals(contextName,
										methodPattern.getDeclaringSimpleName(), false)) {
					// Note we have to do a case-insensitive comparison here, because
					// methodPattern.getDeclaringSimpleName() is lowercased.
					return true;
				}

				patternName = methodPattern.selector;
				if (matchesName(patternName, nodeName)) {
					IJavaScriptElement enclosingElement = getEnclosingElement(node);
					MethodDeclarationMatch match = new MethodDeclarationMatch(
								enclosingElement,
								SearchMatch.A_ACCURATE,
								node.getStartPosition(), node.getLength(),
								participant, rootJSUnit.getResource());
					matches.add(match);
				}
				break;
			case IIndexConstants.CONSTRUCTOR_PATTERN :
				ConstructorPattern constrPattern = (ConstructorPattern) pattern;
				if (!node.isConstructor() || !constrPattern.findDeclarations) {
					return true;
				}
				patternName = constrPattern.getSearchPrefix();
				if (matchesName(patternName, contextName)) {
					IJavaScriptElement enclosingElement = getEnclosingElement(node);
					MethodDeclarationMatch match = new MethodDeclarationMatch(
								enclosingElement,
								SearchMatch.A_ACCURATE,
								node.getStartPosition(), node.getLength(),
								participant, rootJSUnit.getResource());
					matches.add(match);
				}
				break;
		}
		return true;
	}

	@Override
	public void endVisit(FunctionDeclaration node) {
		if (node.getMethodName() != null) {
			stackDepth--;
			if (stackDepth < contextNames.size()) {
				contextNames.pop();
			}
		}
	}

	@Override
	public boolean visit(FunctionInvocation node) {
		if ((searchKind & IIndexConstants.METHOD_PATTERN) == 0) {
			return true;
		}

		MethodPattern methodPattern = (MethodPattern) pattern;
		if (!methodPattern.findReferences) {
			return true;
		}
		String name, superName = null;
		if (node.getName() != null) {
			name = node.getName().getIdentifier();
		} else if (node.getExpression().getNodeType() == ASTNode.FIELD_ACCESS) {
			FieldAccess fieldAccess = (FieldAccess) node.getExpression();
			name = fieldAccess.getName().getIdentifier();
			if (fieldAccess.getExpression() != null 
						&& fieldAccess.getExpression().getNodeType() == ASTNode.SIMPLE_NAME) {
				superName = ((SimpleName) fieldAccess.getExpression()).getIdentifier();
			}
		} else {
			// Can't match an anonymous function.
			return true;
		}
		char[] nodeName = name.toCharArray();
		char[] nodeSuper;
		if (superName != null) {
			nodeSuper = superName.toCharArray();
		} else {
			nodeSuper = contextNames.isEmpty() ? new char[0] : contextNames.peek();
		}
		char[] patternName = methodPattern.selector;
		char[] patternSuper = methodPattern.getDeclaringSimpleName();
		
		if (patternSuper != null && !CharOperation.equals(nodeSuper, patternSuper, false)) {
			return true;
		}

		if (matchesName(patternName, nodeName)) {
			IJavaScriptElement enclosingElement = getEnclosingElement(node);
			MethodReferenceMatch match = new MethodReferenceMatch(
						enclosingElement,
						SearchMatch.A_ACCURATE,
						node.getStartPosition(), node.getLength(),
						false, false, false, participant,
						rootJSUnit.getResource());
			matches.add(match);
		}
		return true;
	}

	@Override
	public boolean visit(FieldAccess node) {
		if (searchKind != IIndexConstants.FIELD_PATTERN) {
			return true;
		}
		FieldPattern fieldPattern = (FieldPattern) pattern;

		char[] nodeName = node.getName().getIdentifier().toCharArray();
		char[] rootName = new char[0];
		Expression expression = node.getExpression();

		switch (expression.getNodeType()) {
			case ASTNode.SIMPLE_NAME :
				rootName = ((SimpleName) expression).getIdentifier().toCharArray();
				break;
			case ASTNode.THIS_EXPRESSION :
				rootName = contextNames.isEmpty() ? new char[0] : contextNames.peek();
				break;
			case ASTNode.FIELD_ACCESS :
				rootName = ((FieldAccess) expression).getName().getIdentifier().toCharArray();
				break;
		}

		if (fieldPattern.getDeclaringSimpleName() != null
				&& !CharOperation.equals(rootName, fieldPattern.getDeclaringSimpleName(), false)) {
			return true;
		}

		char[] patternName = fieldPattern.name;
		IJavaScriptElement enclosingElement = getEnclosingElement(node);

		if (matchesName(patternName, nodeName)) {
			boolean isDeclaration = isFieldDeclaration(rootName, nodeName);
			if (fieldPattern.findDeclarations && isDeclaration) {
				FieldDeclarationMatch match = new FieldDeclarationMatch(
							enclosingElement,
							SearchMatch.A_ACCURATE,
							node.getStartPosition(), node.getLength(),
							participant, rootJSUnit.getResource());
				matches.add(match);
			} else if (fieldPattern.findReferences && !isDeclaration) {
				FieldReferenceMatch match = new FieldReferenceMatch(
							enclosingElement,
							SearchMatch.A_ACCURATE,
							node.getStartPosition(), node.getLength(),
							true, true, false,
							participant, rootJSUnit.getResource());
				matches.add(match);
			}
		}
		if (patternName != null && matchesName(patternName, rootName)) {
			FieldReferenceMatch match = new FieldReferenceMatch(
						enclosingElement,
						SearchMatch.A_ACCURATE,
						node.getStartPosition(), node.getLength(),
						true, true, false,
						participant, rootJSUnit.getResource());
			matches.add(match);
		}
		if (node.getExpression() != null
					&& node.getExpression().getNodeType() != ASTNode.SIMPLE_NAME) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean visit(Assignment node) {
		stackDepth++;
		ASTNode lhs = node.getLeftHandSide();
		char[] elementName = null;
		switch (lhs.getNodeType()) {
			case ASTNode.SIMPLE_NAME :
				elementName = ((SimpleName) lhs).getIdentifier().toCharArray();
				break;
			case ASTNode.FIELD_ACCESS :
				elementName = ((FieldAccess) lhs).getName().getIdentifier().toCharArray();
				break;
		}
		lhs.accept(this);
		contextNames.push(elementName);
		node.getRightHandSide().accept(this);
		return false;
	}

	public void endVisit(Assignment node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(SimpleName node) {

		int parentNodeType = node.getParent().getNodeType();
		switch (parentNodeType) {
			case ASTNode.VARIABLE_DECLARATION_FRAGMENT :
			case ASTNode.FIELD_ACCESS :
				return false;
		}

		if (searchKind == IIndexConstants.FIELD_PATTERN) {
			FieldPattern fieldPattern = (FieldPattern) pattern;
			if (!fieldPattern.findReferences) return false;
			char[] nodeName = node.getIdentifier().toCharArray();
			char[] patternName = fieldPattern.name;
			if (matchesName(patternName, nodeName)) {
				IJavaScriptElement enclosingElement = getEnclosingElement(node);
				FieldReferenceMatch match = new FieldReferenceMatch(
							enclosingElement,
							SearchMatch.A_ACCURATE,
							node.getStartPosition(), node.getLength(),
							true, true, false,
							participant, rootJSUnit.getResource());
				matches.add(match);
			}
		}
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		stackDepth++;

		SimpleName name = node.getName();
		if (name == null) {
			return true;
		}
		char[] nodeName = name.getIdentifier().toCharArray();
		contextNames.push(nodeName);

		Expression superClass = node.getSuperclassExpression();
		char[] nodeSuperName = null;
		if (superClass != null && superClass.getNodeType() == ASTNode.SIMPLE_NAME) {
			nodeSuperName = ((SimpleName) superClass).getIdentifier().toCharArray();
		}

		if (nodeSuperName != null) {
			superTypeNames.put(new String(nodeName), nodeSuperName);
		}

		char[] patternName = new char[0];
		char[] superName = null;
		switch (searchKind) {
			// Type searches are handled strangely;
			case IIndexConstants.TYPE_DECL_PATTERN :
				TypeDeclarationPattern typeDecl = (TypeDeclarationPattern) pattern;
				patternName = typeDecl.simpleName;
				superName = typeDecl.qualification;
				break;
			case IIndexConstants.OR_PATTERN :
				// Searching for references or declarations is implemented as an OrPattern.
				OrPattern orPattern = (OrPattern) pattern;
				TypeDeclarationPattern typeDeclPattern =
							(TypeDeclarationPattern) orPattern.findPatternKind(
										IIndexConstants.TYPE_DECL_PATTERN);
				if (typeDeclPattern != null) {
					patternName = typeDeclPattern.simpleName;
					superName = typeDeclPattern.qualification;
					break;
				}
				return true;
			default :
				return true;
		}

		if (superName != null && !CharOperation.equals(superName, nodeSuperName, false)) {
			return true;
		}

		if (matchesName(patternName, nodeName)) {
			IJavaScriptElement enclosingElement = getEnclosingElement(node);
			TypeDeclarationMatch match = new TypeDeclarationMatch(
						enclosingElement,
						SearchMatch.A_ACCURATE,
						node.getName().getStartPosition(), node.getName().getLength(),
						participant, rootJSUnit.getResource());
			matches.add(match);
		}

		return true;
	}

	public void endVisit(TypeDeclaration node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		char[] patternName, patternSuperName;
		switch (searchKind) {
			case IIndexConstants.TYPE_REF_PATTERN :
				TypeReferencePattern typeRef = (TypeReferencePattern) pattern;
				patternName = typeRef.simpleName;
				patternSuperName = typeRef.qualification;
				break;
			case IIndexConstants.OR_PATTERN :
				// Searching for references or declarations is implemented as an OrPattern.
				OrPattern orPattern = (OrPattern) pattern;
				TypeReferencePattern typeRefPattern =
							(TypeReferencePattern) orPattern.findPatternKind(
										IIndexConstants.TYPE_REF_PATTERN);
				if (typeRefPattern != null) {
					patternName = typeRefPattern.simpleName;
					patternSuperName = typeRefPattern.qualification;
					break;
				}
				return true;
			case IIndexConstants.CONSTRUCTOR_PATTERN :
				ConstructorPattern constrPattern = (ConstructorPattern) pattern;
				if (!constrPattern.findReferences) {
					return true;
				}
				patternName = constrPattern.getSearchPrefix();
				patternSuperName = constrPattern.declaringQualification;
				break;
			default :
				return true;
		}

		Expression name = node.getMember();
		if (name == null || (name.getNodeType() != ASTNode.SIMPLE_NAME)) {
			return true;
		}
		char[] nodeName = ((SimpleName) name).getIdentifier().toCharArray();

		if (patternSuperName != null) {
			char[] nodeSuperName = superTypeNames.get(new String(nodeName));
			if (!CharOperation.equals(nodeSuperName, patternSuperName, false)){
				return true;
			}
		}

		if (matchesName(patternName, nodeName)) {
			IJavaScriptElement enclosingElement = getEnclosingElement(node);
			TypeReferenceMatch match = new TypeReferenceMatch(
						enclosingElement,
						SearchMatch.A_ACCURATE,
						node.getStartPosition(), node.getLength(), false,
						participant, rootJSUnit.getResource());
			matches.add(match);
		}

		return true;
	}

	/**
	 * Compares two names according to the rules defined in the current
	 * SearchPattern (e.g. case-sensitive, camelCase). Names provided are
	 * assumed to be simple (i.e. someName instead of someObject.someName)
	 * <p>
	 * Note that a camelCase comparison will fall through to a regular prefix
	 * comparison if it fails.
	 *
	 * @param patternName the name being searched for.
	 * @param nodeName the name of the current node.
	 * @return true if names match, false otherwise.
	 */
	private boolean matchesName(char[] patternName, char[] nodeName) {
		if (this.isCamelCase) {
			if (CharOperation.camelCaseMatch(patternName, nodeName)) {
				return true;
			}
		}
		if (this.isCaseSensitive) {
			return CharOperation.match(patternName, nodeName, true);
		} else {
			return CharOperation.match(CharOperation.toLowerCase(patternName), nodeName, false);
		}
	}

	/**
	 * Finds the closest IJavaScriptElement that encloses the provided node.
	 * This enclosing element is created as a child of the root IJavaScriptUnit
	 * for the current file (since the constructors required to build a hierarchy
	 * are inaccessible).
	 * <p>
	 * For top level elements (i.e. those whose parent is the root IJavaScriptUnit),
	 * the enclosing element is returned as a SourceField with name {@link ROOT_ELEMENT_NAME}
	 * since search results are difficult to read otherwise.
	 *
	 * @param node
	 * @return
	 */
	private IJavaScriptElement getEnclosingElement(ASTNode node) {
		ASTNode parent = node;
		String name;
		while(true) {
			parent = parent.getParent();
			switch (parent.getNodeType()) {
				case ASTNode.JAVASCRIPT_UNIT :
					return rootJSUnit.getField(ROOT_ELEMENT_NAME);

				case ASTNode.FUNCTION_DECLARATION :

					Expression methodName = ((FunctionDeclaration) parent).getMethodName();
					if (methodName != null) {
						name = methodName.toString();
						return rootJSUnit.getFunction(name, null);
					} else {
						// try to walk up the AST to find a variable
						name = ANONYMOUS_FUNCTION_NAME;
						ASTNode pparent = parent;
						for (int i = 0; i < 3; i++) {
							pparent = pparent.getParent();
							if (pparent == null) {
								break;
							} else if (pparent.getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
								name = ((VariableDeclarationFragment) pparent).getName().getIdentifier();
								break;
							} else if (pparent.getNodeType() == ASTNode.ASSIGNMENT) {
								ASTNode lhs = ((Assignment) pparent).getLeftHandSide();
								if (lhs.getNodeType() == ASTNode.SIMPLE_NAME) {
									name = ((SimpleName) lhs).getIdentifier();
								} else if (lhs.getNodeType() == ASTNode.FIELD_ACCESS) {
									name = ((FieldAccess) lhs).getName().getIdentifier();
								}
							}
						}
						// We have to return the enclosing element as a field instead of a method,
						// because the searchResult is compared against the outline later, and this
						// type of declaration appears as a variable there.
						return rootJSUnit.getField(name);
					}

				case ASTNode.VARIABLE_DECLARATION_FRAGMENT :

					name = ((VariableDeclarationFragment) parent).getName().getIdentifier();
					return rootJSUnit.getField(name);

				case ASTNode.OBJECT_LITERAL_FIELD :

					name = ((ObjectLiteralField) parent).getFieldName().toString();
					return rootJSUnit.getField(name);

				case ASTNode.TYPE_DECLARATION :

					name = ((TypeDeclaration) parent).getName().getIdentifier();
					return rootJSUnit.getType(name);

				case ASTNode.IMPORT_DECLARATION :
					continue;

				case ASTNode.EXPORT_DECLARATION :
					continue;
			}
		}
	}

	/**
	 * Checks whether the current names have been used to declare a field
	 * earlier in the visiting of this AST. Since object properties can be
	 * defined anywhere in JavaScript, this is necessary to properly find
	 * field declarations versus field references.
	 * <p>
	 * Parameters are intended to represent objectName.propName
	 *
	 * @param sName
	 * @param propName
	 * @return true if this is the first time objectName.propName has been
	 *         encountered, false otherwise.
	 */
	private boolean isFieldDeclaration(char[] objectName, char[] propName) {
		char[] fullNameArray;
		String fullName;

		if (objectName == null) {
			objectName = new char[0];
		}

		fullNameArray = new char[propName.length + objectName.length + 1];
		System.arraycopy(objectName, 0, fullNameArray, 0, objectName.length);
		fullNameArray[objectName.length] = '.';
		System.arraycopy(propName, 0, fullNameArray, objectName.length+1, propName.length);
		fullName = new String(fullNameArray);
		if (fieldNames.contains(fullName)) {
			return false;
		} else {
			fieldNames.add(fullName);
			return true;
		}
	}
}
