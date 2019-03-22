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

package org.eclipse.wst.jsdt.js.gulp.internal;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;

public class GulpProjectPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		return WorkbenchResourceUtil.getNamedFileOrTaskSelection(GulpConstants.GULP_FILE_JS, GulpTask.class) != null;
	}

}
