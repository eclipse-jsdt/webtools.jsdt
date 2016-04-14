/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.dom.binding;

import org.eclipse.wst.jsdt.core.dom.SimpleName;

/**
 * Variable Declaration
 * 
 * @since 2.0
 *
 */
public class VariableDeclaration extends DeclarationBase {

	public enum VariableKind {
		VAR, LET, CONST, CATCH, ARGUMENTS
	}

	private final VariableKind variableKind;

	VariableDeclaration(SimpleName node, Scope scope, int index, VariableKind variableKind) {
		super(node, scope, index, Kind.VARIABLE);
		this.variableKind = variableKind;
	}

	@Override
	public String toString() {
		return "VD " + getName(); //$NON-NLS-1$
	}

	public boolean isVar() {
		return variableKind == VariableKind.VAR;
	}

	public boolean isCatch() {
		return variableKind == VariableKind.CATCH;
	}

	public boolean isLet() {
		return variableKind == VariableKind.LET;
	}

	public boolean isConst() {
		return variableKind == VariableKind.CONST;
	}

	boolean isArguments() {
		return variableKind == VariableKind.ARGUMENTS;
	}

	public static VariableDeclaration createArgumentsDeclaration(Scope scope) {
		return new ArgumentsDeclaration(scope);
	}

	static class ArgumentsDeclaration extends VariableDeclaration {

		ArgumentsDeclaration(Scope scope) {
			super(null, scope, -1, VariableKind.ARGUMENTS);
		}

		@Override
		public String getName() {
			return "arguments"; //$NON-NLS-1$
		}
		
		@Override
		public int hashCode() {
			//We have to override it because node is null in this case 
			return scope.getRootNode().hashCode();
		}

	}
}
