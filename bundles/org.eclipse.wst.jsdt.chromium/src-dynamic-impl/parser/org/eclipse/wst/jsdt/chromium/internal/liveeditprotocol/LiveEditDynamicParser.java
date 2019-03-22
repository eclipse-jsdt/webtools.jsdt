// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.liveeditprotocol;

import java.util.Arrays;
import java.util.Collections;

import org.eclipse.wst.jsdt.chromium.internal.liveeditprotocol.LiveEditResult;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolModelParseException;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.DynamicParserImpl;

/**
 * A dynamic implementation of a v8 protocol parser.
 */
public class LiveEditDynamicParser {
  public static DynamicParserImpl<LiveEditProtocolParser> create() {
    try {
      return new DynamicParserImpl<LiveEditProtocolParser>(LiveEditProtocolParser.class,
          Arrays.asList(new Class<?>[] {
              LiveEditResult.class,
              LiveEditResult.OldTreeNode.class,
              LiveEditResult.NewTreeNode.class,
              LiveEditResult.Positions.class,
              LiveEditResult.TextualDiff.class,
              }),
          Collections.<DynamicParserImpl<?>>emptyList(), false);
    } catch (JsonProtocolModelParseException e) {
      throw new RuntimeException(e);
    }
  }
}
