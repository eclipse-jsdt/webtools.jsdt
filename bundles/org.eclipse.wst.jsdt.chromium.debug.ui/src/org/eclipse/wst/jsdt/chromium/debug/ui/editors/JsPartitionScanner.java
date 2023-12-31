// Copyright (c) 2009-2016 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.editors;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 * JavaScript partition scanner.
 */
public class JsPartitionScanner extends RuleBasedPartitionScanner {

  static final String PARTITIONING = "ChromiumJavaScriptPartitioning"; //$NON-NLS-1$
  static final String MULTILINE_COMMENT= "__js_multiline_comment"; //$NON-NLS-1$
  static final String JSDOC = "__jsdoc"; //$NON-NLS-1$
  static final String TEMPLATE_LITERAL = "__js_template_literal"; 
  static final String[] PARTITION_TYPES = {
    MULTILINE_COMMENT,
    TEMPLATE_LITERAL,
    JSDOC
  };

  /**
   * Empty comments should be handled so as not to be confused with JSDoc.
   */
  private static class EmptyCommentDetector implements IWordDetector {

    public boolean isWordStart(char c) {
      return (c == '/');
    }

    public boolean isWordPart(char c) {
      return (c == '*' || c == '/');
    }
  }

  private static class EmptyCommentPredicateRule extends WordRule implements IPredicateRule {
    private final IToken successToken;

    public EmptyCommentPredicateRule(IToken successToken) {
      super(new EmptyCommentDetector());
      this.successToken = successToken;
      addWord("/**/", successToken); //$NON-NLS-1$
    }

    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
      return super.evaluate(scanner);
    }

    public IToken getSuccessToken() {
      return successToken;
    }
  }

  public JsPartitionScanner() {
    IToken jsDocToken= new Token(JSDOC);
    IToken multilineCommentToken= new Token(MULTILINE_COMMENT);
    IToken templateLiteralToken = new Token(TEMPLATE_LITERAL);

    setPredicateRules(new IPredicateRule[] {
        new EndOfLineRule("//", Token.UNDEFINED), //$NON-NLS-1$
        new SingleLineRule("\"", "\"", Token.UNDEFINED, '\\'), //$NON-NLS-2$ //$NON-NLS-1$
        new SingleLineRule("'", "'", Token.UNDEFINED, '\\'), //$NON-NLS-2$ //$NON-NLS-1$
        new EmptyCommentPredicateRule(multilineCommentToken),
        new MultiLineRule("/**", "*/", jsDocToken, (char) 0, true),  //$NON-NLS-1$ //$NON-NLS-2$
        new MultiLineRule("/*", "*/", multilineCommentToken, (char) 0, true), //$NON-NLS-1$ //$NON-NLS-2$
        new MultiLineRule("`", "`", templateLiteralToken, (char) 0, true) //$NON-NLS-1$ //$NON-NLS-2$
    });
  }
}
