// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.output;

import org.eclipse.wst.jsdt.chromium.internal.v8native.DebuggerCommand;

/**
 * Represents a "source" V8 request message.
 */
public class SourceMessage extends ContextlessDebuggerMessage {

  /**
   * @param frame number. Nullable, default is the selected frame
   * @param fromLine nullable start line within the source. Default is line 0
   * @param toLine nullable end line within the source (this line is not included in the
   *        result). Default is the number of lines in the script
   */
  public SourceMessage(Integer frame, Integer fromLine, Integer toLine) {
    super(DebuggerCommand.SOURCE.value);
    putArgument("frame", frame);
    putArgument("fromLine", fromLine);
    putArgument("toLine", toLine);
  }
}
