// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.liveeditprotocol;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.ParserGeneratorBase;

/**
 * A main class that generates V8 protocol static parser implementation.
 */
public class LiveEditParserGenerator extends ParserGeneratorBase {
  public static void main(String[] args) {
    mainImpl(args, createConfiguration());
  }

  public static GenerateConfiguration createConfiguration() {
    return new GenerateConfiguration("org.eclipse.wst.jsdt.chromium.internal.liveeditprotocol",
        "GeneratedLiveEditProtocolParser", LiveEditDynamicParser.create());
  }
}
