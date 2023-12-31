// Copyright (c) 2009-2016 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * A JavaScript source viewer configuration.
 */
public class JsSourceViewerConfiguration extends TextSourceViewerConfiguration {

  private static class MultilineCommentScanner extends BufferedRuleBasedScanner {
    public MultilineCommentScanner(TextAttribute attr) {
      setDefaultReturnToken(new Token(attr));
    }
  }

  private static final String[] CONTENT_TYPES = new String[] {
      IDocument.DEFAULT_CONTENT_TYPE,
      JsPartitionScanner.JSDOC,
      JsPartitionScanner.MULTILINE_COMMENT,
      JsPartitionScanner.TEMPLATE_LITERAL
  };

  private final JsCodeScanner scanner = new JsCodeScanner();

  @Override
  public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
    return new JsDebugTextHover();
  }

  @Override
  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    PresentationReconciler pr = new PresentationReconciler();
    pr.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
    setDamagerRepairer(pr, new DefaultDamagerRepairer(scanner), IDocument.DEFAULT_CONTENT_TYPE);
    setDamagerRepairer(
        pr, new DefaultDamagerRepairer(new MultilineCommentScanner(scanner.getCommentAttribute())),
        JsPartitionScanner.MULTILINE_COMMENT);
    setDamagerRepairer(
        pr, new DefaultDamagerRepairer(new MultilineCommentScanner(scanner.getJsDocAttribute())),
        JsPartitionScanner.JSDOC);
    setDamagerRepairer(
    	pr, new DefaultDamagerRepairer(new MultilineCommentScanner(scanner.getCommentAttribute())),
    	JsPartitionScanner.TEMPLATE_LITERAL);
    return pr;
  }

  private void setDamagerRepairer(
      PresentationReconciler pr,
      DefaultDamagerRepairer damagerRepairer,
      String tokenType) {
    pr.setDamager(damagerRepairer, tokenType);
    pr.setRepairer(damagerRepairer, tokenType);
  }

  @Override
  public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
    return CONTENT_TYPES;
  }

}
