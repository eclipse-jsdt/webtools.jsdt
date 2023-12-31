// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal;

import org.eclipse.wst.jsdt.chromium.internal.tools.v8.V8Tests;
import org.eclipse.wst.jsdt.chromium.internal.v8native.DebugContextImplTest;
import org.eclipse.wst.jsdt.chromium.internal.v8native.DebugEventListenerTest;
import org.eclipse.wst.jsdt.chromium.internal.v8native.ScriptsTest;
import org.eclipse.wst.jsdt.chromium.internal.v8native.value.JsArrayImplTest;
import org.eclipse.wst.jsdt.chromium.internal.v8native.value.JsObjectImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class provides all tests for the ChromeDevTools SDK.
 */
@RunWith(Suite.class)
@SuiteClasses({
  DebugContextImplTest.class,
  DebugEventListenerTest.class,
  JsArrayImplTest.class,
  JsObjectImplTest.class,
  ScriptsTest.class,
  V8Tests.class})
public class AllTests {
}
