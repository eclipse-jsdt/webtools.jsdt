// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network;

/**
 Fired when WebSocket is about to initiate handshake.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface WebSocketWillSendHandshakeRequestEventData {
  /**
   Request identifier.
   */
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.RequestIdTypedef*/ requestId();

  /**
   Timestamp.
   */
  Number/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.TimestampTypedef*/ timestamp();

  /**
   WebSocket request data.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.WebSocketRequestValue request();

  public static final org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.WebSocketWillSendHandshakeRequestEventData> TYPE
      = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.WebSocketWillSendHandshakeRequestEventData>("Network.webSocketWillSendHandshakeRequest", org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.WebSocketWillSendHandshakeRequestEventData.class) {
    @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.WebSocketWillSendHandshakeRequestEventData parse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parseNetworkWebSocketWillSendHandshakeRequestEventData(obj);
    }
  };
}
