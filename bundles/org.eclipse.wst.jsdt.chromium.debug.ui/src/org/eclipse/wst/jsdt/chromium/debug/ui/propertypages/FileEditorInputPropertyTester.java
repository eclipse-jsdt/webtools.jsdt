// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.propertypages;

import org.eclipse.wst.jsdt.chromium.debug.core.FilePropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;

/**
 * Implementation of additional properties for {@link IFileEditorInput} that holds JavaScript
 * sources.
 */
public class FileEditorInputPropertyTester extends FilePropertyTester {
  @Override
  protected IFile extractFile(Object receiver) {
    IFileEditorInput editorInput = (IFileEditorInput) receiver;
    return editorInput.getFile();
  }
}
