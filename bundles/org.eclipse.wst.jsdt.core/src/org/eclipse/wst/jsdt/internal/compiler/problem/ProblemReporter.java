/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Michael Spector <spektom@gmail.com> -  Bug 243886
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.problem;

import java.io.CharConversionException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import org.eclipse.wst.jsdt.core.compiler.CategorizedProblem;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.compiler.InvalidInputException;
import org.eclipse.wst.jsdt.core.infer.InferredAttribute;
import org.eclipse.wst.jsdt.core.infer.InferredType;
import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.wst.jsdt.internal.compiler.IProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.Argument;
import org.eclipse.wst.jsdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.ArrayReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.Assignment;
import org.eclipse.wst.jsdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.Block;
import org.eclipse.wst.jsdt.internal.compiler.ast.BranchStatement;
import org.eclipse.wst.jsdt.internal.compiler.ast.CaseStatement;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.wst.jsdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.EqualExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.wst.jsdt.internal.compiler.ast.Expression;
import org.eclipse.wst.jsdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.FieldReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.ImportReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.Initializer;
import org.eclipse.wst.jsdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.IntLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.LabeledStatement;
import org.eclipse.wst.jsdt.internal.compiler.ast.Literal;
import org.eclipse.wst.jsdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.LongLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.NameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.NumberLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.Reference;
import org.eclipse.wst.jsdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.wst.jsdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.Statement;
import org.eclipse.wst.jsdt.internal.compiler.ast.ThisReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.wst.jsdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.wst.jsdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeIds;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.parser.RecoveryScanner;
import org.eclipse.wst.jsdt.internal.compiler.parser.Scanner;
import org.eclipse.wst.jsdt.internal.compiler.parser.ScannerHelper;
import org.eclipse.wst.jsdt.internal.compiler.parser.TerminalTokens;
import org.eclipse.wst.jsdt.internal.compiler.util.Messages;
import org.eclipse.wst.jsdt.internal.compiler.util.Util;

public class ProblemReporter extends ProblemHandler {

	public ReferenceContext referenceContext;
	private Scanner positionScanner;

public static long getIrritant(int problemID) {
	switch(problemID){

		case IProblem.MaskedCatch :
			return CompilerOptions.MaskedCatchBlock;

		case IProblem.MethodButWithConstructorName :
			return CompilerOptions.MethodWithConstructorName;

		/* START -------------------------------- Bug 203292 Type/Method/Filed resolution error configuration --------------------- */

		case IProblem.UndefinedName:
			return CompilerOptions.UnresolvedType;
		case IProblem.UndefinedFunction:
 		case IProblem.UndefinedMethod:
 		case IProblem.UndefinedConstructor:
			return CompilerOptions.UnresolvedMethod;
		/* END -------------------------------- Bug 203292 Type/Method/Filed resolution error configuration --------------------- */
 		case IProblem.OptionalSemiColon:
 				return CompilerOptions.OptionalSemicolon;
 		case IProblem.LooseVarDecl:
 				return CompilerOptions.LooseVariableDecl;
		/* START -------------------------------- Bug 197884 Loosly defined var (for statement) and optional semi-colon --------------------- */


		/* END   -------------------------------- Bug 197884 Loosly defined var (for statement) and optional semi-colon --------------------- */
		case IProblem.IncompatibleReturnTypeForNonInheritedInterfaceMethod :
		case IProblem.IncompatibleExceptionInThrowsClauseForNonInheritedInterfaceMethod :
			return CompilerOptions.IncompatibleNonInheritedInterfaceMethod;

		case IProblem.OverridingDeprecatedMethod :
		case IProblem.UsingDeprecatedType :
		case IProblem.UsingDeprecatedMethod :
		case IProblem.UsingDeprecatedConstructor :
		case IProblem.UsingDeprecatedField :
			return CompilerOptions.UsingDeprecatedAPI;

		case IProblem.LocalVariableIsNeverUsed :
			return CompilerOptions.UnusedLocalVariable;

		case IProblem.ArgumentIsNeverUsed :
			return CompilerOptions.UnusedArgument;

		case IProblem.NoImplicitStringConversionForCharArrayExpression :
			return CompilerOptions.NoImplicitStringConversion;

		case IProblem.NeedToEmulateFieldReadAccess :
		case IProblem.NeedToEmulateFieldWriteAccess :
		case IProblem.NeedToEmulateMethodAccess :
		case IProblem.NeedToEmulateConstructorAccess :
			return CompilerOptions.AccessEmulation;

		case IProblem.NonExternalizedStringLiteral :
		case IProblem.UnnecessaryNLSTag :
			return CompilerOptions.NonExternalizedString;

		case IProblem.UseAssertAsAnIdentifier :
			return CompilerOptions.AssertUsedAsAnIdentifier;

		case IProblem.UseEnumAsAnIdentifier :
			return CompilerOptions.EnumUsedAsAnIdentifier;

		case IProblem.NonStaticAccessToStaticMethod :
		case IProblem.NonStaticAccessToStaticField :
			return CompilerOptions.NonStaticAccessToStatic;

		case IProblem.IndirectAccessToStaticMethod :
		case IProblem.IndirectAccessToStaticField :
		case IProblem.IndirectAccessToStaticType :
			return CompilerOptions.IndirectStaticAccess;

		case IProblem.AssignmentHasNoEffect:
			return CompilerOptions.NoEffectAssignment;

		case IProblem.UnusedPrivateConstructor:
		case IProblem.UnusedPrivateMethod:
		case IProblem.UnusedPrivateField:
		case IProblem.UnusedPrivateType:
			return CompilerOptions.UnusedPrivateMember;

		case IProblem.LocalVariableHidingLocalVariable:
		case IProblem.LocalVariableHidingField:
		case IProblem.ArgumentHidingLocalVariable:
		case IProblem.ArgumentHidingField:
			return CompilerOptions.LocalVariableHiding;

		case IProblem.FieldHidingLocalVariable:
		case IProblem.FieldHidingField:
			return CompilerOptions.FieldHiding;

		case IProblem.TypeHidingType:
			return CompilerOptions.TypeHiding;

		case IProblem.PossibleAccidentalBooleanAssignment:
			return CompilerOptions.AccidentalBooleanAssign;

		case IProblem.SuperfluousSemicolon:
		case IProblem.EmptyControlFlowStatement:
			return CompilerOptions.EmptyStatement;

		case IProblem.UndocumentedEmptyBlock:
			return CompilerOptions.UndocumentedEmptyBlock;

		case IProblem.UnnecessaryInstanceof:
			return CompilerOptions.UnnecessaryTypeCheck;

		case IProblem.FinallyMustCompleteNormally:
			return CompilerOptions.FinallyBlockNotCompleting;

		case IProblem.UnusedMethodDeclaredThrownException:
		case IProblem.UnusedConstructorDeclaredThrownException:
			return CompilerOptions.UnusedDeclaredThrownException;

		case IProblem.UnqualifiedFieldAccess:
			return CompilerOptions.UnqualifiedFieldAccess;

		case IProblem.UnnecessaryElse:
			return CompilerOptions.UnnecessaryElse;

		case IProblem.ForbiddenReference:
			return CompilerOptions.ForbiddenReference;

		case IProblem.DiscouragedReference:
			return CompilerOptions.DiscouragedReference;

		case IProblem.NullLocalVariableReference:
			return CompilerOptions.NullReference;

		case IProblem.PotentialNullLocalVariableReference:
			return CompilerOptions.PotentialNullReference;
			
		case IProblem.RedefinedLocal:
			return CompilerOptions.DuplicateLocalVariables;

		case IProblem.RedundantLocalVariableNullAssignment:
		case IProblem.RedundantNullCheckOnNonNullLocalVariable:
		case IProblem.RedundantNullCheckOnNullLocalVariable:
		case IProblem.NonNullLocalVariableComparisonYieldsFalse:
		case IProblem.NullLocalVariableComparisonYieldsFalse:
		case IProblem.NullLocalVariableInstanceofYieldsFalse:
			return CompilerOptions.RedundantNullCheck;

		case IProblem.UnusedLabel :
			return CompilerOptions.UnusedLabel;

		case IProblem.JavadocUnexpectedTag:
		case IProblem.JavadocDuplicateReturnTag:
		case IProblem.JavadocInvalidThrowsClass:
		case IProblem.JavadocInvalidSeeReference:
		case IProblem.JavadocInvalidParamTagName:
		case IProblem.JavadocInvalidParamTagTypeParameter:
		case IProblem.JavadocMalformedSeeReference:
		case IProblem.JavadocInvalidSeeHref:
		case IProblem.JavadocInvalidSeeArgs:
		case IProblem.JavadocInvalidTag:
		case IProblem.JavadocUnterminatedInlineTag:
		case IProblem.JavadocMissingHashCharacter:
		case IProblem.JavadocEmptyReturnTag:
		case IProblem.JavadocUnexpectedText:
		case IProblem.JavadocInvalidParamName:
		case IProblem.JavadocDuplicateParamName:
		case IProblem.JavadocMissingParamName:
		case IProblem.JavadocMissingIdentifier:
		case IProblem.JavadocInvalidMemberTypeQualification:
		case IProblem.JavadocInvalidThrowsClassName:
		case IProblem.JavadocDuplicateThrowsClassName:
		case IProblem.JavadocMissingThrowsClassName:
		case IProblem.JavadocMissingSeeReference:
		case IProblem.JavadocUndefinedField:
		case IProblem.JavadocAmbiguousField:
		case IProblem.JavadocUndefinedConstructor:
		case IProblem.JavadocAmbiguousConstructor:
		case IProblem.JavadocUndefinedMethod:
		case IProblem.JavadocAmbiguousMethod:
		case IProblem.JavadocParameterMismatch:
		case IProblem.JavadocUndefinedType:
		case IProblem.JavadocAmbiguousType:
		case IProblem.JavadocInternalTypeNameProvided:
		case IProblem.JavadocNoMessageSendOnArrayType:
		case IProblem.JavadocNoMessageSendOnBaseType:
		case IProblem.JavadocInheritedMethodHidesEnclosingName:
		case IProblem.JavadocInheritedFieldHidesEnclosingName:
		case IProblem.JavadocInheritedNameHidesEnclosingTypeName:
		case IProblem.JavadocNonStaticTypeFromStaticInvocation:
		case IProblem.JavadocNotVisibleField:
		case IProblem.JavadocNotVisibleConstructor:
		case IProblem.JavadocNotVisibleMethod:
		case IProblem.JavadocNotVisibleType:
		case IProblem.JavadocUsingDeprecatedField:
		case IProblem.JavadocUsingDeprecatedConstructor:
		case IProblem.JavadocUsingDeprecatedMethod:
		case IProblem.JavadocUsingDeprecatedType:
		case IProblem.JavadocHiddenReference:
			return CompilerOptions.InvalidJavadoc;

		case IProblem.JavadocMissingParamTag:
		case IProblem.JavadocMissingReturnTag:
		case IProblem.JavadocMissingThrowsTag:
			return CompilerOptions.MissingJavadocTags;

		case IProblem.JavadocMissing:
			return CompilerOptions.MissingJavadocComments;

		case IProblem.ParameterAssignment:
			return CompilerOptions.ParameterAssignment;

		case IProblem.FallthroughCase:
			return CompilerOptions.FallthroughCase;

		case IProblem.OverridingMethodWithoutSuperInvocation:
			return CompilerOptions.OverridingMethodWithoutSuperInvocation;
		case IProblem.UndefinedField:
			return CompilerOptions.UndefinedField;

		case IProblem.WrongNumberOfArguments:
			return CompilerOptions.WrongNumberOfArguments;
			
		case IProblem.MissingSemiColon:
			return CompilerOptions.OptionalSemicolon;

	
	}
	return 0;
}
/**
 * Compute problem category ID based on problem ID
 * @param problemID
 * @return a category ID
 * @see CategorizedProblem
 */
public static int getProblemCategory(int severity, int problemID) {
	categorizeOnIrritant: {
		// fatal problems even if optional are all falling into same category (not irritant based)
		if ((severity & ProblemSeverities.Fatal) != 0)
			break categorizeOnIrritant;
		long irritant = getIrritant(problemID);
		int irritantInt = (int) irritant;
		if (irritantInt == irritant) {
			switch (irritantInt) {
				case (int)CompilerOptions.MethodWithConstructorName:
				case (int)CompilerOptions.AccessEmulation:
				case (int)CompilerOptions.AssertUsedAsAnIdentifier:
				case (int)CompilerOptions.NonStaticAccessToStatic:
				case (int)CompilerOptions.UnqualifiedFieldAccess:
				case (int)CompilerOptions.UndocumentedEmptyBlock:
				case (int)CompilerOptions.IndirectStaticAccess:
					return CategorizedProblem.CAT_CODE_STYLE;

				case (int)CompilerOptions.MaskedCatchBlock:
				case (int)CompilerOptions.NoImplicitStringConversion:
				case (int)CompilerOptions.NoEffectAssignment:
				case (int)CompilerOptions.AccidentalBooleanAssign:
				case (int)CompilerOptions.EmptyStatement:
				case (int)CompilerOptions.FinallyBlockNotCompleting:
				case (int)CompilerOptions.UndefinedField:
					return CategorizedProblem.CAT_POTENTIAL_PROGRAMMING_PROBLEM;

				case (int)CompilerOptions.IncompatibleNonInheritedInterfaceMethod:
				case (int)CompilerOptions.LocalVariableHiding:
				case (int)CompilerOptions.FieldHiding:
					return CategorizedProblem.CAT_NAME_SHADOWING_CONFLICT;

				case (int)CompilerOptions.UnusedLocalVariable:
				case (int)CompilerOptions.UnusedArgument:
				case (int)CompilerOptions.UnusedPrivateMember:
				case (int)CompilerOptions.UnusedDeclaredThrownException:
				case (int)CompilerOptions.UnnecessaryTypeCheck:
				case (int)CompilerOptions.UnnecessaryElse:
					return CategorizedProblem.CAT_UNNECESSARY_CODE;

				case (int)CompilerOptions.Task:
					return CategorizedProblem.CAT_UNSPECIFIED; // TODO may want to improve

				case (int)CompilerOptions.MissingJavadocComments:
				case (int)CompilerOptions.MissingJavadocTags:
				case (int)CompilerOptions.InvalidJavadoc:
				case (int)(CompilerOptions.InvalidJavadoc | CompilerOptions.UsingDeprecatedAPI):
					return CategorizedProblem.CAT_JAVADOC;

				default:
					break categorizeOnIrritant;
			}
		} else {
			irritantInt = (int)(irritant >>> 32);
			switch (irritantInt) {
				case (int)(CompilerOptions.FinalParameterBound >>> 32):
				case (int)(CompilerOptions.EnumUsedAsAnIdentifier >>> 32):
				case (int)(CompilerOptions.ParameterAssignment >>> 32):
					return CategorizedProblem.CAT_CODE_STYLE;

				case (int)(CompilerOptions.NullReference >>> 32):
				case (int)(CompilerOptions.PotentialNullReference >>> 32):
				case (int)(CompilerOptions.DuplicateLocalVariables >>> 32):
				case (int)(CompilerOptions.RedundantNullCheck >>> 32):
				case (int)(CompilerOptions.FallthroughCase >>> 32):
				case (int)(CompilerOptions.OverridingMethodWithoutSuperInvocation >>> 32):
					return CategorizedProblem.CAT_POTENTIAL_PROGRAMMING_PROBLEM;

				case (int)(CompilerOptions.TypeHiding >>> 32):
					return CategorizedProblem.CAT_NAME_SHADOWING_CONFLICT;

				case (int)(CompilerOptions.UnusedLabel >>> 32):
					return CategorizedProblem.CAT_UNNECESSARY_CODE;

				case (int)(CompilerOptions.ForbiddenReference >>> 32):
				case (int)(CompilerOptions.DiscouragedReference >>> 32):
					return CategorizedProblem.CAT_RESTRICTION;

				default:
					break categorizeOnIrritant;
			}
		}
	}
	// categorize fatal problems per ID
	switch (problemID) {
		case IProblem.IsClassPathCorrect :
		case IProblem.CorruptedSignature :
			return CategorizedProblem.CAT_BUILDPATH;

		default :
			if ((problemID & IProblem.Syntax) != 0)
				return CategorizedProblem.CAT_SYNTAX;
			if ((problemID & IProblem.ImportRelated) != 0)
				return CategorizedProblem.CAT_IMPORT;
			if ((problemID & IProblem.TypeRelated) != 0)
				return CategorizedProblem.CAT_TYPE;
			if ((problemID & (IProblem.FieldRelated|IProblem.MethodRelated|IProblem.ConstructorRelated)) != 0)
				return CategorizedProblem.CAT_MEMBER;
	}
	return CategorizedProblem.CAT_INTERNAL;
}
public ProblemReporter(IErrorHandlingPolicy policy, CompilerOptions options, IProblemFactory problemFactory) {
	super(policy, options, problemFactory);
}
public void abortDueToInternalError(String errorMessage) {
	this.abortDueToInternalError(errorMessage, null);
}
public void abortDueToInternalError(String errorMessage, ASTNode location) {
	String[] arguments = new String[] {errorMessage};
	this.handle(
		IProblem.Unclassified,
		arguments,
		arguments,
		ProblemSeverities.Error | ProblemSeverities.Abort | ProblemSeverities.Fatal,
		location == null ? 0 : location.sourceStart,
		location == null ? 0 : location.sourceEnd);
}
public void abstractMethodCannotBeOverridden(SourceTypeBinding type, MethodBinding concreteMethod) {

	this.handle(
		// %1 must be abstract since it cannot override the inherited package-private abstract method %2
		IProblem.AbstractMethodCannotBeOverridden,
		new String[] {
			new String(type.sourceName()),
			new String(
					CharOperation.concat(
						concreteMethod.declaringClass.readableName(),
						concreteMethod.readableName(),
						'.'))},
		new String[] {
			new String(type.sourceName()),
			new String(
					CharOperation.concat(
						concreteMethod.declaringClass.shortReadableName(),
						concreteMethod.shortReadableName(),
						'.'))},
		type.sourceStart(),
		type.sourceEnd());
}
public void abstractMethodInAbstractClass(SourceTypeBinding type, AbstractMethodDeclaration methodDecl) {

	String[] arguments = new String[] {new String(type.sourceName()), new String(methodDecl.getSafeName())};
	this.handle(
		IProblem.AbstractMethodInAbstractClass,
		arguments,
		arguments,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void abstractMethodMustBeImplemented(SourceTypeBinding type, MethodBinding abstractMethod) {
	this.handle(
		// Must implement the inherited abstract method %1
		// 8.4.3 - Every non-abstract subclass of an abstract type, A, must provide a concrete implementation of all of A's methods.
		IProblem.AbstractMethodMustBeImplemented,
		new String[] {
		        new String(abstractMethod.selector),
		        typesAsString(abstractMethod.isVarargs(), abstractMethod.parameters, false),
		        new String(abstractMethod.declaringClass.readableName()),
		        new String(type.readableName()),
		},
		new String[] {
		        new String(abstractMethod.selector),
		        typesAsString(abstractMethod.isVarargs(), abstractMethod.parameters, true),
		        new String(abstractMethod.declaringClass.shortReadableName()),
		        new String(type.shortReadableName()),
		},
		type.sourceStart(),
		type.sourceEnd());
}
public void abstractMethodNeedingNoBody(AbstractMethodDeclaration method) {
	this.handle(
		IProblem.BodyForAbstractMethod,
		NoArgument,
		NoArgument,
		method.sourceStart,
		method.sourceEnd,
		method,
		method.compilationResult());
}
public void alreadyDefinedLabel(char[] labelName, ASTNode location) {
	String[] arguments = new String[] {new String(labelName)};
	this.handle(
		IProblem.DuplicateLabel,
		arguments,
		arguments,
		location.sourceStart,
		location.sourceEnd);
}
public void anonymousClassCannotExtendFinalClass(Expression expression, TypeBinding type) {
	this.handle(
		IProblem.AnonymousClassCannotExtendFinalClass,
		new String[] {new String(type.readableName())},
		new String[] {new String(type.shortReadableName())},
		expression.sourceStart,
		expression.sourceEnd);
}
public void argumentTypeCannotBeVoid(SourceTypeBinding type, AbstractMethodDeclaration methodDecl, Argument arg) {
	String[] arguments = new String[] {new String(methodDecl.getSafeName()), new String(arg.name)};
	this.handle(
		IProblem.ArgumentTypeCannotBeVoid,
		arguments,
		arguments,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void argumentTypeCannotBeVoidArray(Argument arg) {
	this.handle(
		IProblem.CannotAllocateVoidArray,
		NoArgument,
		NoArgument,
		arg.type.sourceStart,
		arg.type.sourceEnd);
}
public void arrayConstantsOnlyInArrayInitializers(int sourceStart, int sourceEnd) {
	this.handle(
		IProblem.ArrayConstantsOnlyInArrayInitializers,
		NoArgument,
		NoArgument,
		sourceStart,
		sourceEnd);
}
public void assignmentHasNoEffect(AbstractVariableDeclaration location, char[] name){
	int severity = computeSeverity(IProblem.AssignmentHasNoEffect);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] { new String(name) };
	int start = location.sourceStart;
	int end = location.sourceEnd;
	if (location.initialization != null) {
		end = location.initialization.sourceEnd;
	}
	this.handle(
			IProblem.AssignmentHasNoEffect,
			arguments,
			arguments,
			severity,
			start,
			end);
}
public void assignmentHasNoEffect(Assignment location, char[] name){
	int severity = computeSeverity(IProblem.AssignmentHasNoEffect);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] { new String(name) };
	this.handle(
			IProblem.AssignmentHasNoEffect,
			arguments,
			arguments,
			severity,
			location.sourceStart,
			location.sourceEnd);
}
public void attemptToReturnNonVoidExpression(ReturnStatement returnStatement, TypeBinding expectedType) {
	this.handle(
		IProblem.VoidMethodReturnsValue,
		new String[] {new String(expectedType.readableName())},
		new String[] {new String(expectedType.shortReadableName())},
		returnStatement.sourceStart,
		returnStatement.sourceEnd);
}
public void attemptToReturnVoidValue(ReturnStatement returnStatement) {
	this.handle(
		IProblem.MethodReturnsVoid,
		NoArgument,
		NoArgument,
		returnStatement.sourceStart,
		returnStatement.sourceEnd);
}
public void cannotAllocateVoidArray(Expression expression) {
	this.handle(
		IProblem.CannotAllocateVoidArray,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}
public void cannotAssignToFinalField(FieldBinding field, ASTNode location) {
	this.handle(
		IProblem.FinalFieldAssignment,
		new String[] {
			(field.declaringClass == null ? "array" : new String(field.declaringClass.readableName())), //$NON-NLS-1$
			new String(field.readableName())},
		new String[] {
			(field.declaringClass == null ? "array" : new String(field.declaringClass.shortReadableName())), //$NON-NLS-1$
			new String(field.shortReadableName())},
		nodeSourceStart(field, location),
		nodeSourceEnd(field, location));
}
public void cannotAssignToFinalLocal(LocalVariableBinding local, ASTNode location) {
	String[] arguments = new String[] { new String(local.readableName())};
	this.handle(
		IProblem.NonBlankFinalLocalAssignment,
		arguments,
		arguments,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void cannotAssignToFinalOuterLocal(LocalVariableBinding local, ASTNode location) {
	String[] arguments = new String[] {new String(local.readableName())};
	this.handle(
		IProblem.FinalOuterLocalAssignment,
		arguments,
		arguments,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void cannotDefineDimensionsAndInitializer(ArrayAllocationExpression expresssion) {
	this.handle(
		IProblem.CannotDefineDimensionExpressionsWithInit,
		NoArgument,
		NoArgument,
		expresssion.sourceStart,
		expresssion.sourceEnd);
}
public void cannotDireclyInvokeAbstractMethod(MessageSend messageSend, MethodBinding method) {
	this.handle(
		IProblem.DirectInvocationOfAbstractMethod,
		new String[] {new String(method.declaringClass.readableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, false)},
		new String[] {new String(method.declaringClass.shortReadableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, true)},
		messageSend.sourceStart,
		messageSend.sourceEnd);
}
public void cannotImportPackage(ImportReference importRef) {
	String[] arguments = new String[] {CharOperation.toString(importRef.tokens)};
	this.handle(
		IProblem.CannotImportPackage,
		arguments,
		arguments,
		importRef.sourceStart,
		importRef.sourceEnd);
}
public void cannotInstantiate(TypeReference typeRef, TypeBinding type) {
	this.handle(
		IProblem.InvalidClassInstantiation,
		new String[] {new String(type.readableName())},
		new String[] {new String(type.shortReadableName())},
		typeRef.sourceStart,
		typeRef.sourceEnd);
}
public void cannotReadSource(CompilationUnitDeclaration unit, AbortCompilationUnit abortException, boolean verbose) {
	String fileName = new String(unit.compilationResult.fileName);
	if (abortException.exception instanceof CharConversionException) {
		// specific encoding issue
		String encoding = abortException.encoding;
		if (encoding == null) {
			encoding = System.getProperty("file.encoding"); //$NON-NLS-1$
		}
		String[] arguments = new String[]{ fileName, encoding, };
		this.handle(
				IProblem.InvalidEncoding,
				arguments,
				arguments,
				0,
				0);
		return;
	}
	StringWriter stringWriter = new StringWriter();
	PrintWriter writer = new PrintWriter(stringWriter);
	if (verbose) {
		abortException.exception.printStackTrace(writer);
	} else {
		writer.print(abortException.exception.getClass().getName());
		writer.print(':');
		writer.print(abortException.exception.getMessage());
	}
	String exceptionTrace = stringWriter.toString();
	String[] arguments = new String[]{ fileName, exceptionTrace, };
	this.handle(
			IProblem.CannotReadSource,
			arguments,
			arguments,
			0,
			0);
}
public void cannotReferToNonFinalOuterLocal(LocalVariableBinding local, ASTNode location) {
	String[] arguments =new String[]{ new String(local.readableName())};
	this.handle(
		IProblem.OuterLocalMustBeFinal,
		arguments,
		arguments,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void cannotReturnOutsideFunction(ASTNode location) {
	this.handle(
		IProblem.CannotReturnOutsideFunction,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void cannotThrowNull(ASTNode expression) {
	this.handle(
		IProblem.CannotThrowNull,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}
public void cannotThrowType(ASTNode exception, TypeBinding expectedType) {
	this.handle(
		IProblem.CannotThrowType,
		new String[] {new String(expectedType.readableName())},
		new String[] {new String(expectedType.shortReadableName())},
		exception.sourceStart,
		exception.sourceEnd);
}
public void cannotUseSuperInCodeSnippet(int start, int end) {
	this.handle(
		IProblem.CannotUseSuperInCodeSnippet,
		NoArgument,
		NoArgument,
		ProblemSeverities.Error | ProblemSeverities.Abort | ProblemSeverities.Fatal,
		start,
		end);
}
public void cannotUseSuperInJavaLangObject(ASTNode reference) {
	this.handle(
		IProblem.ObjectHasNoSuperclass,
		NoArgument,
		NoArgument,
		reference.sourceStart,
		reference.sourceEnd);
}
public void classExtendFinalClass(SourceTypeBinding type, TypeReference superclass, TypeBinding superTypeBinding) {
	String name = new String(type.sourceName());
	String superTypeFullName = new String(superTypeBinding.readableName());
	String superTypeShortName = new String(superTypeBinding.shortReadableName());
	if (superTypeShortName.equals(name)) superTypeShortName = superTypeFullName;
	this.handle(
		IProblem.ClassExtendFinalClass,
		new String[] {superTypeFullName, name},
		new String[] {superTypeShortName, name},
		superclass.sourceStart,
		superclass.sourceEnd);
}
public void codeSnippetMissingClass(String missing, int start, int end) {
	String[] arguments = new String[]{missing};
	this.handle(
		IProblem.CodeSnippetMissingClass,
		arguments,
		arguments,
		ProblemSeverities.Error | ProblemSeverities.Abort | ProblemSeverities.Fatal,
		start,
		end);
}
public void codeSnippetMissingMethod(String className, String missingMethod, String argumentTypes, int start, int end) {
	String[] arguments = new String[]{ className, missingMethod, argumentTypes };
	this.handle(
		IProblem.CodeSnippetMissingMethod,
		arguments,
		arguments,
		ProblemSeverities.Error | ProblemSeverities.Abort | ProblemSeverities.Fatal,
		start,
		end);
}
/*
 * Given the current configuration, answers which category the problem
 * falls into:
 *		ProblemSeverities.Error | ProblemSeverities.Warning | ProblemSeverities.Ignore
 * when different from Ignore, severity can be coupled with ProblemSeverities.Optional
 * to indicate that this problem is configurable through options
 */
public int computeSeverity(int problemID){
 	

	/*
	 * If semantic validation is not enabled and this is anything but a
	 * syntax, documentation, or task problem, ignore.
	 */
	if (!this.options.enableSemanticValidation && (problemID & IProblem.Syntax) == 0 && (problemID & IProblem.Javadoc) == 0 && problemID != IProblem.Task) {
		return ProblemSeverities.Ignore;
	}
	
	switch (problemID) {
		case IProblem.Task :
			return ProblemSeverities.Warning;
 		case IProblem.TypeCollidesWithPackage :
			return ProblemSeverities.Warning;

		/*
		 * JS Type mismatch is set to default as Warning
		 */
 		case IProblem.NotAFunction:
 		case IProblem.TypeMismatch:
 			return ProblemSeverities.Warning;

// 		case IProblem.UndefinedName:
// 		case IProblem.UndefinedFunction:
// 		case IProblem.UndefinedMethod:
// 		case IProblem.UndefinedField:
// 		case IProblem.UndefinedConstructor:
// 			break;
		/*
		 * Javadoc tags resolved references errors
		 */
		case IProblem.JavadocInvalidParamName:
		case IProblem.JavadocDuplicateParamName:
		case IProblem.JavadocMissingParamName:
		case IProblem.JavadocMissingIdentifier:
		case IProblem.JavadocInvalidMemberTypeQualification:
		case IProblem.JavadocInvalidThrowsClassName:
		case IProblem.JavadocDuplicateThrowsClassName:
		case IProblem.JavadocMissingThrowsClassName:
		case IProblem.JavadocMissingSeeReference:
		case IProblem.JavadocUndefinedField:
		case IProblem.JavadocAmbiguousField:
		case IProblem.JavadocUndefinedConstructor:
		case IProblem.JavadocAmbiguousConstructor:
		case IProblem.JavadocUndefinedMethod:
		case IProblem.JavadocAmbiguousMethod:
		case IProblem.JavadocParameterMismatch:
		case IProblem.JavadocUndefinedType:
		case IProblem.JavadocAmbiguousType:
		case IProblem.JavadocInternalTypeNameProvided:
		case IProblem.JavadocNoMessageSendOnArrayType:
		case IProblem.JavadocNoMessageSendOnBaseType:
		case IProblem.JavadocInheritedMethodHidesEnclosingName:
		case IProblem.JavadocInheritedFieldHidesEnclosingName:
		case IProblem.JavadocInheritedNameHidesEnclosingTypeName:
		case IProblem.JavadocNonStaticTypeFromStaticInvocation:
		case IProblem.JavadocEmptyReturnTag:
			if (!this.options.reportInvalidJavadocTags) {
				return ProblemSeverities.Ignore;
			}
			break;
		/*
		 * Javadoc invalid tags due to deprecated references
		 */
		case IProblem.JavadocUsingDeprecatedField:
		case IProblem.JavadocUsingDeprecatedConstructor:
		case IProblem.JavadocUsingDeprecatedMethod:
		case IProblem.JavadocUsingDeprecatedType:
			if (!(this.options.reportInvalidJavadocTags && this.options.reportInvalidJavadocTagsDeprecatedRef)) {
				return ProblemSeverities.Ignore;
			}
			break;
		/*
		 * Javadoc invalid tags due to non-visible references
		 */
		case IProblem.JavadocNotVisibleField:
		case IProblem.JavadocNotVisibleConstructor:
		case IProblem.JavadocNotVisibleMethod:
		case IProblem.JavadocNotVisibleType:
		case IProblem.JavadocHiddenReference:
			if (!(this.options.reportInvalidJavadocTags && this.options.reportInvalidJavadocTagsNotVisibleRef)) {
				return ProblemSeverities.Ignore;
			}
			break;
	}
	long irritant = getIrritant(problemID);
	if (irritant != 0) {
		if ((problemID & IProblem.Javadoc) != 0 && !this.options.docCommentSupport)
			return ProblemSeverities.Ignore;
		return this.options.getSeverity(irritant);
	}
	return ProblemSeverities.Error | ProblemSeverities.Fatal;
}
public void conditionalArgumentsIncompatibleTypes(ConditionalExpression expression, TypeBinding trueType, TypeBinding falseType) {
	this.handle(
		IProblem.IncompatibleTypesInConditionalOperator,
		new String[] {new String(trueType.readableName()), new String(falseType.readableName())},
		new String[] {new String(trueType.readableName()), new String(falseType.readableName())},
		expression.sourceStart,
		expression.sourceEnd);
}
public void conflictingImport(ImportReference importRef) {
	String[] arguments = new String[] {CharOperation.toString(importRef.tokens)};
	this.handle(
		IProblem.ConflictingImport,
		arguments,
		arguments,
		importRef.sourceStart,
		importRef.sourceEnd);
}
public void constantOutOfFormat(NumberLiteral literal) {
	// the literal is not in a correct format
	// this code is called on IntLiteral and LongLiteral
	// example 000811 ...the 8 is uncorrect.

	if ((literal instanceof LongLiteral) || (literal instanceof IntLiteral)) {
		char[] source = literal.source();
		try {
			final String Radix;
			final int radix;
			if ((source[1] == 'x') || (source[1] == 'X')) {
				radix = 16;
				Radix = "Hex"; //$NON-NLS-1$
			} else {
				radix = 8;
				Radix = "Octal"; //$NON-NLS-1$
			}
			//look for the first digit that is incorrect
			int place = -1;
			label : for (int i = radix == 8 ? 1 : 2; i < source.length; i++) {
				if (ScannerHelper.digit(source[i], radix) == -1) {
					place = i;
					break label;
				}
			}
			String[] arguments = new String[] {
				new String(literal.literalType(null).readableName()), // numeric literals do not need scope to reach type
				Radix + " " + new String(source) + " (digit " + new String(new char[] {source[place]}) + ")"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			this.handle(
				IProblem.NumericValueOutOfRange,
				arguments,
				arguments,
				literal.sourceStart,
				literal.sourceEnd);
			return;
		} catch (IndexOutOfBoundsException ex) {
			// should never happen
		}

		// just in case .... use a predefined error..
		// we should never come here...(except if the code changes !)
		this.constantOutOfRange(literal, literal.literalType(null)); // numeric literals do not need scope to reach type
	}
}
public void constantOutOfRange(Literal literal, TypeBinding literalType) {
	String[] arguments = new String[] {new String(literalType.readableName()), new String(literal.source())};
	this.handle(
		IProblem.NumericValueOutOfRange,
		arguments,
		arguments,
		literal.sourceStart,
		literal.sourceEnd);
}
public void corruptedSignature(TypeBinding enclosingType, char[] signature, int position) {
	this.handle(
		IProblem.CorruptedSignature,
		new String[] { new String(enclosingType.readableName()), new String(signature), String.valueOf(position) },
		new String[] { new String(enclosingType.shortReadableName()), new String(signature), String.valueOf(position) },
		ProblemSeverities.Error | ProblemSeverities.Abort | ProblemSeverities.Fatal,
		0,
		0);
}
public void deprecatedField(FieldBinding field, ASTNode location) {
	int severity = computeSeverity(IProblem.UsingDeprecatedField);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		IProblem.UsingDeprecatedField,
		new String[] {new String(field.declaringClass.readableName()), new String(field.name)},
		new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name)},
		severity,
		nodeSourceStart(field, location),
		nodeSourceEnd(field, location));
}
public void deprecatedMethod(MethodBinding method, ASTNode location) {
	boolean isConstructor = method.isConstructor();
	int severity = computeSeverity(isConstructor ? IProblem.UsingDeprecatedConstructor : IProblem.UsingDeprecatedMethod);
	if (severity == ProblemSeverities.Ignore) return;
	if (isConstructor) {
		this.handle(
			IProblem.UsingDeprecatedConstructor,
			new String[] {new String(method.declaringClass.readableName()), typesAsString(method.isVarargs(), method.parameters, false)},
			new String[] {new String(method.declaringClass.shortReadableName()), typesAsString(method.isVarargs(), method.parameters, true)},
			severity,
			location.sourceStart,
			location.sourceEnd);
	} else {
		this.handle(
			IProblem.UsingDeprecatedMethod,
			new String[] {new String(method.declaringClass.readableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, false)},
			new String[] {new String(method.declaringClass.shortReadableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, true)},
			severity,
			location.sourceStart,
			location.sourceEnd);
	}
}
public void deprecatedType(TypeBinding type, ASTNode location) {
	if (location == null) return; // 1G828DN - no type ref for synthetic arguments
	int severity = computeSeverity(IProblem.UsingDeprecatedType);
	if (severity == ProblemSeverities.Ignore) return;
	type = type.leafComponentType();
	this.handle(
		IProblem.UsingDeprecatedType,
		new String[] {new String(type.readableName())},
		new String[] {new String(type.shortReadableName())},
		severity,
		location.sourceStart,
		nodeSourceEnd(null, location));
}
public void duplicateCase(CaseStatement caseStatement) {
	this.handle(
		IProblem.DuplicateCase,
		NoArgument,
		NoArgument,
		caseStatement.sourceStart,
		caseStatement.sourceEnd);
}
public void duplicateDefaultCase(ASTNode statement) {
	this.handle(
		IProblem.DuplicateDefaultCase,
		NoArgument,
		NoArgument,
		statement.sourceStart,
		statement.sourceEnd);
}
public void duplicateFieldInType(SourceTypeBinding type, FieldDeclaration fieldDecl) {
	this.handle(
		IProblem.DuplicateField,
		new String[] {new String(type.sourceName()), new String(fieldDecl.name)},
		new String[] {new String(type.shortReadableName()), new String(fieldDecl.name)},
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}

public void duplicateFieldInType(SourceTypeBinding type, InferredAttribute fieldDecl) {
	this.handle(
		IProblem.DuplicateField,
		new String[] {new String(type.sourceName()), new String(fieldDecl.name)},
		new String[] {new String(type.shortReadableName()), new String(fieldDecl.name)},
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}

public void duplicateImport(ImportReference importRef) {
	String[] arguments = new String[] {CharOperation.toString(importRef.tokens)};
	this.handle(
		IProblem.DuplicateImport,
		arguments,
		arguments,
		importRef.sourceStart,
		importRef.sourceEnd);
}
public void duplicateInitializationOfBlankFinalField(FieldBinding field, Reference reference) {
	String[] arguments = new String[]{ new String(field.readableName())};
	this.handle(
		IProblem.DuplicateBlankFinalFieldInitialization,
		arguments,
		arguments,
		nodeSourceStart(field, reference),
		nodeSourceEnd(field, reference));
}
public void duplicateInitializationOfFinalLocal(LocalVariableBinding local, ASTNode location) {
	String[] arguments = new String[] { new String(local.readableName())};
	this.handle(
		IProblem.DuplicateFinalLocalInitialization,
		arguments,
		arguments,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}

public void duplicateMethodInType( Binding type, AbstractMethodDeclaration methodDecl) {
    MethodBinding method = methodDecl.binding;
    
	this.handle(
		IProblem.DuplicateMethod,
		new String[] {
	        new String(methodDecl.getSafeName()),
			new String(method.declaringClass.readableName()),
			typesAsString(method.isVarargs(), method.parameters, false)},
		new String[] {
			new String(methodDecl.getSafeName()),
			new String(method.declaringClass.shortReadableName()),
			typesAsString(method.isVarargs(), method.parameters, true)},
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
    
}
public void duplicateModifierForField(ReferenceBinding type, FieldDeclaration fieldDecl) {
/* to highlight modifiers use:
	this.handle(
		new Problem(
			DuplicateModifierForField,
			new String[] {new String(fieldDecl.name)},
			fieldDecl.modifiers.sourceStart,
			fieldDecl.modifiers.sourceEnd));
*/
	String[] arguments = new String[] {new String(fieldDecl.name)};
	this.handle(
		IProblem.DuplicateModifierForField,
		arguments,
		arguments,
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void duplicateModifierForMethod(ReferenceBinding type, AbstractMethodDeclaration methodDecl) {
	this.handle(
		IProblem.DuplicateModifierForMethod,
		new String[] {new String(type.sourceName()), new String(methodDecl.getSafeName())},
		new String[] {new String(type.shortReadableName()), new String(methodDecl.getSafeName())},
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void duplicateModifierForType(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.DuplicateModifierForType,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void duplicateModifierForVariable(LocalDeclaration localDecl, boolean complainForArgument) {
	String[] arguments = new String[] {new String(localDecl.name)};
	this.handle(
		complainForArgument
			? IProblem.DuplicateModifierForArgument
			: IProblem.DuplicateModifierForVariable,
		arguments,
		arguments,
		localDecl.sourceStart,
		localDecl.sourceEnd);
}
public void duplicateNestedType(TypeDeclaration typeDecl) {
	String[] arguments = new String[] {new String(typeDecl.name)};
	this.handle(
		IProblem.DuplicateNestedType,
		arguments,
		arguments,
		typeDecl.sourceStart,
		typeDecl.sourceEnd);
}
public void duplicateSuperinterface(SourceTypeBinding type, TypeReference reference, ReferenceBinding superType) {
	this.handle(
		IProblem.DuplicateSuperInterface,
		new String[] {
			new String(superType.readableName()),
			new String(type.sourceName())},
		new String[] {
			new String(superType.shortReadableName()),
			new String(type.sourceName())},
		reference.sourceStart,
		reference.sourceEnd);
}
public void duplicateTypes(CompilationUnitDeclaration compUnitDecl, TypeDeclaration typeDecl) {
	String[] arguments = new String[] {new String(compUnitDecl.getFileName()), new String(typeDecl.name)};
	this.referenceContext = typeDecl; // report the problem against the type not the entire compilation unit
	this.handle(
		IProblem.DuplicateTypes,
		arguments,
		arguments,
		typeDecl.sourceStart,
		typeDecl.sourceEnd,
		compUnitDecl.compilationResult);
}
public void duplicateTypes(CompilationUnitDeclaration compUnitDecl, InferredType typeDecl) {
	String[] arguments = new String[] {new String(compUnitDecl.getFileName()), new String(typeDecl.getName())};
	this.referenceContext = compUnitDecl; // report the problem against the type not the entire compilation unit
	this.handle(
		IProblem.DuplicateTypes,
		arguments,
		arguments,
		typeDecl.sourceStart,
		typeDecl.sourceEnd,
		compUnitDecl.compilationResult);
}
public void emptyControlFlowStatement(int sourceStart, int sourceEnd) {
	this.handle(
		IProblem.EmptyControlFlowStatement,
		NoArgument,
		NoArgument,
		sourceStart,
		sourceEnd);
}
public void errorNoMethodFor(MessageSend messageSend, TypeBinding recType, TypeBinding[] params) {
	StringBuffer buffer = new StringBuffer();
	StringBuffer shortBuffer = new StringBuffer();
	for (int i = 0, length = params.length; i < length; i++) {
		if (i != 0){
			buffer.append(", "); //$NON-NLS-1$
			shortBuffer.append(", "); //$NON-NLS-1$
		}
		buffer.append(new String(params[i].readableName()));
		shortBuffer.append(new String(params[i].shortReadableName()));
	}

	String shortName=""; //$NON-NLS-1$
	String readableName=""; //$NON-NLS-1$
	if (recType!=null)
	{
		readableName=new String(recType.readableName());
		shortName=new String(recType.shortReadableName());
	}
	int id = recType!=null && recType.isArrayType() ? IProblem.NoMessageSendOnArrayType : IProblem.NoMessageSendOnBaseType;
	this.handle(
		id,
		new String[] {readableName, new String(messageSend.selector), buffer.toString()},
		new String[] {shortName, new String(messageSend.selector), shortBuffer.toString()},
		messageSend.sourceStart,
		messageSend.sourceEnd);
}
public void errorThisSuperInStatic(ASTNode reference) {
	String[] arguments = new String[] {reference.isSuper() ? "super" : "this"}; //$NON-NLS-2$ //$NON-NLS-1$
	this.handle(
		IProblem.ThisInStaticContext,
		arguments,
		arguments,
		reference.sourceStart,
		reference.sourceEnd);
}
public void expressionShouldBeAVariable(Expression expression) {
	this.handle(
		IProblem.ExpressionShouldBeAVariable,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}
public void fieldHiding(FieldDeclaration fieldDecl, Binding hiddenVariable) {
	FieldBinding field = fieldDecl.binding;
	boolean isLocal = hiddenVariable instanceof LocalVariableBinding;
	int severity = computeSeverity(isLocal ? IProblem.FieldHidingLocalVariable : IProblem.FieldHidingField);
	if (severity == ProblemSeverities.Ignore) return;
	if (isLocal) {
		this.handle(
			IProblem.FieldHidingLocalVariable,
			new String[] {new String(field.declaringClass.readableName()), new String(field.name) },
			new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name) },
			severity,
			nodeSourceStart(hiddenVariable, fieldDecl),
			nodeSourceEnd(hiddenVariable, fieldDecl));
	} else if (hiddenVariable instanceof FieldBinding) {
		FieldBinding hiddenField = (FieldBinding) hiddenVariable;
		this.handle(
			IProblem.FieldHidingField,
			new String[] {new String(field.declaringClass.readableName()), new String(field.name) , new String(hiddenField.declaringClass.readableName())  },
			new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name) , new String(hiddenField.declaringClass.shortReadableName()) },
			severity,
			nodeSourceStart(hiddenField, fieldDecl),
			nodeSourceEnd(hiddenField, fieldDecl));
	}
}
public void fieldsOrThisBeforeConstructorInvocation(ThisReference reference) {
	this.handle(
		IProblem.ThisSuperDuringConstructorInvocation,
		NoArgument,
		NoArgument,
		reference.sourceStart,
		reference.sourceEnd);
}
public void finallyMustCompleteNormally(Block finallyBlock) {
	this.handle(
		IProblem.FinallyMustCompleteNormally,
		NoArgument,
		NoArgument,
		finallyBlock.sourceStart,
		finallyBlock.sourceEnd);
}
public void forbiddenReference(FieldBinding field, ASTNode location,
		String messageTemplate, int problemId) {
	this.handle(
		problemId,
		new String[] { new String(field.readableName()) }, // distinct from msg arg for quickfix purpose
		new String[] {
			MessageFormat.format(messageTemplate,
				new String[]{
					new String(field.shortReadableName()),
			        new String(field.declaringClass.shortReadableName())})},
		nodeSourceStart(field, location),
		nodeSourceEnd(field, location));
}
public void forbiddenReference(MethodBinding method, ASTNode location,
		String messageTemplate, int problemId) {
	if (method.isConstructor())
		this.handle(
			problemId,
			new String[] { new String(method.readableName()) }, // distinct from msg arg for quickfix purpose
			new String[] {
				MessageFormat.format(messageTemplate,
						new String[]{new String(method.shortReadableName())})},
			location.sourceStart,
			location.sourceEnd);
	else
		this.handle(
			problemId,
			new String[] { new String(method.readableName()) }, // distinct from msg arg for quickfix purpose
			new String[] {
				MessageFormat.format(messageTemplate,
					new String[]{
						new String(method.shortReadableName()),
				        new String(method.declaringClass.shortReadableName())})},
			location.sourceStart,
			location.sourceEnd);
}
public void forbiddenReference(TypeBinding type, ASTNode location, String messageTemplate, int problemId) {
	if (location == null) return;
	int severity = computeSeverity(problemId);
	if (severity == ProblemSeverities.Ignore) return;
	// this problem has a message template extracted from the access restriction rule
	this.handle(
		problemId,
		new String[] { new String(type.readableName()) }, // distinct from msg arg for quickfix purpose
		new String[] { MessageFormat.format(messageTemplate, new String[]{ new String(type.shortReadableName())})},
		severity,
		location.sourceStart,
		location.sourceEnd);
}
public void forwardReference(Reference reference, int indexInQualification, TypeBinding type) {
	this.handle(
		IProblem.ReferenceToForwardField,
		NoArgument,
		NoArgument,
		reference.sourceStart,
		reference.sourceEnd);
}
// use this private API when the compilation unit result can be found through the
// reference context. Otherwise, use the other API taking a problem and a compilation result
// as arguments
private void handle(
	int problemId,
	String[] problemArguments,
	String[] messageArguments,
	int problemStartPosition,
	int problemEndPosition){

	this.handle(
			problemId,
			problemArguments,
			messageArguments,
			problemStartPosition,
			problemEndPosition,
			this.referenceContext,
			this.referenceContext == null ? null : this.referenceContext.compilationResult());
	this.referenceContext = null;
}
// use this private API when the compilation unit result cannot be found through the
// reference context.
private void handle(
	int problemId,
	String[] problemArguments,
	String[] messageArguments,
	int problemStartPosition,
	int problemEndPosition,
	CompilationResult unitResult){

	this.handle(
			problemId,
			problemArguments,
			messageArguments,
			problemStartPosition,
			problemEndPosition,
			this.referenceContext,
			unitResult);
	this.referenceContext = null;
}
// use this private API when the compilation unit result can be found through the
// reference context. Otherwise, use the other API taking a problem and a compilation result
// as arguments
private void handle(
	int problemId,
	String[] problemArguments,
	String[] messageArguments,
	int severity,
	int problemStartPosition,
	int problemEndPosition){

	this.handle(
			problemId,
			problemArguments,
			messageArguments,
			severity,
			problemStartPosition,
			problemEndPosition,
			this.referenceContext,
			this.referenceContext == null ? null : this.referenceContext.compilationResult());
	this.referenceContext = null;
}

public void hiddenCatchBlock(ReferenceBinding exceptionType, ASTNode location) {
	this.handle(
		IProblem.MaskedCatch,
		new String[] {
			new String(exceptionType.readableName()),
		 },
		new String[] {
			new String(exceptionType.shortReadableName()),
		 },
		location.sourceStart,
		location.sourceEnd);
}

public void hierarchyCircularity(SourceTypeBinding sourceType, ReferenceBinding superType, TypeReference reference) {
	int start = 0;
	int end = 0;

	if (reference == null) {	// can only happen when java.lang.Object is busted
		start = sourceType.sourceStart();
		end = sourceType.sourceEnd();
	} else {
		start = reference.sourceStart;
		end = reference.sourceEnd;
	}

	if (sourceType == superType)
		this.handle(
			IProblem.HierarchyCircularitySelfReference,
			new String[] {new String(sourceType.readableName()) },
			new String[] {new String(sourceType.shortReadableName()) },
			start,
			end);
	else
		this.handle(
			IProblem.HierarchyCircularity,
			new String[] {new String(sourceType.readableName()), new String(superType.readableName())},
			new String[] {new String(sourceType.shortReadableName()), new String(superType.shortReadableName())},
			start,
			end);
}

public void hierarchyHasProblems(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.HierarchyHasProblems,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalAbstractModifierCombinationForMethod(ReferenceBinding type, AbstractMethodDeclaration methodDecl) {
	String[] arguments = new String[] {new String(type.sourceName()), new String(methodDecl.getSafeName())};
	this.handle(
		IProblem.IllegalAbstractModifierCombinationForMethod,
		arguments,
		arguments,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void illegalLocalTypeDeclaration(TypeDeclaration typeDeclaration) {
	if (isRecoveredName(typeDeclaration.name)) return;

	int problemID = 0;
	if (problemID != 0) {
		String[] arguments = new String[] {new String(typeDeclaration.name)};
		this.handle(
			problemID,
			arguments,
			arguments,
			typeDeclaration.sourceStart,
			typeDeclaration.sourceEnd);
	}
}
public void illegalModifierCombinationFinalAbstractForClass(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalModifierCombinationFinalAbstractForClass,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalModifierCombinationFinalVolatileForField(ReferenceBinding type, FieldDeclaration fieldDecl) {
	String[] arguments = new String[] {new String(fieldDecl.name)};

	this.handle(
		IProblem.IllegalModifierCombinationFinalVolatileForField,
		arguments,
		arguments,
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void illegalModifierForClass(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalModifierForClass,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalModifierForField(ReferenceBinding type, FieldDeclaration fieldDecl) {
	String[] arguments = new String[] {new String(fieldDecl.name)};
	this.handle(
		IProblem.IllegalModifierForField,
		arguments,
		arguments,
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void illegalModifierForInterface(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalModifierForInterface,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}

public void illegalModifierForInterfaceField(FieldDeclaration fieldDecl) {
	String name = new String(fieldDecl.name);
	this.handle(
		IProblem.IllegalModifierForInterfaceField,
		new String[] {
			new String(fieldDecl.binding.declaringClass.readableName()),
			name,
		},
		new String[] {
			new String(fieldDecl.binding.declaringClass.shortReadableName()),
			name,
		},
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void illegalModifierForInterfaceMethod(AbstractMethodDeclaration methodDecl) {
	this.handle(
		IProblem.IllegalModifierForInterfaceMethod,
		new String[] {
			new String(methodDecl.binding.declaringClass.readableName()),
			new String(methodDecl.getSafeName()),
			typesAsString(methodDecl.binding.isVarargs(), methodDecl.binding.parameters, false),
		},
		new String[] {
			new String(methodDecl.binding.declaringClass.shortReadableName()),
			new String(methodDecl.getSafeName()),
			typesAsString(methodDecl.binding.isVarargs(), methodDecl.binding.parameters, true),
		},
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void illegalModifierForLocalClass(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalModifierForLocalClass,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalModifierForMemberClass(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalModifierForMemberClass,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalModifierForMemberInterface(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalModifierForMemberInterface,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalModifierForMethod(AbstractMethodDeclaration methodDecl) {
	this.handle(
		IProblem.IllegalModifierForMethod,
		new String[] {
			new String(methodDecl.getSafeName()),
			typesAsString(methodDecl.binding.isVarargs(), methodDecl.binding.parameters, false),
			new String(methodDecl.binding.declaringClass.readableName()),
		},
		new String[] {
			new String(methodDecl.getSafeName()),
			typesAsString(methodDecl.binding.isVarargs(), methodDecl.binding.parameters, true),
			new String(methodDecl.binding.declaringClass.shortReadableName()),
		},
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void illegalPrimitiveOrArrayTypeForEnclosingInstance(TypeBinding enclosingType, ASTNode location) {
	this.handle(
		IProblem.IllegalPrimitiveOrArrayTypeForEnclosingInstance,
		new String[] {new String(enclosingType.readableName())},
		new String[] {new String(enclosingType.shortReadableName())},
		location.sourceStart,
		location.sourceEnd);
}
public void illegalStaticModifierForMemberType(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalStaticModifierForMemberType,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalUsageOfQualifiedTypeReference(QualifiedTypeReference qualifiedTypeReference) {
	StringBuffer buffer = new StringBuffer();
	char[][] tokens = qualifiedTypeReference.tokens;
	for (int i = 0; i < tokens.length; i++) {
		if (i > 0) buffer.append('.');
		buffer.append(tokens[i]);
	}
	String[] arguments = new String[] { String.valueOf(buffer)};
	this.handle(
		IProblem.IllegalUsageOfQualifiedTypeReference,
		arguments,
		arguments,
		qualifiedTypeReference.sourceStart,
		qualifiedTypeReference.sourceEnd);
}
public void illegalVararg(Argument argType, AbstractMethodDeclaration methodDecl) {
	String[] arguments = new String[] {CharOperation.toString(argType.type.getTypeName()), new String(methodDecl.getSafeName())};
	this.handle(
		IProblem.IllegalVararg,
		arguments,
		arguments,
		argType.sourceStart,
		argType.sourceEnd);
}
public void illegalVisibilityModifierCombinationForField(ReferenceBinding type, FieldDeclaration fieldDecl) {
	String[] arguments = new String[] {new String(fieldDecl.name)};
	this.handle(
		IProblem.IllegalVisibilityModifierCombinationForField,
		arguments,
		arguments,
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void illegalVisibilityModifierCombinationForMemberType(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalVisibilityModifierCombinationForMemberType,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalVisibilityModifierCombinationForMethod(ReferenceBinding type, AbstractMethodDeclaration methodDecl) {
	String[] arguments = new String[] {new String(type.sourceName()), new String(methodDecl.getSafeName())};
	this.handle(
		IProblem.IllegalVisibilityModifierCombinationForMethod,
		arguments,
		arguments,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void illegalVisibilityModifierForInterfaceMemberType(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.IllegalVisibilityModifierForInterfaceMemberType,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void illegalVoidExpression(ASTNode location) {
	this.handle(
		IProblem.InvalidVoidExpression,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void importProblem(ImportReference importRef, Binding expectedImport) {
	if (expectedImport instanceof FieldBinding) {
		int id = IProblem.UndefinedField;
		FieldBinding field = (FieldBinding) expectedImport;
		String[] readableArguments = null;
		String[] shortArguments = null;
		switch (expectedImport.problemId()) {
			case ProblemReasons.NotVisible :
				id = IProblem.NotVisibleField;
				readableArguments = new String[] {CharOperation.toString(importRef.tokens), new String(field.declaringClass.readableName())};
				shortArguments = new String[] {CharOperation.toString(importRef.tokens), new String(field.declaringClass.shortReadableName())};
				break;
			case ProblemReasons.Ambiguous :
				id = IProblem.AmbiguousField;
				readableArguments = new String[] {new String(field.readableName())};
				shortArguments = new String[] {new String(field.readableName())};
				break;
			case ProblemReasons.ReceiverTypeNotVisible :
				id = IProblem.NotVisibleType;
				readableArguments = new String[] {new String(field.declaringClass.leafComponentType().readableName())};
				shortArguments = new String[] {new String(field.declaringClass.leafComponentType().shortReadableName())};
				break;
		}
		this.handle(
			id,
			readableArguments,
			shortArguments,
			nodeSourceStart(field, importRef),
			nodeSourceEnd(field, importRef));
		return;
	}

	if (expectedImport.problemId() == ProblemReasons.NotFound) {
		char[][] tokens = expectedImport instanceof ProblemReferenceBinding
			? ((ProblemReferenceBinding) expectedImport).compoundName
			: importRef.tokens;
		String[] arguments = new String[]{CharOperation.toString(tokens)};
		this.handle(
		        IProblem.ImportNotFound,
		        arguments,
		        arguments,
		        importRef.sourceStart,
		        (int) importRef.sourcePositions[tokens.length - 1]);
		return;
	}
	invalidType(importRef, (TypeBinding)expectedImport);
}
public void incompatibleExceptionInThrowsClause(SourceTypeBinding type, MethodBinding currentMethod, MethodBinding inheritedMethod, ReferenceBinding exceptionType) {
	if (type == currentMethod.declaringClass) {
		int id = IProblem.IncompatibleExceptionInThrowsClause;
		
		this.handle(
			// Exception %1 is not compatible with throws clause in %2
			// 9.4.4 - The type of exception in the throws clause is incompatible.
			id,
			new String[] {
				new String(exceptionType.sourceName()),
				new String(
					CharOperation.concat(
						inheritedMethod.declaringClass.readableName(),
						inheritedMethod.readableName(),
						'.'))},
			new String[] {
				new String(exceptionType.sourceName()),
				new String(
					CharOperation.concat(
						inheritedMethod.declaringClass.shortReadableName(),
						inheritedMethod.shortReadableName(),
						'.'))},
			currentMethod.sourceStart(),
			currentMethod.sourceEnd());
	} else
		this.handle(
			// Exception %1 in throws clause of %2 is not compatible with %3
			// 9.4.4 - The type of exception in the throws clause is incompatible.
			IProblem.IncompatibleExceptionInInheritedMethodThrowsClause,
			new String[] {
				new String(exceptionType.sourceName()),
				new String(
					CharOperation.concat(
						currentMethod.declaringClass.sourceName(),
						currentMethod.readableName(),
						'.')),
				new String(
					CharOperation.concat(
						inheritedMethod.declaringClass.readableName(),
						inheritedMethod.readableName(),
						'.'))},
			new String[] {
				new String(exceptionType.sourceName()),
				new String(
					CharOperation.concat(
						currentMethod.declaringClass.sourceName(),
						currentMethod.shortReadableName(),
						'.')),
				new String(
					CharOperation.concat(
						inheritedMethod.declaringClass.shortReadableName(),
						inheritedMethod.shortReadableName(),
						'.'))},
			type.sourceStart(),
			type.sourceEnd());
}
public void incompatibleReturnType(MethodBinding currentMethod, MethodBinding inheritedMethod) {
	StringBuffer methodSignature = new StringBuffer();
	methodSignature
		.append(inheritedMethod.declaringClass.readableName())
		.append('.')
		.append(inheritedMethod.readableName());

	StringBuffer shortSignature = new StringBuffer();
	shortSignature
		.append(inheritedMethod.declaringClass.shortReadableName())
		.append('.')
		.append(inheritedMethod.shortReadableName());

	int id;
	final ReferenceBinding declaringClass = currentMethod.declaringClass;
	
	id = IProblem.IncompatibleReturnType;
	
	AbstractMethodDeclaration method = currentMethod.sourceMethod();
	int sourceStart = 0;
	int sourceEnd = 0;
	if (method == null) {
		if (declaringClass instanceof SourceTypeBinding) {
			SourceTypeBinding sourceTypeBinding = (SourceTypeBinding) declaringClass;
			sourceStart = sourceTypeBinding.sourceStart();
			sourceEnd = sourceTypeBinding.sourceEnd();
		}
	} else if (method.isConstructor() || ((MethodDeclaration) method).returnType==null){
		sourceStart = method.sourceStart;
		sourceEnd = method.sourceEnd;
	} else {
		TypeReference returnType = ((MethodDeclaration) method).returnType;
		sourceStart = returnType.sourceStart;
		sourceEnd = returnType.sourceEnd;
	}
	this.handle(
		id,
		new String[] {methodSignature.toString()},
		new String[] {shortSignature.toString()},
		sourceStart,
		sourceEnd);
}
public void incorrectLocationForNonEmptyDimension(ArrayAllocationExpression expression, int index) {
	this.handle(
		IProblem.IllegalDimension,
		NoArgument,
		NoArgument,
		expression.dimensions[index].sourceStart,
		expression.dimensions[index].sourceEnd);
}
public void incorrectSwitchType(Expression expression, TypeBinding testType) {
	this.handle(
		IProblem.IncorrectSwitchType,
		new String[] {new String(testType.readableName())},
		new String[] {new String(testType.shortReadableName())},
		expression.sourceStart,
		expression.sourceEnd);
}
public void indirectAccessToStaticField(ASTNode location, FieldBinding field){
	int severity = computeSeverity(IProblem.IndirectAccessToStaticField);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		IProblem.IndirectAccessToStaticField,
		new String[] {new String(field.declaringClass.readableName()), new String(field.name)},
		new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name)},
		severity,
		nodeSourceStart(field, location),
		nodeSourceEnd(field, location));
}
public void indirectAccessToStaticMethod(ASTNode location, MethodBinding method) {
	int severity = computeSeverity(IProblem.IndirectAccessToStaticMethod);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		IProblem.IndirectAccessToStaticMethod,
		new String[] {new String(method.declaringClass.readableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, false)},
		new String[] {new String(method.declaringClass.shortReadableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, true)},
		severity,
		location.sourceStart,
		location.sourceEnd);
}
public void inheritedMethodReducesVisibility(SourceTypeBinding type, MethodBinding concreteMethod, MethodBinding[] abstractMethods) {
	StringBuffer concreteSignature = new StringBuffer();
	concreteSignature
		.append(concreteMethod.declaringClass.readableName())
		.append('.')
		.append(concreteMethod.readableName());
	StringBuffer shortSignature = new StringBuffer();
	shortSignature
		.append(concreteMethod.declaringClass.shortReadableName())
		.append('.')
		.append(concreteMethod.shortReadableName());
	this.handle(
		// The inherited method %1 cannot hide the public abstract method in %2
		IProblem.InheritedMethodReducesVisibility,
		new String[] {
			concreteSignature.toString(),
			new String(abstractMethods[0].declaringClass.readableName())},
		new String[] {
			new String(shortSignature.toString()),
			new String(abstractMethods[0].declaringClass.shortReadableName())},
		type.sourceStart(),
		type.sourceEnd());
}
public void inheritedMethodsHaveIncompatibleReturnTypes(ASTNode location, MethodBinding[] inheritedMethods, int length) {
	StringBuffer methodSignatures = new StringBuffer();
	StringBuffer shortSignatures = new StringBuffer();
	for (int i = length; --i >= 0;) {
		methodSignatures
			.append(inheritedMethods[i].declaringClass.readableName())
			.append('.')
			.append(inheritedMethods[i].readableName());
		shortSignatures
			.append(inheritedMethods[i].declaringClass.shortReadableName())
			.append('.')
			.append(inheritedMethods[i].shortReadableName());
		if (i != 0){
			methodSignatures.append(", "); //$NON-NLS-1$
			shortSignatures.append(", "); //$NON-NLS-1$
		}
	}

	this.handle(
		// Return type is incompatible with %1
		// 9.4.2 - The return type from the method is incompatible with the declaration.
		IProblem.IncompatibleReturnType,
		new String[] {methodSignatures.toString()},
		new String[] {shortSignatures.toString()},
		location.sourceStart,
		location.sourceEnd);
}
public void inheritedMethodsHaveIncompatibleReturnTypes(SourceTypeBinding type, MethodBinding[] inheritedMethods, int length) {
	StringBuffer methodSignatures = new StringBuffer();
	StringBuffer shortSignatures = new StringBuffer();
	for (int i = length; --i >= 0;) {
		methodSignatures
			.append(inheritedMethods[i].declaringClass.readableName())
			.append('.')
			.append(inheritedMethods[i].readableName());
		shortSignatures
			.append(inheritedMethods[i].declaringClass.shortReadableName())
			.append('.')
			.append(inheritedMethods[i].shortReadableName());
		if (i != 0){
			methodSignatures.append(", "); //$NON-NLS-1$
			shortSignatures.append(", "); //$NON-NLS-1$
		}
	}

	this.handle(
		// Return type is incompatible with %1
		// 9.4.2 - The return type from the method is incompatible with the declaration.
		IProblem.IncompatibleReturnType,
		new String[] {methodSignatures.toString()},
		new String[] {shortSignatures.toString()},
		type.sourceStart(),
		type.sourceEnd());
}
public void initializerMustCompleteNormally(FieldDeclaration fieldDecl) {
	this.handle(
		IProblem.InitializerMustCompleteNormally,
		NoArgument,
		NoArgument,
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void innerTypesCannotDeclareStaticInitializers(ReferenceBinding innerType, Initializer initializer) {
	this.handle(
		IProblem.CannotDefineStaticInitializerInLocalType,
		new String[] {new String(innerType.readableName())},
		new String[] {new String(innerType.shortReadableName())},
		initializer.sourceStart,
		initializer.sourceStart);
}
public void interfaceCannotHaveInitializers(SourceTypeBinding type, FieldDeclaration fieldDecl) {
	String[] arguments = new String[] {new String(type.sourceName())};

	this.handle(
		IProblem.InterfaceCannotHaveInitializers,
		arguments,
		arguments,
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void invalidBreak(ASTNode location) {
	this.handle(
		IProblem.InvalidBreak,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void invalidConstructor(Statement statement, MethodBinding targetConstructor) {
	boolean insideDefaultConstructor =
		(this.referenceContext instanceof ConstructorDeclaration)
			&& ((ConstructorDeclaration)this.referenceContext).isDefaultConstructor();
	boolean insideImplicitConstructorCall =
		(statement instanceof ExplicitConstructorCall)
			&& (((ExplicitConstructorCall) statement).accessMode == ExplicitConstructorCall.ImplicitSuper);

	int sourceStart = statement.sourceStart;
	int sourceEnd = statement.sourceEnd;

	int id = IProblem.UndefinedConstructor; //default...
    MethodBinding shownConstructor = targetConstructor;
	switch (targetConstructor.problemId()) {
		case ProblemReasons.NotFound :
			if (insideDefaultConstructor){
				id = IProblem.UndefinedConstructorInDefaultConstructor;
			} else if (insideImplicitConstructorCall){
				id = IProblem.UndefinedConstructorInImplicitConstructorCall;
			} else {
				id = IProblem.UndefinedConstructor;
			}
			break;
		case ProblemReasons.NotVisible :
			if (insideDefaultConstructor){
				id = IProblem.NotVisibleConstructorInDefaultConstructor;
			} else if (insideImplicitConstructorCall){
				id = IProblem.NotVisibleConstructorInImplicitConstructorCall;
			} else {
				id = IProblem.NotVisibleConstructor;
			}
			ProblemMethodBinding problemConstructor = (ProblemMethodBinding) targetConstructor;
			if (problemConstructor.closestMatch != null) {
			    shownConstructor = problemConstructor.closestMatch.original();
		    }
			break;
		case ProblemReasons.Ambiguous :
			if (insideDefaultConstructor){
				id = IProblem.AmbiguousConstructorInDefaultConstructor;
			} else if (insideImplicitConstructorCall){
				id = IProblem.AmbiguousConstructorInImplicitConstructorCall;
			} else {
				id = IProblem.AmbiguousConstructor;
			}
			break;

		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}

	this.handle(
		id,
		new String[] {new String(targetConstructor.declaringClass.readableName()), typesAsString(shownConstructor.isVarargs(), shownConstructor.parameters, false)},
		new String[] {new String(targetConstructor.declaringClass.shortReadableName()), typesAsString(shownConstructor.isVarargs(), shownConstructor.parameters, true)},
		sourceStart,
		sourceEnd);
}
public void invalidContinue(ASTNode location) {
	this.handle(
		IProblem.InvalidContinue,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void invalidEnclosingType(Expression expression, TypeBinding type, ReferenceBinding enclosingType) {

	if (enclosingType.isAnonymousType()) enclosingType = enclosingType.superclass();
	if (enclosingType.sourceName != null && enclosingType.sourceName.length == 0) return;

	int flag = IProblem.UndefinedType; // default
	switch (type.problemId()) {
		case ProblemReasons.NotFound : // 1
			flag = IProblem.UndefinedType;
			break;
		case ProblemReasons.NotVisible : // 2
			flag = IProblem.NotVisibleType;
			break;
		case ProblemReasons.Ambiguous : // 3
			flag = IProblem.AmbiguousType;
			break;
		case ProblemReasons.InternalNameProvided :
			flag = IProblem.InternalTypeNameProvided;
			break;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}

	this.handle(
		flag,
		new String[] {new String(enclosingType.readableName()) + "." + new String(type.readableName())}, //$NON-NLS-1$
		new String[] {new String(enclosingType.shortReadableName()) + "." + new String(type.shortReadableName())}, //$NON-NLS-1$
		expression.sourceStart,
		expression.sourceEnd);
}
public void invalidExplicitConstructorCall(ASTNode location) {

	this.handle(
		IProblem.InvalidExplicitConstructorCall,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void invalidExpressionAsStatement(Expression expression){
	this.handle(
		IProblem.InvalidExpressionAsStatement,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}
public void invalidField(FieldReference fieldRef, TypeBinding searchedType) {
	if(isRecoveredName(fieldRef.token)) return;

	int id = IProblem.UndefinedField;
	FieldBinding field = fieldRef.binding;
	switch (field.problemId()) {
		case ProblemReasons.NotFound :
			id = IProblem.UndefinedField;
/* also need to check that the searchedType is the receiver type
			if (searchedType.isHierarchyInconsistent())
				severity = SecondaryError;
*/
			break;
		case ProblemReasons.NotVisible :
			this.handle(
				IProblem.NotVisibleField,
				new String[] {new String(fieldRef.token), new String(field.declaringClass.readableName())},
				new String[] {new String(fieldRef.token), new String(field.declaringClass.shortReadableName())},
				nodeSourceStart(field, fieldRef),
				nodeSourceEnd(field, fieldRef));
			return;
		case ProblemReasons.Ambiguous :
			id = IProblem.AmbiguousField;
			break;
		case ProblemReasons.NonStaticReferenceInStaticContext :
			id = IProblem.NonStaticFieldFromStaticInvocation;
			break;
		case ProblemReasons.NonStaticReferenceInConstructorInvocation :
			id = IProblem.InstanceFieldDuringConstructorInvocation;
			break;
		case ProblemReasons.InheritedNameHidesEnclosingName :
			id = IProblem.InheritedFieldHidesEnclosingName;
			break;
		case ProblemReasons.ReceiverTypeNotVisible :
			this.handle(
				IProblem.NotVisibleType, // cannot occur in javadoc comments
				new String[] {new String(searchedType.leafComponentType().readableName())},
				new String[] {new String(searchedType.leafComponentType().shortReadableName())},
				fieldRef.receiver.sourceStart,
				fieldRef.receiver.sourceEnd);
			return;

		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}

	String[] arguments = new String[] {new String(field.readableName())};
	this.handle(
		id,
		arguments,
		arguments,
		nodeSourceStart(field, fieldRef),
		nodeSourceEnd(field, fieldRef));
}
public void invalidField(NameReference nameRef, FieldBinding field) {
	if (nameRef instanceof QualifiedNameReference) {
		QualifiedNameReference ref = (QualifiedNameReference) nameRef;
		if (isRecoveredName(ref.tokens)) return;
	} else {
		SingleNameReference ref = (SingleNameReference) nameRef;
		if (isRecoveredName(ref.token)) return;
	}
	int id = IProblem.UndefinedField;
	switch (field.problemId()) {
		case ProblemReasons.NotFound :
			id = IProblem.UndefinedField;
			break;
		case ProblemReasons.NotVisible :
			char[] name = field.readableName();
			name = CharOperation.lastSegment(name, '.');
			this.handle(
				IProblem.NotVisibleField,
				new String[] {new String(name), new String(field.declaringClass.readableName())},
				new String[] {new String(name), new String(field.declaringClass.shortReadableName())},
				nodeSourceStart(field, nameRef),
				nodeSourceEnd(field, nameRef));
			return;
		case ProblemReasons.Ambiguous :
			id = IProblem.AmbiguousField;
			break;
		case ProblemReasons.NonStaticReferenceInStaticContext :
			id = IProblem.NonStaticFieldFromStaticInvocation;
			break;
		case ProblemReasons.NonStaticReferenceInConstructorInvocation :
			id = IProblem.InstanceFieldDuringConstructorInvocation;
			break;
		case ProblemReasons.InheritedNameHidesEnclosingName :
			id = IProblem.InheritedFieldHidesEnclosingName;
			break;
		case ProblemReasons.ReceiverTypeNotVisible :
			this.handle(
				IProblem.NotVisibleType,
				new String[] {new String(field.declaringClass.leafComponentType().readableName())},
				new String[] {new String(field.declaringClass.leafComponentType().shortReadableName())},
				nameRef.sourceStart,
				nameRef.sourceEnd);
			return;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}
	String[] arguments = new String[] {new String(field.readableName())};
	this.handle(
		id,
		arguments,
		arguments,
		nameRef.sourceStart,
		nameRef.sourceEnd);
}
public void invalidField(QualifiedNameReference nameRef, FieldBinding field, int index, TypeBinding searchedType) {
	//the resolution of the index-th field of qname failed
	//qname.otherBindings[index] is the binding that has produced the error

	//The different targetted errors should be :
	//UndefinedField
	//NotVisibleField
	//AmbiguousField

	if (isRecoveredName(nameRef.tokens)) return;

	if (searchedType.isBaseType()) {
		this.handle(
			IProblem.NoFieldOnBaseType,
			new String[] {
				new String(searchedType.readableName()),
				CharOperation.toString(CharOperation.subarray(nameRef.tokens, 0, index)),
				new String(nameRef.tokens[index])},
			new String[] {
				new String(searchedType.sourceName()),
				CharOperation.toString(CharOperation.subarray(nameRef.tokens, 0, index)),
				new String(nameRef.tokens[index])},
			nameRef.sourceStart,
			(int) nameRef.sourcePositions[index]);
		return;
	}

	int id = IProblem.UndefinedField;
	switch (field.problemId()) {
		case ProblemReasons.NotFound :
			id = IProblem.UndefinedField;
/* also need to check that the searchedType is the receiver type
			if (searchedType.isHierarchyInconsistent())
				severity = SecondaryError;
*/
			break;
		case ProblemReasons.NotVisible :
			String fieldName = new String(nameRef.tokens[index]);
			this.handle(
				IProblem.NotVisibleField,
				new String[] {fieldName, new String(field.declaringClass.readableName())},
				new String[] {fieldName, new String(field.declaringClass.shortReadableName())},
				nodeSourceStart(field, nameRef),
				nodeSourceEnd(field, nameRef));
			return;
		case ProblemReasons.Ambiguous :
			id = IProblem.AmbiguousField;
			break;
		case ProblemReasons.NonStaticReferenceInStaticContext :
			id = IProblem.NonStaticFieldFromStaticInvocation;
			break;
		case ProblemReasons.NonStaticReferenceInConstructorInvocation :
			id = IProblem.InstanceFieldDuringConstructorInvocation;
			break;
		case ProblemReasons.InheritedNameHidesEnclosingName :
			id = IProblem.InheritedFieldHidesEnclosingName;
			break;
		case ProblemReasons.ReceiverTypeNotVisible :
			this.handle(
				IProblem.NotVisibleType,
				new String[] {new String(searchedType.leafComponentType().readableName())},
				new String[] {new String(searchedType.leafComponentType().shortReadableName())},
				nameRef.sourceStart,
				nameRef.sourceEnd);
			return;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}
	String[] arguments = new String[] {CharOperation.toString(CharOperation.subarray(nameRef.tokens, 0, index + 1))};
	this.handle(
		id,
		arguments,
		arguments,
		nameRef.sourceStart,
		(int) nameRef.sourcePositions[index]);
}
public void invalidMethod(MessageSend messageSend, MethodBinding method) {
	if (isRecoveredName(messageSend.selector)) return;

	boolean isFunction = method.declaringClass==null;
	int id = isFunction? IProblem.UndefinedFunction : IProblem.UndefinedMethod; //default...
    MethodBinding shownMethod = method;

	switch (method.problemId()) {
		case ProblemReasons.NotFound :
			id = isFunction? IProblem.UndefinedFunction : IProblem.UndefinedMethod;
			ProblemMethodBinding problemMethod = (ProblemMethodBinding) method;
			if (problemMethod.closestMatch != null) {
			    	shownMethod = problemMethod.closestMatch;
					String closestParameterTypeNames = typesAsString(shownMethod.isVarargs(), shownMethod.parameters, false);
					String parameterTypeNames = typesAsString(false, problemMethod.parameters, false);
					String closestParameterTypeShortNames = typesAsString(shownMethod.isVarargs(), shownMethod.parameters, true);
					String parameterTypeShortNames = typesAsString(false, problemMethod.parameters, true);
					this.handle(
						IProblem.ParameterMismatch,
						new String[] {
							new String(shownMethod.declaringClass.readableName()),
							new String(shownMethod.selector),
							closestParameterTypeNames,
							parameterTypeNames
						},
						new String[] {
							new String(shownMethod.declaringClass.shortReadableName()),
							new String(shownMethod.selector),
							closestParameterTypeShortNames,
							parameterTypeShortNames
						},
						(int) (messageSend.nameSourcePosition >>> 32),
						(int) messageSend.nameSourcePosition);
					return;
			}
			break;
		case ProblemReasons.NotVisible :
			id = IProblem.NotVisibleMethod;
			problemMethod = (ProblemMethodBinding) method;
			if (problemMethod.closestMatch != null) {
			    shownMethod = problemMethod.closestMatch.original();
		    }
			break;
		case ProblemReasons.Ambiguous :
			id = IProblem.AmbiguousMethod;
			break;
		case ProblemReasons.NotAFunction :
			id = IProblem.NotAFunction;
			break;
		case ProblemReasons.InheritedNameHidesEnclosingName :
			id = IProblem.InheritedMethodHidesEnclosingName;
			break;
		case ProblemReasons.NonStaticReferenceInConstructorInvocation :
			id = IProblem.InstanceMethodDuringConstructorInvocation;
			break;
		case ProblemReasons.NonStaticReferenceInStaticContext :
			id = IProblem.StaticMethodRequested;
			break;
		case ProblemReasons.ReceiverTypeNotVisible :
			this.handle(
				IProblem.NotVisibleType,	// cannot occur in javadoc comments
				new String[] {new String(method.declaringClass.leafComponentType().readableName())},
				new String[] {new String(method.declaringClass.leafComponentType().shortReadableName())},
				messageSend.receiver.sourceStart,
				messageSend.receiver.sourceEnd);
			return;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}

	String shortName=""; //$NON-NLS-1$
	String readableName=""; //$NON-NLS-1$
	String methodName=(shownMethod.selector!=null)? new String(shownMethod.selector):""; //$NON-NLS-1$
	if (method.declaringClass!=null)
	{
		shortName=		readableName=new String(method.declaringClass.readableName());

	}
	this.handle(
		id,
		new String[] {
				readableName,
				shortName, typesAsString(shownMethod.isVarargs(), shownMethod.parameters, false)},
		new String[] {
				shortName,
				methodName, typesAsString(shownMethod.isVarargs(), shownMethod.parameters, true)},
		(int) (messageSend.nameSourcePosition >>> 32),
		(int) messageSend.nameSourcePosition);
}


public void invalidNullToSynchronize(Expression expression) {
	this.handle(
		IProblem.InvalidNullToSynchronized,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}

public void wrongNumberOfArguments(MessageSend functionCall, MethodBinding binding) {
	String functionName =  new String(functionCall.selector);
	int actualArguments=(functionCall.arguments!=null) ? functionCall.arguments.length : 0;
    String actualNumber=String.valueOf(actualArguments);
    String expectingNumber=String.valueOf(binding.parameters.length);
	
	this.handle(
		IProblem.WrongNumberOfArguments,
		new String[] {
				functionName,expectingNumber,actualNumber}, //$NON-NLS-1$
		new String[] {
				functionName,expectingNumber,actualNumber}, //$NON-NLS-1$
		functionCall.sourceStart,
		functionCall.sourceEnd);
}

public void wrongNumberOfArguments(AllocationExpression allocationExpression, MethodBinding binding) {
	char[] typeName = Util.getTypeName(allocationExpression.member);
	String functionName =  typeName!=null ? new String(typeName) : "";
	int actualArguments=(allocationExpression.arguments!=null) ? allocationExpression.arguments.length : 0;
    String actualNumber=String.valueOf(actualArguments);
    String expectingNumber=String.valueOf(binding.parameters.length);
	
	this.handle(
		IProblem.WrongNumberOfArguments,
		new String[] {
				functionName,expectingNumber,actualNumber}, //$NON-NLS-1$
		new String[] {
				functionName,expectingNumber,actualNumber}, //$NON-NLS-1$
				allocationExpression.sourceStart,
				allocationExpression.sourceEnd);
}


public void invalidOperator(BinaryExpression expression, TypeBinding leftType, TypeBinding rightType) {
	String leftName = new String(leftType.readableName());
	String rightName = new String(rightType.readableName());
	String leftShortName = new String(leftType.shortReadableName());
	String rightShortName = new String(rightType.shortReadableName());
	if (leftShortName.equals(rightShortName)){
		leftShortName = leftName;
		rightShortName = rightName;
	}
	this.handle(
		IProblem.InvalidOperator,
		new String[] {
			expression.operatorToString(),
			leftName + ", " + rightName}, //$NON-NLS-1$
		new String[] {
			expression.operatorToString(),
			leftShortName + ", " + rightShortName}, //$NON-NLS-1$
		expression.sourceStart,
		expression.sourceEnd);
}
public void invalidOperator(CompoundAssignment assign, TypeBinding leftType, TypeBinding rightType) {
	String leftName = new String(leftType.readableName());
	String rightName = new String(rightType.readableName());
	String leftShortName = new String(leftType.shortReadableName());
	String rightShortName = new String(rightType.shortReadableName());
	if (leftShortName.equals(rightShortName)){
		leftShortName = leftName;
		rightShortName = rightName;
	}
	this.handle(
		IProblem.InvalidOperator,
		new String[] {
			assign.operatorToString(),
			leftName + ", " + rightName}, //$NON-NLS-1$
		new String[] {
			assign.operatorToString(),
			leftShortName + ", " + rightShortName}, //$NON-NLS-1$
		assign.sourceStart,
		assign.sourceEnd);
}
public void invalidOperator(UnaryExpression expression, TypeBinding type) {
	this.handle(
		IProblem.InvalidOperator,
		new String[] {expression.operatorToString(), new String(type.readableName())},
		new String[] {expression.operatorToString(), new String(type.shortReadableName())},
		expression.sourceStart,
		expression.sourceEnd);
}
public void invalidParenthesizedExpression(ASTNode reference) {
	this.handle(
		IProblem.InvalidParenthesizedExpression,
		NoArgument,
		NoArgument,
		reference.sourceStart,
		reference.sourceEnd);
}
public void invalidType(ASTNode location, TypeBinding type) {
	if (type instanceof ReferenceBinding) {
		if (isRecoveredName(((ReferenceBinding)type).compoundName)) return;
	}
	else if (type instanceof ArrayBinding) {
		TypeBinding leafType = ((ArrayBinding)type).leafComponentType;
		if (leafType instanceof ReferenceBinding) {
			if (isRecoveredName(((ReferenceBinding)leafType).compoundName)) return;
		}
	}

	int id = IProblem.UndefinedType; // default
	switch (type.problemId()) {
		case ProblemReasons.NotFound :
			id = IProblem.UndefinedType;
			break;
		case ProblemReasons.NotVisible :
			id = IProblem.NotVisibleType;
			break;
		case ProblemReasons.Ambiguous :
			id = IProblem.AmbiguousType;
			break;
		case ProblemReasons.InternalNameProvided :
			id = IProblem.InternalTypeNameProvided;
			break;
		case ProblemReasons.InheritedNameHidesEnclosingName :
			id = IProblem.InheritedTypeHidesEnclosingName;
			break;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}

	int end = location.sourceEnd;
	if (location instanceof QualifiedNameReference) {
		QualifiedNameReference ref = (QualifiedNameReference) location;
		if (isRecoveredName(ref.tokens)) return;
		if (ref.indexOfFirstFieldBinding >= 1)
			end = (int) ref.sourcePositions[ref.indexOfFirstFieldBinding - 1];
	} else if (location instanceof ArrayQualifiedTypeReference) {
		ArrayQualifiedTypeReference arrayQualifiedTypeReference = (ArrayQualifiedTypeReference) location;
		if (isRecoveredName(arrayQualifiedTypeReference.tokens)) return;
		long[] positions = arrayQualifiedTypeReference.sourcePositions;
		end = (int) positions[positions.length - 1];
	} else if (location instanceof QualifiedTypeReference) {
		QualifiedTypeReference ref = (QualifiedTypeReference) location;
		if (isRecoveredName(ref.tokens)) return;
		if (type instanceof ReferenceBinding) {
			char[][] name = ((ReferenceBinding) type).compoundName;
			if (name.length <= ref.sourcePositions.length)
				end = (int) ref.sourcePositions[name.length - 1];
		}
	} else if (location instanceof ImportReference) {
		ImportReference ref = (ImportReference) location;
		if (isRecoveredName(ref.tokens)) return;
		if (type instanceof ReferenceBinding) {
			char[][] name = ((ReferenceBinding) type).compoundName;
			end = (int) ref.sourcePositions[name.length - 1];
		}
	} else if (location instanceof ArrayTypeReference) {
		ArrayTypeReference arrayTypeReference = (ArrayTypeReference) location;
		if (isRecoveredName(arrayTypeReference.token)) return;
		end = arrayTypeReference.originalSourceEnd;
	}
	this.handle(
		id,
		new String[] {new String(type.leafComponentType().readableName()) },
		new String[] {new String(type.leafComponentType().shortReadableName())},
		location.sourceStart,
		end);
}
public void invalidTypeForCollection(Expression expression) {
	this.handle(
			IProblem.InvalidTypeForCollection,
			NoArgument,
			NoArgument,
			expression.sourceStart,
			expression.sourceEnd);
}
public void invalidTypeToSynchronize(Expression expression, TypeBinding type) {
	this.handle(
		IProblem.InvalidTypeToSynchronized,
		new String[] {new String(type.readableName())},
		new String[] {new String(type.shortReadableName())},
		expression.sourceStart,
		expression.sourceEnd);
}
public void invalidUnaryExpression(Expression expression) {
	this.handle(
		IProblem.InvalidUnaryExpression,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}
public void invalidUsageOfForeachStatements(LocalDeclaration elementVariable, Expression collection) {
	this.handle(
		IProblem.InvalidUsageOfForeachStatements,
		NoArgument,
		NoArgument,
		elementVariable.declarationSourceStart,
		collection.sourceEnd);
}
public void isClassPathCorrect(char[][] wellKnownTypeName, CompilationUnitDeclaration compUnitDecl, Object location) {
	this.referenceContext = compUnitDecl;
	String[] arguments = new String[] {CharOperation.toString(wellKnownTypeName)};
	int start = 0, end = 0;
	if (location != null) {
		if (location instanceof InvocationSite) {
			InvocationSite site = (InvocationSite) location;
			start = site.sourceStart();
			end = site.sourceEnd();
		} else if (location instanceof ASTNode) {
			ASTNode node = (ASTNode) location;
			start = node.sourceStart();
			end = node.sourceEnd();
		}
	}
	this.handle(
		IProblem.IsClassPathCorrect,
		arguments,
		arguments,
		start,
		end);
}
private boolean isIdentifier(int token) {
	return token == TerminalTokens.TokenNameIdentifier;
}
private boolean isKeyword(int token) {
	switch(token) {
		case TerminalTokens.TokenNameabstract:
		case TerminalTokens.TokenNamebyte:
		case TerminalTokens.TokenNamebreak:
		case TerminalTokens.TokenNameboolean:
		case TerminalTokens.TokenNamecase:
		case TerminalTokens.TokenNamechar:
		case TerminalTokens.TokenNamecatch:
		case TerminalTokens.TokenNameclass:
		case TerminalTokens.TokenNamecontinue:
		case TerminalTokens.TokenNamedo:
		case TerminalTokens.TokenNamedouble:
		case TerminalTokens.TokenNamedefault:
		case TerminalTokens.TokenNameelse:
		case TerminalTokens.TokenNameextends:
		case TerminalTokens.TokenNamefor:
		case TerminalTokens.TokenNamefinal:
		case TerminalTokens.TokenNamefloat:
		case TerminalTokens.TokenNamefalse:
		case TerminalTokens.TokenNamefinally:
		case TerminalTokens.TokenNameif:
		case TerminalTokens.TokenNameint:
		case TerminalTokens.TokenNameimport:
		case TerminalTokens.TokenNameinterface:
		case TerminalTokens.TokenNameimplements:
		case TerminalTokens.TokenNameinstanceof:
		case TerminalTokens.TokenNamelong:
		case TerminalTokens.TokenNamenew:
		case TerminalTokens.TokenNamenull:
		case TerminalTokens.TokenNamenative:
		case TerminalTokens.TokenNamepublic:
		case TerminalTokens.TokenNamepackage:
		case TerminalTokens.TokenNameprivate:
		case TerminalTokens.TokenNameprotected:
		case TerminalTokens.TokenNamereturn:
		case TerminalTokens.TokenNameshort:
		case TerminalTokens.TokenNamesuper:
		case TerminalTokens.TokenNamestatic:
		case TerminalTokens.TokenNameswitch:
		case TerminalTokens.TokenNamestrictfp:
		case TerminalTokens.TokenNamesynchronized:
		case TerminalTokens.TokenNametry:
		case TerminalTokens.TokenNamethis:
		case TerminalTokens.TokenNametrue:
		case TerminalTokens.TokenNamethrow:
		case TerminalTokens.TokenNamethrows:
		case TerminalTokens.TokenNametransient:
		case TerminalTokens.TokenNamevoid:
		case TerminalTokens.TokenNamevolatile:
		case TerminalTokens.TokenNamewhile:
		case TerminalTokens.TokenNamedelete :
		case TerminalTokens.TokenNamedebugger :
		case TerminalTokens.TokenNameexport :
		case TerminalTokens.TokenNamefunction :
		case TerminalTokens.TokenNamein :
		case TerminalTokens.TokenNameinfinity :
		case TerminalTokens.TokenNameundefined :
		case TerminalTokens.TokenNamewith :
			return true;
		default:
			return false;
	}
}
private boolean isLiteral(int token) {
	switch(token) {
		case TerminalTokens.TokenNameIntegerLiteral:
		case TerminalTokens.TokenNameLongLiteral:
		case TerminalTokens.TokenNameFloatingPointLiteral:
		case TerminalTokens.TokenNameDoubleLiteral:
		case TerminalTokens.TokenNameStringLiteral:
		case TerminalTokens.TokenNameCharacterLiteral:
			return true;
		default:
			return false;
	}
}
private boolean isRecoveredName(char[] simpleName) {
	return simpleName == RecoveryScanner.FAKE_IDENTIFIER;
}
private boolean isRecoveredName(char[][] qualifiedName) {
	if(qualifiedName == null) return false;

	for (int i = 0; i < qualifiedName.length; i++) {
		if(qualifiedName[i] == RecoveryScanner.FAKE_IDENTIFIER) return true;
	}

	return false;
}

public void javadocDeprecatedField(FieldBinding field, ASTNode location, int modifiers) {
	int severity = computeSeverity(IProblem.JavadocUsingDeprecatedField);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		this.handle(
			IProblem.JavadocUsingDeprecatedField,
			new String[] {new String(field.declaringClass.readableName()), new String(field.name)},
			new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name)},
			severity,
			nodeSourceStart(field, location),
			nodeSourceEnd(field, location));
	}
}

public void javadocDeprecatedMethod(MethodBinding method, ASTNode location, int modifiers) {
	boolean isConstructor = method.isConstructor();
	int severity = computeSeverity(isConstructor ? IProblem.JavadocUsingDeprecatedConstructor : IProblem.JavadocUsingDeprecatedMethod);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		if (isConstructor) {
			this.handle(
				IProblem.JavadocUsingDeprecatedConstructor,
				new String[] {new String(method.declaringClass.readableName()), typesAsString(method.isVarargs(), method.parameters, false)},
				new String[] {new String(method.declaringClass.shortReadableName()), typesAsString(method.isVarargs(), method.parameters, true)},
				severity,
				location.sourceStart,
				location.sourceEnd);
		} else {
			this.handle(
				IProblem.JavadocUsingDeprecatedMethod,
				new String[] {new String(method.declaringClass.readableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, false)},
				new String[] {new String(method.declaringClass.shortReadableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, true)},
				severity,
				location.sourceStart,
				location.sourceEnd);
		}
	}
}
public void javadocDeprecatedType(TypeBinding type, ASTNode location, int modifiers) {
	if (location == null) return; // 1G828DN - no type ref for synthetic arguments
	int severity = computeSeverity(IProblem.JavadocUsingDeprecatedType);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		if (type.isMemberType() && type instanceof ReferenceBinding && !javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, ((ReferenceBinding)type).modifiers)) {
			this.handle(IProblem.JavadocHiddenReference, NoArgument, NoArgument, location.sourceStart, location.sourceEnd);
		} else {
			this.handle(
				IProblem.JavadocUsingDeprecatedType,
				new String[] {new String(type.readableName())},
				new String[] {new String(type.shortReadableName())},
				severity,
				location.sourceStart,
				location.sourceEnd);
		}
	}
}
public void javadocDuplicatedParamTag(char[] token, int sourceStart, int sourceEnd, int modifiers) {
	int severity = computeSeverity(IProblem.JavadocDuplicateParamName);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		String[] arguments = new String[] {String.valueOf(token)};
		this.handle(
			IProblem.JavadocDuplicateParamName,
			arguments,
			arguments,
			severity,
			sourceStart,
			sourceEnd);
	}
}
public void javadocDuplicatedReturnTag(int sourceStart, int sourceEnd){
	this.handle(IProblem.JavadocDuplicateReturnTag, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocDuplicatedThrowsClassName(TypeReference typeReference, int modifiers) {
	int severity = computeSeverity(IProblem.JavadocDuplicateThrowsClassName);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		String[] arguments = new String[] {String.valueOf(typeReference.resolvedType.sourceName())};
		this.handle(
			IProblem.JavadocDuplicateThrowsClassName,
			arguments,
			arguments,
			severity,
			typeReference.sourceStart,
			typeReference.sourceEnd);
	}
}
public void javadocEmptyReturnTag(int sourceStart, int sourceEnd, int modifiers) {
	int severity = computeSeverity(IProblem.JavadocEmptyReturnTag);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		this.handle(IProblem.JavadocEmptyReturnTag, NoArgument, NoArgument, sourceStart, sourceEnd);
	}
}
public void javadocErrorNoMethodFor(MessageSend messageSend, TypeBinding recType, TypeBinding[] params, int modifiers) {
	int id = recType.isArrayType() ? IProblem.JavadocNoMessageSendOnArrayType : IProblem.JavadocNoMessageSendOnBaseType;
	int severity = computeSeverity(id);
	if (severity == ProblemSeverities.Ignore) return;
	StringBuffer buffer = new StringBuffer();
	StringBuffer shortBuffer = new StringBuffer();
	for (int i = 0, length = params.length; i < length; i++) {
		if (i != 0){
			buffer.append(", "); //$NON-NLS-1$
			shortBuffer.append(", "); //$NON-NLS-1$
		}
		buffer.append(new String(params[i].readableName()));
		shortBuffer.append(new String(params[i].shortReadableName()));
	}
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		this.handle(
			id,
			new String[] {new String(recType.readableName()), new String(messageSend.selector), buffer.toString()},
			new String[] {new String(recType.shortReadableName()), new String(messageSend.selector), shortBuffer.toString()},
			severity,
			messageSend.sourceStart,
			messageSend.sourceEnd);
	}
}
public void javadocHiddenReference(int sourceStart, int sourceEnd, Scope scope, int modifiers) {
	Scope currentScope = scope;
	while (currentScope.parent.kind != Scope.COMPILATION_UNIT_SCOPE ) {
		if (!javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, currentScope.getDeclarationModifiers())) {
			return;
		}
		currentScope = currentScope.parent;
	}
	String[] arguments = new String[] { this.options.getVisibilityString(this.options.reportInvalidJavadocTagsVisibility), this.options.getVisibilityString(modifiers) };
	this.handle(IProblem.JavadocHiddenReference, arguments, arguments, sourceStart, sourceEnd);
}
public void javadocInvalidConstructor(Statement statement, MethodBinding targetConstructor, int modifiers) {

	if (!javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) return;
	int id = IProblem.JavadocUndefinedConstructor; //default...
	switch (targetConstructor.problemId()) {
		case ProblemReasons.NotFound :
			id = IProblem.JavadocUndefinedConstructor;
			break;
		case ProblemReasons.NotVisible :
			id = IProblem.JavadocNotVisibleConstructor;
			break;
		case ProblemReasons.Ambiguous :
			id = IProblem.JavadocAmbiguousConstructor;
			break;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}
	int severity = computeSeverity(id);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		id,
		new String[] {new String(targetConstructor.declaringClass.readableName()), typesAsString(targetConstructor.isVarargs(), targetConstructor.parameters, false)},
		new String[] {new String(targetConstructor.declaringClass.shortReadableName()), typesAsString(targetConstructor.isVarargs(), targetConstructor.parameters, true)},
		severity,
		statement.sourceStart,
		statement.sourceEnd);
}
/*
 * Similar implementation than invalidField(FieldReference...)
 * Note that following problem id cannot occur for Javadoc:
 * 	- NonStaticReferenceInStaticContext :
 * 	- NonStaticReferenceInConstructorInvocation :
 * 	- ReceiverTypeNotVisible :
 */
public void javadocInvalidField(int sourceStart, int sourceEnd, Binding fieldBinding, TypeBinding searchedType, int modifiers) {
	int id = IProblem.JavadocUndefinedField;
	switch (fieldBinding.problemId()) {
		case ProblemReasons.NotFound :
			id = IProblem.JavadocUndefinedField;
			break;
		case ProblemReasons.NotVisible :
			id = IProblem.JavadocNotVisibleField;
			break;
		case ProblemReasons.Ambiguous :
			id = IProblem.JavadocAmbiguousField;
			break;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}
	int severity = computeSeverity(id);
	if (severity == ProblemSeverities.Ignore) return;
	// report issue
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		String[] arguments = new String[] {new String(fieldBinding.readableName())};
		handle(
			id,
			arguments,
			arguments,
			severity,
			sourceStart,
			sourceEnd);
	}
}
public void javadocInvalidMemberTypeQualification(int sourceStart, int sourceEnd, int modifiers){
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		this.handle(IProblem.JavadocInvalidMemberTypeQualification, NoArgument, NoArgument, sourceStart, sourceEnd);
	}
}
/*
 * Similar implementation than invalidMethod(MessageSend...)
 * Note that following problem id cannot occur for Javadoc:
 * 	- NonStaticReferenceInStaticContext :
 * 	- NonStaticReferenceInConstructorInvocation :
 * 	- ReceiverTypeNotVisible :
 */
public void javadocInvalidMethod(MessageSend messageSend, MethodBinding method, int modifiers) {
	if (!javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) return;
	// set problem id
	ProblemMethodBinding problemMethod = null;
	MethodBinding shownMethod = null;
	int id = IProblem.JavadocUndefinedMethod; //default...
	switch (method.problemId()) {
		case ProblemReasons.NotFound :
			id = IProblem.JavadocUndefinedMethod;
			problemMethod = (ProblemMethodBinding) method;
			if (problemMethod.closestMatch != null) {
				int severity = computeSeverity(IProblem.JavadocParameterMismatch);
				if (severity == ProblemSeverities.Ignore) return;
				String closestParameterTypeNames = typesAsString(problemMethod.closestMatch.isVarargs(), problemMethod.closestMatch.parameters, false);
				String parameterTypeNames = typesAsString(method.isVarargs(), method.parameters, false);
				String closestParameterTypeShortNames = typesAsString(problemMethod.closestMatch.isVarargs(), problemMethod.closestMatch.parameters, true);
				String parameterTypeShortNames = typesAsString(method.isVarargs(), method.parameters, true);
				if (closestParameterTypeShortNames.equals(parameterTypeShortNames)){
					closestParameterTypeShortNames = closestParameterTypeNames;
					parameterTypeShortNames = parameterTypeNames;
				}
				this.handle(
					IProblem.JavadocParameterMismatch,
					new String[] {
						new String(problemMethod.closestMatch.declaringClass.readableName()),
						new String(problemMethod.closestMatch.selector),
						closestParameterTypeNames,
						parameterTypeNames
					},
					new String[] {
						new String(problemMethod.closestMatch.declaringClass.shortReadableName()),
						new String(problemMethod.closestMatch.selector),
						closestParameterTypeShortNames,
						parameterTypeShortNames
					},
					severity,
					(int) (messageSend.nameSourcePosition >>> 32),
					(int) messageSend.nameSourcePosition);
				return;
			}
			break;
		case ProblemReasons.NotVisible :
			id = IProblem.JavadocNotVisibleMethod;
			break;
		case ProblemReasons.Ambiguous :
			id = IProblem.JavadocAmbiguousMethod;
			break;
		case ProblemReasons.NoError : // 0
		default :
			needImplementation(); // want to fail to see why we were here...
			break;
	}
	int severity = computeSeverity(id);
	if (severity == ProblemSeverities.Ignore) return;
	// report issue
	this.handle(
		id,
		new String[] {
			new String(method.declaringClass.readableName()),
			new String(method.selector), typesAsString(method.isVarargs(), method.parameters, false)},
		new String[] {
			new String(method.declaringClass.shortReadableName()),
			new String(method.selector), typesAsString(method.isVarargs(), method.parameters, true)},
		severity,
		(int) (messageSend.nameSourcePosition >>> 32),
		(int) messageSend.nameSourcePosition);
}
public void javadocInvalidParamTagName(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocInvalidParamTagName, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocInvalidParamTypeParameter(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocInvalidParamTagTypeParameter, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocInvalidReference(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocInvalidSeeReference, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocInvalidSeeReferenceArgs(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocInvalidSeeArgs, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocInvalidSeeUrlReference(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocInvalidSeeHref, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocInvalidTag(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocInvalidTag, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocInvalidThrowsClass(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocInvalidThrowsClass, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocInvalidThrowsClassName(TypeReference typeReference, int modifiers) {
	int severity = computeSeverity(IProblem.JavadocInvalidThrowsClassName);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		String[] arguments = new String[] {String.valueOf(typeReference.resolvedType.sourceName())};
		this.handle(
			IProblem.JavadocInvalidThrowsClassName,
			arguments,
			arguments,
			severity,
			typeReference.sourceStart,
			typeReference.sourceEnd);
	}
}
public void javadocInvalidType(ASTNode location, TypeBinding type, int modifiers) {
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		int id = IProblem.JavadocUndefinedType; // default
		switch (type.problemId()) {
			case ProblemReasons.NotFound :
				id = IProblem.JavadocUndefinedType;
				break;
			case ProblemReasons.NotVisible :
				id = IProblem.JavadocNotVisibleType;
				break;
			case ProblemReasons.Ambiguous :
				id = IProblem.JavadocAmbiguousType;
				break;
			case ProblemReasons.InternalNameProvided :
				id = IProblem.JavadocInternalTypeNameProvided;
				break;
			case ProblemReasons.InheritedNameHidesEnclosingName :
				id = IProblem.JavadocInheritedNameHidesEnclosingTypeName;
				break;
			case ProblemReasons.NonStaticReferenceInStaticContext :
				id = IProblem.JavadocNonStaticTypeFromStaticInvocation;
			    break;
			case ProblemReasons.NoError : // 0
			default :
				needImplementation(); // want to fail to see why we were here...
				break;
		}
		int severity = computeSeverity(id);
		if (severity == ProblemSeverities.Ignore) return;
		this.handle(
			id,
			new String[] {new String(type.readableName())},
			new String[] {new String(type.shortReadableName())},
			severity,
			location.sourceStart,
			location.sourceEnd);
	}
}
public void javadocMalformedSeeReference(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocMalformedSeeReference, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocMissing(int sourceStart, int sourceEnd, int modifiers){
	int severity = computeSeverity(IProblem.JavadocMissing);
	if (severity == ProblemSeverities.Ignore) return;
	boolean overriding = (modifiers & (ExtraCompilerModifiers.AccImplementing|ExtraCompilerModifiers.AccOverriding)) != 0;
	boolean report = (this.options.getSeverity(CompilerOptions.MissingJavadocComments) != ProblemSeverities.Ignore)
					&& (!overriding || this.options.reportMissingJavadocCommentsOverriding);
	if (report) {
		String arg = javadocVisibilityArgument(this.options.reportMissingJavadocCommentsVisibility, modifiers);
		if (arg != null) {
			String[] arguments = new String[] { arg };
			this.handle(
				IProblem.JavadocMissing,
				arguments,
				arguments,
				severity,
				sourceStart,
				sourceEnd);
		}
	}
}
public void javadocMissingHashCharacter(int sourceStart, int sourceEnd, String ref){
	int severity = computeSeverity(IProblem.JavadocMissingHashCharacter);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] { ref };
	this.handle(
		IProblem.JavadocMissingHashCharacter,
		arguments,
		arguments,
		severity,
		sourceStart,
		sourceEnd);
}
public void javadocMissingIdentifier(int sourceStart, int sourceEnd, int modifiers){
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers))
		this.handle(IProblem.JavadocMissingIdentifier, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocMissingParamName(int sourceStart, int sourceEnd, int modifiers){
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers))
		this.handle(IProblem.JavadocMissingParamName, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocMissingParamTag(char[] name, int sourceStart, int sourceEnd, int modifiers) {
	int severity = computeSeverity(IProblem.JavadocMissingParamTag);
	if (severity == ProblemSeverities.Ignore) return;
	boolean overriding = (modifiers & (ExtraCompilerModifiers.AccImplementing|ExtraCompilerModifiers.AccOverriding)) != 0;
	boolean report = (this.options.getSeverity(CompilerOptions.MissingJavadocTags) != ProblemSeverities.Ignore)
					&& (!overriding || this.options.reportMissingJavadocTagsOverriding);
	if (report && javadocVisibility(this.options.reportMissingJavadocTagsVisibility, modifiers)) {
		String[] arguments = new String[] { String.valueOf(name) };
		this.handle(
			IProblem.JavadocMissingParamTag,
			arguments,
			arguments,
			severity,
			sourceStart,
			sourceEnd);
	}
}
public void javadocMissingReference(int sourceStart, int sourceEnd, int modifiers){
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers))
		this.handle(IProblem.JavadocMissingSeeReference, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocMissingReturnTag(int sourceStart, int sourceEnd, int modifiers){
	boolean overriding = (modifiers & (ExtraCompilerModifiers.AccImplementing|ExtraCompilerModifiers.AccOverriding)) != 0;
	boolean report = (this.options.getSeverity(CompilerOptions.MissingJavadocTags) != ProblemSeverities.Ignore)
					&& (!overriding || this.options.reportMissingJavadocTagsOverriding);
	if (report && javadocVisibility(this.options.reportMissingJavadocTagsVisibility, modifiers)) {
		this.handle(IProblem.JavadocMissingReturnTag, NoArgument, NoArgument, sourceStart, sourceEnd);
	}
}
public void javadocMissingThrowsClassName(int sourceStart, int sourceEnd, int modifiers){
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers))
		this.handle(IProblem.JavadocMissingThrowsClassName, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocMissingThrowsTag(TypeReference typeRef, int modifiers){
	int severity = computeSeverity(IProblem.JavadocMissingThrowsTag);
	if (severity == ProblemSeverities.Ignore) return;
	boolean overriding = (modifiers & (ExtraCompilerModifiers.AccImplementing|ExtraCompilerModifiers.AccOverriding)) != 0;
	boolean report = (this.options.getSeverity(CompilerOptions.MissingJavadocTags) != ProblemSeverities.Ignore)
					&& (!overriding || this.options.reportMissingJavadocTagsOverriding);
	if (report && javadocVisibility(this.options.reportMissingJavadocTagsVisibility, modifiers)) {
		String[] arguments = new String[] { String.valueOf(typeRef.resolvedType.sourceName()) };
		this.handle(
			IProblem.JavadocMissingThrowsTag,
			arguments,
			arguments,
			severity,
			typeRef.sourceStart,
			typeRef.sourceEnd);
	}
}
public void javadocUndeclaredParamTagName(char[] token, int sourceStart, int sourceEnd, int modifiers) {
	int severity = computeSeverity(IProblem.JavadocInvalidParamName);
	if (severity == ProblemSeverities.Ignore) return;
	if (javadocVisibility(this.options.reportInvalidJavadocTagsVisibility, modifiers)) {
		String[] arguments = new String[] {String.valueOf(token)};
		this.handle(
			IProblem.JavadocInvalidParamName,
			arguments,
			arguments,
			severity,
			sourceStart,
			sourceEnd);
	}
}
public void javadocUnexpectedTag(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocUnexpectedTag, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocUnexpectedText(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocUnexpectedText, NoArgument, NoArgument, sourceStart, sourceEnd);
}
public void javadocUnterminatedInlineTag(int sourceStart, int sourceEnd) {
	this.handle(IProblem.JavadocUnterminatedInlineTag, NoArgument, NoArgument, sourceStart, sourceEnd);
}
private boolean javadocVisibility(int visibility, int modifiers) {
	if (modifiers < 0) return true;
	switch (modifiers & ExtraCompilerModifiers.AccVisibilityMASK) {
		case ClassFileConstants.AccPublic :
			return true;
		case ClassFileConstants.AccProtected:
			return (visibility != ClassFileConstants.AccPublic);
		case ClassFileConstants.AccDefault:
			return (visibility == ClassFileConstants.AccDefault || visibility == ClassFileConstants.AccPrivate);
		case ClassFileConstants.AccPrivate:
			return (visibility == ClassFileConstants.AccPrivate);
	}
	return true;
}
private String javadocVisibilityArgument(int visibility, int modifiers) {
	String argument = null;
	switch (modifiers & ExtraCompilerModifiers.AccVisibilityMASK) {
		case ClassFileConstants.AccPublic :
			argument = CompilerOptions.PUBLIC;
			break;
		case ClassFileConstants.AccProtected:
			if (visibility != ClassFileConstants.AccPublic) {
				argument = CompilerOptions.PROTECTED;
			}
			break;
		case ClassFileConstants.AccDefault:
			if (visibility == ClassFileConstants.AccDefault || visibility == ClassFileConstants.AccPrivate) {
				argument = CompilerOptions.DEFAULT;
			}
			break;
		case ClassFileConstants.AccPrivate:
			if (visibility == ClassFileConstants.AccPrivate) {
				argument = CompilerOptions.PRIVATE;
			}
			break;
	}
	return argument;
}
public void localVariableHiding(LocalDeclaration local, Binding hiddenVariable, boolean  isSpecialArgHidingField) {
	if (hiddenVariable instanceof LocalVariableBinding) {
		int id = (local instanceof Argument)
				? IProblem.ArgumentHidingLocalVariable
				: IProblem.LocalVariableHidingLocalVariable;
		int severity = computeSeverity(id);
		if (severity == ProblemSeverities.Ignore) return;
		String[] arguments = new String[] {new String(local.name)  };
		this.handle(
			id,
			arguments,
			arguments,
			severity,
			nodeSourceStart(hiddenVariable, local),
			nodeSourceEnd(hiddenVariable, local));
	} else if (hiddenVariable instanceof FieldBinding) {
		if (isSpecialArgHidingField && !this.options.reportSpecialParameterHidingField){
			return;
		}
		int id = (local instanceof Argument)
				? IProblem.ArgumentHidingField
				: IProblem.LocalVariableHidingField;
		int severity = computeSeverity(id);
		if (severity == ProblemSeverities.Ignore) return;
		FieldBinding field = (FieldBinding) hiddenVariable;
		this.handle(
			id,
			new String[] {new String(local.name) , new String(field.declaringClass.readableName()) },
			new String[] {new String(local.name), new String(field.declaringClass.shortReadableName()) },
			severity,
			local.sourceStart,
			local.sourceEnd);
	}
}
public void localVariableNonNullComparedToNull(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.NonNullLocalVariableComparisonYieldsFalse);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)  };
	this.handle(
		IProblem.NonNullLocalVariableComparisonYieldsFalse,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void localVariableNullComparedToNonNull(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.NullLocalVariableComparisonYieldsFalse);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)  };
	this.handle(
		IProblem.NullLocalVariableComparisonYieldsFalse,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void localVariableNullInstanceof(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.NullLocalVariableInstanceofYieldsFalse);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)  };
	this.handle(
		IProblem.NullLocalVariableInstanceofYieldsFalse,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void localVariableNullReference(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.NullLocalVariableReference);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)  };
	this.handle(
		IProblem.NullLocalVariableReference,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void localVariablePotentialNullReference(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.PotentialNullLocalVariableReference);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)};
	this.handle(
		IProblem.PotentialNullLocalVariableReference,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void localVariableRedundantCheckOnNonNull(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.RedundantNullCheckOnNonNullLocalVariable);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)  };
	this.handle(
		IProblem.RedundantNullCheckOnNonNullLocalVariable,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void localVariableRedundantCheckOnNull(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.RedundantNullCheckOnNullLocalVariable);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)  };
	this.handle(
		IProblem.RedundantNullCheckOnNullLocalVariable,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void localVariableRedundantNullAssignment(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.RedundantLocalVariableNullAssignment);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(local.name)  };
	this.handle(
		IProblem.RedundantLocalVariableNullAssignment,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void methodNeedBody(AbstractMethodDeclaration methodDecl) {
	this.handle(
		IProblem.MethodRequiresBody,
		NoArgument,
		NoArgument,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void methodNeedingNoBody(MethodDeclaration methodDecl) {
	this.handle(
		((methodDecl.modifiers & ClassFileConstants.AccNative) != 0) ? IProblem.BodyForNativeMethod : IProblem.BodyForAbstractMethod,
		NoArgument,
		NoArgument,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}

public void methodWithConstructorName(MethodDeclaration methodDecl) {
	this.handle(
		IProblem.MethodButWithConstructorName,
		NoArgument,
		NoArgument,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void missingReturnType(AbstractMethodDeclaration methodDecl) {
	this.handle(
		IProblem.MissingReturnType,
		NoArgument,
		NoArgument,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void missingSemiColon(Expression expression, int start, int end){
	this.handle(
		IProblem.MissingSemiColon,
		NoArgument,
		NoArgument,
		start,
		end);
}
public void mustDefineDimensionsOrInitializer(ArrayAllocationExpression expression) {
	this.handle(
		IProblem.MustDefineEitherDimensionExpressionsOrInitializer,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}
public void mustSpecifyPackage(CompilationUnitDeclaration compUnitDecl) {
	String[] arguments = new String[] {new String(compUnitDecl.getFileName())};
	this.handle(
		IProblem.MustSpecifyPackage,
		arguments,
		arguments,
		compUnitDecl.sourceStart,
		compUnitDecl.sourceStart + 1);
}
public void mustUseAStaticMethod(ASTNode messageSend, MethodBinding method) {
	this.handle(
		IProblem.StaticMethodRequested,
		new String[] {new String(method.declaringClass.readableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, false)},
		new String[] {new String(method.declaringClass.shortReadableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, true)},
		messageSend.sourceStart,
		messageSend.sourceEnd);
}
public void nativeMethodsCannotBeStrictfp(ReferenceBinding type, AbstractMethodDeclaration methodDecl) {
	String[] arguments = new String[] {new String(type.sourceName()), new String(methodDecl.getSafeName())};
	this.handle(
		IProblem.NativeMethodsCannotBeStrictfp,
		arguments,
		arguments,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void needImplementation() {
	this.abortDueToInternalError(Messages.abort_missingCode);
}

public void needToEmulateFieldAccess(FieldBinding field, ASTNode location, boolean isReadAccess) {
	int id = isReadAccess
			? IProblem.NeedToEmulateFieldReadAccess
			: IProblem.NeedToEmulateFieldWriteAccess;
	int severity = computeSeverity(id);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		id,
		new String[] {new String(field.declaringClass.readableName()), new String(field.name)},
		new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name)},
		severity,
		nodeSourceStart(field, location),
		nodeSourceEnd(field, location));
}
public void needToEmulateMethodAccess(
	MethodBinding method,
	ASTNode location) {

	if (method.isConstructor()) {
		int severity = computeSeverity(IProblem.NeedToEmulateConstructorAccess);
		if (severity == ProblemSeverities.Ignore) return;
		this.handle(
			IProblem.NeedToEmulateConstructorAccess,
			new String[] {
				new String(method.declaringClass.readableName()),
				typesAsString(method.isVarargs(), method.parameters, false)
			 },
			new String[] {
				new String(method.declaringClass.shortReadableName()),
				typesAsString(method.isVarargs(), method.parameters, true)
			 },
			severity,
			location.sourceStart,
			location.sourceEnd);
		return;
	}
	int severity = computeSeverity(IProblem.NeedToEmulateMethodAccess);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		IProblem.NeedToEmulateMethodAccess,
		new String[] {
			new String(method.declaringClass.readableName()),
			new String(method.selector),
			typesAsString(method.isVarargs(), method.parameters, false)
		 },
		new String[] {
			new String(method.declaringClass.shortReadableName()),
			new String(method.selector),
			typesAsString(method.isVarargs(), method.parameters, true)
		 },
		 severity,
		location.sourceStart,
		location.sourceEnd);
}
private int nodeSourceEnd(Binding field, ASTNode node) {
	return nodeSourceEnd(field, node, 0);
}
private int nodeSourceEnd(Binding field, ASTNode node, int index) {
	if (node instanceof ArrayTypeReference) {
		return ((ArrayTypeReference) node).originalSourceEnd;
	} else if (node instanceof QualifiedNameReference) {
		QualifiedNameReference ref = (QualifiedNameReference) node;
		if (ref.binding == field) {
			return (int) (ref.sourcePositions[ref.indexOfFirstFieldBinding-1]);
		}
		FieldBinding[] otherFields = ref.otherBindings;
		if (otherFields != null) {
			int offset = ref.indexOfFirstFieldBinding;
			for (int i = 0, length = otherFields.length; i < length; i++) {
				if (otherFields[i] == field)
					return (int) (ref.sourcePositions[i + offset]);
			}
		}
	} else if (node instanceof ArrayQualifiedTypeReference) {
		ArrayQualifiedTypeReference arrayQualifiedTypeReference = (ArrayQualifiedTypeReference) node;
		int length = arrayQualifiedTypeReference.sourcePositions.length;
		return (int) arrayQualifiedTypeReference.sourcePositions[length - 1];
	}
	return node.sourceEnd;
}
private int nodeSourceStart(Binding field, ASTNode node) {
	if (node instanceof FieldReference) {
		FieldReference fieldReference = (FieldReference) node;
		return (int) (fieldReference.nameSourcePosition >> 32);
	} else 	if (node instanceof QualifiedNameReference) {
		QualifiedNameReference ref = (QualifiedNameReference) node;
		if (ref.binding == field) {
			return (int) (ref.sourcePositions[ref.indexOfFirstFieldBinding-1] >> 32);
		}
		FieldBinding[] otherFields = ref.otherBindings;
		if (otherFields != null) {
			int offset = ref.indexOfFirstFieldBinding;
			for (int i = 0, length = otherFields.length; i < length; i++) {
				if (otherFields[i] == field)
					return (int) (ref.sourcePositions[i + offset] >> 32);
			}
		}
	}
	return node.sourceStart;
}
public void noMoreAvailableSpaceForArgument(LocalVariableBinding local, ASTNode location) {
	String[] arguments = new String[]{ new String(local.name) };
	this.handle(
		IProblem.TooManyArgumentSlots,
		arguments,
		arguments,
		ProblemSeverities.Abort | ProblemSeverities.Error | ProblemSeverities.Fatal,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void noMoreAvailableSpaceForConstant(TypeDeclaration typeDeclaration) {
	this.handle(
		IProblem.TooManyBytesForStringConstant,
		new String[]{ new String(typeDeclaration.binding.readableName())},
		new String[]{ new String(typeDeclaration.binding.shortReadableName())},
		ProblemSeverities.Abort | ProblemSeverities.Error | ProblemSeverities.Fatal,
		typeDeclaration.sourceStart,
		typeDeclaration.sourceEnd);
}

public void noMoreAvailableSpaceForLocal(LocalVariableBinding local, ASTNode location) {
	String[] arguments = new String[]{ new String(local.name) };
	this.handle(
		IProblem.TooManyLocalVariableSlots,
		arguments,
		arguments,
		ProblemSeverities.Abort | ProblemSeverities.Error | ProblemSeverities.Fatal,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location));
}
public void noMoreAvailableSpaceInConstantPool(TypeDeclaration typeDeclaration) {
	this.handle(
		IProblem.TooManyConstantsInConstantPool,
		new String[]{ new String(typeDeclaration.binding.readableName())},
		new String[]{ new String(typeDeclaration.binding.shortReadableName())},
		ProblemSeverities.Abort | ProblemSeverities.Error | ProblemSeverities.Fatal,
		typeDeclaration.sourceStart,
		typeDeclaration.sourceEnd);
}

public void nonExternalizedStringLiteral(ASTNode location) {
	this.handle(
		IProblem.NonExternalizedStringLiteral,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void nonStaticAccessToStaticField(ASTNode location, FieldBinding field) {
	int severity = computeSeverity(IProblem.NonStaticAccessToStaticField);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		IProblem.NonStaticAccessToStaticField,
		new String[] {new String(field.declaringClass.readableName()), new String(field.name)},
		new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name)},
		severity,
		nodeSourceStart(field, location),
		nodeSourceEnd(field, location));
}
public void nonStaticAccessToStaticMethod(ASTNode location, MethodBinding method) {
	this.handle(
		IProblem.NonStaticAccessToStaticMethod,
		new String[] {new String(method.declaringClass.readableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, false)},
		new String[] {new String(method.declaringClass.shortReadableName()), new String(method.selector), typesAsString(method.isVarargs(), method.parameters, true)},
		location.sourceStart,
		location.sourceEnd);
}


public void looseVariableDecleration(ASTNode location, Assignment assignment) {
	String[] arguments = new String[] {assignment.lhs.toString()};
	this.handle(
			IProblem.LooseVarDecl,
			 arguments,
			 arguments,
			assignment.sourceStart,
			assignment.sourceEnd);

}

public void optionalSemicolon(ASTNode location) {
	// Do something else
	System.out.println("Optional Semi"); //$NON-NLS-1$
}

public void nonStaticContextForEnumMemberType(SourceTypeBinding type) {
	String[] arguments = new String[] {new String(type.sourceName())};
	this.handle(
		IProblem.NonStaticContextForEnumMemberType,
		arguments,
		arguments,
		type.sourceStart(),
		type.sourceEnd());
}
public void noSuchEnclosingInstance(TypeBinding targetType, ASTNode location, boolean isConstructorCall) {

	int id;

	if (isConstructorCall) {
		//28 = No enclosing instance of type {0} is available due to some intermediate constructor invocation
		id = IProblem.EnclosingInstanceInConstructorCall;
	} else if ((location instanceof ExplicitConstructorCall)
				&& ((ExplicitConstructorCall) location).accessMode == ExplicitConstructorCall.ImplicitSuper) {
		//20 = No enclosing instance of type {0} is accessible to invoke the super constructor. Must define a constructor and explicitly qualify its super constructor invocation with an instance of {0} (e.g. x.super() where x is an instance of {0}).
		id = IProblem.MissingEnclosingInstanceForConstructorCall;
	} else if (location instanceof AllocationExpression
				&& (((AllocationExpression) location).binding.declaringClass.isMemberType()
					|| (((AllocationExpression) location).binding.declaringClass.isAnonymousType()
						&& ((AllocationExpression) location).binding.declaringClass.superclass().isMemberType()))) {
		//21 = No enclosing instance of type {0} is accessible. Must qualify the allocation with an enclosing instance of type {0} (e.g. x.new A() where x is an instance of {0}).
		id = IProblem.MissingEnclosingInstance;
	} else { // default
		//22 = No enclosing instance of the type {0} is accessible in scope
		id = IProblem.IncorrectEnclosingInstanceReference;
	}

	this.handle(
		id,
		new String[] { new String(targetType.readableName())},
		new String[] { new String(targetType.shortReadableName())},
		location.sourceStart,
		location.sourceEnd);
}
public void notCompatibleTypesError(EqualExpression expression, TypeBinding leftType, TypeBinding rightType) {
	String leftName = new String(leftType.readableName());
	String rightName = new String(rightType.readableName());
	String leftShortName = new String(leftType.shortReadableName());
	String rightShortName = new String(rightType.shortReadableName());
	if (leftShortName.equals(rightShortName)){
		leftShortName = leftName;
		rightShortName = rightName;
	}
	this.handle(
		IProblem.IncompatibleTypesInEqualityOperator,
		new String[] {leftName, rightName },
		new String[] {leftShortName, rightShortName },
		expression.sourceStart,
		expression.sourceEnd);
}
public void notCompatibleTypesError(InstanceOfExpression expression, TypeBinding leftType, TypeBinding rightType) {
	String leftName = new String(leftType.readableName());
	String rightName = new String(rightType.readableName());
	String leftShortName = new String(leftType.shortReadableName());
	String rightShortName = new String(rightType.shortReadableName());
	if (leftShortName.equals(rightShortName)){
		leftShortName = leftName;
		rightShortName = rightName;
	}
	this.handle(
		IProblem.IncompatibleTypesInConditionalOperator,
		new String[] {leftName, rightName },
		new String[] {leftShortName, rightShortName },
		expression.sourceStart,
		expression.sourceEnd);
}
public void notCompatibleTypesErrorInForeach(Expression expression, TypeBinding leftType, TypeBinding rightType) {
	String leftName = new String(leftType.readableName());
	String rightName = new String(rightType.readableName());
	String leftShortName = new String(leftType.shortReadableName());
	String rightShortName = new String(rightType.shortReadableName());
	if (leftShortName.equals(rightShortName)){
		leftShortName = leftName;
		rightShortName = rightName;
	}
	this.handle(
		IProblem.IncompatibleTypesInForeach,
		new String[] {leftName, rightName },
		new String[] {leftShortName, rightShortName },
		expression.sourceStart,
		expression.sourceEnd);
}
public void objectCannotHaveSuperTypes(SourceTypeBinding type) {
	this.handle(
		IProblem.ObjectCannotHaveSuperTypes,
		NoArgument,
		NoArgument,
		type.sourceStart(),
		type.sourceEnd());
}
public void objectMustBeClass(SourceTypeBinding type) {
	this.handle(
		IProblem.ObjectMustBeClass,
		NoArgument,
		NoArgument,
		type.sourceStart(),
		type.sourceEnd());
}
public void operatorOnlyValidOnNumericType(CompoundAssignment  assignment, TypeBinding leftType, TypeBinding rightType) {
	String leftName = new String(leftType.readableName());
	String rightName = new String(rightType.readableName());
	String leftShortName = new String(leftType.shortReadableName());
	String rightShortName = new String(rightType.shortReadableName());
	if (leftShortName.equals(rightShortName)){
		leftShortName = leftName;
		rightShortName = rightName;
	}
	this.handle(
		IProblem.TypeMismatch,
		new String[] {leftName, rightName },
		new String[] {leftShortName, rightShortName },
		assignment.sourceStart,
		assignment.sourceEnd);
}
public void overridesDeprecatedMethod(MethodBinding localMethod, MethodBinding inheritedMethod) {
	this.handle(
		IProblem.OverridingDeprecatedMethod,
		new String[] {
			new String(
					CharOperation.concat(
						localMethod.declaringClass.readableName(),
						localMethod.readableName(),
						'.')),
			new String(inheritedMethod.declaringClass.readableName())},
		new String[] {
			new String(
					CharOperation.concat(
						localMethod.declaringClass.shortReadableName(),
						localMethod.shortReadableName(),
						'.')),
			new String(inheritedMethod.declaringClass.shortReadableName())},
		localMethod.sourceStart(),
		localMethod.sourceEnd());
}
public void overridesMethodWithoutSuperInvocation(MethodBinding localMethod) {
	this.handle(
		IProblem.OverridingMethodWithoutSuperInvocation,
		new String[] {
			new String(
					CharOperation.concat(
						localMethod.declaringClass.readableName(),
						localMethod.readableName(),
						'.'))
			},
		new String[] {
			new String(
					CharOperation.concat(
						localMethod.declaringClass.shortReadableName(),
						localMethod.shortReadableName(),
						'.'))
			},
		localMethod.sourceStart(),
		localMethod.sourceEnd());
}
public void overridesPackageDefaultMethod(MethodBinding localMethod, MethodBinding inheritedMethod) {
	this.handle(
		IProblem.OverridingNonVisibleMethod,
		new String[] {
			new String(
					CharOperation.concat(
						localMethod.declaringClass.readableName(),
						localMethod.readableName(),
						'.')),
			new String(inheritedMethod.declaringClass.readableName())},
		new String[] {
			new String(
					CharOperation.concat(
						localMethod.declaringClass.shortReadableName(),
						localMethod.shortReadableName(),
						'.')),
			new String(inheritedMethod.declaringClass.shortReadableName())},
		localMethod.sourceStart(),
		localMethod.sourceEnd());
}
public void packageCollidesWithType(CompilationUnitDeclaration compUnitDecl) {
	String[] arguments = new String[] {CharOperation.toString(compUnitDecl.currentPackage.tokens)};
	this.handle(
		IProblem.PackageCollidesWithType,
		arguments,
		arguments,
		compUnitDecl.currentPackage.sourceStart,
		compUnitDecl.currentPackage.sourceEnd);
}
public void packageIsNotExpectedPackage(CompilationUnitDeclaration compUnitDecl) {
	String[] arguments = new String[] {
		CharOperation.toString(compUnitDecl.compilationResult.getPackageName()),
		compUnitDecl.currentPackage == null ? "" : CharOperation.toString(compUnitDecl.currentPackage.tokens), //$NON-NLS-1$
	};
	this.handle(
		IProblem.PackageIsNotExpectedPackage,
		arguments,
		arguments,
		compUnitDecl.currentPackage == null ? 0 : compUnitDecl.currentPackage.sourceStart,
		compUnitDecl.currentPackage == null ? 0 : compUnitDecl.currentPackage.sourceEnd);
}
public void parameterAssignment(LocalVariableBinding local, ASTNode location) {
	int severity = computeSeverity(IProblem.ParameterAssignment);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] { new String(local.readableName())};
	this.handle(
		IProblem.ParameterAssignment,
		arguments,
		arguments,
		severity,
		nodeSourceStart(local, location),
		nodeSourceEnd(local, location)); // should never be a qualified name reference
}
public void parseError(
	int startPosition,
	int endPosition,
	int currentToken,
	char[] currentTokenSource,
	String errorTokenName,
	String[] possibleTokens) {

	if (possibleTokens.length == 0) { //no suggestion available
		if (isKeyword(currentToken)) {
			String[] arguments = new String[] {new String(currentTokenSource)};
			this.handle(
				IProblem.ParsingErrorOnKeywordNoSuggestion,
				arguments,
				arguments,
				// this is the current -invalid- token position
				startPosition,
				endPosition);
			return;
		} else {
			String[] arguments = new String[] {errorTokenName};
			this.handle(
				IProblem.ParsingErrorNoSuggestion,
				arguments,
				arguments,
				// this is the current -invalid- token position
				startPosition,
				endPosition);
			return;
		}
	}

	//build a list of probable right tokens
	StringBuffer list = new StringBuffer(20);
	for (int i = 0, max = possibleTokens.length; i < max; i++) {
		if (i > 0)
			list.append(", "); //$NON-NLS-1$
		list.append('"');
		list.append(possibleTokens[i]);
		list.append('"');
	}

	if (isKeyword(currentToken)) {
		String[] arguments = new String[] {new String(currentTokenSource), list.toString()};
		this.handle(
			IProblem.ParsingErrorOnKeyword,
			arguments,
			arguments,
			// this is the current -invalid- token position
			startPosition,
			endPosition);
		return;
	}
	//extract the literal when it's a literal
	if (isLiteral(currentToken) ||
		isIdentifier(currentToken)) {
			errorTokenName = new String(currentTokenSource);
	}

	String[] arguments = new String[] {errorTokenName, list.toString()};
	this.handle(
		IProblem.ParsingError,
		arguments,
		arguments,
		// this is the current -invalid- token position
		startPosition,
		endPosition);
}
public void parseErrorDeleteToken(
	int start,
	int end,
	int currentKind,
	char[] errorTokenSource,
	String errorTokenName){
	this.syntaxError(
		IProblem.ParsingErrorDeleteToken,
		start,
		end,
		currentKind,
		errorTokenSource,
		errorTokenName,
		null);
}

public void parseErrorDeleteTokens(
	int start,
	int end){
	this.handle(
		IProblem.ParsingErrorDeleteTokens,
		NoArgument,
		NoArgument,
		start,
		end);
}
public void parseErrorInsertAfterToken(
	int start,
	int end,
	int currentKind,
	char[] errorTokenSource,
	String errorTokenName,
	String expectedToken){
	this.syntaxError(
		IProblem.ParsingErrorInsertTokenAfter,
		start,
		end,
		currentKind,
		errorTokenSource,
		errorTokenName,
		expectedToken);
}
public void parseErrorInsertBeforeToken(
	int start,
	int end,
	int currentKind,
	char[] errorTokenSource,
	String errorTokenName,
	String expectedToken){
	this.syntaxError(
		IProblem.ParsingErrorInsertTokenBefore,
		start,
		end,
		currentKind,
		errorTokenSource,
		errorTokenName,
		expectedToken);
}
public void parseErrorInsertToComplete(
	int start,
	int end,
	String inserted,
	String completed){
	String[] arguments = new String[] {inserted, completed};
	if (";".equals(inserted))	// ignore missing semicolon error //$NON-NLS-1$
		return;
	this.handle(
		IProblem.ParsingErrorInsertToComplete,
		arguments,
		arguments,
		start,
		end);
}

public void parseErrorInsertToCompletePhrase(
	int start,
	int end,
	String inserted){
	String[] arguments = new String[] {inserted};
	this.handle(
		IProblem.ParsingErrorInsertToCompletePhrase,
		arguments,
		arguments,
		start,
		end);
}
public void parseErrorInsertToCompleteScope(
	int start,
	int end,
	String inserted){
	String[] arguments = new String[] {inserted};
	this.handle(
		IProblem.ParsingErrorInsertToCompleteScope,
		arguments,
		arguments,
		start,
		end);
}
public void parseErrorInvalidToken(
	int start,
	int end,
	int currentKind,
	char[] errorTokenSource,
	String errorTokenName,
	String expectedToken){
	this.syntaxError(
		IProblem.ParsingErrorInvalidToken,
		start,
		end,
		currentKind,
		errorTokenSource,
		errorTokenName,
		expectedToken);
}
public void parseErrorMergeTokens(
	int start,
	int end,
	String expectedToken){
	String[] arguments = new String[] {expectedToken};
	this.handle(
		IProblem.ParsingErrorMergeTokens,
		arguments,
		arguments,
		start,
		end);
}
public void parseErrorMisplacedConstruct(
	int start,
	int end){
	this.handle(
		IProblem.ParsingErrorMisplacedConstruct,
		NoArgument,
		NoArgument,
		start,
		end);
}
public void parseErrorNoSuggestion(
	int start,
	int end,
	int currentKind,
	char[] errorTokenSource,
	String errorTokenName){
	this.syntaxError(
		IProblem.ParsingErrorNoSuggestion,
		start,
		end,
		currentKind,
		errorTokenSource,
		errorTokenName,
		null);
}
public void parseErrorNoSuggestionForTokens(
	int start,
	int end){
	this.handle(
		IProblem.ParsingErrorNoSuggestionForTokens,
		NoArgument,
		NoArgument,
		start,
		end);
}
public void parseErrorReplaceToken(
	int start,
	int end,
	int currentKind,
	char[] errorTokenSource,
	String errorTokenName,
	String expectedToken){
	this.syntaxError(
		IProblem.ParsingError,
		start,
		end,
		currentKind,
		errorTokenSource,
		errorTokenName,
		expectedToken);
}
public void parseErrorReplaceTokens(
	int start,
	int end,
	String expectedToken){
	String[] arguments = new String[] {expectedToken};
	this.handle(
		IProblem.ParsingErrorReplaceTokens,
		arguments,
		arguments,
		start,
		end);
}
public void parseErrorUnexpectedEnd(
	int start,
	int end){

	String[] arguments;
	if(this.referenceContext instanceof ConstructorDeclaration) {
		arguments = new String[] {Messages.parser_endOfConstructor};
	} else if(this.referenceContext instanceof MethodDeclaration) {
		arguments = new String[] {Messages.parser_endOfMethod};
	} else if(this.referenceContext instanceof TypeDeclaration) {
		arguments = new String[] {Messages.parser_endOfInitializer};
	} else {
		arguments = new String[] {Messages.parser_endOfFile};
	}
	this.handle(
		IProblem.ParsingErrorUnexpectedEOF,
		arguments,
		arguments,
		start,
		end);
}
public void possibleAccidentalBooleanAssignment(Assignment assignment) {
	this.handle(
		IProblem.PossibleAccidentalBooleanAssignment,
		NoArgument,
		NoArgument,
		assignment.sourceStart,
		assignment.sourceEnd);
}
public void possibleFallThroughCase(CaseStatement caseStatement) {
	// as long as we consider fake reachable as reachable, better keep 'possible' in the name
	this.handle(
		IProblem.FallthroughCase,
		NoArgument,
		NoArgument,
		caseStatement.sourceStart,
		caseStatement.sourceEnd);
}
public void recursiveConstructorInvocation(ExplicitConstructorCall constructorCall) {
	this.handle(
		IProblem.RecursiveConstructorInvocation,
		new String[] {
			new String(constructorCall.binding.declaringClass.readableName()),
			typesAsString(constructorCall.binding.isVarargs(), constructorCall.binding.parameters, false)
		},
		new String[] {
			new String(constructorCall.binding.declaringClass.shortReadableName()),
			typesAsString(constructorCall.binding.isVarargs(), constructorCall.binding.parameters, true)
		},
		constructorCall.sourceStart,
		constructorCall.sourceEnd);
}
public void redefineArgument(Argument arg) {
	String[] arguments = new String[] {new String(arg.name)};
	this.handle(
		IProblem.RedefinedArgument,
		arguments,
		arguments,
		arg.sourceStart,
		arg.sourceEnd);
}
public void redefineLocal(LocalDeclaration localDecl) {
	String[] arguments = new String[] {new String(localDecl.name)};
	this.handle(
		IProblem.RedefinedLocal,
		arguments,
		arguments,
		localDecl.sourceStart,
		localDecl.sourceEnd);
}

public void referenceMustBeArrayTypeAt(TypeBinding arrayType, ArrayReference arrayRef) {
	this.handle(
		IProblem.ArrayReferenceRequired,
		new String[] {new String(arrayType.readableName())},
		new String[] {new String(arrayType.shortReadableName())},
		arrayRef.sourceStart,
		arrayRef.sourceEnd);
}
public void reset() {
	this.positionScanner = null;
}
private int retrieveEndingPositionAfterOpeningParenthesis(int sourceStart, int sourceEnd, int numberOfParen) {
	if (this.referenceContext == null) return sourceEnd;
	CompilationResult compilationResult = this.referenceContext.compilationResult();
	if (compilationResult == null) return sourceEnd;
	ICompilationUnit compilationUnit = compilationResult.getCompilationUnit();
	if (compilationUnit == null) return sourceEnd;
	char[] contents = compilationUnit.getContents();
	if (contents.length == 0) return sourceEnd;
	if (this.positionScanner == null) {
		this.positionScanner = new Scanner(false, false, false, this.options.sourceLevel, this.options.complianceLevel, null, null, false);
	}
	this.positionScanner.setSource(contents);
	this.positionScanner.resetTo(sourceStart, sourceEnd);
	try {
		int token;
		int previousSourceEnd = sourceEnd;
		while ((token = this.positionScanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
			switch(token) {
				case TerminalTokens.TokenNameRPAREN:
					return previousSourceEnd;
				default :
					previousSourceEnd = this.positionScanner.currentPosition - 1;
			}
		}
	} catch(InvalidInputException e) {
		// ignore
	}
	return sourceEnd;
}
private int retrieveStartingPositionAfterOpeningParenthesis(int sourceStart, int sourceEnd, int numberOfParen) {
	if (this.referenceContext == null) return sourceStart;
	CompilationResult compilationResult = this.referenceContext.compilationResult();
	if (compilationResult == null) return sourceStart;
	ICompilationUnit compilationUnit = compilationResult.getCompilationUnit();
	if (compilationUnit == null) return sourceStart;
	char[] contents = compilationUnit.getContents();
	if (contents.length == 0) return sourceStart;
	if (this.positionScanner == null) {
		this.positionScanner = new Scanner(false, false, false, this.options.sourceLevel, this.options.complianceLevel, null, null, false);
	}
	this.positionScanner.setSource(contents);
	this.positionScanner.resetTo(sourceStart, sourceEnd);
	int count = 0;
	try {
		int token;
		while ((token = this.positionScanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
			switch(token) {
				case TerminalTokens.TokenNameLPAREN:
					count++;
					if (count == numberOfParen) {
						this.positionScanner.getNextToken();
						return this.positionScanner.startPosition;
					}
			}
		}
	} catch(InvalidInputException e) {
		// ignore
	}
	return sourceStart;
}
public void returnTypeCannotBeVoidArray(MethodDeclaration methodDecl) {
	this.handle(
		IProblem.CannotAllocateVoidArray,
		NoArgument,
		NoArgument,
		methodDecl.returnType.sourceStart,
		methodDecl.returnType.sourceEnd);
}
public void scannerError(Parser parser, String errorTokenName) {
	Scanner scanner = parser.scanner;

	int flag = IProblem.ParsingErrorNoSuggestion;
	int startPos = scanner.startPosition;
	int endPos = scanner.currentPosition - 1;

	//special treatment for recognized errors....
	if (errorTokenName.equals(Scanner.END_OF_SOURCE))
		flag = IProblem.EndOfSource;
	else if (errorTokenName.equals(Scanner.INVALID_HEXA))
		flag = IProblem.InvalidHexa;
	else if (errorTokenName.equals(Scanner.INVALID_OCTAL))
		flag = IProblem.InvalidOctal;
	else if (errorTokenName.equals(Scanner.INVALID_CHARACTER_CONSTANT))
		flag = IProblem.InvalidCharacterConstant;
	else if (errorTokenName.equals(Scanner.INVALID_ESCAPE))
		flag = IProblem.InvalidEscape;
	else if (errorTokenName.equals(Scanner.INVALID_UNICODE_ESCAPE)){
		flag = IProblem.InvalidUnicodeEscape;
		// better locate the error message
		char[] source = scanner.source;
		int checkPos = scanner.currentPosition - 1;
		if (checkPos >= source.length) checkPos = source.length - 1;
		while (checkPos >= startPos){
			if (source[checkPos] == '\\') break;
			checkPos --;
		}
		startPos = checkPos;
	} else if (errorTokenName.equals(Scanner.INVALID_LOW_SURROGATE)) {
		flag = IProblem.InvalidLowSurrogate;
	} else if (errorTokenName.equals(Scanner.INVALID_HIGH_SURROGATE)) {
		flag = IProblem.InvalidHighSurrogate;
		// better locate the error message
		char[] source = scanner.source;
		int checkPos = scanner.startPosition + 1;
		while (checkPos <= endPos){
			if (source[checkPos] == '\\') break;
			checkPos ++;
		}
		endPos = checkPos - 1;
	} else if (errorTokenName.equals(Scanner.INVALID_FLOAT))
		flag = IProblem.InvalidFloat;
	else if (errorTokenName.equals(Scanner.UNTERMINATED_STRING))
		flag = IProblem.UnterminatedString;
	else if (errorTokenName.equals(Scanner.UNTERMINATED_COMMENT))
		flag = IProblem.UnterminatedComment;
	else if (errorTokenName.equals(Scanner.INVALID_CHAR_IN_STRING))
		flag = IProblem.UnterminatedString;
	else if (errorTokenName.equals(Scanner.INVALID_DIGIT))
		flag = IProblem.InvalidDigit;

	String[] arguments = flag == IProblem.ParsingErrorNoSuggestion
			? new String[] {errorTokenName}
			: NoArgument;
	this.handle(
		flag,
		arguments,
		arguments,
		// this is the current -invalid- token position
		startPos,
		endPos,
		parser.compilationUnit.compilationResult);
}
public void shouldReturn(TypeBinding returnType, ASTNode location) {
	this.handle(
		IProblem.ShouldReturnValue,
		new String[] { new String (returnType.readableName())},
		new String[] { new String (returnType.shortReadableName())},
		location.sourceStart,
		location.sourceEnd);
}

public void signalNoImplicitStringConversionForCharArrayExpression(Expression expression) {
	this.handle(
		IProblem.NoImplicitStringConversionForCharArrayExpression,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}
public void staticAndInstanceConflict(MethodBinding currentMethod, MethodBinding inheritedMethod) {
	if (currentMethod.isStatic())
		this.handle(
			// This static method cannot hide the instance method from %1
			// 8.4.6.4 - If a class inherits more than one method with the same signature a static (non-abstract) method cannot hide an instance method.
			IProblem.CannotHideAnInstanceMethodWithAStaticMethod,
			new String[] {new String(inheritedMethod.declaringClass.readableName())},
			new String[] {new String(inheritedMethod.declaringClass.shortReadableName())},
			currentMethod.sourceStart(),
			currentMethod.sourceEnd());
	else
		this.handle(
			// This instance method cannot override the static method from %1
			// 8.4.6.4 - If a class inherits more than one method with the same signature an instance (non-abstract) method cannot override a static method.
			IProblem.CannotOverrideAStaticMethodWithAnInstanceMethod,
			new String[] {new String(inheritedMethod.declaringClass.readableName())},
			new String[] {new String(inheritedMethod.declaringClass.shortReadableName())},
			currentMethod.sourceStart(),
			currentMethod.sourceEnd());
}
public void staticFieldAccessToNonStaticVariable(ASTNode location, FieldBinding field) {
	String[] arguments = new String[] {new String(field.readableName())};
	this.handle(
		IProblem.NonStaticFieldFromStaticInvocation,
		arguments,
		arguments,
		nodeSourceStart(field,location),
		nodeSourceEnd(field, location));
}
public void staticInheritedMethodConflicts(SourceTypeBinding type, MethodBinding concreteMethod, MethodBinding[] abstractMethods) {
	this.handle(
		// The static method %1 conflicts with the abstract method in %2
		// 8.4.6.4 - If a class inherits more than one method with the same signature it is an error for one to be static (non-abstract) and the other abstract.
		IProblem.StaticInheritedMethodConflicts,
		new String[] {
			new String(concreteMethod.readableName()),
			new String(abstractMethods[0].declaringClass.readableName())},
		new String[] {
			new String(concreteMethod.readableName()),
			new String(abstractMethods[0].declaringClass.shortReadableName())},
		type.sourceStart(),
		type.sourceEnd());
}
public void stringConstantIsExceedingUtf8Limit(ASTNode location) {
	this.handle(
		IProblem.StringConstantIsExceedingUtf8Limit,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void superclassMustBeAClass(SourceTypeBinding type, TypeReference superclassRef, ReferenceBinding superType) {
	this.handle(
		IProblem.SuperclassMustBeAClass,
		new String[] {new String(superType.readableName()), new String(type.sourceName())},
		new String[] {new String(superType.shortReadableName()), new String(type.sourceName())},
		superclassRef.sourceStart,
		superclassRef.sourceEnd);
}
public void superclassMustBeAClass(SourceTypeBinding type, InferredType superclassRef, ReferenceBinding superType) {
	this.handle(
		IProblem.SuperclassMustBeAClass,
		new String[] {new String(superType.readableName()), new String(type.sourceName())},
		new String[] {new String(superType.shortReadableName()), new String(type.sourceName())},
		superclassRef.sourceStart,
		superclassRef.sourceEnd);
}
public void superfluousSemicolon(int sourceStart, int sourceEnd) {
	this.handle(
		IProblem.SuperfluousSemicolon,
		NoArgument,
		NoArgument,
		sourceStart,
		sourceEnd);
}
public void superinterfaceMustBeAnInterface(SourceTypeBinding type, TypeReference superInterfaceRef, ReferenceBinding superType) {
	this.handle(
		IProblem.SuperInterfaceMustBeAnInterface,
		new String[] {new String(superType.readableName()), new String(type.sourceName())},
		new String[] {new String(superType.shortReadableName()), new String(type.sourceName())},
		superInterfaceRef.sourceStart,
		superInterfaceRef.sourceEnd);
}
private void syntaxError(
	int id,
	int startPosition,
	int endPosition,
	int currentKind,
	char[] currentTokenSource,
	String errorTokenName,
	String expectedToken) {

	String eTokenName;
	if (isKeyword(currentKind) ||
		isLiteral(currentKind) ||
		isIdentifier(currentKind)) {
			eTokenName = new String(currentTokenSource);
	} else {
		eTokenName = errorTokenName;
	}

	String[] arguments;
	if(expectedToken != null) {
		arguments = new String[] {eTokenName, expectedToken};
	} else {
		arguments = new String[] {eTokenName};
	}
	this.handle(
		id,
		arguments,
		arguments,
		startPosition,
		endPosition);
}
public void task(String tag, String message, String priority, int start, int end){
	this.handle(
		IProblem.Task,
		new String[] { tag, message, priority/*secret argument that is not surfaced in getMessage()*/},
		new String[] { tag, message, priority/*secret argument that is not surfaced in getMessage()*/},
		start,
		end);
}

public void tooManyDimensions(ASTNode expression) {
	this.handle(
		IProblem.TooManyArrayDimensions,
		NoArgument,
		NoArgument,
		expression.sourceStart,
		expression.sourceEnd);
}

public void tooManyFields(TypeDeclaration typeDeclaration) {
	this.handle(
		IProblem.TooManyFields,
		new String[]{ new String(typeDeclaration.binding.readableName())},
		new String[]{ new String(typeDeclaration.binding.shortReadableName())},
		ProblemSeverities.Abort | ProblemSeverities.Error | ProblemSeverities.Fatal,
		typeDeclaration.sourceStart,
		typeDeclaration.sourceEnd);
}
public void tooManyMethods(TypeDeclaration typeDeclaration) {
	this.handle(
		IProblem.TooManyMethods,
		new String[]{ new String(typeDeclaration.binding.readableName())},
		new String[]{ new String(typeDeclaration.binding.shortReadableName())},
		ProblemSeverities.Abort | ProblemSeverities.Error | ProblemSeverities.Fatal,
		typeDeclaration.sourceStart,
		typeDeclaration.sourceEnd);
}
public void typeCollidesWithEnclosingType(TypeDeclaration typeDecl) {
	String[] arguments = new String[] {new String(typeDecl.name)};
	this.handle(
		IProblem.HidingEnclosingType,
		arguments,
		arguments,
		typeDecl.sourceStart,
		typeDecl.sourceEnd);
}
public void typeCollidesWithPackage(CompilationUnitDeclaration compUnitDecl, TypeDeclaration typeDecl) {
	this.referenceContext = typeDecl; // report the problem against the type not the entire compilation unit
	String[] arguments = new String[] {new String(compUnitDecl.getFileName()), new String(typeDecl.name)};
	this.handle(
		IProblem.TypeCollidesWithPackage,
		arguments,
		arguments,
		typeDecl.sourceStart,
		typeDecl.sourceEnd,
		compUnitDecl.compilationResult);
}
public void typeHiding(TypeDeclaration typeDecl, TypeBinding hiddenType) {
	int severity = computeSeverity(IProblem.TypeHidingType);
	if (severity == ProblemSeverities.Ignore) return;
	this.handle(
		IProblem.TypeHidingType,
		new String[] { new String(typeDecl.name) , new String(hiddenType.shortReadableName()) },
		new String[] { new String(typeDecl.name) , new String(hiddenType.readableName()) },
		severity,
		typeDecl.sourceStart,
		typeDecl.sourceEnd);
}
public void typeMismatchError(TypeBinding actualType, TypeBinding expectedType, ASTNode location) {
	this.handle(
		IProblem.TypeMismatch,
		new String[] {new String(actualType.readableName()), new String(expectedType.readableName())},
		new String[] {new String(actualType.shortReadableName()), new String(expectedType.shortReadableName())},
		location.sourceStart,
		location.sourceEnd);
}
private String typesAsString(boolean isVarargs, TypeBinding[] types, boolean makeShort) {
	StringBuffer buffer = new StringBuffer(10);
	for (int i = 0, length = types.length; i < length; i++) {
		if (i != 0)
			buffer.append(", "); //$NON-NLS-1$
		TypeBinding type = types[i];
		boolean isVarargType = isVarargs && i == length-1;
		if (isVarargType) type = ((ArrayBinding)type).elementsType();
		buffer.append(new String(makeShort ? type.shortReadableName() : type.readableName()));
		if (isVarargType) buffer.append("..."); //$NON-NLS-1$
	}
	return buffer.toString();
}
public void undefinedLabel(BranchStatement statement) {
	if (isRecoveredName(statement.label)) return;
	String[] arguments = new String[] {new String(statement.label)};
	this.handle(
		IProblem.UndefinedLabel,
		arguments,
		arguments,
		statement.sourceStart,
		statement.sourceEnd);
}
public void undocumentedEmptyBlock(int blockStart, int blockEnd) {
	this.handle(
		IProblem.UndocumentedEmptyBlock,
		NoArgument,
		NoArgument,
		blockStart,
		blockEnd);
}
public void unexpectedStaticModifierForField(SourceTypeBinding type, FieldDeclaration fieldDecl) {
	String[] arguments = new String[] {new String(fieldDecl.name)};
	this.handle(
		IProblem.UnexpectedStaticModifierForField,
		arguments,
		arguments,
		fieldDecl.sourceStart,
		fieldDecl.sourceEnd);
}
public void unexpectedStaticModifierForMethod(ReferenceBinding type, AbstractMethodDeclaration methodDecl) {
	String[] arguments = new String[] {new String(type.sourceName()), new String(methodDecl.getSafeName())};
	this.handle(
		IProblem.UnexpectedStaticModifierForMethod,
		arguments,
		arguments,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void unhandledException(TypeBinding exceptionType, ASTNode location) {

	boolean insideDefaultConstructor =
		(this.referenceContext instanceof ConstructorDeclaration)
			&& ((ConstructorDeclaration)this.referenceContext).isDefaultConstructor();
	boolean insideImplicitConstructorCall =
		(location instanceof ExplicitConstructorCall)
			&& (((ExplicitConstructorCall) location).accessMode == ExplicitConstructorCall.ImplicitSuper);

	this.handle(
		insideDefaultConstructor
			? IProblem.UnhandledExceptionInDefaultConstructor
			: (insideImplicitConstructorCall
					? IProblem.UndefinedConstructorInImplicitConstructorCall
					: IProblem.UnhandledException),
		new String[] {new String(exceptionType.readableName())},
		new String[] {new String(exceptionType.shortReadableName())},
		location.sourceStart,
		location.sourceEnd);
}
public void uninitializedBlankFinalField(FieldBinding field, ASTNode location) {
	String[] arguments = new String[] {new String(field.readableName())};
	this.handle(
		IProblem.UninitializedBlankFinalField,
		arguments,
		arguments,
		nodeSourceStart(field, location),
		nodeSourceEnd(field, location));
}
public void uninitializedLocalVariable(LocalVariableBinding binding, ASTNode location) {
	String[] arguments = new String[] {new String(binding.readableName())};
	this.handle(
		IProblem.UninitializedLocalVariable,
		arguments,
		arguments,
		nodeSourceStart(binding, location),
		nodeSourceEnd(binding, location));
}
public void unmatchedBracket(int position, ReferenceContext context, CompilationResult compilationResult) {
	this.handle(
		IProblem.UnmatchedBracket,
		NoArgument,
		NoArgument,
		position,
		position,
		context,
		compilationResult);
}
public void unnecessaryElse(ASTNode location) {
	this.handle(
		IProblem.UnnecessaryElse,
		NoArgument,
		NoArgument,
		location.sourceStart,
		location.sourceEnd);
}
public void unnecessaryEnclosingInstanceSpecification(Expression expression, ReferenceBinding targetType) {
	this.handle(
		IProblem.IllegalEnclosingInstanceSpecification,
		new String[]{ new String(targetType.readableName())},
		new String[]{ new String(targetType.shortReadableName())},
		expression.sourceStart,
		expression.sourceEnd);
}
public void unnecessaryInstanceof(InstanceOfExpression instanceofExpression, TypeBinding checkType) {
	int severity = computeSeverity(IProblem.UnnecessaryInstanceof);
	if (severity == ProblemSeverities.Ignore) return;
	TypeBinding expressionType = instanceofExpression.expression.resolvedType;
	this.handle(
		IProblem.UnnecessaryInstanceof,
		new String[]{ new String(expressionType.readableName()), new String(checkType.readableName())},
		new String[]{ new String(expressionType.shortReadableName()), new String(checkType.shortReadableName())},
		severity,
		instanceofExpression.sourceStart,
		instanceofExpression.sourceEnd);
}
public void unnecessaryNLSTags(int sourceStart, int sourceEnd) {
	this.handle(
		IProblem.UnnecessaryNLSTag,
		NoArgument,
		NoArgument,
		sourceStart,
		sourceEnd);
}
public void unqualifiedFieldAccess(NameReference reference, FieldBinding field) {
	int sourceStart = reference.sourceStart;
	int sourceEnd = reference.sourceEnd;
	if (reference instanceof SingleNameReference) {
		int numberOfParens = (reference.bits & ASTNode.ParenthesizedMASK) >> ASTNode.ParenthesizedSHIFT;
		if (numberOfParens != 0) {
			sourceStart = retrieveStartingPositionAfterOpeningParenthesis(sourceStart, sourceEnd, numberOfParens);
			sourceEnd = retrieveEndingPositionAfterOpeningParenthesis(sourceStart, sourceEnd, numberOfParens);
		} else {
			sourceStart = nodeSourceStart(field, reference);
			sourceEnd = nodeSourceEnd(field, reference);
		}
	} else {
		sourceStart = nodeSourceStart(field, reference);
		sourceEnd = nodeSourceEnd(field, reference);
	}
	this.handle(
		IProblem.UnqualifiedFieldAccess,
		new String[] {new String(field.declaringClass.readableName()), new String(field.name)},
		new String[] {new String(field.declaringClass.shortReadableName()), new String(field.name)},
		sourceStart,
		sourceEnd);
}
public void unreachableCatchBlock(ReferenceBinding exceptionType, ASTNode location) {
	this.handle(
		IProblem.UnreachableCatch,
		new String[] {
			new String(exceptionType.readableName()),
		 },
		new String[] {
			new String(exceptionType.shortReadableName()),
		 },
		location.sourceStart,
		location.sourceEnd);
}
public void unreachableCode(Statement statement) {
	int sourceStart = statement.sourceStart;
	int sourceEnd = statement.sourceEnd;
	if (statement instanceof LocalDeclaration) {
		LocalDeclaration declaration = (LocalDeclaration) statement;
		sourceStart = declaration.declarationSourceStart;
		sourceEnd = declaration.declarationSourceEnd;
	} else if (statement instanceof Expression) {
		int statemendEnd = ((Expression) statement).statementEnd;
		if (statemendEnd != -1) sourceEnd = statemendEnd;
	}
	this.handle(
		IProblem.CodeCannotBeReached,
		NoArgument,
		NoArgument,
		sourceStart,
		sourceEnd);
}
public void unresolvableReference(NameReference nameRef, Binding binding) {
/* also need to check that the searchedType is the receiver type
	if (binding instanceof ProblemBinding) {
		ProblemBinding problem = (ProblemBinding) binding;
		if (problem.searchType != null && problem.searchType.isHierarchyInconsistent())
			severity = SecondaryError;
	}
*/
	String[] arguments = new String[] {new String(binding.readableName())};
	int end = nameRef.sourceEnd;
	if (nameRef instanceof QualifiedNameReference) {
		QualifiedNameReference ref = (QualifiedNameReference) nameRef;
		if (isRecoveredName(ref.tokens)) return;
		if (ref.indexOfFirstFieldBinding >= 1)
			end = (int) ref.sourcePositions[ref.indexOfFirstFieldBinding - 1];
	} else {
		SingleNameReference ref = (SingleNameReference) nameRef;
		if (isRecoveredName(ref.token)) return;
	}
	this.handle(
		IProblem.UndefinedName,
		arguments,
		arguments,
		nameRef.sourceStart,
		end);
}
public void unusedArgument(LocalDeclaration localDecl) {
	int severity = computeSeverity(IProblem.ArgumentIsNeverUsed);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(localDecl.name)};
	this.handle(
		IProblem.ArgumentIsNeverUsed,
		arguments,
		arguments,
		severity,
		localDecl.sourceStart,
		localDecl.sourceEnd);
}
public void unusedDeclaredThrownException(ReferenceBinding exceptionType, AbstractMethodDeclaration method, ASTNode location) {
	boolean isConstructor = method.isConstructor();
	int severity = computeSeverity(isConstructor ? IProblem.UnusedConstructorDeclaredThrownException : IProblem.UnusedMethodDeclaredThrownException);
	if (severity == ProblemSeverities.Ignore) return;
	if (isConstructor) {
		this.handle(
			IProblem.UnusedConstructorDeclaredThrownException,
			new String[] {
				new String(method.binding.declaringClass.readableName()),
				typesAsString(method.binding.isVarargs(), method.binding.parameters, false),
				new String(exceptionType.readableName()),
			 },
			new String[] {
				new String(method.binding.declaringClass.shortReadableName()),
				typesAsString(method.binding.isVarargs(), method.binding.parameters, true),
				new String(exceptionType.shortReadableName()),
			 },
			severity,
			location.sourceStart,
			location.sourceEnd);
	} else {
		this.handle(
			IProblem.UnusedMethodDeclaredThrownException,
			new String[] {
				new String(method.binding.declaringClass.readableName()),
				new String(method.getSafeName()),
				typesAsString(method.binding.isVarargs(), method.binding.parameters, false),
				new String(exceptionType.readableName()),
			 },
			new String[] {
				new String(method.binding.declaringClass.shortReadableName()),
				new String(method.getSafeName()),
				typesAsString(method.binding.isVarargs(), method.binding.parameters, true),
				new String(exceptionType.shortReadableName()),
			 },
			severity,
			location.sourceStart,
			location.sourceEnd);
	}
}
public void unusedLabel(LabeledStatement statement) {
	int severity = computeSeverity(IProblem.UnusedLabel);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(statement.label)};
	this.handle(
		IProblem.UnusedLabel,
		arguments,
		arguments,
		severity,
		statement.sourceStart,
		statement.labelEnd);
}
public void unusedLocalVariable(LocalDeclaration localDecl) {
	int severity = computeSeverity(IProblem.LocalVariableIsNeverUsed);
	if (severity == ProblemSeverities.Ignore) return;
	String[] arguments = new String[] {new String(localDecl.name)};
	this.handle(
		IProblem.LocalVariableIsNeverUsed,
		arguments,
		arguments,
		severity,
		localDecl.sourceStart,
		localDecl.sourceEnd);
}
public void unusedPrivateConstructor(ConstructorDeclaration constructorDecl) {

	int severity = computeSeverity(IProblem.UnusedPrivateConstructor);
	if (severity == ProblemSeverities.Ignore) return;

	MethodBinding constructor = constructorDecl.binding;
	this.handle(
			IProblem.UnusedPrivateConstructor,
		new String[] {
			new String(constructor.declaringClass.readableName()),
			typesAsString(constructor.isVarargs(), constructor.parameters, false)
		 },
		new String[] {
			new String(constructor.declaringClass.shortReadableName()),
			typesAsString(constructor.isVarargs(), constructor.parameters, true)
		 },
		severity,
		constructorDecl.sourceStart,
		constructorDecl.sourceEnd);
}
public void unusedPrivateField(FieldDeclaration fieldDecl) {

	int severity = computeSeverity(IProblem.UnusedPrivateField);
	if (severity == ProblemSeverities.Ignore) return;

	FieldBinding field = fieldDecl.binding;

	this.handle(
			IProblem.UnusedPrivateField,
		new String[] {
			new String(field.declaringClass.readableName()),
			new String(field.name),
		 },
		new String[] {
			new String(field.declaringClass.shortReadableName()),
			new String(field.name),
		 },
		severity,
		nodeSourceStart(field, fieldDecl),
		nodeSourceEnd(field, fieldDecl));
}
public void unusedPrivateMethod(AbstractMethodDeclaration methodDecl) {

	int severity = computeSeverity(IProblem.UnusedPrivateMethod);
	if (severity == ProblemSeverities.Ignore) return;

	MethodBinding method = methodDecl.binding;
	char[] methodSelector = method.selector;
	if(methodSelector == null)
		methodSelector = methodDecl.getSafeName();

	// no report for serialization support 'Object readResolve()'
	if (!method.isStatic()
			&& TypeIds.T_JavaLangObject == method.returnType.id
			&& method.parameters.length == 0
			&& CharOperation.equals(methodSelector, TypeConstants.READRESOLVE)) {
		return;
	}
	// no report for serialization support 'Object writeReplace()'
	if (!method.isStatic()
			&& TypeIds.T_JavaLangObject == method.returnType.id
			&& method.parameters.length == 0
			&& CharOperation.equals(methodSelector, TypeConstants.WRITEREPLACE)) {
		return;
	}
	this.handle(
			IProblem.UnusedPrivateMethod,
		new String[] {
			new String(method.declaringClass.readableName()),
			new String(methodSelector),
			typesAsString(method.isVarargs(), method.parameters, false)
		 },
		new String[] {
			new String(method.declaringClass.shortReadableName()),
			new String(methodSelector),
			typesAsString(method.isVarargs(), method.parameters, true)
		 },
		severity,
		methodDecl.sourceStart,
		methodDecl.sourceEnd);
}
public void unusedPrivateType(TypeDeclaration typeDecl) {
	int severity = computeSeverity(IProblem.UnusedPrivateType);
	if (severity == ProblemSeverities.Ignore) return;

	ReferenceBinding type = typeDecl.binding;
	this.handle(
			IProblem.UnusedPrivateType,
		new String[] {
			new String(type.readableName()),
		 },
		new String[] {
			new String(type.shortReadableName()),
		 },
		severity,
		typeDecl.sourceStart,
		typeDecl.sourceEnd);
}
public void useAssertAsAnIdentifier(char [] keyword, int sourceStart, int sourceEnd) {
	this.handle(
		IProblem.UseAssertAsAnIdentifier,
		new String[]{new String(keyword)},
		new String[]{new String(keyword)},
		sourceStart,
		sourceEnd);
}
public void useEnumAsAnIdentifier(int sourceStart, int sourceEnd) {
	this.handle(
		IProblem.UseEnumAsAnIdentifier,
		NoArgument,
		NoArgument,
		sourceStart,
		sourceEnd);
}
public void variableTypeCannotBeVoid(AbstractVariableDeclaration varDecl) {
	String[] arguments = new String[] {new String(varDecl.name)};
	this.handle(
		IProblem.VariableTypeCannotBeVoid,
		arguments,
		arguments,
		varDecl.sourceStart,
		varDecl.sourceEnd);
}
public void variableTypeCannotBeVoidArray(AbstractVariableDeclaration varDecl) {
	this.handle(
		IProblem.CannotAllocateVoidArray,
		NoArgument,
		NoArgument,
		varDecl.type.sourceStart,
		varDecl.type.sourceEnd);
}
public void visibilityConflict(MethodBinding currentMethod, MethodBinding inheritedMethod) {
	this.handle(
		//	Cannot reduce the visibility of the inherited method from %1
		// 8.4.6.3 - The access modifier of an hiding method must provide at least as much access as the hidden method.
		// 8.4.6.3 - The access modifier of an overiding method must provide at least as much access as the overriden method.
		IProblem.MethodReducesVisibility,
		new String[] {new String(inheritedMethod.declaringClass.readableName())},
		new String[] {new String(inheritedMethod.declaringClass.shortReadableName())},
		currentMethod.sourceStart(),
		currentMethod.sourceEnd());
}
}
