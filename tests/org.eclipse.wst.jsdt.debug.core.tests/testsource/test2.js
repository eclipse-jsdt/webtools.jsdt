/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

function f1() {
	var f1v1 = 1;
	
	var f1v2 = 2;
}

f2 = function() {
	var f2v1 = 1;
	
	var f2v2 = 2;
}

(function f3() {
	var f3v1 = 1;
	
	var f3v2 = 2;
})

(f4 = function() {
	var f4v1 = 1;
	
	var f4v2 = 2;
})