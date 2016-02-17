package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ScriptWithId;

@JsonType
public interface ScriptCollectedBody extends JsonSubtype<EventNotificationBody> {
  ScriptWithId script();
}
