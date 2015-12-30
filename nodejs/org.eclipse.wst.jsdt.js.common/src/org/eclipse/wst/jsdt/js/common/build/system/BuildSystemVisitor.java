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
package org.eclipse.wst.jsdt.js.common.build.system;

import java.util.Set;

import org.eclipse.wst.jsdt.core.dom.ASTVisitor;;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public abstract class BuildSystemVisitor extends ASTVisitor {

	public abstract Set<String> getTasks();
}
