/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.dom;

import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.jsdt.core.dom.Assignment.Operator;
import org.eclipse.wst.jsdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField.FieldKind;

import com.google.javascript.jscomp.parsing.parser.IdentifierToken;
import com.google.javascript.jscomp.parsing.parser.Token;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayPatternTree;
import com.google.javascript.jscomp.parsing.parser.trees.AssignmentRestElementTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.BreakStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.CaseClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.CatchTree;
import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.CommaExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.Comment;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyDefinitionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyGetterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyMemberVariableTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyMethodTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertySetterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ConditionalExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ContinueStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DebuggerStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.GetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ModuleImportTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NullTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectPatternTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.PostfixExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.trees.PropertyNameAssignmentTree;
import com.google.javascript.jscomp.parsing.parser.trees.RestParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.SetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.SpreadExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SuperExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateLiteralPortionTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateSubstitutionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeNameTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.YieldExpressionTree;
import com.google.javascript.jscomp.parsing.parser.util.SourceRange;

/**
 * Converts closure compiler's IR model to DOM AST.
 * 
 * @author Gorkem Ercan
 *
 */
@SuppressWarnings("unchecked")
public class ClosureCompilerASTConverter {

	private static final String KEYWORD_SUPER = "super"; //$NON-NLS-1$
	private static final boolean DEBUG = false;
	private final AST ast;
	private final List<Comment> comments;
	private Comment currentComment;
	private final Iterator<Comment> nextCommentIter;

	public ClosureCompilerASTConverter(AST t, List<Comment> comment){
		this.ast = t;
		this.comments = comment;
	    this.nextCommentIter = comments.iterator();
		this.currentComment = nextCommentIter.hasNext() ? nextCommentIter.next() : null;
		
	}

    public ASTNode transform(StructuralPropertyDescriptor property, ParseTree tree) {
    	if(DEBUG){
    		System.out.println(">> transform:: property : "+ property +" tree: "+tree);  //$NON-NLS-1$//$NON-NLS-2$
    	}
    	
    	if(tree == null )
    		return null;
    	ASTNode node = process(property, tree);
        if (node == null ) 
        	return null;
        setSourceRange(node, tree);
        if(DEBUG){
        	System.out.println("<< transform:: tree: "+tree +" --> node:"+ node); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return node;
      }

	/**
	 * @param tree
	 * @param node
	 */
	private <T extends ASTNode> void attachJSDoc(ParseTree tree, BodyDeclaration node) {
		Comment info = handleJsDoc(tree);
        if (info != null && info.isJsDoc()) {
        	JSdoc doc = ast.newJSdoc();
        	doc.setComment(info.value);
        	doc.setSourceRange(info.location.start.offset, info.location.end.offset - info.location.start.offset);
        	node.setJavadoc(doc);
        }
	}
	
	/**
	 * @param tree
	 * @param node
	 */
	private <T extends ASTNode> void attachJSDoc(ParseTree tree, VariableDeclarationStatement node) {
		Comment info = handleJsDoc(tree);
        if (info != null && info.isJsDoc()) {
        	JSdoc doc = ast.newJSdoc();
        	doc.setComment(info.value);
        	doc.setSourceRange(info.location.start.offset, info.location.end.offset - info.location.start.offset);
        	node.setJavadoc(doc);
        }
	}
   
    private InfixExpression.Operator convertBinaryOperator(Token operator) {
		return InfixExpression.Operator.toOperator(operator.toString());
	}
    
    private VariableKind convertVariableKind(TokenType token){
    	switch(token){
    		case LET   : return VariableKind.LET;
    		case CONST : return VariableKind.CONST;
    		// Returns VAR by default
    		default: return VariableKind.VAR;
    	}
    }

	private boolean notNullStatement(ParseTree tree){
		return tree.type != ParseTreeType.NULL;
	}

	private ASTNode process(StructuralPropertyDescriptor property, ParseTree node) {
      switch (node.type) {
        case BINARY_OPERATOR:
          return  processBinaryExpression(node.asBinaryOperator());
        case ARRAY_LITERAL_EXPRESSION:
          return  processArrayLiteral(node.asArrayLiteralExpression());
        case TEMPLATE_LITERAL_EXPRESSION:
          return  processTemplateLiteral(node.asTemplateLiteralExpression());
        case TEMPLATE_LITERAL_PORTION:
          return  processTemplateLiteralPortion(node.asTemplateLiteralPortion());
        case TEMPLATE_SUBSTITUTION:
          return  processTemplateSubstitution(node.asTemplateSubstitution());
        case UNARY_EXPRESSION:
          return  processUnaryExpression(node.asUnaryExpression());
        case BLOCK:
          return  processBlock(node.asBlock());
        case BREAK_STATEMENT:
          return  processBreakStatement(node.asBreakStatement());
        case CALL_EXPRESSION:
          return  processFunctionCall(node.asCallExpression());
        case SWITCH_STATEMENT:
        	return  processSwitchStatement(node.asSwitchStatement());
        case CASE_CLAUSE:
          return  processSwitchCase(node.asCaseClause());
        case DEFAULT_CLAUSE:
          return  processSwitchDefault(node.asDefaultClause());
        case CATCH:
          return  processCatchClause(node.asCatch());
        case CONTINUE_STATEMENT:
          return  processContinueStatement(node.asContinueStatement());
        case DO_WHILE_STATEMENT:
          return  processDoLoop(node.asDoWhileStatement());
        case EMPTY_STATEMENT:
          return  processEmptyStatement(node.asEmptyStatement());
        case EXPRESSION_STATEMENT:
          return  processExpressionStatement(node.asExpressionStatement());
        case DEBUGGER_STATEMENT:
          return  processDebuggerStatement(node.asDebuggerStatement());
        case THIS_EXPRESSION:
          return processThisExpression(node.asThisExpression());
        case FOR_STATEMENT:
          return  processForLoop(node.asForStatement());
        case FOR_IN_STATEMENT:
          return  processForInLoop(node.asForInStatement());
        case FUNCTION_DECLARATION:
          return  processFunction(property, node.asFunctionDeclaration());
        case MEMBER_LOOKUP_EXPRESSION:
          return  processElementGet(node.asMemberLookupExpression());
        case MEMBER_EXPRESSION:
          return  processPropertyGet(property, node.asMemberExpression());
        case CONDITIONAL_EXPRESSION:
          return  processConditionalExpression(node.asConditionalExpression());
        case IF_STATEMENT:
          return processIfStatement(node.asIfStatement());
        case LABELLED_STATEMENT:
          return processLabeledStatement(node.asLabelledStatement());
        case PAREN_EXPRESSION:
          return processParenthesizedExpression(node.asParenExpression());
        case IDENTIFIER_EXPRESSION:
          return processName(property, node.asIdentifierExpression());
        case NEW_EXPRESSION:
          return processNewExpression(node.asNewExpression());
        case OBJECT_LITERAL_EXPRESSION:
          return processObjectLiteral(node.asObjectLiteralExpression());
        case COMPUTED_PROPERTY_GETTER:
          return  processComputedPropertyGetter(property, node.asComputedPropertyGetter());
        case COMPUTED_PROPERTY_SETTER:
           return processComputedPropertySetter(property, node.asComputedPropertySetter());
        case COMPUTED_PROPERTY_METHOD:
        	return processComputedPropertyMethod(property, node.asComputedPropertyMethod());
        case COMPUTED_PROPERTY_DEFINITION:
        case COMPUTED_PROPERTY_MEMBER_VARIABLE:
        	// Handled on processObjectLiteral should never happen here for legal JS
        	return null;
        case RETURN_STATEMENT:
          return processReturnStatement(node.asReturnStatement());
        case POSTFIX_EXPRESSION:
          return processPostfixExpression(node.asPostfixExpression());
        case PROGRAM:
          return processAstRoot(node.asProgram());
        case LITERAL_EXPRESSION: // STRING, NUMBER, TRUE, FALSE, NULL, REGEXP
          return processLiteralExpression(node.asLiteralExpression());
        case THROW_STATEMENT:
          return processThrowStatement(node.asThrowStatement());
        case TRY_STATEMENT:
          return processTryStatement(node.asTryStatement());
        case VARIABLE_STATEMENT: // var const let
          return processVariableStatement(node.asVariableStatement());
        case VARIABLE_DECLARATION_LIST:
          return processVariableDeclarationList(node.asVariableDeclarationList());
        case VARIABLE_DECLARATION:
          return processVariableDeclaration(node.asVariableDeclaration());
        case WHILE_STATEMENT:
          return processWhileLoop(node.asWhileStatement());
        case WITH_STATEMENT:
          return processWithStatement(node.asWithStatement());
        case COMMA_EXPRESSION:
          return processCommaExpression(node.asCommaExpression());
        case NULL: // this is not the null literal
          return processNull(node.asNull());
        case FINALLY:
          return transform(property, node.asFinally().block);

        case MISSING_PRIMARY_EXPRESSION:
        	// DOM AST provides a syntactically plausible initial value to required
        	// properties skip processing this node type.
        	return null;
        case PROPERTY_NAME_ASSIGNMENT:
          return processPropertyNameAssignment(node.asPropertyNameAssignment());
        case GET_ACCESSOR:
          return processGetAccessor(property, node.asGetAccessor());
        case SET_ACCESSOR:
          return processSetAccessor(property, node.asSetAccessor());
        case FORMAL_PARAMETER_LIST:
        	//Should be handled on processFunction
        	// if we end up here it is probably because of tolerant parsing 
        	return null;
        case CLASS_DECLARATION:
          return processClassDeclaration(property, node.asClassDeclaration());
        case SUPER_EXPRESSION:
          return processSuper(node.asSuperExpression());
        case YIELD_EXPRESSION:
          return processYield(node.asYieldStatement());
        case FOR_OF_STATEMENT:
          return processForOf(node.asForOfStatement());

        case EXPORT_DECLARATION:
          return processExportDecl(node.asExportDeclaration());
        case EXPORT_SPECIFIER:
          return processExportSpec(node.asExportSpecifier());
        case IMPORT_DECLARATION:
          return processImportDecl(node.asImportDeclaration());
        case IMPORT_SPECIFIER:
          return processImportSpec(node.asImportSpecifier());
        case MODULE_IMPORT:
          return processModuleImport(node.asModuleImport());

        case ARRAY_PATTERN:
          return processArrayPattern(property,node.asArrayPattern());
        case OBJECT_PATTERN:
          return processObjectPattern(property,node.asObjectPattern());
        case ASSIGNMENT_REST_ELEMENT:
          return processAssignmentRestElement(node.asAssignmentRestElement());

//        case COMPREHENSION:
//          return processComprehension(node.asComprehension());
//        case COMPREHENSION_FOR:
//          return processComprehensionFor(node.asComprehensionFor());
//        case COMPREHENSION_IF:
//          return processComprehensionIf(node.asComprehensionIf());

        case DEFAULT_PARAMETER:
          return processDefaultParameter(property, node.asDefaultParameter());
        case REST_PARAMETER:
          return processRestParameter(property, node.asRestParameter());
        case SPREAD_EXPRESSION:
          return processSpreadExpression(node.asSpreadExpression());

        // ES6 Typed
        case TYPE_NAME:
          return processTypeName(node.asTypeName());
//        case TYPED_PARAMETER:
//          return processTypedParameter(node.asTypedParameter());
//        case OPTIONAL_PARAMETER:
//          return processOptionalParameter(node.asOptionalParameter());
//        case PARAMETERIZED_TYPE_TREE:
//          return processParameterizedType(node.asParameterizedType());
//        case ARRAY_TYPE:
//          return processArrayType(node.asArrayType());
//        case RECORD_TYPE:
//          return processRecordType(node.asRecordType());
//        case UNION_TYPE:
//          return processUnionType(node.asUnionType());
//        case FUNCTION_TYPE:
//          return processFunctionType(node.asFunctionType());
//        case TYPE_QUERY:
//          return processTypeQuery(node.asTypeQuery());
//        case GENERIC_TYPE_LIST:
//          return processGenericTypeList(node.asGenericTypeList());
//        case MEMBER_VARIABLE:
//          return processMemberVariable(node.asMemberVariable());
//
//        case INTERFACE_DECLARATION:
//          return processInterfaceDeclaration(node.asInterfaceDeclaration());
//        case ENUM_DECLARATION:
//          return processEnumDeclaration(node.asEnumDeclaration());
//
//        case TYPE_ALIAS:
//          return processTypeAlias(node.asTypeAlias());
//        case AMBIENT_DECLARATION:
//          return processAmbientDeclaration(node.asAmbientDeclaration());
//        case NAMESPACE_DECLARATION:
//          return processNamespaceDeclaration(node.asNamespaceDeclaration());
//
//        case INDEX_SIGNATURE:
//          return processIndexSignature(node.asIndexSignature());
//        case CALL_SIGNATURE:
//          return processCallSignature(node.asCallSignature());
        case ARGUMENT_LIST:
        default:
          break;
      }
      return processIllegalToken(node);
    }

	/**
	 * @param asComputedPropertyMethod
	 * @return
	 */
	private ASTNode processComputedPropertyMethod(StructuralPropertyDescriptor property, ComputedPropertyMethodTree tree) {
		FunctionDeclaration $ = ast.newFunctionDeclaration();
		FunctionDeclarationTree methodTree= tree.method.asFunctionDeclaration();
		transformAndSetProperty($,FunctionDeclaration.BODY_PROPERTY, methodTree.functionBody);
		transformAndSetProperty($,FunctionDeclaration.METHOD_NAME_PROPERTY, tree.property);
		if(methodTree.isStatic){
			$.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
		}
		return adjustFunctionDeclarationToProperty(property,$);
	}

	/**
	 * @param asComputedPropertySetter
	 * @return
	 */
	private ASTNode processComputedPropertySetter(StructuralPropertyDescriptor property, ComputedPropertySetterTree tree) {
		FunctionDeclaration $ = ast.newFunctionDeclaration();
		transformAndSetProperty($,FunctionDeclaration.BODY_PROPERTY,tree.body);
		transformAndSetProperty($,FunctionDeclaration.METHOD_NAME_PROPERTY,tree.property);
		$.modifiers().add(ast.newModifier(ModifierKeyword.SET_KEYWORD));
		if(tree.isStatic){
			$.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
		}
		return adjustFunctionDeclarationToProperty(property,$);
	}

	/**
	 * @param asComputedPropertyGetter
	 * @return
	 */
	private ASTNode processComputedPropertyGetter(StructuralPropertyDescriptor property, ComputedPropertyGetterTree tree) {
		FunctionDeclaration $ = ast.newFunctionDeclaration();
		transformAndSetProperty($,FunctionDeclaration.BODY_PROPERTY,tree.body);
		transformAndSetProperty($,FunctionDeclaration.METHOD_NAME_PROPERTY,tree.property);
		$.modifiers().add(ast.newModifier(ModifierKeyword.GET_KEYWORD));
		if(tree.isStatic){
			$.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
		}
		return adjustFunctionDeclarationToProperty(property,$);
	}

	/**
	 * @param asArrayLiteralExpression
	 * @return
	 */
	private ASTNode processArrayLiteral(ArrayLiteralExpressionTree tree) {
		ArrayInitializer $ = ast.newArrayInitializer();
		for ( ParseTree pe : tree.elements) {
			if(notNullStatement(pe))
				$.expressions().add(transform(ArrayInitializer.EXPRESSIONS_PROPERTY,pe));
		}
		return $;
	}

	/**
	 * @param asArrayPattern
	 * @return
	 */
	private ASTNode processArrayPattern(StructuralPropertyDescriptor property, ArrayPatternTree tree) {
		ArrayName $ = ast.newArrayName();
		for(ParseTree child: tree.elements){
			if(notNullStatement(child))
			$.elements().add(transform(ArrayName.ELEMENTS_PROPERTY,child));
		}
		Class<?> claz = classForProperty(property);
		if(claz.isAssignableFrom(SingleVariableDeclaration.class)){
			SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
			svd.setPattern($);
			return svd;
		}
		return $;
	}

	/**
	 * @param asAssignmentRestElement
	 * @return
	 */
	private ASTNode processAssignmentRestElement(AssignmentRestElementTree tree) {
		RestElementName $ = ast.newRestElementName();
		if(tree.identifier != null){
			$.setArgument(transformLabelName(tree.identifier));
		}
		return $;
	}

	/**
	 * @param asProgram
	 * @return
	 */
	private ASTNode processAstRoot(ProgramTree tree) {
		JavaScriptUnit $ = ast.newJavaScriptUnit();
	    for (ParseTree child : tree.sourceElements) {
	    	switch (child.type) {
				case EXPORT_DECLARATION:
					$.exports().add(transform(JavaScriptUnit.EXPORTS_PROPERTY,child));
					break;
				case IMPORT_DECLARATION : 
					$.imports().add(transform(JavaScriptUnit.IMPORTS_PROPERTY,child));
					break;
				default :
					$.statements().add(transform(JavaScriptUnit.STATEMENTS_PROPERTY,child));
					break;
			}
	    }
		return $;
	}

	/**
	 * @param asBinaryOperator
	 * @return
	 */
	private ASTNode processBinaryExpression(BinaryOperatorTree tree) {
		Operator assignOp = Assignment.Operator.toOperator(tree.operator.toString());
		if(assignOp != null){
			Assignment $ = ast.newAssignment();
			safeSetProperty($, Assignment.OPERATOR_PROPERTY,assignOp);
			transformAndSetProperty($,Assignment.LEFT_HAND_SIDE_PROPERTY,tree.left);
			transformAndSetProperty($,Assignment.RIGHT_HAND_SIDE_PROPERTY,tree.right);
			return $;
		}
		InfixExpression $ = ast.newInfixExpression();
		safeSetProperty($,InfixExpression.OPERATOR_PROPERTY,convertBinaryOperator(tree.operator));
		transformAndSetProperty($,InfixExpression.LEFT_OPERAND_PROPERTY, tree.left);
		transformAndSetProperty($,InfixExpression.RIGHT_OPERAND_PROPERTY, tree.right);
		return $;
	}

	/**
	 * @param asBlock
	 * @return
	 */
	private ASTNode processBlock(BlockTree tree) {
		Block $ = ast.newBlock();
		for (ParseTree pt : tree.statements) {
			if(notNullStatement(pt))
			$.statements().add(transform(Block.STATEMENTS_PROPERTY, pt));
		}
		return $;
	}

	/**
	 * @param asBreakStatement
	 * @return
	 */
	private ASTNode processBreakStatement(BreakStatementTree tree) {
		BreakStatement $ = ast.newBreakStatement();
		if(tree.name != null)
			$.setLabel(transformLabelName(tree.name));
		return $;
	}

	/**
	 * @param asCatch
	 * @return
	 */
	private ASTNode processCatchClause(CatchTree tree) {
		CatchClause $ = ast.newCatchClause();
		transformAndSetProperty($, CatchClause.EXCEPTION_PROPERTY,tree.exception);
		transformAndSetProperty($,CatchClause.BODY_PROPERTY,tree.catchBody);
		return $;
	}

	/**
	 * @param asClassDeclaration
	 * @return
	 */
	private ASTNode processClassDeclaration(StructuralPropertyDescriptor property, ClassDeclarationTree tree) {
		TypeDeclaration $ = ast.newTypeDeclaration();
		if(tree.name != null){
			$.setName(transformLabelName(tree.name));
		}
		transformAndSetProperty($,TypeDeclaration.SUPERCLASS_EXPRESSION_PROPERTY,tree.superClass);
		for(ParseTree child : tree.elements){
					final ASTNode declaration = transform(TypeDeclaration.BODY_DECLARATIONS_PROPERTY,child);
					// We can get empty statements from closure compiler due to tolerant parsing 
					if(declaration instanceof EmptyStatement)
						continue;
					$.bodyDeclarations().add(declaration);
		}
		attachJSDoc(tree,$);
		Class<?> claz = classForProperty(property);
		if(claz.isAssignableFrom(Expression.class)){
			return ast.newTypeDeclarationExpression($);
		}
		if(claz.isAssignableFrom(Statement.class)){
			return ast.newTypeDeclarationStatement($);
		}
		return $;
	}

	/**
	 * @param asCommaExpression
	 * @return
	 */
	private ASTNode processCommaExpression(CommaExpressionTree tree) {
		ListExpression $ = ast.newListExpression();
		for(ParseTree expr : tree.expressions){
			$.expressions().add(transform(ListExpression.EXPRESSIONS_PROPERTY,expr));
		}
		return $;
	}

	/**
	 * @param asConditionalExpression
	 * @return
	 */
	private ASTNode processConditionalExpression(ConditionalExpressionTree tree) {
		ConditionalExpression $ = ast.newConditionalExpression();
		transformAndSetProperty($,ConditionalExpression.EXPRESSION_PROPERTY,tree.condition);
		transformAndSetProperty($,ConditionalExpression.THEN_EXPRESSION_PROPERTY,tree.left);
		transformAndSetProperty($,ConditionalExpression.ELSE_EXPRESSION_PROPERTY,tree.right);
		return $;
	}

	/**
	 * @param asContinueStatement
	 * @return
	 */
	private ASTNode processContinueStatement(ContinueStatementTree tree) {
		ContinueStatement $ = ast.newContinueStatement();
		if(tree.name != null)
			$.setLabel(transformLabelName(tree.name));
		return $;
	}

	/**
	 * @param asDebuggerStatement
	 * @return
	 */
	private ASTNode processDebuggerStatement(DebuggerStatementTree tree) {
		return ast.newDebuggerStatement();
	}

	/**
	 * @param asDefaultParameter
	 * @return
	 */
	private ASTNode processDefaultParameter(StructuralPropertyDescriptor property, DefaultParameterTree tree) {
		// TODO: handle default values properly. DOM ast does not have support 
		// for it and needs to be enhanced.
		return transform(property,tree.lhs);
	}

	/**
	 * @param asDoWhileStatement
	 * @return
	 */
	private ASTNode processDoLoop(DoWhileStatementTree tree) {
		DoStatement $ = ast.newDoStatement();
		transformAndSetProperty($,DoStatement.EXPRESSION_PROPERTY,tree.condition);
		transformAndSetProperty($,DoStatement.BODY_PROPERTY,tree.body);
		return $;
	}

	/**
	 * @param asMemberLookupExpression
	 * @return
	 */
	private ASTNode processElementGet(MemberLookupExpressionTree tree) {
		ArrayAccess $ = ast.newArrayAccess();
		transformAndSetProperty($,ArrayAccess.INDEX_PROPERTY,tree.memberExpression);
		transformAndSetProperty($,ArrayAccess.ARRAY_PROPERTY,tree.operand);
		return $;
	}

	/**
	 * @param asEmptyStatement
	 * @return
	 */
	private ASTNode processEmptyStatement(EmptyStatementTree tree) {
		return ast.newEmptyStatement();
	}

	/**
	 * @param asExportDeclaration
	 * @return
	 */
	private ASTNode processExportDecl(ExportDeclarationTree tree) {
		ExportDeclaration $ = ast.newExportDeclaration();
		$.setAll(tree.isExportAll);
		$.setDefault(tree.isDefault);
		if(tree.declaration != null)
		{
			if(tree.declaration.type == ParseTreeType.VARIABLE_DECLARATION_LIST){
				VariableDeclarationListTree vdTree = tree.declaration.asVariableDeclarationList();
				VariableDeclarationStatement statement = ast.newVariableDeclarationStatement();
				for(ParseTree child : vdTree.declarations){
					statement.fragments().add(transform(VariableDeclarationStatement.FRAGMENTS_PROPERTY,child));
				}
				$.setDeclaration(statement);
			}else{
				transformAndSetProperty($,ExportDeclaration.DECLARATION_PROPERTY,tree.declaration);
			}
		}
		if (tree.exportSpecifierList != null) {
			for (ParseTree spec : tree.exportSpecifierList) {
				$.specifiers().add(transform(ExportDeclaration.SPECIFIERS_PROPERTY,spec));
			}
		}
		if(tree.from != null){
			$.setSource(transformStringLiteral(tree.from));
		}
		return $;
	}

	/**
	 * @param asExportSpecifier
	 * @return
	 */
	private ASTNode processExportSpec(ExportSpecifierTree tree) {
		ModuleSpecifier $ = ast.newModuleSpecifier();
		if(tree.importedName != null){
			$.setLocal(transformLabelName(tree.importedName));
		}
		if(tree.destinationName != null)
			$.setDiscoverableName(transformLabelName(tree.destinationName));
		return $;
	}

	/**
	 * @param asExpressionStatement
	 * @return
	 */
	private ASTNode processExpressionStatement(ExpressionStatementTree tree ) {
		ExpressionStatement $ = ast.newExpressionStatement();
		transformAndSetProperty($,ExpressionStatement.EXPRESSION_PROPERTY,tree.expression);
		return $;
	}

	/**
	 * @param asForInStatement
	 * @return
	 */
	private ASTNode processForInLoop(ForInStatementTree tree) {
		ForInStatement $ = ast.newForInStatement();
		transformAndSetProperty($, ForInStatement.BODY_PROPERTY,tree.body);
		transformAndSetProperty($, ForInStatement.ITERATION_VARIABLE_PROPERTY,tree.initializer);
		transformAndSetProperty($, ForInStatement.COLLECTION_PROPERTY,tree.collection);
		return $;
	}
	
	/**
	 * @param asForStatement
	 * @return
	 */
	private ASTNode processForLoop(ForStatementTree tree) {
		ForStatement $ = ast.newForStatement();
		transformAndSetProperty($, ForStatement.BODY_PROPERTY, tree.body);
		if(notNullStatement(tree.condition))
			transformAndSetProperty($,ForStatement.EXPRESSION_PROPERTY,tree.condition);
		if(notNullStatement(tree.initializer)){
			if(tree.initializer.type == ParseTreeType.VARIABLE_DECLARATION_LIST){
				VariableDeclarationListTree listTree = tree.initializer.asVariableDeclarationList();
				int startPosition =listTree.location.start.offset;
				final VariableDeclarationExpression vd = ast.newVariableDeclarationExpression();
				vd.setKind(convertVariableKind(listTree.declarationType));
				
				for(ParseTree child : listTree.declarations){
					vd.fragments().add(transform(VariableDeclarationExpression.FRAGMENTS_PROPERTY,child));
					//The start position for the very first expression should start from list Tree. 
					if(startPosition <0){
						startPosition = child.location.start.offset;
					}
					vd.setSourceRange(startPosition, child.location.end.offset -startPosition);
					startPosition = -1;
				}
				$.initializers().add(vd);
			}else{
				$.initializers().add(transform(ForStatement.INITIALIZERS_PROPERTY, tree.initializer));
			}
		}
		if( notNullStatement(tree.increment))
			$.updaters().add(transform(ForStatement.UPDATERS_PROPERTY, tree.increment));
		return $;
	}	

	/**
	 * @param asForOfStatement
	 * @return
	 */
	private ASTNode processForOf(ForOfStatementTree tree) {
		ForOfStatement $ = ast.newForOfStatement();
		transformAndSetProperty($,ForOfStatement.BODY_PROPERTY, tree.body);
		transformAndSetProperty($,ForOfStatement.ITERATION_VARIABLE_PROPERTY,tree.initializer);
		transformAndSetProperty($,ForOfStatement.COLLECTION_PROPERTY,tree.collection);
		return $;
	}

	/**
	 * @param asFunctionDeclaration
	 * @return
	 */
	private ASTNode processFunction(StructuralPropertyDescriptor property, FunctionDeclarationTree tree) {
		
		if(tree.kind == FunctionDeclarationTree.Kind.ARROW){
			ArrowFunctionExpression $ = ast.newArrowFunctionExpression();
			if(tree.functionBody.type == ParseTreeType.BLOCK){
				transformAndSetProperty($,ArrowFunctionExpression.BODY_PROPERTY,tree.functionBody);
			}else{
				transformAndSetProperty($,ArrowFunctionExpression.EXPRESSION_PROPERTY,tree.functionBody);
			}
			if(tree.formalParameterList != null ){ 
				for(ParseTree param : tree.formalParameterList.parameters){
					$.parameters().add(transform(ArrowFunctionExpression.PARAMETERS_PROPERTY,param));
				}
			}
			return $;
		}
		
		FunctionDeclaration $ = ast.newFunctionDeclaration();
		attachJSDoc(tree,$);
		$.setGenerator(tree.isGenerator);
		transformAndSetProperty($,FunctionDeclaration.BODY_PROPERTY,tree.functionBody);
		transformAndSetProperty($, FunctionDeclaration.RETURN_TYPE2_PROPERTY,tree.returnType);
		if(tree.name != null ){
			SimpleName name = transformLabelName(tree.name);
			$.setMethodName(name);
			$.setConstructor(name.getIdentifier().equals("constructor") || name.getIdentifier().equals("\"constructor\""));  //$NON-NLS-1$//$NON-NLS-2$
			setSourceRange($, tree.name);
		} else {
			setSourceRange($, tree.formalParameterList);
		}
		if(tree.isStatic)
			$.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
		
		for(ParseTree param : tree.formalParameterList.parameters){
			$.parameters().add(transform(FunctionDeclaration.PARAMETERS_PROPERTY,param));
		}
		
		if(tree.access != null){
			ModifierKeyword modifierKeyword = null;
			switch (tree.access) {
				case PUBLIC:
					modifierKeyword = ModifierKeyword.PUBLIC_KEYWORD;
					break;
				case PROTECTED:
					modifierKeyword = ModifierKeyword.PROTECTED_KEYWORD;
					break;
				case PRIVATE:
					modifierKeyword = ModifierKeyword.PRIVATE_KEYWORD;
					break;
				default :
					throw new IllegalStateException("Unexpected access modifier type"); //$NON-NLS-1$
			}
			if(modifierKeyword != null){
				$.modifiers().add(ast.newModifier(modifierKeyword));
			}
		}
		
		return adjustFunctionDeclarationToProperty(property, $);
			
	}

	private ASTNode adjustFunctionDeclarationToProperty(final StructuralPropertyDescriptor property, final FunctionDeclaration declaration) {
		Class<?> claz = classForProperty(property);

		//test and return FunctionDeclaration first 
		if(claz.isAssignableFrom(FunctionDeclaration.class)){
			return declaration;
		}
		if(claz.isAssignableFrom(Expression.class)){
			FunctionExpression e = ast.newFunctionExpression();
			e.setMethod(declaration);
			return e;
		}
		if(claz.isAssignableFrom(Statement.class)){
			return ast.newFunctionDeclarationStatement(declaration);
		}
		return declaration;
	}

	/**
	 * @param asCallExpression
	 * @return
	 */
	private ASTNode processFunctionCall(CallExpressionTree tree) {
		FunctionInvocation $  = ast.newFunctionInvocation();
		if(tree.operand.type == ParseTreeType.IDENTIFIER_EXPRESSION){
			transformAndSetProperty($,FunctionInvocation.NAME_PROPERTY,tree.operand);
		}else{
			transformAndSetProperty($,FunctionInvocation.EXPRESSION_PROPERTY,tree.operand);
		}
		for (ParseTree pt : tree.arguments.arguments) {
			$.arguments().add(transform(FunctionInvocation.ARGUMENTS_PROPERTY, pt));
		}
		return $;
	}

	/**
	 * @param asGetAccessor
	 * @return
	 */
	private ASTNode processGetAccessor(StructuralPropertyDescriptor property, GetAccessorTree tree) {
		FunctionDeclaration $ = ast.newFunctionDeclaration();
		$.setMethodName(transformObjectLitKeyAsString(tree.propertyName));
		transformAndSetProperty($,FunctionDeclaration.BODY_PROPERTY,tree.body);
		$.modifiers().add(ast.newModifier(ModifierKeyword.GET_KEYWORD));
		if(tree.isStatic){
			$.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
		}
		return adjustFunctionDeclarationToProperty(property, $);
	}
	
	/**
	 * @param asIfStatement
	 * @return
	 */
	private ASTNode processIfStatement(IfStatementTree tree) {
		IfStatement $ = ast.newIfStatement();
		transformAndSetProperty($,IfStatement.EXPRESSION_PROPERTY,tree.condition);
		transformAndSetProperty($,IfStatement.THEN_STATEMENT_PROPERTY,tree.ifClause);
		transformAndSetProperty($, IfStatement.ELSE_STATEMENT_PROPERTY,tree.elseClause);
		return $;
	}

	private ASTNode processIllegalToken(ParseTree node) {
    	System.out.println( "Unsupported syntax: " + node.type +" at "+ node.location.start.line +1);  //$NON-NLS-1$//$NON-NLS-2$
        return ast.newEmptyStatement();
      }
	
	/**
	 * @param asImportDeclaration
	 * @return
	 */
	private ASTNode processImportDecl(ImportDeclarationTree tree) {
		ImportDeclaration $ = ast.newImportDeclaration();
		if(tree.moduleSpecifier != null)
			$.setSource(transformStringLiteral(tree.moduleSpecifier.asLiteral()));
		if(tree.defaultBindingIdentifier != null){
			ModuleSpecifier defaultModule = ast.newModuleSpecifier();
			defaultModule.setDefault(true);
			defaultModule.setLocal(transformLabelName(tree.defaultBindingIdentifier));
			setSourceRange(defaultModule,tree.defaultBindingIdentifier);
			$.specifiers().add(defaultModule);
		}
		if(tree.nameSpaceImportIdentifier != null ){
			ModuleSpecifier m = ast.newModuleSpecifier();
			m.setNamespace(true);
			m.setLocal(transformLabelName(tree.nameSpaceImportIdentifier.asIdentifier()));
			setSourceRange(m,tree.nameSpaceImportIdentifier);
			$.specifiers().add(m);
		}else if(tree.importSpecifierList != null){
			for(ParseTree spec : tree.importSpecifierList){
				$.specifiers().add(transform(ImportDeclaration.SPECIFIERS_PROPERTY,spec));
			}
		}
		return $;
	}


	/**
	 * @param asImportSpecifier
	 * @return
	 */
	private ASTNode processImportSpec(ImportSpecifierTree tree) {
		ModuleSpecifier $ = ast.newModuleSpecifier();
		if(tree.destinationName != null)
			$.setLocal(transformLabelName(tree.destinationName.asIdentifier()));
		$.setDiscoverableName(transformLabelName(tree.importedName.asIdentifier()));
		return $;
	}

	/**
	 * @param asLabelledStatement
	 * @return
	 */
	private ASTNode processLabeledStatement(LabelledStatementTree tree) {
		LabeledStatement $ = ast.newLabeledStatement();
		$.setLabel(transformLabelName(tree.name));
		transformAndSetProperty($,LabeledStatement.BODY_PROPERTY,tree.statement);
		return $;
	}

	/**
	 * @param asLiteralExpression
	 * @return
	 */
	private ASTNode processLiteralExpression(LiteralExpressionTree tree) {
		switch (tree.literalToken.type) {
			case NUMBER :
				return transformNumberLiteral(tree.literalToken);
			case STRING: 
				return transformStringLiteral(tree.literalToken);
			case FALSE:
			case TRUE:
				return transformBooleanLiteral(tree.literalToken);
			case NULL:
				return transformNullLiteral(tree.literalToken);
			case REGULAR_EXPRESSION:
				return transformRegExpLiteral(tree.literalToken);
			default :
		          throw new IllegalStateException("Unexpected literal type: " //$NON-NLS-1$
		                      + tree.literalToken.getClass() + " type: " //$NON-NLS-1$
		                      + tree.literalToken.type);
		}
	}

	/**
	 * @param asModuleImport
	 * @return
	 */
	private ASTNode processModuleImport(ModuleImportTree tree) {
		ImportDeclaration $ = ast.newImportDeclaration();
		ModuleSpecifier m = ast.newModuleSpecifier();
		if(tree.name != null)
			m.setLocal(transformLabelName(tree.name));
		m.setDiscoverableName(transformLabelName(tree.from.asIdentifier()));
		m.setNamespace(true);
		$.specifiers().add(m);
		return $;
	}

	/**
	 * @param asIdentifierExpression
	 * @return
	 */
	private ASTNode processName(StructuralPropertyDescriptor property, IdentifierExpressionTree tree) {
		Class<?> claz = classForProperty(property);
		if(tree.identifierToken == null){
			return null;
		}
		SimpleName sn = transformLabelName(tree.identifierToken);
		if(claz.isAssignableFrom(SingleVariableDeclaration.class)){
			SingleVariableDeclaration $ = ast.newSingleVariableDeclaration();
			$.setName(sn);
			return $;
		}
		return sn;
	}

	/**
	 * @param asNewExpression
	 * @return
	 */
	private ASTNode processNewExpression(NewExpressionTree tree) {
		ClassInstanceCreation $ = ast.newClassInstanceCreation();
		transformAndSetProperty($,ClassInstanceCreation.MEMBER_PROPERTY,tree.operand);
		if(tree.arguments != null ){
			for(ParseTree arg : tree.arguments.arguments){
				$.arguments().add(transform(ClassInstanceCreation.ARGUMENTS_PROPERTY,arg));
			}
		}
		return $;
	}

	/**
	 * @param asNull
	 * @return
	 */
	private ASTNode processNull(NullTree tree) {
		return ast.newEmptyStatement();
	}

	
	/**
	 * @param asObjectPattern
	 * @return
	 */
	private ASTNode processObjectPattern(StructuralPropertyDescriptor property, ObjectPatternTree tree) {
		ObjectName $ = ast.newObjectName();
		for(ParseTree child: tree.fields){
			$.objectProperties().add(convertToObjectLiteralField(child));
		}
		if(classForProperty(property).isAssignableFrom(SingleVariableDeclaration.class)){
			SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
			svd.setPattern($);
			return svd;
		}
		return $;
	}
	
	/**
	 * @param asObjectLiteralExpression
	 * @return
	 */
	private ASTNode processObjectLiteral(ObjectLiteralExpressionTree tree) {
		ObjectLiteral $ = ast.newObjectLiteral(); 
		for(ParseTree  elem : tree.propertyNameAndValues){
			ObjectLiteralField f = convertToObjectLiteralField(elem);
			$.fields().add(f);
		}
		return $;
	}

	/**
	 * @param elem
	 * @return
	 */
	private ObjectLiteralField convertToObjectLiteralField(ParseTree elem) {
		ObjectLiteralField f = ast.newObjectLiteralField();
		switch (elem.type) {
			case GET_ACCESSOR:{
				f.setKind(FieldKind.GET);
				final GetAccessorTree getAccessor = elem.asGetAccessor();
				f.setFieldName(transformObjectLitKeyAsString(getAccessor.propertyName));
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,getAccessor);
				break;
			}
			case COMPUTED_PROPERTY_GETTER:{
				f.setKind(FieldKind.GET);
				final ComputedPropertyGetterTree compGetter= elem.asComputedPropertyGetter();
				transformAndSetProperty(f,ObjectLiteralField.FIELD_NAME_PROPERTY,compGetter.property);
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,compGetter);
				break;
			}
			case SET_ACCESSOR:{
				f.setKind(FieldKind.SET);
				final SetAccessorTree setAccessor= elem.asSetAccessor();
				f.setFieldName(transformObjectLitKeyAsString(setAccessor.propertyName));
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,setAccessor);
				break;
			}
			case COMPUTED_PROPERTY_SETTER:{
				f.setKind(FieldKind.SET);
				final ComputedPropertySetterTree compSetter = elem.asComputedPropertySetter();
				transformAndSetProperty(f, ObjectLiteralField.FIELD_NAME_PROPERTY,compSetter.property);
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,compSetter.body);
				break;
			}
			case COMPUTED_PROPERTY_DEFINITION:{
				f.setKind(FieldKind.INIT);
				final ComputedPropertyDefinitionTree compDef = elem.asComputedPropertyDefinition();
				transformAndSetProperty(f, ObjectLiteralField.FIELD_NAME_PROPERTY,compDef.property);
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY, compDef.value);
				break;
			}
			case COMPUTED_PROPERTY_MEMBER_VARIABLE:{
				f.setKind(FieldKind.INIT);
				final ComputedPropertyMemberVariableTree compVariable = elem.asComputedPropertyMemberVariable();
				transformAndSetProperty(f, ObjectLiteralField.FIELD_NAME_PROPERTY, compVariable.property);
				transformAndSetProperty(f, ObjectLiteralField.INITIALIZER_PROPERTY, compVariable.declaredType);
				break;
			}
			case COMPUTED_PROPERTY_METHOD:{
				f.setKind(FieldKind.INIT);
				final ComputedPropertyMethodTree compMethod = elem.asComputedPropertyMethod();
				transformAndSetProperty(f,ObjectLiteralField.FIELD_NAME_PROPERTY,compMethod.property);
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,compMethod);
				break;
			}
			case FUNCTION_DECLARATION:{
				f.setKind(FieldKind.INIT);
				final FunctionDeclarationTree functionDeclaration = elem.asFunctionDeclaration();
				f.setFieldName(transformObjectLitKeyAsString(functionDeclaration.name));
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,functionDeclaration);
				break;
			}
			case DEFAULT_PARAMETER:{
				f.setKind(FieldKind.INIT);
				final DefaultParameterTree defaultTree = elem.asDefaultParameter();
				transformAndSetProperty(f,ObjectLiteralField.FIELD_NAME_PROPERTY,defaultTree.lhs);
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,defaultTree.defaultValue);
				break;
			}
			default :{
				f.setKind(FieldKind.INIT);
				final PropertyNameAssignmentTree assignment = elem.asPropertyNameAssignment();
				f.setFieldName(transformObjectLitKeyAsString(assignment.name));
				transformAndSetProperty(f,ObjectLiteralField.INITIALIZER_PROPERTY,assignment.value);
				break;
			}
		}
		setSourceRange(f,elem);
		return f;
	}



	/**
	 * @param asParenExpression
	 * @return
	 */
	private ASTNode processParenthesizedExpression(ParenExpressionTree tree) {
		ParenthesizedExpression $ = ast.newParenthesizedExpression();
		transformAndSetProperty($, ParenthesizedExpression.EXPRESSION_PROPERTY,tree.expression);
		return $;
	}

	/**
	 * @param asPostfixExpression
	 * @return
	 */
	private ASTNode processPostfixExpression(PostfixExpressionTree tree) {
		PostfixExpression $ = ast.newPostfixExpression();
		transformAndSetProperty($,PostfixExpression.OPERAND_PROPERTY,tree.operand);
		$.setOperator(PostfixExpression.Operator.toOperator(tree.operator.toString()));
		return $;
	}

	/**
	 * @param asMemberExpression
	 * @return
	 */
	private ASTNode processPropertyGet(StructuralPropertyDescriptor property, MemberExpressionTree tree) {
		SimpleName name = null;
		if(tree.memberName != null)
			name = transformLabelName(tree.memberName);
		if(tree.operand == null && classForProperty(property) == SimpleName.class)
			return name;
		FieldAccess $ = ast.newFieldAccess();
		if(name != null){
			$.setName(name);
		}
		transformAndSetProperty($,FieldAccess.EXPRESSION_PROPERTY,tree.operand);
		return $;
	}

	/**
	 * @param asPropertyNameAssignment
	 * @return
	 */
	private ASTNode processPropertyNameAssignment(PropertyNameAssignmentTree tree) {
		VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
		if(tree.name != null)
			vdf.setName(transformLabelName((IdentifierToken) tree.name));
		transformAndSetProperty(vdf,VariableDeclarationFragment.INITIALIZER_PROPERTY,tree.value);
		return ast.newFieldDeclaration(vdf);
	}

	/**
	 * @param asRestParameter
	 * @return
	 */
	private ASTNode processRestParameter(StructuralPropertyDescriptor property, RestParameterTree tree) {
		RestElementName rest = null;
		if(tree.identifier != null){
			rest = ast.newRestElementName();
			rest.setArgument(transformLabelName(tree.identifier));
		}		
		if(classForProperty(property).isAssignableFrom(SingleVariableDeclaration.class)){
			SingleVariableDeclaration $ = ast.newSingleVariableDeclaration();
			if(rest !=null)
				$.setPattern(rest);
			$.setVarargs(true);
			return $;
		}
		return rest;
	}

	/**
	 * @param asReturnStatement
	 * @return
	 */
	private ASTNode processReturnStatement(ReturnStatementTree tree	) {
		ReturnStatement $ = ast.newReturnStatement();
		transformAndSetProperty($,ReturnStatement.EXPRESSION_PROPERTY, tree.expression);
		return $;
	}

	/**
	 * @param asSetAccessor
	 * @return
	 */
	private ASTNode processSetAccessor(StructuralPropertyDescriptor property, SetAccessorTree tree) {
		FunctionDeclaration $ = ast.newFunctionDeclaration();
		$.modifiers().add(ast.newModifier(ModifierKeyword.SET_KEYWORD));
		$.setMethodName(transformObjectLitKeyAsString(tree.propertyName));
		transformAndSetProperty($,FunctionDeclaration.BODY_PROPERTY,tree.body);
		final SingleVariableDeclaration p = ast.newSingleVariableDeclaration();
		if(tree.parameter != null)
			p.setName(transformLabelName(tree.parameter));
		$.parameters().add(p);
		if(tree.isStatic){
			$.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
		}
		return adjustFunctionDeclarationToProperty(property,$);
	}

	/**
	 * @param asSpreadExpression
	 * @return
	 */
	private ASTNode processSpreadExpression(SpreadExpressionTree tree) {
		SpreadElement $ = ast.newSpreadElement();
		transformAndSetProperty($, SpreadElement.ARGUMENT_PROPERTY,tree.expression);
		return $;
	}

	/**
	 * @param asSuperExpression
	 * @return
	 */
	private ASTNode processSuper(SuperExpressionTree tree) {
		//FIXME: we need a better way to handle super references. Simply
		// treating as a name is not enough.
		return ast.newSimpleName(KEYWORD_SUPER);
	}

	/**
	 * @param asCaseClause
	 * @return
	 */
	private ASTNode processSwitchCase(CaseClauseTree tree) {
		// statements of CaseClauseTree are handled on processSwitchStatement()
		SwitchCase $ = ast.newSwitchCase();
		transformAndSetProperty($, SwitchCase.EXPRESSION_PROPERTY,tree.expression);
		return $;
	}

	/**
	 * @param asDefaultClause
	 * @return
	 */
	private ASTNode processSwitchDefault(DefaultClauseTree tree) {
		// statements of DefaultClauseTree are handled on processSwitchStatement()
		SwitchCase $ = ast.newSwitchCase();
		$.setExpression(null);
		return $;
	}

	/**
	 * @param asSwitchStatement
	 * @return
	 */
	private ASTNode processSwitchStatement(SwitchStatementTree tree) {
		SwitchStatement $ = ast.newSwitchStatement();
		transformAndSetProperty($,SwitchStatement.EXPRESSION_PROPERTY,tree.expression);
		for (ParseTree pt : tree.caseClauses) {
			if(pt.type == ParseTreeType.DEFAULT_CLAUSE){
				DefaultClauseTree dct = pt.asDefaultClause();
				$.statements().add(transform(SwitchStatement.STATEMENTS_PROPERTY,dct));
				for (ParseTree dcs : dct.statements) {
					$.statements().add(transform(SwitchStatement.STATEMENTS_PROPERTY, dcs));
				}
			}else{
				CaseClauseTree cct = pt.asCaseClause();
				$.statements().add(transform(SwitchStatement.STATEMENTS_PROPERTY,cct));
				for(ParseTree ccs : cct.statements){
					$.statements().add(transform(SwitchStatement.STATEMENTS_PROPERTY,ccs));
				}
			}
		}
		return $;
	}

	/**
	 * @param asTemplateLiteralExpression
	 * @return
	 */
	private ASTNode processTemplateLiteral(TemplateLiteralExpressionTree tree) {
		TemplateLiteral $ = ast.newTemplateLiteral();
		transformAndSetProperty($,TemplateLiteral.TAG_PROPERTY,tree.operand);
		Iterator<ParseTree> iterator = tree.elements.iterator();
		ParseTree pt = null;
		while(iterator.hasNext()){
			pt = iterator.next();
			if(pt.type == ParseTreeType.TEMPLATE_LITERAL_PORTION){
				final TemplateElement element = (TemplateElement) transform(TemplateLiteral.ELEMENTS_PROPERTY,pt);
				element.setTail(!iterator.hasNext());
				$.elements().add(element);
			}
			else{
				$.expressions().add(transform(TemplateLiteral.EXPRESSIONS_PROPERTY,pt));
			}
		}
		//Add a tail element if there is not one.
		if(pt != null && pt.type != ParseTreeType.TEMPLATE_LITERAL_PORTION){
			TemplateElement el = ast.newTemplateElement();
			el.setSourceRange(pt.location.start.offset,0);
			el.setTail(true);
			$.elements().add(el);
		}
		return $;
	}

	/**
	 * @param asTemplateLiteralPortion
	 * @return
	 */
	private ASTNode processTemplateLiteralPortion(TemplateLiteralPortionTree tree) {
		TemplateElement $ = ast.newTemplateElement();
		safeSetProperty($,TemplateElement.RAW_VALUE_PROPERTY,tree.value.asLiteral().value);
		return $;
	}

	/**
	 * @param asTemplateSubstitution
	 * @return
	 */
	private ASTNode processTemplateSubstitution(TemplateSubstitutionTree tree) {
		return transform(TemplateLiteral.EXPRESSIONS_PROPERTY,tree.expression);
	}

	/**
	 * @param asThisExpression
	 * @return
	 */
	private ASTNode processThisExpression(ThisExpressionTree tree) {
		return ast.newThisExpression(); 
	}

	/**
	 * @param asThrowStatement
	 * @return
	 */
	private ASTNode processThrowStatement(ThrowStatementTree tree) {
		ThrowStatement $ = ast.newThrowStatement();
		transformAndSetProperty($, ThrowStatement.EXPRESSION_PROPERTY,tree.value);
		return $;	
	}

	/**
	 * @param asTryStatement
	 * @return
	 */
	private ASTNode processTryStatement(TryStatementTree tree) {
		TryStatement $ = ast.newTryStatement();
		transformAndSetProperty($,TryStatement.BODY_PROPERTY,tree.body);
		if(tree.catchBlock != null ){
			$.catchClauses().add(transform(TryStatement.CATCH_CLAUSES_PROPERTY,tree.catchBlock));
		}
		transformAndSetProperty($,TryStatement.FINALLY_PROPERTY,tree.finallyBlock);
		return $;
	}

	/**
	 * @param asTypeName
	 * @return
	 */
	private ASTNode processTypeName(TypeNameTree tree) {
		Name $ = null;
		Iterator<String> segmentsIt = tree.segments.iterator();
		while(segmentsIt.hasNext()){
			SimpleName n = ast.newSimpleName(segmentsIt.next());
			if($ == null ){
				$ = n;
			}else{
				$ = ast.newQualifiedName($,n);
			}
		}
		return $;
	}

	/**
	 * @param asUnaryExpression
	 * @return
	 */
	private ASTNode processUnaryExpression(UnaryExpressionTree tree) {
		PrefixExpression $ = ast.newPrefixExpression();
		$.setOperator(PrefixExpression.Operator.toOperator(tree.operator.toString()));
		transformAndSetProperty($,PrefixExpression.OPERAND_PROPERTY,tree.operand);
		return $;
	}

	/**
	 * @param asVariableDeclaration
	 * @return
	 */
	private ASTNode processVariableDeclaration(VariableDeclarationTree tree) {
		VariableDeclarationFragment $ = ast.newVariableDeclarationFragment();
		//TODO: Handle destructuring assignment
		transformAndSetProperty($,VariableDeclarationFragment.PATTERN_PROPERTY,tree.lvalue);
		transformAndSetProperty($,VariableDeclarationFragment.INITIALIZER_PROPERTY,tree.initializer);
		return $;
	}

	/**
	 * @param asVariableDeclarationList
	 * @return
	 */
	private ASTNode processVariableDeclarationList(VariableDeclarationListTree tree) {
		// Only trees with single elements are handled here
		// multiple elements should be handled on their caller 
		// process* methods
		VariableDeclarationExpression $ = ast.newVariableDeclarationExpression((VariableDeclarationFragment) transform(VariableDeclarationExpression.FRAGMENTS_PROPERTY,tree.declarations.get(0)));
		$.setKind(convertVariableKind(tree.declarationType));
		return $;
	}

	/**
	 * @param asVariableStatement
	 * @return
	 */
	private ASTNode processVariableStatement(VariableStatementTree tree) {
		VariableDeclarationStatement $ = ast.newVariableDeclarationStatement();
		switch (tree.declarations.declarationType) {
			case CONST :
				$.setKind(VariableKind.CONST);
				break;
			case LET: 
				$.setKind(VariableKind.LET);
				break;
			default :
				$.setKind(VariableKind.VAR);
				break;
		}
		for(ParseTree decl : tree.declarations.declarations){
			$.fragments().add(transform(VariableDeclarationStatement.FRAGMENTS_PROPERTY,decl));
		}
		attachJSDoc(tree,$);
		return $;
	}

	/**
	 * @param asWhileStatement
	 * @return
	 */
	private ASTNode processWhileLoop(WhileStatementTree tree) {
		WhileStatement $ = ast.newWhileStatement();
		transformAndSetProperty($, WhileStatement.EXPRESSION_PROPERTY, tree.condition);
		transformAndSetProperty($, WhileStatement.BODY_PROPERTY, tree.body);
		return $;
	}

	/**
	 * @param asWithStatement
	 * @return
	 */
	private ASTNode processWithStatement(WithStatementTree tree) {
		WithStatement $ = ast.newWithStatement();
		transformAndSetProperty($,WithStatement.BODY_PROPERTY,tree.body);
		transformAndSetProperty($,WithStatement.EXPRESSION_PROPERTY,tree.expression);
		return $;
	}

	/**
	 * @param asYieldStatement
	 * @return
	 */
	private ASTNode processYield(YieldExpressionTree tree) {
		YieldExpression $ = ast.newYieldExpression();
		$.setDelegate(Boolean.valueOf(tree.isYieldFor));
		transformAndSetProperty($,YieldExpression.ARGUMENT_PROPERTY,tree.expression);
		return $;
	}

	/**
	 * @param node
	 * @param tree
	 */
	private void setSourceRange(ASTNode node, ParseTree tree) {
		int startOffset = tree.location.start.offset;
		if(node instanceof BodyDeclaration ){
			BodyDeclaration bd = (BodyDeclaration)node;
			if(bd.getJavadoc() != null ){
				startOffset = bd.getJavadoc().getStartPosition();
			}
		}else if(node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT){
			VariableDeclarationStatement vs = (VariableDeclarationStatement)node;
			if(vs.getJavadoc() != null){
				startOffset = vs.getJavadoc().getStartPosition();
			}
		}
		node.setSourceRange(startOffset, Math.max(tree.location.end.offset - startOffset, 0));
	}

	/**
	 * @param node
	 * @param tree
	 */
	private void setSourceRange(ASTNode node, Token token) {
		node.setSourceRange(token.location.start.offset, token.location.end.offset - token.location.start.offset);
	}
	
	private Comment handleJsDoc(ParseTree node) {
	    if (!shouldAttachJSDocHere(node)) {
	      return null;
	    }
	    return getJsDoc(node.location);
	 }
	
  private boolean shouldAttachJSDocHere(ParseTree tree) {
	    switch (tree.type) {
	      case EXPRESSION_STATEMENT:
	      case LABELLED_STATEMENT:
	      case EXPORT_DECLARATION:
	        return false;
	      case CALL_EXPRESSION:
	      case CONDITIONAL_EXPRESSION:
	      case BINARY_OPERATOR:
	      case MEMBER_EXPRESSION:
	      case MEMBER_LOOKUP_EXPRESSION:
	      case POSTFIX_EXPRESSION:
	        ParseTree nearest = findNearestNode(tree);
	        if (nearest.type == ParseTreeType.PAREN_EXPRESSION) {
	          return false;
	        }
	        return true;
	      default:
	        return true;
	    }
	  }
	 
	  private Comment getJsDoc(SourceRange location) {
		    Comment closestPreviousComment = null;
		    while (currentComment != null &&
		        currentComment.location.end.offset <= location.start.offset) {
		      if (currentComment.type == Comment.Type.JSDOC) {
		        closestPreviousComment = currentComment;
		      }
		      if (this.nextCommentIter.hasNext()) {
		        currentComment = this.nextCommentIter.next();
		      } else {
		        currentComment = null;
		      }
		    }

		    return closestPreviousComment;
		  }
	  
	  private static ParseTree findNearestNode(ParseTree tree) {
		    while (true) {
		      switch (tree.type) {
		        case EXPRESSION_STATEMENT:
		          tree = tree.asExpressionStatement().expression;
		          continue;
		        case CALL_EXPRESSION:
		          tree = tree.asCallExpression().operand;
		          continue;
		        case BINARY_OPERATOR:
		          tree = tree.asBinaryOperator().left;
		          continue;
		        case CONDITIONAL_EXPRESSION:
		          tree = tree.asConditionalExpression().condition;
		          continue;
		        case MEMBER_EXPRESSION:
		          tree = tree.asMemberExpression().operand;
		          continue;
		        case MEMBER_LOOKUP_EXPRESSION:
		          tree = tree.asMemberLookupExpression().operand;
		          continue;
		        case POSTFIX_EXPRESSION:
		          tree = tree.asPostfixExpression().operand;
		          continue;
		        default:
		          return tree;
		      }
		    }
		  }

	private ASTNode transformBooleanLiteral(Token token) {
		BooleanLiteral $ = ast.newBooleanLiteral(token.type == TokenType.TRUE);
		setSourceRange($,token);
		return $;
	}

	private SimpleName transformLabelName(IdentifierToken token) {
		SimpleName $ = ast.newSimpleName(token.value);
		setSourceRange($, token);
		return $;
	 }

	private ASTNode transformNullLiteral(Token token) {
		NullLiteral $ = ast.newNullLiteral();
		setSourceRange($,token);
		return $;
	}
	
	private ASTNode transformNumberLiteral(Token token) {
        NumberLiteral $ = ast.newNumberLiteral();
        $.internalSetToken(token.asLiteral().value);
        setSourceRange($, token);
        return $;
      }

	private SimpleName transformObjectLitKeyAsString(com.google.javascript.jscomp.parsing.parser.Token token) {
		SimpleName $ = null;
		if (token == null) {
			$ = ast.newSimpleName(""); //$NON-NLS-1$
			setSourceRange($, token);
		}
		else if (token.type == TokenType.IDENTIFIER) {
			$ = transformLabelName(token.asIdentifier());
		}
		else {// literal or number
			$ = ast.newSimpleName(token.asLiteral().value);
			setSourceRange($, token);
		}
		return $;
	}
	
	private ASTNode transformRegExpLiteral(Token token) {
		RegularExpressionLiteral $ = ast.newRegularExpressionLiteral();
		$.internalSetRegularExpression(token.asLiteral().value);
		setSourceRange($,token);
		return $;
	}
	
	private StringLiteral transformStringLiteral(Token token) {
		StringLiteral $ = ast.newStringLiteral();
		$.internalSetEscapedValue(token.asLiteral().value);
		setSourceRange($,token);
		return $;
	}
	
	private void transformAndSetProperty(ASTNode node, StructuralPropertyDescriptor property, ParseTree tree){
		if(tree == null ) return;
		safeSetProperty(node, property, transform(property, tree));
	}
	
	private void safeSetProperty(ASTNode node, StructuralPropertyDescriptor property, Object value){
		if(property.isChildProperty()){
			ChildPropertyDescriptor cp = (ChildPropertyDescriptor)property;
			if(cp.isMandatory() && value == null ){
				return;
			}
		}
		node.setStructuralProperty(property, value);
	}
	
	/**
	 * @param property
	 */
	private Class<?> classForProperty(StructuralPropertyDescriptor property) {
		if(property.isChildProperty()){
			ChildPropertyDescriptor cp = (ChildPropertyDescriptor)property;
			return cp.getChildType();
		}
		if (property.isChildListProperty() ){
			ChildListPropertyDescriptor cl = (ChildListPropertyDescriptor) property;
			return cl.getElementType();
		}
		if(property.isSimpleProperty()){
			SimplePropertyDescriptor sp = (SimplePropertyDescriptor) property;
			sp.getValueType();
		}
		return Object.class;
	}
	
}
