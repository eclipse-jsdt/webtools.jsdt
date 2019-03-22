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

package org.eclipse.wst.jsdt.js.grunt.internal;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;

/**
 * PropertyTester implementation that tests whether the currently selected
 * project is a Grunt project, i.e. it contains a resource named Gruntfile.js.
 *
 * @author Shane Bryzak
 */
public class GruntProjectPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		return WorkbenchResourceUtil.getNamedFileOrTaskSelection(GruntConstants.GRUNT_FILE_JS, GruntTask.class) != null;
	}
}