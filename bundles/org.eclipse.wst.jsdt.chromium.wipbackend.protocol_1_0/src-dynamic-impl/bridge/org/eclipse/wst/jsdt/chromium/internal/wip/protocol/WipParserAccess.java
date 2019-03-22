// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip.protocol;

import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.WipProtocolParser;

/**
 * An accessor to dynamic implementation of a WebInspector protocol parser.
 * Should be replaceable with a similar class that provides access to generated
 * parser implementation.
 */
public class WipParserAccess {
  public static WipProtocolParser get() {
    return PARSER;
  }

  private static final WipProtocolParser PARSER = WipDynamicParser.create().getParserRoot();
}
