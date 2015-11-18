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

import static org.eclipse.wst.jsdt.core.dom.ASTNode.*;

import java.util.Stack;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.core.UnimplementedException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayName;
import org.eclipse.wst.jsdt.core.dom.ArrowFunctionExpression;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.AssignmentName;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BreakStatement;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
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
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.ImportDeclaration;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LabeledStatement;
import org.eclipse.wst.jsdt.core.dom.ListExpression;
import org.eclipse.wst.jsdt.core.dom.MetaProperty;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.wst.jsdt.core.dom.ModuleSpecifier;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField.FieldKind;
import org.eclipse.wst.jsdt.core.dom.ObjectName;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.ProgramElement;
import org.eclipse.wst.jsdt.core.dom.RestElementName;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.SpreadElement;
import org.eclipse.wst.jsdt.core.dom.Statement;
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
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableKind;
import org.eclipse.wst.jsdt.core.dom.WhileStatement;
import org.eclipse.wst.jsdt.core.dom.WithStatement;
import org.eclipse.wst.jsdt.core.dom.YieldExpression;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Converts ESTree AST to DOM AST.
 * 
 * @author Gorkem Ercan
 *
 */
public class DOMASTConverter extends EStreeVisitor{

	private final AST ast;
	private Stack<ASTNode> nodes = new Stack<ASTNode>();
	private final JavaScriptUnit root;
	// Because switch also hosts all the statements in the case
	// statement we need to pointer to hold the currently processed switch
	private Stack<SwitchStatement> processingSwitchStatements = new Stack<SwitchStatement>();
 	
	/**
	 * 
	 */
	public DOMASTConverter(final JavaScriptUnit unit) {
		if(unit == null ){
			throw new IllegalArgumentException();
		}
		this.root = unit;
		this.ast = unit.getAST();
	}
	
	public JavaScriptUnit convert(ScriptObjectMirror jsobject){
		this.traverse(jsobject);
		Assert.isTrue(nodes.empty(),"Some nodes are not processed"); //$NON-NLS-1$
		return root;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.esprima.EStreeVisitor#visit(jdk.nashorn.api.scripting.ScriptObjectMirror, org.eclipse.wst.jsdt.internal.esprima.ESTreeNodeTypes)
	 */
	public VisitOptions visit(final ScriptObjectMirror object, final ESTreeNodeTypes nodeType, final String key) {
		//Generate the dom AST objects and push it to stack.
		switch(nodeType){
			case Program:
				return convertProgram(object);
			case Identifier:
				return convertIdentifier(object);
			case Literal:
				return convertLiteral(object);
			// Declarations	
			case VariableDeclaration: 
				return convertVariableDeclaration(object);
			case VariableDeclarator:
				return convertVariableDeclarator(object);
			case FunctionDeclaration:
				return convertFunctionDeclaration(object);
		    //Statements
			case ExpressionStatement:
				return convertExpressionStatement(object);
			case BlockStatement:
				return convertBlockStatement(object);
			case EmptyStatement:
				return convertEmptyStatement(object);
			case DebuggerStatement:
				return convertDebuggerStatememt(object);
			case WithStatement:
				return convertWithStatement(object);
			case ReturnStatement:
				return convertReturnStatement(object);
			case LabeledStatement:
				return converLabeledStatement(object);
			case BreakStatement:
				return convertBreakStatement(object);
			case ContinueStatement:
				return convertContinueStatement(object);
			case IfStatement:
				return convertIfStatement(object);
			case SwitchStatement:
				return convertSwitchStatement(object);
			case SwitchCase:
				return convertSwitchCaseStatement(object);
			case ThrowStatement:
				return convertThrowStatement(object);
			case TryStatement:
				return convertTryStatement(object);
			case CatchClause:
				return convertCatchClause(object);
			case WhileStatement:
				return convertWhileStatement(object);
			case DoWhileStatement:
				return convertDoWhileStatement(object);
			case ForStatement:
				return convertForStatement(object);
			case ForInStatement:
				return convertForInStatement(object);
			case ForOfStatement:
				return convertForOfStatement(object);
			case ClassDeclaration:
				return convertClassDeclaration(object);
			case ClassBody: 
				// ClassBody is represented with a single TypeDeclaration 
				// pushing the TypeDeclaration created for ClassDeclaration
				// twice to stack so that endVisit does not fail.
				return VisitOptions.CONTINUE;
			case MethodDefinition:
				return convertMethodDefinition(object);
			// Expressions	
			case ThisExpression:
				return convertThisExpression(object);
			case ArrayExpression:
				return convertArrayExpression(object);
			case ObjectExpression:
				return convertObjectExpression(object);
			case Property:// related to ObjectExpression
				return convertPropertyExpression(object);
			case FunctionExpression:
				return convertFunctionExpression(object);
			case UnaryExpression://intentional
			case UpdateExpression:
				return convertUnaryOperation(object);
			case BinaryExpression: //intentional
			case LogicalExpression:
				return convertBinaryExpression(object);
			case AssignmentExpression:
				return convertAssignmentExpression(object);
			case MemberExpression:
				return convertMemberExpression(object);
			case ConditionalExpression:
				return convertConditionalExpression(object);
			case CallExpression:
				return convertCallExpression(object);
			case SequenceExpression:
				return convertSequenceExpression(object);
			case YieldExpression:
				return convertYieldExpression(object);
			case NewExpression:
				return convertNewExpression(object);
			case ArrowFunctionExpression:
				return convertArrowFunctionExpression(object);
			case Super://Skip super handled on callExpression
				return VisitOptions.CONTINUE;
			case RestElement:
				return convertRestElement(object);
			case ArrayPattern:
				return convertArrayPattern(object);
			case ObjectPattern:
				return convertObjectPattern(object);
			case TemplateLiteral:
			case TaggedTemplateExpression:
				return convertTemplateLiteral(object,key);
			case TemplateElement:
				return convertTemplateElement(object);
			case AssignmentPattern:
				return convertAssignmentPattern(object);
			case SpreadElement:
				return convertSpreadElement(object);
			case MetaProperty:
				return convertMetaProperty(object);
			case ImportDeclaration:
				return convertImportDeclaration(object);
			case ImportSpecifier:
				return convertImportSpecifer(object, false, false);
			case ImportDefaultSpecifier:
				return convertImportSpecifer(object, true, false);
			case ImportNamespaceSpecifier:
				return convertImportSpecifer(object, true, false);
			case ExportNamedDeclaration:
				return convertExportDeclaration(object, false, false);
			case ExportSpecifier:
				return convertExportSpecifier(object);
			case ExportAllDeclaration: 
				return convertExportDeclaration(object, false, true);
			case ExportDefaultDeclaration:
				return convertExportDeclaration(object, true, false);
			default:
				throw new UnimplementedException(nodeType.getTypeString() + " conversion is not implemented"); //$NON-NLS-1$
		}
	}




	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.esprima.EStreeVisitor#endVisit(jdk.nashorn.api.scripting.ScriptObjectMirror, org.eclipse.wst.jsdt.internal.esprima.ESTreeNodeTypes)
	 */
	public VisitOptions endVisit(final ScriptObjectMirror object, final ESTreeNodeTypes nodeType, final String key) {
		
		if(nodeType == ESTreeNodeTypes.Super){//Skip nothing is pushed for these nodes during visit
			return VisitOptions.CONTINUE;
		}
		//Read the dom ast model objects from stack and connect them to parent.
		ASTNode current = nodes.pop();
		//Set source range for all the nodes.
		//we can safely move this to visit
		setRange(object, current);
		ASTNode parent = null;
		if( nodeType != ESTreeNodeTypes.Program){ //Nodes can be empty only for Program
			Assert.isTrue(!nodes.empty());
			parent = nodes.peek();
		}
		
		try{
			//clean-up the switch statement
			if(current.getNodeType() == SWITCH_STATEMENT){
				processingSwitchStatements.pop();
			}
			if(current.getNodeType() == MODULE_SPECIFIER){
				assignModuleSpecifier((ModuleSpecifier)current, parent);
			}else
			if(current instanceof Expression ){
				assignExpressionToParent((Expression)current, parent, key);
			}else
			if(current instanceof ProgramElement){
				assignStatementToParent((ProgramElement)current,parent, key);
			}else
			if(current instanceof VariableDeclaration){
				assignVariableDeclarationToParent((VariableDeclaration)current, parent);
			}else
			if(current.getNodeType() == CATCH_CLAUSE){
				assignCatchToTry(current, parent);
			}else
			if(current.getNodeType() == TEMPLATE_ELEMENT){
				assingTemplateElement(current, parent);
			}else 
			if(current.getNodeType() == IMPORT_DECLARATION){
				root.imports().add(current);
			}else
			if(current.getNodeType() == EXPORT_DECLARATION){
				root.exports().add(current);
			}
			
		}catch(Exception e){
			StringBuilder sb = new StringBuilder(e.toString());
			sb.append(" assigning "); //$NON-NLS-1$
			sb.append(current.getClass().getSimpleName());
			sb.append("["); //$NON-NLS-1$
			sb.append(current);
			sb.append("]"); //$NON-NLS-1$
			if( parent!=null ){
				sb.append(" to "); //$NON-NLS-1$
				sb.append(parent.getClass().getSimpleName());
				sb.append("["); //$NON-NLS-1$
				sb.append(parent);
				sb.append("]"); //$NON-NLS-1$
			}
			System.out.println(sb);
			throw e;
		}

		return VisitOptions.CONTINUE;
	}

	private void assignModuleSpecifier(ModuleSpecifier module, ASTNode parent) {
		switch(parent.getNodeType()){
			case IMPORT_DECLARATION:
				ImportDeclaration importDec = (ImportDeclaration) parent;
				importDec.specifiers().add(module);
				break;
			case EXPORT_DECLARATION:
				ExportDeclaration exportDec = (ExportDeclaration)parent;
				exportDec.specifiers().add(module);
				break;
			default:
				throw new UnimplementedException("Assigning "+ module + " to "+parent+ " is not handled");	

		}
		
	}

	private void assingTemplateElement(ASTNode current, ASTNode parent) {
		TemplateLiteral tl = (TemplateLiteral)parent;
		tl.elements().add(current);
	}

	
	private void setRange(final ScriptObjectMirror object, final ASTNode node){
		Object o = object.getMember("range");
		if( !ScriptObjectMirror.isUndefined(o) ){
			ScriptObjectMirror range = (ScriptObjectMirror)o;
			Number x = (Number) range.getSlot(0);
			Number y = (Number) range.getSlot(1);
			node.setSourceRange(x.intValue(), y.intValue()-x.intValue());
		}
	}
	
	private void assignStatementToParent(final ProgramElement statement, final ASTNode parent, final String key){
		switch(parent.getNodeType()){
			case JAVASCRIPT_UNIT:
				JavaScriptUnit unit = (JavaScriptUnit)parent;
				unit.statements().add(statement);
				break;
			case FUNCTION_EXPRESSION:
				FunctionExpression fe = (FunctionExpression)parent;
				//assign to contained FunctionDeclaration
				fe.getMethod().setBody((Block) statement);
				break;
			case FUNCTION_DECLARATION:
				FunctionDeclaration fdec = (FunctionDeclaration)parent;
				fdec.setBody((Block)statement);
				break;
			case ARROW_FUNCTION_EXPRESSION:
				ArrowFunctionExpression af = (ArrowFunctionExpression) parent;
				af.setBody((Block)statement);
				break;
			case BLOCK:
				Block b = (Block) parent;
				b.statements().add(statement);
				break;
			case WITH_STATEMENT:
				WithStatement ws = (WithStatement)parent;
				ws.setBody((Statement)statement);
				break;
			case LABELED_STATEMENT:
				LabeledStatement ls = (LabeledStatement)parent;
				ls.setBody((Statement)statement);
				break;
			case IF_STATEMENT:
				IfStatement is = (IfStatement)parent;
				if(key!= null && key.equals("consequent")){
					is.setElseStatement((Statement)statement);
				}else{
					is.setThenStatement((Statement)statement);
				}
				break;
			case SWITCH_STATEMENT:
				SwitchStatement ss  = (SwitchStatement) parent;
				if(statement.getNodeType() != SWITCH_CASE){
					// case is added during #convertSwitchCase to keep order.
					ss.statements().add(statement);
				}
				break;
			case SWITCH_CASE:
				// all statements on the switchcase goes into switch.
				processingSwitchStatements.peek().statements().add(statement);
				break;
			case CATCH_CLAUSE:
				CatchClause cc = (CatchClause)parent;
				cc.setBody((Block)statement);
				break;
			case TRY_STATEMENT:
				TryStatement ts = (TryStatement)parent;
				if("block".equals(key)){
					ts.setBody((Block)statement);
				}else{
					ts.setFinally((Block)statement);
				}
				break;
			case WHILE_STATEMENT:
				WhileStatement whileS = (WhileStatement)parent;
				whileS.setBody((Statement)statement);
				break;
			case DO_STATEMENT:
				DoStatement ds = (DoStatement)parent;
				ds.setBody((Statement)statement);
				break;
			case FOR_STATEMENT:
				ForStatement fs = (ForStatement)parent;
				fs.setBody((Statement) statement);
				break;
			case FOR_IN_STATEMENT:
				ForInStatement fis = (ForInStatement)parent;
				if("left".equals(key)){
					fis.setIterationVariable((Statement) statement);
				}else if("body".equals(key)){
					fis.setBody((Statement) statement);
				}
				break;
			case FOR_OF_STATEMENT:
				ForOfStatement fos = (ForOfStatement)parent;
				if("left".equals(key)){
					fos.setIterationVariable((Statement) statement);
				}else if("body".equals(key)){
					fos.setBody((Statement) statement);
				}
				break;
			case TYPE_DECLARATION:
				TypeDeclaration typeDec = (TypeDeclaration) parent;
				typeDec.bodyDeclarations().add(statement);
				break;
			case TYPE_DECLARATION_STATEMENT:
				//TypeDeclaration is already assigned during convert.
				break;
			case EXPORT_DECLARATION:
				ExportDeclaration edec = (ExportDeclaration) parent;
				if("declaration".equals(key)){
					edec.setDeclaration(statement);
				}
				break;
			default:
				throw new UnimplementedException("Assigning "+statement + " to "+parent+ " is not handled");	
				}
	}
	
	
	private void assignVariableDeclarationToParent(VariableDeclaration declaration, ASTNode parent) {
		switch(parent.getNodeType()){
			
			case VARIABLE_DECLARATION_STATEMENT:
				VariableDeclarationStatement vd = (VariableDeclarationStatement)parent;
				vd.fragments().add(declaration);
				break;
			case FUNCTION_EXPRESSION:
				FunctionExpression fe = (FunctionExpression)parent;
				fe.getMethod().parameters().add((SingleVariableDeclaration)declaration);
				break;
			case VARIABLE_DECLARATION_EXPRESSION:
				VariableDeclarationExpression ve = (VariableDeclarationExpression)parent;
				ve.fragments().add(declaration);
				break;
			case FUNCTION_DECLARATION:
				FunctionDeclaration fd = (FunctionDeclaration)parent;
				fd.parameters().add(declaration);
				break;
			default:
				throw new UnimplementedException("Assigning "+ declaration + " to "+parent+ " is not handled");	
		}
		
	}
	
	private void assignExpressionToParent(final Expression expression, final ASTNode parent, final String key ){
		
		switch (parent.getNodeType()) {
			case EXPRESSION_STATEMENT :
				((ExpressionStatement)parent).setExpression(expression);
				break;
			case ASSIGNMENT:
				if(key != null && key.equals("left")){
					((Assignment)parent).setLeftHandSide(expression);
				}else{
					((Assignment)parent).setRightHandSide(expression);
				}
				break;
			case VARIABLE_DECLARATION_FRAGMENT:
				if(expression.getNodeType() == SIMPLE_NAME){
					((VariableDeclarationFragment)parent).setName((SimpleName)expression);	
				}else{
					((VariableDeclarationFragment)parent).setInitializer(expression);
				}
				break;
			case ARRAY_INITIALIZER:
				((ArrayInitializer)parent).expressions().add(expression);
				break;
			case OBJECT_LITERAL:
				((ObjectLiteral)parent).fields().add(expression);
				break;
			case OBJECT_LITERAL_FIELD:
				ObjectLiteralField olf = (ObjectLiteralField)parent;
				if("key".equals(key)){
					olf.setFieldName(expression);
				}else 
				if("value".equals(key)){
					olf.setInitializer(expression);					
				}
				break;
			case FUNCTION_EXPRESSION:
				FunctionExpression fe = (FunctionExpression)parent;
				//assign to contained FunctionDeclaration
				if("params".equals(key)){
					SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
					svd.setPattern((Name) expression);
					fe.getMethod().parameters().add(svd);
				}else{
					fe.getMethod().setName((SimpleName) expression);
				}
				break;
			case FUNCTION_DECLARATION:
				FunctionDeclaration fdec = (FunctionDeclaration)parent;
				if("params".equals(key)){
					SingleVariableDeclaration svd  = ast.newSingleVariableDeclaration();
					svd.setPattern((Name) expression);
					fdec.parameters().add(svd);
				}
				//value and key applies to  MethodDefiniton
				if("value".equals(key)){
					FunctionExpression fex = (FunctionExpression)expression;
					Block b = (Block) ASTNode.copySubtree(ast, fex.getMethod().getBody());
					fdec.setBody(b);
				}
				if("key".equals(key)){
					fdec.setMethodName(expression);
				}
				if("id".equals(key)){
					fdec.setMethodName(expression);
				}
				break;
			case POSTFIX_EXPRESSION:
				((PostfixExpression)parent).setOperand(expression);
				break;
			case PREFIX_EXPRESSION:
				((PrefixExpression)parent).setOperand(expression);
				break;
			case INFIX_EXPRESSION:
				if(key != null && key.equals("left")){
					((InfixExpression)parent).setLeftOperand(expression);
				}else{
					((InfixExpression)parent).setRightOperand(expression);
				}
				break;
			case FIELD_ACCESS:
				if(key != null && key.equals("object")){
					((FieldAccess)parent).setExpression(expression);
				}else{
					((FieldAccess)parent).setName((SimpleName)expression);
				}
				break;
			case ARRAY_ACCESS:
				if(key != null && key.equals("object")){
					((ArrayAccess)parent).setArray(expression);
				}else{
					((ArrayAccess)parent).setIndex(expression);
				}
				break;
			case CONDITIONAL_EXPRESSION:
				ConditionalExpression conditional = (ConditionalExpression)parent;
				if(key!= null && key.equals("test") ){
					conditional.setExpression(expression);
				}else if("consequent".equals(key)){
					conditional.setThenExpression(expression);
				}else{
					conditional.setElseExpression(expression);
				}
				break;
			case FUNCTION_INVOCATION:
				FunctionInvocation fi = (FunctionInvocation)parent;
				if("callee".equals(key)){
					fi.setExpression(expression);
				}else
				if("arguments".equals(key)){
					fi.arguments().add(expression);
				}
				break;
			case SUPER_METHOD_INVOCATION:
				SuperMethodInvocation smi = (SuperMethodInvocation)parent;
				if("arguments".equals(key)){
					smi.arguments().add(expression);
				}
				break;
			case LIST_EXPRESSION:
				((ListExpression)parent).expressions().add(expression);
				break;
			case YIELD_EXPRESSION:
				((YieldExpression)parent).setArgument(expression);
				break;
			case CLASS_INSTANCE_CREATION:
				ClassInstanceCreation ci = (ClassInstanceCreation)parent;
				if(key != null && key.equals("callee")){
					ci.setMember(expression);
				}else
				{
					ci.arguments().add(expression);
				}
				break;
			case ARROW_FUNCTION_EXPRESSION:
				ArrowFunctionExpression af = (ArrowFunctionExpression)parent;
				if(key!= null && key.equals("params")){
					SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
					svd.setPattern((Name) expression);
					af.parameters().add(svd);
				}else{
					af.setExpression(expression);
				}
				break;
			case WITH_STATEMENT:
				WithStatement ws = (WithStatement)parent;
				ws.setExpression(expression);
				break;
			case RETURN_STATEMENT:
				ReturnStatement rs = (ReturnStatement)parent;
				rs.setExpression(expression);
				break;
			case LABELED_STATEMENT:
				LabeledStatement ls = (LabeledStatement)parent;
				ls.setLabel((SimpleName)expression);
				break;
			case BREAK_STATEMENT:
				BreakStatement bs = (BreakStatement)parent;
				bs.setLabel((SimpleName)expression);
				break;
			case CONTINUE_STATEMENT:
				ContinueStatement cs = (ContinueStatement)parent;
				cs.setLabel((SimpleName)expression);
				break;
			case IF_STATEMENT:
				IfStatement is = (IfStatement)parent;
				is.setExpression(expression);
				break;
			case SWITCH_STATEMENT:
				SwitchStatement ss = (SwitchStatement)parent;
				ss.setExpression(expression);
				break;
			case SWITCH_CASE:
				SwitchCase sc = (SwitchCase) parent;
				if(key != null && key.equals("test")){
					sc.setExpression(expression);
				}
				break;
			case THROW_STATEMENT:
				ThrowStatement ts = (ThrowStatement)parent;
				ts.setExpression(expression);
				break;
			case CATCH_CLAUSE:
				CatchClause c =(CatchClause) parent;
				SingleVariableDeclaration d = ast.newSingleVariableDeclaration();
				d.setPattern((Name) expression);
				c.setException(d);
				break;
			case WHILE_STATEMENT:
				WhileStatement whileS = (WhileStatement)parent;
				whileS.setExpression(expression);
				break;
			case DO_STATEMENT:
				DoStatement ds = (DoStatement)parent;
				ds.setExpression(expression);
				break;
			case FOR_STATEMENT:
				ForStatement fs=(ForStatement)parent;
				if("test".equals(key)){
					fs.setExpression(expression);
				}else if("init".equals(key))
				{
					fs.initializers().add(expression);
				}else if("update".equals(key)){
					fs.updaters().add(expression);
				}
				break;
			case FOR_IN_STATEMENT:
				ForInStatement fis = (ForInStatement)parent;
				if("left".equals(key)){
					fis.setIterationVariable(ast.newExpressionStatement(expression));
				}else if("right".equals(key)){
					fis.setCollection(expression);
				}
				break;
			case FOR_OF_STATEMENT:
				ForOfStatement fos = (ForOfStatement)parent;
				if("left".equals(key)){
					fos.setIterationVariable(ast.newExpressionStatement(expression));
				}else if("right".equals(key)){
					fos.setCollection(expression);
				}
				break;
			case TYPE_DECLARATION:
				TypeDeclaration td = (TypeDeclaration) parent;
				if("superClass".equals(key)){
					td.setSuperclassExpression(expression);
				}else if("id".equals(key)){
					td.setName((SimpleName) expression);
				}
				break;
			case TYPE_DECLARATION_STATEMENT:
				break;
			case ARRAY_NAME:
				ArrayName an = (ArrayName) parent;
				an.elements().add(expression);
				break;
			case OBJECT_NAME:
				ObjectName on = (ObjectName)parent;
				on.objectProperties().add(expression);
				break;
			case TEMPLATE_LITERAL:
				TemplateLiteral tl = (TemplateLiteral)parent;
				if("expressions".equals(key)){
					tl.expressions().add(expression);
				}else
				if("tag".equals(key)){
					tl.setTag(expression);
				}
				break;
			case ASSIGNMENT_NAME:
				AssignmentName asn = (AssignmentName)parent;
				if("right".equals(key)){
					asn.setRight(expression);
				}else
				if("left".equals(key)){
					asn.setLeft((Name) expression);
				}
				break;
			case REST_ELEMENT_NAME:
				RestElementName ren = (RestElementName) parent;
				if("argument".equals(key)){
					ren.setArgument((Name) expression);
				}
				break;
			case SPREAD_ELEMENT:
				SpreadElement sel= (SpreadElement) parent;
				if("argument".equals(key)){
					sel.setArgument(expression);
				}
				break;
			case IMPORT_DECLARATION: 
				ImportDeclaration idec = (ImportDeclaration) parent;
				idec.setSource((StringLiteral)expression);
				break;
			case MODULE_SPECIFIER:
				ModuleSpecifier specifier = (ModuleSpecifier) parent;
				if("local".equals(key)){
					specifier.setLocal((SimpleName) expression);
				}
				if("imported".equals(key)){
					specifier.setDiscoverableName((SimpleName) expression);
				}
				break;
			case EXPORT_DECLARATION:
				ExportDeclaration edec = (ExportDeclaration) parent;
				if("source".equals(key)){
					edec.setSource((StringLiteral) expression);
				}
				break;
			default :
				throw new UnimplementedException("Assigning "+expression + " to "+parent+ " is not handled");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}
	}

	private void assignCatchToTry(ASTNode current, ASTNode parent) {
		TryStatement ts = (TryStatement) parent;
		ts.catchClauses().add(current);
	}
	
	private VisitOptions convertLiteral(final ScriptObjectMirror object) {
		Object value = object.getMember("value");
		String raw = (String)object.getMember("raw");
		Expression literal = null;
		if(value instanceof Number ){
			literal = ast.newNumberLiteral(raw);
		}else
		if(value instanceof Boolean){
			literal = ast.newBooleanLiteral(raw);
		}else
		if(value instanceof String){
			literal = ast.newStringLiteral(raw);
		}else
		if(object.hasMember("regex")){
			literal = ast.newRegularExpressionLiteral(raw);
		}else
		if(value == null ){
			literal = ast.newNullLiteral();
		}
		
		if(literal == null ){
			throw new UnimplementedException("Failed to translate Literal " + value); //$NON-NLS-1$
		}
		else{
			nodes.push(literal);
		}
		return VisitOptions.CONTINUE;
	}
	 
	private VisitOptions convertProgram(final ScriptObjectMirror object){
		nodes.push(root);	
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertVariableDeclaration(final ScriptObjectMirror object) {
		String kind = (String) object.getMember("kind");
		VariableKind variableKind = VariableKind.VAR;
		if(kind.equals("let")){
			variableKind = VariableKind.LET;
		}else if(kind.equals("const")){
			variableKind = VariableKind.CONST;
		}
		int parentType = nodes.peek().getNodeType();
		if(parentType == FOR_STATEMENT ){// For statements use expression
			final VariableDeclarationExpression e = ast.newVariableDeclarationExpression();	
			e.setKind(variableKind);
			nodes.push(e);
		}else{
			final VariableDeclarationStatement e = ast.newVariableDeclarationStatement();
			e.setKind(variableKind);
			nodes.push(e);
		}
		return VisitOptions.CONTINUE;	
	}
	
	private VisitOptions convertVariableDeclarator(final ScriptObjectMirror object) {
		final VariableDeclarationFragment f  = ast.newVariableDeclarationFragment();
		nodes.push(f);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertIdentifier(final ScriptObjectMirror object) {
		final String s = (String)object.getMember("name");
		SimpleName name = null;
		try{
			name = ast.newSimpleName(s);
		}catch(IllegalArgumentException e){
			// SimpleName does not allow keywords 
			// try propertyName.
			name = ast.newPropertyName(s);
		}
		nodes.push(name);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertExpressionStatement(final ScriptObjectMirror object) {
		final ExpressionStatement es = ast.newExpressionStatement();
		nodes.push(es);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertAssignmentExpression(final ScriptObjectMirror object) {
		final Assignment a = ast.newAssignment();
		final String op = (String) object.getMember("operator");
		a.setOperator(Assignment.Operator.toOperator(op));
		nodes.push(a);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertThisExpression(final ScriptObjectMirror object) {
		final ThisExpression t = ast.newThisExpression();
		nodes.push(t);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertArrayExpression(final ScriptObjectMirror object) {
		ArrayInitializer ai = ast.newArrayInitializer();
		nodes.push(ai);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertObjectExpression(final ScriptObjectMirror object) {
		ObjectLiteral o = ast.newObjectLiteral();
		nodes.push(o);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertPropertyExpression(final ScriptObjectMirror object) {
		ObjectLiteralField of = ast.newObjectLiteralField();
		String kind = (String) object.getMember("kind");
		FieldKind k = null;
		if("init".equals(kind)){
			k = FieldKind.INIT;
		}else if("get".equals(kind)){
			k = FieldKind.GET;
		}else if("set".equals(kind)){
			k = FieldKind.SET;
		}
		of.setKind(k);
		nodes.push(of);
		return VisitOptions.CONTINUE; 
	}
	
	private VisitOptions convertFunctionExpression(final ScriptObjectMirror object) {
		// We break from the usual pattern and create a functionDeclaration. 
		// has a single object type for function expression while DOM AST 
		// needs two objects for the same result. All further assignments needs 
		// to handle this properly
		FunctionExpression fe = ast.newFunctionExpression();
		FunctionDeclaration d = ast.newFunctionDeclaration();
		Boolean isGenerator = (Boolean)object.getMember("generator");
		d.setGenerator(isGenerator.booleanValue());
		fe.setMethod(d);
		nodes.add(fe);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertFunctionDeclaration(final ScriptObjectMirror object) {
		FunctionDeclaration dec = ast.newFunctionDeclaration();
		Boolean isGenerator = (Boolean)object.getMember("generator");
		dec.setGenerator(isGenerator.booleanValue());
		nodes.push(dec);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertUnaryOperation(final ScriptObjectMirror object) {
		final Boolean isPrefix = (Boolean) object.getMember("prefix");
		final String operator = (String) object.getMember("operator");
		if(isPrefix){
			PrefixExpression pe= ast.newPrefixExpression();
			pe.setOperator( PrefixExpression.Operator.toOperator(operator));
			nodes.push(pe);
		}else{
			PostfixExpression po = ast.newPostfixExpression();
			po.setOperator( PostfixExpression.Operator.toOperator(operator));
			nodes.push(po);
		}
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertBinaryExpression(final ScriptObjectMirror object) {
		final String operator = (String) object.getMember("operator");
		InfixExpression ie = ast.newInfixExpression();
		ie.setOperator(InfixExpression.Operator.toOperator(operator));
		nodes.push(ie);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertMemberExpression(final ScriptObjectMirror object) {
		Boolean computed = (Boolean)object.getMember("computed");
		if(computed){
			ArrayAccess aa = ast.newArrayAccess();
			nodes.push(aa);
		}else{
			FieldAccess fa = ast.newFieldAccess();
			nodes.push(fa);
		}
		return VisitOptions.CONTINUE; 
	}
	
	private VisitOptions convertConditionalExpression(final ScriptObjectMirror object) {
		ConditionalExpression ce = ast.newConditionalExpression();
		nodes.push(ce);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertCallExpression(final ScriptObjectMirror object) {
		ScriptObjectMirror callee = (ScriptObjectMirror) object.getMember("callee");
		String type = (String) callee.getMember("type");
		if("Super".equals(type)){
			SuperMethodInvocation smi = ast.newSuperMethodInvocation();
			nodes.push(smi);
		}else{
			FunctionInvocation fi = ast.newFunctionInvocation();
			nodes.push(fi);
		}
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertSequenceExpression(final ScriptObjectMirror object) {
		ListExpression le = ast.newListExpression();
		nodes.push(le);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertYieldExpression(final ScriptObjectMirror object) {
		Boolean isDelegate = (Boolean)object.getMember("delegate");
		YieldExpression ye = ast.newYieldExpression();
		ye.setDelegate(isDelegate);
		nodes.push(ye);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertNewExpression(final ScriptObjectMirror object) {
		ClassInstanceCreation ci = ast.newClassInstanceCreation();
		nodes.push(ci);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertArrowFunctionExpression(final ScriptObjectMirror object) {
		ArrowFunctionExpression af = ast.newArrowFunctionExpression();
		nodes.push(af);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertBlockStatement(final ScriptObjectMirror object) {
		Block b = ast.newBlock();
		nodes.push(b);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertEmptyStatement(final ScriptObjectMirror object) {
		EmptyStatement es = ast.newEmptyStatement();
		nodes.push(es);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertDebuggerStatememt(final ScriptObjectMirror object) {
		DebuggerStatement ds = ast.newDebuggerStatement();
		nodes.push(ds);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertWithStatement(final ScriptObjectMirror object) {
		WithStatement ws = ast.newWithStatement();
		nodes.push(ws);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertReturnStatement(final ScriptObjectMirror object) {
		ReturnStatement rs = ast.newReturnStatement();
		nodes.push(rs);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions converLabeledStatement(final ScriptObjectMirror object) {
		LabeledStatement ls = ast.newLabeledStatement();
		nodes.push(ls);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertBreakStatement(final ScriptObjectMirror object) {
		BreakStatement bs = ast.newBreakStatement();
		nodes.push(bs);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertContinueStatement(final ScriptObjectMirror object) {
		ContinueStatement cs = ast.newContinueStatement();
		nodes.push(cs);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertIfStatement(final ScriptObjectMirror object) {
		IfStatement is = ast.newIfStatement();
		nodes.push(is);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertSwitchStatement(final ScriptObjectMirror object) {
		SwitchStatement ss = ast.newSwitchStatement();
		this.processingSwitchStatements.push(ss);
		nodes.push(ss);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertSwitchCaseStatement(final ScriptObjectMirror object) {
		SwitchCase sc = ast.newSwitchCase();
		nodes.push(sc);
		if(processingSwitchStatements.empty() ){
			throw new IllegalStateException("Case statement without a switch");
		}
		//add the case to switch statement here so that the order is correct
		processingSwitchStatements.peek().statements().add(sc);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertThrowStatement(final ScriptObjectMirror object) {
		ThrowStatement ts = ast.newThrowStatement();
		nodes.push(ts);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertCatchClause(final ScriptObjectMirror object) {
		CatchClause cc = ast.newCatchClause();
		nodes.push(cc);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertTryStatement(final ScriptObjectMirror object) {
		TryStatement ts = ast.newTryStatement();
		nodes.push(ts);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertWhileStatement(final ScriptObjectMirror object) {
		WhileStatement ws = ast.newWhileStatement();
		nodes.push(ws);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertDoWhileStatement(final ScriptObjectMirror object) {
		DoStatement ds = ast.newDoStatement();
		nodes.push(ds);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertForStatement(final ScriptObjectMirror object) {
		ForStatement fs = ast.newForStatement();
		nodes.push(fs);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertForInStatement(final ScriptObjectMirror object) {
		ForInStatement fi = ast.newForInStatement();
		nodes.push(fi);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertForOfStatement(final ScriptObjectMirror object) {
		ForOfStatement fo = ast.newForOfStatement();
		nodes.push(fo);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertClassDeclaration(final ScriptObjectMirror object) {
		AbstractTypeDeclaration td = ast.newTypeDeclaration();
		TypeDeclarationStatement tds = ast.newTypeDeclarationStatement(td);
		nodes.push(tds);
		nodes.push(td);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertMethodDefinition(final ScriptObjectMirror object) {
		FunctionDeclaration fd = ast.newFunctionDeclaration();
		Boolean isStatic = (Boolean)object.getMember("static");
		if(isStatic){
			Modifier staticModifier = ast.newModifier(ModifierKeyword.STATIC_KEYWORD);
			fd.modifiers().add(staticModifier);
		}
		String kind = (String)object.getMember("kind");
		if(!"method".equals(kind)){
			if("constructor".equals(kind)){
				fd.setConstructor(true);
			}else if("get".equals(kind)){
				Modifier getModifier = ast.newModifier(ModifierKeyword.GET_KEYWORD);
				fd.modifiers().add(getModifier);
			}else if("set".equals(kind)){
				Modifier setModifier = ast.newModifier(ModifierKeyword.SET_KEYWORD);
				fd.modifiers().add(setModifier);
			}
		}
		nodes.push(fd);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertRestElement(final ScriptObjectMirror object) {
		RestElementName ren = ast.newRestElementName();
		nodes.push(ren);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertArrayPattern(final ScriptObjectMirror object) {
		ArrayName an = ast.newArrayName();
		nodes.push(an);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertObjectPattern(final ScriptObjectMirror object) {
		ObjectName on = ast.newObjectName();
		nodes.push(on);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertTemplateLiteral(final ScriptObjectMirror object, String key) {
		if("quasi".equals(key)){
			// reuse the tempalateLiteral created with parent TaggedTemplateLiteral so 
			//that it eventually cascades to one TemplateLiteral
			nodes.push(nodes.peek());
			return VisitOptions.CONTINUE;
		}
		TemplateLiteral literal = ast.newTemplateLiteral();
		nodes.push(literal);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertTemplateElement(final ScriptObjectMirror object) {
		TemplateElement te = ast.newTemplateElement();
		ScriptObjectMirror val = (ScriptObjectMirror)object.getMember("value");
		String value = (String)val.getMember("raw");
		Boolean isTail = (Boolean) object.getMember("tail");
		te.setRawValue(value);
		te.setTail(isTail.booleanValue());
		nodes.push(te);
		return VisitOptions.SKIP;
	}
	
	private VisitOptions convertAssignmentPattern(final ScriptObjectMirror object) {
		AssignmentName an = ast.newAssignmentName();
		nodes.push(an);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertSpreadElement(final ScriptObjectMirror object) {
		SpreadElement spread = ast.newSpreadElement();
		nodes.push(spread);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertMetaProperty(final ScriptObjectMirror object) {
		MetaProperty mp = ast.newMetaProperty();
		String meta = (String) object.getMember("meta");
		String prop = (String) object.getMember("property");
		mp.setMeta(meta);
		mp.setPropertyName(prop);
		nodes.push(mp);
		return VisitOptions.CONTINUE;
	}

	private VisitOptions convertImportDeclaration(final ScriptObjectMirror object) {
		ImportDeclaration importDecl = ast.newImportDeclaration();
		nodes.push(importDecl);
		return VisitOptions.CONTINUE;
	}
	
	
	private VisitOptions convertImportSpecifer(ScriptObjectMirror object, boolean isDefault, boolean isNamespace) {
		ModuleSpecifier specifier = ast.newModuleSpecifier();
		specifier.setDefault(isDefault);
		specifier.setNamespace(isNamespace);
		nodes.push(specifier);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertExportSpecifier(ScriptObjectMirror object) {
		ModuleSpecifier specifier = ast.newModuleSpecifier();
		nodes.push(specifier);
		return VisitOptions.CONTINUE;
	}
	
	private VisitOptions convertExportDeclaration(ScriptObjectMirror object, boolean isDefault, boolean isAll) {
		ExportDeclaration declaration = ast.newExportDeclaration();
		declaration.setDefault(isDefault);
		declaration.setAll(isAll);
		nodes.push(declaration);
		return VisitOptions.CONTINUE;
	}	
}
