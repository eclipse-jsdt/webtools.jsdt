/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.wst.jsdt.integration.tests.internal.wizard.js;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * Wizard page for creating a JavaScript Source File.
 * @author Pavol Srna
 *
 */
public class NewJSFileWizardPage extends WizardPage{

	/**
	 * Sets a given file name.
	 * 
	 * @param name Name
	 */
	public void setFileName(String fileName){
		new LabeledText("File name:").setText(fileName);
	}
}
