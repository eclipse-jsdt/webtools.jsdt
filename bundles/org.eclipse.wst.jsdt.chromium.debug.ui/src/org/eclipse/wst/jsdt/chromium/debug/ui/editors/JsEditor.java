// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.editors;

import org.eclipse.wst.jsdt.chromium.debug.ui.PluginUtil;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * A simplistic JavaScript editor which supports its own key binding scope.
 */
public class JsEditor extends TextEditor {

  /** The ID of this editor as defined in plugin.xml */
  public static final String EDITOR_ID =
      "org.eclipse.wst.jsdt.chromium.debug.ui.editors.JsEditor"; //$NON-NLS-1$

  /** The ID of the editor context menu */
  public static final String EDITOR_CONTEXT = EDITOR_ID + ".context"; //$NON-NLS-1$

  /** The ID of the editor ruler context menu */
  public static final String RULER_CONTEXT = EDITOR_ID + ".ruler"; //$NON-NLS-1$

  @Override
  protected void initializeEditor() {
    super.initializeEditor();
    setEditorContextMenuId(EDITOR_CONTEXT);
    setRulerContextMenuId(RULER_CONTEXT);
    setDocumentProvider(new JsDocumentProvider());
  }

  public JsEditor() {
    setSourceViewerConfiguration(new JsSourceViewerConfiguration());
    setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope", //$NON-NLS-1$
        "org.eclipse.wst.jsdt.chromium.debug.ui.editors.JsEditor.context" }); //$NON-NLS-1$
  }

  @Override
  protected void setPartName(String partName) {
    super.setPartName(PluginUtil.stripChromiumExtension(partName, true));
  }
}
