package org.eclipse.wst.jsdt.chromium.sdk.internal.v8native.protocol.input.data;

import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType;

@JsonType
public interface ScriptWithId {
  long id();
}
