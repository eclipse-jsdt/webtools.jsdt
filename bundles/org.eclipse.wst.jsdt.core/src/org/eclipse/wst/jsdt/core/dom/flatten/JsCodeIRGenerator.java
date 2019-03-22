/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom.flatten;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.core.UnimplementedException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayName;
import org.eclipse.wst.jsdt.core.dom.ArrowFunctionExpression;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.AssignmentName;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BooleanLiteral;
import org.eclipse.wst.jsdt.core.dom.BreakStatement;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.CharacterLiteral;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ContinueStatement;
import org.eclipse.wst.jsdt.core.dom.DebuggerStatement;
import org.eclipse.wst.jsdt.core.dom.DoStatement;
import org.eclipse.wst.jsdt.core.dom.EmptyStatement;
import org.eclipse.wst.jsdt.core.dom.ExportDeclaration;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.ForInStatement;
import org.eclipse.wst.jsdt.core.dom.ForOfStatement;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.ImportDeclaration;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LabeledStatement;
import org.eclipse.wst.jsdt.core.dom.LineComment;
import org.eclipse.wst.jsdt.core.dom.ListExpression;
import org.eclipse.wst.jsdt.core.dom.MetaProperty;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.core.dom.ModuleSpecifier;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.NullLiteral;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.ObjectName;
import org.eclipse.wst.jsdt.core.dom.ParenthesizedExpression;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.RegularExpressionLiteral;
import org.eclipse.wst.jsdt.core.dom.RestElementName;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.SpreadElement;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.SuperMethodInvocation;
import org.eclipse.wst.jsdt.core.dom.SwitchCase;
import org.eclipse.wst.jsdt.core.dom.SwitchStatement;
import org.eclipse.wst.jsdt.core.dom.TemplateElement;
import org.eclipse.wst.jsdt.core.dom.TemplateLiteral;
import org.eclipse.wst.jsdt.core.dom.ThisExpression;
import org.eclipse.wst.jsdt.core.dom.ThrowStatement;
import org.eclipse.wst.jsdt.core.dom.TryStatement;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.UndefinedLiteral;
import org.eclipse.wst.jsdt.core.dom.VariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.WhileStatement;
import org.eclipse.wst.jsdt.core.dom.WithStatement;
import org.eclipse.wst.jsdt.core.dom.YieldExpression;

/**
 * Generator that creates internal representation of the AST suitable for
 * further processing (generating source code). For now the vocabulary of the
 * internal representation language is rather small, though it's enough to
 * describe JS source code. In case if more complex emit processing is
 * required additional elements can be added.
 *
 * It's not complete, some elements like import and export require work;
 * future modifications according to the upcoming changes in the ASTDom model
 * will be done as well.
 *
 * @author Eugene Melekhov
 * @since 2.0
 *
 */
@SuppressWarnings("nls")
public class JsCodeIRGenerator extends ASTVisitor {

	/**
	 *
	 * Basic class for code elements - building blocks used to create internal
	 * representation. Derived classes must implement <code>emit</code>
	 * function which outputs element presentation to the output stream.
	 *
	 */
	public static abstract class JsCodeElement {

		/**
		 * Emit element to given stream
		 *
		 * @param out
		 */
		public abstract void emit(JsCodeOutputStream out);

	}

	/**
	 * Generate the presentation of the given node
	 *
	 * @param node
	 *            node to generate
	 * @return Code Element that represents given node
	 */
	public JsCodeElement generate(ASTNode node) {
		return v(node);
	}

	/**
	 * Factory for JsCode elements
	 */
	protected IJsCodeElementFactory factory;

	/**
	 * "accumulator register" to pass return value from <code>visitor</code>
	 * functions. Since visitor supposed to return boolean value to it's
	 * caller we use this method to pass "real" return values. Visitor
	 * function that wants to return value should set it upon return.
	 */
	protected JsCodeElement value = null;

	/**
	 * Creates BasicJsCodeGenerator with provided factory which is used to
	 * create elements.
	 *
	 * @param factory
	 *            factory creating program elements
	 */
	public JsCodeIRGenerator(IJsCodeElementFactory factory) {
		this.factory = factory;
	}

	@Override
	public boolean visit(Modifier node) {
		value = token(node.getKeyword().toString());
		return false;
	}

	@Override
	public boolean visit(ArrayAccess node) {
		value = seqVa(v(node.getArray()), brack(v(node.getIndex())));
		return false;
	}

	@Override
	public boolean visit(ArrayInitializer node) {
		value = brack(seqCsMap(node.expressions()));
		return false;
	}

	@Override
	public boolean visit(Assignment node) {
		JsCodeElement lhs = parenOpt(ASSIGNMENT, node.getLeftHandSide());
		JsCodeElement rhs = parenOpt(ASSIGNMENT, node.getRightHandSide());
		value = seqVa(lhs, token(node.getOperator().toString()), rhs);
		return false;
	}

	@Override
	public boolean visit(Block node) {
		value = braces(seqMap(node.statements()));
		return false;
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		value = token(node.booleanValue() ? "true" : "false");
		return false;
	}

	@Override
	public boolean visit(BreakStatement node) {
		value = seqVa(token("break"), v(node.getLabel()), semiOpt());
		return false;
	}

	@Override
	public boolean visit(CatchClause node) {
		value = seqVa(token("catch"), paren(v(node.getException())), v(node.getBody()));
		return false;
	}

	@Override
	public boolean visit(RegularExpressionLiteral node) {
		value = token(node.getRegularExpression());
		return false;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		value = seqVa(token("new"), v(node.getMember()), paren(seqCsMap(node.arguments())));
		return false;
	}

	@Override
	public boolean visit(JavaScriptUnit node) {
		List<JsCodeElement> l = map(node.imports());
		l.addAll(map(node.exports()));
		l.addAll(map(node.statements()));
		value = seq(l);
		return false;
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		value = seqVa(parenOpt(LOGICAL_OR, node.getExpression()), token("?"), parenOpt(ASSIGNMENT, node.getThenExpression()), token(":"), parenOpt(ASSIGNMENT, node.getElseExpression()));
		return false;
	}

	@Override
	public boolean visit(ContinueStatement node) {
		value = seqVa(token("continue"), v(node.getLabel()), semiOpt());
		return false;
	}

	@Override
	public boolean visit(DoStatement node) {
		value = seqVa(token("do"), v(node.getBody()), token("while"), paren(v(node.getExpression())), semiOpt());
		return false;
	}

	@Override
	public boolean visit(EmptyStatement node) {
		value = semi();
		return false;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		value = v(node.getExpression());

		// TODO (Eugene Melekhov) it's not very elegant, but I don't know
		// other way to do it yet
		if (node.getParent() != null) {
			int nt = node.getParent().getNodeType();
			if (nt != ASTNode.FOR_IN_STATEMENT && nt != ASTNode.FOR_OF_STATEMENT) {
				value = seqVa(value, semiOpt());
			}
		}
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		value = seqVa(v(node.getExpression()), token("."), v(node.getName()));
		return false;
	}

	@Override
	public boolean visit(ForStatement node) {

		// Handle "in" operator. Is there better way to do it?
		List<JsCodeElement> initializers = new ArrayList<>();
		for (ASTNode initializer : (List<ASTNode>) node.initializers()) {
			JsCodeElement element = v(initializer);
			if (element != null) {
				if (initializer.getNodeType() == ASTNode.INFIX_EXPRESSION && ("in".equals(((InfixExpression) initializer).getOperator().toString()))) {
					initializers.add(paren(element));
				}
				else {
					initializers.add(element);
				}
			}
		}
		value = seqVa(token("for"), paren(seqVa(seqCs(initializers), semi(), v(node.getExpression()), semi(), seqCsMap(node.updaters()))), v(node.getBody()));
		return false;
	}

	@Override
	public boolean visit(ForInStatement node) {
		// TODO check correct "let" etc in left part
		JsCodeIRGenerator.JsCodeElement left = v(node.getIterationVariable());
		JsCodeIRGenerator.JsCodeElement right = v(node.getCollection());
		value = seqVa(token("for"), paren(seqVa(left, token("in"), right)), v(node.getBody()));
		return false;
	}

	@Override
	public boolean visit(ForOfStatement node) {
		// TODO check correct "let" etc in left part
		JsCodeIRGenerator.JsCodeElement left = v(node.getIterationVariable());
		JsCodeIRGenerator.JsCodeElement right = v(node.getCollection());
		// TODO make space before/after "of" optional depending on context
		value = seqVa(token("for"), paren(seqVa(left, token("of"), right)), v(node.getBody()));
		return false;
	}

	@Override
	public boolean visit(IfStatement node) {
		List<JsCodeElement> result = new ArrayList<>();
		result.add(token("if"));
		result.add(paren(v(node.getExpression())));
		result.add(v(node.getThenStatement()));

		// TODO handle empty statements more accurate
		JsCodeElement alternate = v(node.getElseStatement());
		if (alternate != null) {
			result.add(token("else"));
			result.add(alternate);
		}
		value = seq(result);
		return false;
	}

	@Override
	public boolean visit(InfixExpression node) {
		int precedence = expressionPrecedence(node);
		List<JsCodeElement> result = new ArrayList<>();
		result.add(parenOpt(precedence, node.getLeftOperand()));
		result.add(token(node.getOperator().toString()));
		result.add(parenOptRight(precedence, node.getRightOperand()));
		final List<ASTNode> extendedOperands = node.extendedOperands();
		if (extendedOperands.size() != 0) {
			for (ASTNode e : extendedOperands) {
				result.add(token(node.getOperator().toString()));
				result.add(parenOpt(precedence, e));
			}
		}

		value = seq(result);
		return false;
	}

	public boolean visit(JSdoc node) {
		value = seqVa(token("/** "), seqMap(node.tags()), token(" */"));
		return false;
	}

	public boolean visit(LineComment node) {
		value = seqVa(token("//"), token("\n"));
		return false;
	}

	@Override
	public boolean visit(LabeledStatement node) {
		value = seqVa(v(node.getLabel()), token(":"), v(node.getBody()));
		return false;
	}

	@Override
	public boolean visit(ListExpression node) {
		value = seqCsMap(node.expressions());
		return false;
	}

	@Override
	public boolean visit(FunctionDeclaration node) {
		List<JsCodeElement> result = new ArrayList<>();
		result.add(v(node.getJavadoc()));
		result.add(seqMap(node.modifiers()));

		if (!node.isConstructor() && !isGetterOrSetter(node.modifiers())) {
			result.add(token("function" + (node.isGenerator() ? "*" : "")));
		}

		result.add(v(node.getMethodName()));
		result.add(paren(seqCsMap(node.parameters())));

		// TODO What is this?
		for (int i = 0; i < node.getExtraDimensions(); i++) {
			result.add(token("[]"));
		}

		result.add(v(node.getBody()));
		value = seq(result);
		return false;
	}

	@Override
	public boolean visit(FunctionInvocation node) {
		List<JsCodeElement> result = new ArrayList<>();

		if (node.getExpression() != null) {
			result.add(v(node.getExpression()));
			if (node.getName() != null) {
				result.add(token("."));
			}
		}

		// TODO What is this
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
				result.add(token("<"));
				result.add(seqCsMap(node.typeArguments()));
				result.add(token(">"));
			}
		}

		result.add(v(node.getName()));
		result.add(paren(seqCsMap(node.arguments())));

		value = seq(result);
		return false;
	}

	@Override
	public boolean visit(NullLiteral node) {
		value = token("null");
		return false;
	}

	@Override
	public boolean visit(UndefinedLiteral node) {
		value = token("undefined");
		return false;
	}

	@Override
	public boolean visit(NumberLiteral node) {
		value = token(node.getToken());
		return false;
	}

	@Override
	public boolean visit(PostfixExpression node) {
		value = seqVa(v(node.getOperand()), token(node.getOperator().toString()));
		return false;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		value = seqVa(token(node.getOperator().toString()), parenOpt(PREFIX, node.getOperand()));
		return false;
	}

	@Override
	public boolean visit(ReturnStatement node) {
		value = seqVa(token("return"), v(node.getExpression()), semiOpt());
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		value = token(node.getIdentifier());
		return false;
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		// TODO Require some clarification. What is the Type for example
		List<JsCodeIRGenerator.JsCodeElement> result = new ArrayList<>();
		result.add(v(node.getType()));
		if (node.isVarargs()) {
			result.add(token("..."));
		}
		result.add(v(node.getPattern()));
		for (int i = 0; i < node.getExtraDimensions(); i++) {
			result.add(token("[]"));
		}
		if (node.getInitializer() != null) {
			result.add(token("="));
			result.add(v(node.getInitializer()));
		}
		value = seq(result);
		return false;
	}

	@Override
	public boolean visit(StringLiteral node) {
		value = token(node.getEscapedValue());
		return false;
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		List<JsCodeElement> args = map(node.arguments());
		value = seqVa(token("super"), paren(seqCs(args)));
		return false;
	}

	@Override
	public boolean visit(SwitchCase node) {
		value = seqVa(node.isDefault() ? token("default") : seqVa(token("case"), v(node.getExpression())), token(":"));
		return false;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		value = seqVa(token("switch"), paren(v(node.getExpression())), braces(seqMap(node.statements())));
		return false;
	}

	@Override
	public boolean visit(ThisExpression node) {
		value = token("this");
		return false;
	}

	@Override
	public boolean visit(ThrowStatement node) {
		value = seqVa(token("throw"), v(node.getExpression()), semiOpt());
		return false;
	}

	@Override
	public boolean visit(TryStatement node) {
		value = seqVa(token("try"), v(node.getBody()), seqMap(node.catchClauses()), (node.getFinally() != null ? seqVa(token("finally"), v(node.getFinally())) : null));
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		JsCodeElement result = seqVa(v(node.getJavadoc()), token("class"), v(node.getName()));
		JsCodeElement superType = v(node.getSuperclassExpression());
		if (superType != null) {
			result = seqVa(result, token("extends"), superType);
		}
		value = seqVa(result, braces(seqMap(node.bodyDeclarations())));
		return false;
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		value = v(node.getDeclaration());
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		List<JsCodeIRGenerator.JsCodeElement> result = new ArrayList<>();
		switch (node.getKind()) {
			case LET :
				result.add(token("let"));
				break;
			case CONST :
				result.add(token("const"));
				break;
			case VAR :// intentional
			default :
				result.add(token("var"));
				break;
		}
		result.add(v(node.getType()));
		result.add(seqCsMap(node.fragments()));
		value = seq(result);
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		List<JsCodeIRGenerator.JsCodeElement> result = new ArrayList<>();
		switch (node.getKind()) {
			case LET :
				result.add(token("let"));
				break;
			case CONST :
				result.add(token("const"));
				break;
			case VAR :// intentional
			default :
				result.add(token("var"));
				break;
		}
		result.add(seqCsMap(node.fragments()));
		value = seq(result);
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		List<JsCodeIRGenerator.JsCodeElement> result = new ArrayList<>();

		result.add(v(node.getName()));
		for (int i = 0; i < node.getExtraDimensions(); i++) {
			result.add(token("[]"));
		}
		if (node.getInitializer() != null) {
			result.add(token("="));
			if (node.getInitializer().getNodeType() == ASTNode.INFIX_EXPRESSION && ("in".equals(((InfixExpression) node.getInitializer()).getOperator().toString())) && node.getParent().getParent().getNodeType() == ASTNode.FOR_STATEMENT) {
				result.add(paren(v(node.getInitializer())));
			}
			else {
				result.add(v(node.getInitializer()));
			}
		}
		value = seq(result);
		return false;
	}

	@Override
	public boolean visit(WhileStatement node) {
		value = seqVa(token("while"), paren(v(node.getExpression())), v(node.getBody()));
		return false;
	}

	@Override
	public boolean visit(WithStatement node) {
		value = seqVa(token("with"), paren(v(node.getExpression())), v(node.getBody()));
		return false;
	}

	@Override
	public boolean visit(ObjectLiteral node) {
		value = braces(seqCsMap(node.fields()));
		return false;
	}

	@Override
	public boolean visit(ObjectLiteralField node) {
		value = seqVa(v(node.getFieldName()), token(":"), v(node.getInitializer()));
		return false;
	}

	@Override
	public boolean visit(FunctionExpression node) {
		value = v(node.getMethod());
		return false;
	}

	@Override
	public boolean visit(YieldExpression yieldExpression) {
		value = seqVa(token("yield"), parenOpt(expressionPrecedence(yieldExpression), yieldExpression.getArgument()));
		return false;
	}

	@Override
	public boolean visit(ArrowFunctionExpression arrowFunctionExpression) {
		List<ASTNode> params = arrowFunctionExpression.parameters();
		JsCodeElement paramsC = seqCsMap(params);

		// TODO Not very clear way to check for BindingIdentifier
		// And model seems to lack "defaults" in ESTree format terms
		if (params == null || params.size() != 1 || params.get(0).getNodeType() != ASTNode.SINGLE_VARIABLE_DECLARATION || ((VariableDeclaration) params.get(0)).getInitializer() != null) {
			paramsC = paren(paramsC);
		}
		value = seqVa(paramsC, token("=>"), v(arrowFunctionExpression.getBody()), v(arrowFunctionExpression.getExpression()));
		return false;
	}

	@Override
	public boolean visit(DebuggerStatement debuggerStatement) {
		value = seqVa(factory.token("debugger"), factory.semiOpt());
		return false;
	}

	@Override
	public boolean visit(ArrayName node) {
		value = brack(seqCsMap(node.elements()));
		return false;
	}

	@Override
	public boolean visit(ObjectName node) {
		value = braces(seqCsMap(node.objectProperties()));
		return false;
	}

	@Override
	public boolean visit(TemplateElement templateElement) {
		String rawValue = templateElement.getRawValue();
		if (rawValue != null && !rawValue.isEmpty()) {
			value = token(rawValue);
		}
		return false;
	}

	@Override
	public boolean visit(TemplateLiteral node) {
		List<JsCodeElement> templateBody = new ArrayList<>();
		templateBody.add(token("`"));
		for (int i = 0; i < node.elements().size(); i++) {
			TemplateElement te = (TemplateElement) node.elements().get(i);
			templateBody.add(v(te));
			if (!te.isTail()) {
				Expression exp = (Expression) node.expressions().get(i);
				templateBody.add(token("${"));
				templateBody.add(v(exp));
				templateBody.add(token("}"));
			}
		}
		templateBody.add(token("`"));
		value = seqVa(parenOpt(CALL, node.getTag()), factory.seqRaw(templateBody));
		return false;
	}

	@Override
	public boolean visit(AssignmentName node) {
		JsCodeElement lhs = v(node.getLeft());
		JsCodeElement rhs = v(node.getRight());
		// TODO Handle precedence
		value = seqVa(lhs, token("="), rhs);
		return false;
	}

	@Override
	public boolean visit(RestElementName node) {
		value = seqVa(token("..."), v(node.getArgument()));
		return false;
	}

	@Override
	public boolean visit(SpreadElement node) {
		value = seqVa(token("..."), parenOpt(ASSIGNMENT, node.getArgument()));
		return false;
	}

	@Override
	public boolean visit(MetaProperty metaProperty) {
		value = token(metaProperty.getMeta() + "." + metaProperty.getPropertyName());
		return false;
	}

	@Override
	public boolean visit(ExportDeclaration node) {
		// TODO This is just the copy of the import declaration. It's to be
		// changed when real ExportDeclaration is
		// available
		List<JsCodeElement> specifiers = map(node.specifiers());
		value = seqVa(token("export"), !specifiers.isEmpty() ? seqVa(seqCs(specifiers), token("from")) : null, v(node.getSource()), semiOpt());
		return false;
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		// TODO This is just initial vanilla implementation. It is not
		// thoroughly tested and may contain bugs.
		List<JsCodeElement> specifiers = map(node.specifiers());
		value = seqVa(token("import"), !specifiers.isEmpty() ? seqVa(seqCs(specifiers), token("from")) : null, v(node.getSource()), semiOpt());
		return false;
	}

	@Override
	public boolean visit(ModuleSpecifier moduleSpecifier) {
		// TODO This is just initial vanilla implementation. It is not
		// thoroughly tested and may contain bugs.
		SimpleName l = moduleSpecifier.getLocal();
		SimpleName d = moduleSpecifier.getDiscoverableName();
		JsCodeElement result = null;
		if (moduleSpecifier.isNamespace()) {
			result = seqVa(token("*"), token("as"), v(l));
		}
		else {
			if (d == null || l.getIdentifier().equals(d.getIdentifier())) {
				result = v(l);
			}
			else {
				result = seqVa(v(d), token("as"), v(l));
			}
			if (!moduleSpecifier.isDefault()) {
				result = braces(result);
			}
		}
		value = result;
		return false;
	}

	@Override
	public boolean visit(TypeDeclarationExpression typeDeclarationExpression) {
		value = v(typeDeclarationExpression.getDeclaration());
		return false;
	}

	@Override
	public boolean visit(FunctionDeclarationStatement functionDeclarationStatement) {
		value = v(functionDeclarationStatement.getDeclaration());
		return false;
	}

	/*
	 * Following functions are just shortcuts for factory calls.
	 */

	protected JsCodeElement token(String token) {
		return factory.token(token);
	}

	protected JsCodeElement semi() {
		return factory.semi();
	}

	protected JsCodeElement semiOpt() {
		return factory.semiOpt();
	}

	protected JsCodeElement wrap(String start, JsCodeElement element, String end) {
		return factory.wrap(start, element, end);
	}

	protected JsCodeElement paren(JsCodeElement element) {
		return factory.paren(element);
	}

	protected JsCodeElement brack(JsCodeElement element) {
		return factory.brack(element);
	}

	protected JsCodeElement braces(JsCodeElement element) {
		return factory.braces(element);
	}

	protected JsCodeElement seq(JsCodeElement[] elements) {
		return factory.seq(elements);
	}

	protected JsCodeElement seq(List<JsCodeElement> elements) {
		return factory.seq(elements);
	}

	protected JsCodeElement seqCs(JsCodeElement[] elements) {
		return factory.seqCs(elements);
	}

	protected JsCodeElement seqCs(List<JsCodeElement> elements) {
		return factory.seqCs(elements);
	}

	/**
	 * Visit given node and return JsCodeElement value created in that
	 * visitor.
	 *
	 * @param node
	 *            node to visit
	 * @return JsCodeElement created by the visitor
	 */
	protected JsCodeElement v(ASTNode node) {
		if (node != null) {
			value = null;
			node.accept(this);
			return value;
		}
		else {
			return null;
		}
	}

	/**
	 * Loop through given list of AST Nodes, visit each of them and collect
	 * returned JsCodeElements into resulting list. If visitor returns
	 * <code>null</code> this value is not included into the result.
	 *
	 * @param nodes
	 *            list of nodes to visit
	 * @return list of visit results for nodes in the given list
	 */
	protected List<JsCodeElement> map(List<ASTNode> nodes) {
		List<JsCodeElement> result = new ArrayList<>();
		for (ASTNode node : nodes) {
			JsCodeElement element = v(node);
			if (element != null) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * Loop through given list of AST Nodes, visit each of them and collect
	 * returned JsCodeElements as JsCode sequence element
	 *
	 * @param nodes
	 *            nodes to visit
	 * @return resulting JsCode sequence element
	 */
	protected JsCodeElement seqMap(List<ASTNode> nodes) {
		return seq(map(nodes));
	}

	/**
	 * Loop through given list of AST Nodes, visit each of them and collect
	 * returned JsCodeElements as JsCode comma separated sequence element
	 *
	 * @param nodes
	 *            nodes nodes to visit
	 * @return resulting JsCode comma separated sequence element
	 */
	protected JsCodeElement seqCsMap(List<ASTNode> nodes) {
		return seqCs(map(nodes));
	}

	/**
	 * Create JsCodeElement sequence from varArgs list of elements
	 *
	 * @param elements
	 *            elements to create sequence from
	 * @return resulting JsCodeElement sequence
	 */
	protected JsCodeElement seqVa(JsCodeElement... elements) {
		return seq(elements);
	}

	/**
	 * Check if given list of modifiers denotes "getter" or "setter"
	 *
	 * @param modifiers
	 *            list of modifiers to process
	 * @return <code>true</code> if given sequence denotes "getter" or
	 *         "setter", <code>false</code> otherwise
	 */
	protected boolean isGetterOrSetter(List<Modifier> modifiers) {
		for (Modifier node : modifiers) {
			String kw = node.getKeyword().toString();
			switch (kw) {
				case "get" :
				case "set" :
					return true;
				default :
					break;
			}
		}
		return false;
	}

	/**
	 * Wrap given child in optional parens in case if its precedence lower
	 * than given parent precedence. Used for correct presentation expressions
	 * like (a+b)*c
	 *
	 * @param parentPrec
	 *            precedence of the parent expression
	 * @param child
	 *            child node to process
	 * @return optionally enclosed in parens JsCodeElement for child
	 */
	protected JsCodeElement parenOpt(int parentPrec, ASTNode child) {
		if (child != null && child instanceof Expression) {
			if (expressionPrecedence((Expression) child) < parentPrec) {
				return paren(v(child));
			}
			else {
				return v(child);
			}
		}
		return v(child);
	}

	/**
	 * Wrap given child in optional parens in case if its precedence lower
	 * than or equals given parent precedence. Used for correct presentation
	 * of right hand expressions in situations like this a << (b << c)
	 *
	 * @param parentPrec
	 *            precedence of the parent expression
	 * @param child
	 *            child node to process
	 * @return optionally enclosed in parens JsCodeElement for child
	 */
	protected JsCodeElement parenOptRight(int rootPrec, ASTNode child) {
		if (child != null && child instanceof Expression) {
			if (expressionPrecedence((Expression) child) <= rootPrec) {
				return paren(v(child));
			}
			else {
				return v(child);
			}
		}
		return v(child);
	}

	/**
	 * Expressions precedence codes
	 */
	protected static final int SEQUENCE = 0;
	protected static final int ASSIGNMENT = 1;
	protected static final int CONDITIONAL = 2;
	protected static final int LOGICAL_OR = 3;
	protected static final int LOGICAL_AND = 4;
	protected static final int BITWISE_OR = 5;
	protected static final int BITWISE_XOR = 6;
	protected static final int BITWISE_AND = 7;
	protected static final int EQUALITY = 8;
	protected static final int RELATIONAL = 9;
	protected static final int SHIFT = 10;
	protected static final int ADDITIVE = 11;
	protected static final int MULTIPLICATIVE = 12;
	protected static final int PREFIX = 13;
	protected static final int POSTFIX = 14;
	protected static final int NEW = 15;
	protected static final int CALL = 16;
	protected static final int MEMBER = 17;
	protected static final int PRIMARY = 18;

	/**
	 * Return precedence for given expression. Implemented here because of
	 * lack precedence in AST
	 *
	 * @param e
	 *            expression to analyze
	 * @return expression's precedence
	 */
	protected int expressionPrecedence(Expression e) {
		if (e instanceof ArrayAccess) {
			return MEMBER;
		}
		else if (e instanceof ArrayInitializer) {
			return PRIMARY;
		}
		else if (e instanceof ArrowFunctionExpression || e instanceof Assignment) {
			return ASSIGNMENT;
		}
		else if (e instanceof BooleanLiteral || e instanceof CharacterLiteral || e instanceof NullLiteral || e instanceof NumberLiteral || e instanceof ObjectLiteral || e instanceof StringLiteral || e instanceof Name || e instanceof RegularExpressionLiteral || e instanceof UndefinedLiteral || e instanceof TemplateLiteral || e instanceof FunctionExpression || e instanceof ThisExpression || e instanceof ObjectLiteralField || e instanceof TypeDeclarationExpression) {
			return PRIMARY;
		}
		else if (e instanceof ClassInstanceCreation) {
			return NEW;
		}
		else if (e instanceof ConditionalExpression) {
			return CONDITIONAL;
		}
		else if (e instanceof FieldAccess) {
			return MEMBER;
		}
		else if (e instanceof FunctionInvocation) {
			return CALL;
		}
		else if (e instanceof ListExpression) {
			return SEQUENCE;
		}
		else if (e instanceof MetaProperty) {
			return MEMBER;
		}
		else if (e instanceof InfixExpression) {
			return operatorPrecedence(((InfixExpression) e).getOperator().toString());
		}
		else if (e instanceof ParenthesizedExpression) {
			return expressionPrecedence(((ParenthesizedExpression)e).getExpression());
		}
		else if (e instanceof PostfixExpression) {
			return POSTFIX;
		}
		else if (e instanceof PrefixExpression) {
			return PREFIX;
		}
		else if (e instanceof SuperMethodInvocation) {
			return MEMBER;
		}
		else if (e instanceof YieldExpression) {
			return ASSIGNMENT;
		}
		throw new UnimplementedException();
	}

	/**
	 * Return operators precedence. Implemented here because of absence of
	 * operator precedence in AST
	 *
	 * @param op
	 *            operator to analyze
	 * @return operator's precedence
	 */
	private int operatorPrecedence(String op) {
		switch (op) {
			case "*" :
			case "/" :
			case "%" :
				return MULTIPLICATIVE;
			case "+" :
			case "-" :
				return ADDITIVE;
			case "<<" :
			case ">>" :
			case ">>>" :
				return SHIFT;
			case "==" :
			case "!=" :
			case "===" :
			case "!==" :
				return EQUALITY;
			case "|" :
				return BITWISE_OR;
			case "^" :
				return BITWISE_XOR;
			case "&" :
				return BITWISE_AND;

			case "||" :
				return LOGICAL_OR;
			case "&&" :
				return LOGICAL_AND;
			case "<" :
			case ">" :
			case "<=" :
			case ">=" :
			case "in" :
			case "instanceof" :
				return RELATIONAL;
		}
		throw new UnimplementedException();
	}
}
