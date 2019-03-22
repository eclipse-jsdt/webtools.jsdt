// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip.protocol;

public interface BasicConstants {
  interface Property {
    String ID = "id";
    String METHOD = "method";
    String PARAMS = "params";
  }

  interface Domain {
    String DEBUGGER = "Debugger";
    String RUNTIME = "Runtime";
    String INSPECTOR = "Inspector";
    String PAGE = "Page";
    String CONSOLE = "Console";
    String DOM = "DOM";
    String NETWORK = "Network";
	String SECURITY = "Security";
  }
}
