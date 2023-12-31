/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler;

public class DefaultErrorHandlingPolicies {

/*
 * Accumulate all problems, then exit without proceeding.
 *
 * Typically, the #proceedWithProblems(Problem[]) should
 * show the problems.
 *
 */
public static IErrorHandlingPolicy exitAfterAllProblems() {
	return new IErrorHandlingPolicy() {
		public boolean stopOnFirstError() {
			return false;
		}
		public boolean proceedOnErrors(){
			return false;
		}
	};
}
/*
 * Exit without proceeding on the first problem wich appears
 * to be an error.
 *
 */
public static IErrorHandlingPolicy exitOnFirstError() {
	return new IErrorHandlingPolicy() {
		public boolean stopOnFirstError() {
			return true;
		}
		public boolean proceedOnErrors(){
			return false;
		}
	};
}
/*
 * Proceed on the first error met.
 *
 */
public static IErrorHandlingPolicy proceedOnFirstError() {
	return new IErrorHandlingPolicy() {
		public boolean stopOnFirstError() {
			return true;
		}
		public boolean proceedOnErrors(){
			return true;
		}
	};
}
/*
 * Accumulate all problems, then proceed with them.
 *
 */
public static IErrorHandlingPolicy proceedWithAllProblems() {
	return new IErrorHandlingPolicy() {
		public boolean stopOnFirstError() {
			return false;
		}
		public boolean proceedOnErrors(){
			return true;
		}
	};
}
}
