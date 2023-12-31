// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core.sourcemap;

import org.eclipse.wst.jsdt.chromium.debug.core.model.VmResourceId;

/**
 * A map between "user" and "vm" source positions. "Vm" position applies
 * to sources that are actually used inside virtual machine. "User" position
 * applies to sources that user works with.
 * All line/column numbers are 0-based.
 */
public interface SourcePositionMap {
  SourcePosition translatePosition(VmResourceId id, int line, int column,
      TranslateDirection direction);

  enum TranslateDirection {
    VM_TO_USER,
    USER_TO_VM;

    public TranslateDirection opposite() {
      return this == VM_TO_USER ? USER_TO_VM : VM_TO_USER;
    }
  }

  /**
   * @return current instance of token
   */
  Token getCurrentToken();

  /**
   * A token that can be kept and used later to check, whether source map has been updated.
   * This helps correctly caching mapped positions.
   */
  interface Token {
    boolean isUpdated();
  }
}
