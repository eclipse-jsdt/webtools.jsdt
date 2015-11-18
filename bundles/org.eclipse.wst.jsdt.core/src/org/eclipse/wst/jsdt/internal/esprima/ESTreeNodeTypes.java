/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.esprima;

/**
 * @author Gorkem Ercan
 *
 */
@SuppressWarnings("nls")
public enum ESTreeNodeTypes {

	AssignmentExpression("AssignmentExpression", "left","right"),
	AssignmentPattern("AssignmentPattern", "left", "right"),
	ArrayExpression("ArrayExpression", "elements"),
    ArrayPattern("ArrayPattern", "elements"),
    ArrowFunctionExpression("ArrowFunctionExpression","params", "body"),
    AwaitExpression("AwaitExpression","argument"), //  ES7.
    BlockStatement("BlockStatement", "body"),
    BinaryExpression("BinaryExpression","left", "right"),
    BreakStatement("BreakStatement", "label"),
    CallExpression("CallExpression", "callee", "arguments"),
    CatchClause("CatchClause", "param", "body"),
    ClassBody("ClassBody", "body"),
    ClassDeclaration("ClassDeclaration","id", "superClass", "body"),
    ClassExpression("ClassExpression", "id", "superClass", "body"),
//    ComprehensionBlock("ComprehensionBlock", "left", "right"),  // ES7.
//    ComprehensionExpression("ComprehensionExpression", "blocks", "filter", "body"),  // ES7.
    ConditionalExpression("ConditionalExpression", "test", "consequent", "alternate"),
    ContinueStatement("ContinueStatement", "label"),
    DebuggerStatement("DebuggerStatement"),
    DirectiveStatement("DirectiveStatement"),
    DoWhileStatement("DoWhileStatement", "body", "test"),
    EmptyStatement("EmptyStatement"),
    ExportAllDeclaration("ExportAllDeclaration", "source"),
    ExportDefaultDeclaration("ExportDefaultDeclaration", "declaration"),
    ExportNamedDeclaration("ExportNamedDeclaration", "declaration", "specifiers", "source"),
    ExportSpecifier("ExportSpecifier", "exported", "local"),
    ExpressionStatement("ExpressionStatement", "expression"),
    ForStatement("ForStatement", "init", "test", "update", "body"),
    ForInStatement("ForInStatement", "left", "right", "body"),
    ForOfStatement("ForOfStatement","left", "right", "body"),
    FunctionDeclaration("FunctionDeclaration", "id", "params", "body"),
    FunctionExpression("FunctionExpression", "id", "params", "body"),
   // GeneratorExpression("GeneratorExpression", "blocks", "filter", "body"),  // ES7.
    Identifier("Identifier"),
	IfStatement("IfStatement", "test", "consequent", "alternate"),
	ImportDeclaration("ImportDeclaration", "specifiers", "source"),
	ImportDefaultSpecifier("ImportDefaultSpecifier", "local"),
	ImportNamespaceSpecifier("ImportNamespaceSpecifier", "local"),
	ImportSpecifier("ImportSpecifier", "imported", "local"),
	Literal("Literal"),
	LabeledStatement("LabeledStatement", "label", "body"),
	LogicalExpression("LogicalExpression", "left", "right"),
	MemberExpression("MemberExpression", "object", "property"),
	MetaProperty("MetaProperty", "meta", "property"),
	MethodDefinition("MethodDefinition", "key", "value"),
	ModuleSpecifier("ModuleSpecifier"),
	NewExpression("NewExpression", "callee", "arguments"),
	ObjectExpression("ObjectExpression", "properties"),
	ObjectPattern("ObjectPattern", "properties"),
	Program("Program", "body"),
	Property("Property", "key", "value"),
	RestElement("RestElement", "argument"),
	ReturnStatement("ReturnStatement", "argument"),
	SequenceExpression("SequenceExpression", "expressions"),
	SpreadElement("SpreadElement", "argument"),
	Super("Super"),
	SwitchStatement("SwitchStatement", "discriminant", "cases"),
	SwitchCase("SwitchCase", "test", "consequent"),
	TaggedTemplateExpression("TaggedTemplateExpression", "tag", "quasi"),
	TemplateElement("TemplateElement"),
	TemplateLiteral("TemplateLiteral", "quasis", "expressions"),
	ThisExpression("ThisExpression"),
	ThrowStatement("ThrowStatement", "argument"),
	TryStatement("TryStatement", "block", "handler", "finalizer"),
	UnaryExpression("UnaryExpression", "argument"),
	UpdateExpression("UpdateExpression", "argument"),
	VariableDeclaration("VariableDeclaration", "declarations"),
	VariableDeclarator("VariableDeclarator", "id", "init"),
	WhileStatement("WhileStatement", "test", "body"),
    WithStatement("WithStatement", "object", "body"),
    YieldExpression("YieldExpression", "argument");
	
	private final	String typeString;
	private final String[] visitorKeys;
	ESTreeNodeTypes(String type, String... visitorKeys){
		this.typeString = type;
		this.visitorKeys = visitorKeys;
	}
	public String getTypeString() {
		return typeString;
	}
	public String[] getVisitorKeys() {
		return visitorKeys;
	}
}
