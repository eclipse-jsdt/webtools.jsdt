// Copyright (c) 2009, 2016 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/
//
// Contributors:
//     eb6dd07e13178c83c12323e16a84554108d40333 - Bug 496914 Nullpointer in JavaScript Development Tools Chromium/V8 Remote Debugger: BreakpointAdapterFactory

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.chromium.debug.core.util.ChromiumDebugPluginUtil;

/**
 * Factory of LineBreakpointAdapters for browser scripts.
 */
public class BreakpointAdapterFactory implements IAdapterFactory {
  
  @Override
  @SuppressWarnings("unchecked")
  public Object getAdapter(Object adaptableObject, Class adapterType) {
    if (adaptableObject instanceof ITextEditor) {
      ITextEditor editorPart = (ITextEditor) adaptableObject;
      IEditorInput editorInput = editorPart.getEditorInput();
      if (editorInput != null) {
        IResource resource = editorInput.getAdapter(IResource.class);
        if (resource != null) {
          String extension = resource.getFileExtension();
          if (extension != null && ChromiumDebugPluginUtil.SUPPORTED_EXTENSIONS.contains(extension)) {
            return new LineBreakpointAdapter.ForVirtualProject();
          }
        }
      }
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public Class[] getAdapterList() {
    return new Class[] { IToggleBreakpointsTarget.class };
  }
}
