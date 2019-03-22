// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.output;

import org.eclipse.wst.jsdt.chromium.internal.v8native.DebuggerCommand;

/**
 * Represents a "changelive" experimental V8 request message.
 */
public class ChangeLiveMessage extends ContextlessDebuggerMessage {
  public ChangeLiveMessage(long scriptId, String newSource, Boolean previewOnly) {
    super(DebuggerCommand.CHANGELIVE.value);
    putArgument("script_id", scriptId);
    putArgument("new_source", newSource);
    if (previewOnly != null) {
      putArgument("preview_only", previewOnly);
    }
  }
}
