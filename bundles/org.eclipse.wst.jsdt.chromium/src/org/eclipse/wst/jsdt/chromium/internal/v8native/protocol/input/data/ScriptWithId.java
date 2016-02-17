package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;

@JsonType
public interface ScriptWithId {
  long id();
}
