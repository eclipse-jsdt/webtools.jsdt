// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal;

import org.eclipse.wst.jsdt.chromium.internal.liveeditprotocol.LiveEditParserGenerator;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.V8ParserGenerator;

/**
 * A main class that generates all protocol static parsers (except tests).
 */
public class AllProtocolParsersGenerator {
  public static void main(String[] args) {
    LiveEditParserGenerator.main(args);
    V8ParserGenerator.main(args);
  }
}
