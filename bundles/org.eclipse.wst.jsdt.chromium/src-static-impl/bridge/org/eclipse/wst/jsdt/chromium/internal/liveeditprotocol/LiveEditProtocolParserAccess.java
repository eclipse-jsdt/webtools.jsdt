// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.liveeditprotocol;

/**
 * An accessor to generated implementation of a liveedit protocol parser.
 */
public class LiveEditProtocolParserAccess {
  private static final GeneratedLiveEditProtocolParser PARSER =
      new GeneratedLiveEditProtocolParser();

  public static LiveEditProtocolParser get() {
    return PARSER;
  }
}