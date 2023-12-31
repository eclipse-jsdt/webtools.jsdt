// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.editors;

import org.eclipse.wst.jsdt.chromium.debug.core.model.EvaluateContext;
import org.eclipse.wst.jsdt.chromium.debug.core.util.JsValueStringifier;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext.ResultOrException;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;

/**
 * Supplies a hover for JavaScript expressions while on a breakpoint.
 */
public class JsDebugTextHover implements ITextHover {

  private static final JsValueStringifier STRINGIFIER = new JsValueStringifier();

  public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
    IDocument doc = textViewer.getDocument();
    String expression = JavascriptUtil.extractSurroundingJsIdentifier(doc, hoverRegion.getOffset());
    if (expression == null) {
      return null;
    }

    IAdaptable context = DebugUITools.getDebugContext();
    if (context == null) { // debugger not active
      return null;
    }

    EvaluateContext evaluateContext = (EvaluateContext) context.getAdapter(EvaluateContext.class);
    if (evaluateContext == null) {
      return null;
    }

    final JsValue[] result = new JsValue[1];
    evaluateContext.getJsEvaluateContext().evaluateSync(expression, null,
        new JsEvaluateContext.EvaluateCallback() {
          @Override
          public void success(ResultOrException valueOrException) {
            result[0] = valueOrException.accept(new ResultOrException.Visitor<JsValue>() {
                  @Override public JsValue visitResult(JsValue value) {
                    return value;
                  }
                  @Override public JsValue visitException(JsValue exception) {
                    return null;
                  }
                });
          }
          public void failure(Exception cause) {
          }
        });
    if (result[0] == null) {
      return null;
    }

    return STRINGIFIER.render(result[0]);
  }

  public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
    IDocument doc = textViewer.getDocument();
    return JavascriptUtil.getSurroundingIdentifierRegion(doc, offset, false);
  }

}
