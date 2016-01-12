// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network;

/**
 WebSocket frame data.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface WebSocketFrameValue {
  /**
   WebSocket frame opcode.
   */
  Number opcode();

  /**
   WebSocke frame mask.
   */
  boolean mask();

  /**
   WebSocke frame payload data.
   */
  String payloadData();

}
