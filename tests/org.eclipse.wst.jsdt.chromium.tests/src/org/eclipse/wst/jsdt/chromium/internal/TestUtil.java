// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal;

import junit.framework.Assert;

import org.eclipse.wst.jsdt.chromium.Breakpoint;
import org.eclipse.wst.jsdt.chromium.Breakpoint.Target;

/**
 * A utility for performing some common test-related operations.
 */
public class TestUtil {

  public static void assertBreakpointsEqual(Breakpoint bpExpected, Breakpoint bpHit) {
    Assert.assertEquals(bpExpected.getId(), bpHit.getId());
    Assert.assertEquals(bpExpected.getCondition(), bpHit.getCondition());
    Assert.assertEquals(bpExpected.getTarget().accept(BREAKPOINT_TARGET_DUMPER),
        bpHit.getTarget().accept(BREAKPOINT_TARGET_DUMPER));
  }

  private static final Breakpoint.Target.Visitor<String> BREAKPOINT_TARGET_DUMPER =
      new Breakpoint.Target.Visitor<String>() {
    @Override public String visitScriptName(String scriptName) {
      return "name=" + scriptName;
    }
    @Override public String visitScriptId(Object scriptId) {
      return "id=" + scriptId;
    }
    @Override public String visitUnknown(Target target) {
      return "unknown " + target;
    }

  };

  private TestUtil() {
    // not instantiable
  }
}
