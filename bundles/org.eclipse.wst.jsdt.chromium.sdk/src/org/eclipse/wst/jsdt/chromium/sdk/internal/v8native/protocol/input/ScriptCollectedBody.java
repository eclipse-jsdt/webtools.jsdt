package org.eclipse.wst.jsdt.chromium.sdk.internal.v8native.protocol.input;

import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType;
import org.eclipse.wst.jsdt.chromium.sdk.internal.v8native.protocol.input.data.ScriptWithId;

@JsonType
public interface ScriptCollectedBody extends JsonSubtype<EventNotificationBody> {
  ScriptWithId script();
}
