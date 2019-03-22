// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal;

import org.eclipse.wst.jsdt.chromium.internal.browserfixture.FixtureChromeStub.FixtureParser;

/**
 * An accessor to generated implementation of a fixture parser.
 */
public class FixtureParserAccess {

  public static FixtureParser get() {
    return PARSER;
  }

  private static final GeneratedV8FixtureParser PARSER = new GeneratedV8FixtureParser();
}
