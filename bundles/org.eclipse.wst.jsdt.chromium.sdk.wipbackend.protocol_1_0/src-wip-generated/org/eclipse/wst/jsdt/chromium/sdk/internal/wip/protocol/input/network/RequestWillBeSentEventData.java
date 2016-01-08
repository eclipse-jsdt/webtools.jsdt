// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network;

/**
 Fired when page is about to send HTTP request.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface RequestWillBeSentEventData {
  /**
   Request identifier.
   */
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.RequestIdTypedef*/ requestId();

  /**
   Frame identifier.
   */
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.FrameIdTypedef*/ frameId();

  /**
   Loader identifier.
   */
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.LoaderIdTypedef*/ loaderId();

  /**
   URL of the document this request is loaded for.
   */
  String documentURL();

  /**
   Request data.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.RequestValue request();

  /**
   Timestamp.
   */
  Number/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.TimestampTypedef*/ timestamp();

  /**
   Request initiator.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.InitiatorValue initiator();

  /**
   JavaScript stack trace upon issuing this request.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.CallFrameValue>/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.StackTraceTypedef*/ stackTrace();

  /**
   Redirect response data.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.ResponseValue redirectResponse();

  public static final org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.RequestWillBeSentEventData> TYPE
      = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.RequestWillBeSentEventData>("Network.requestWillBeSent", org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.RequestWillBeSentEventData.class) {
    @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.RequestWillBeSentEventData parse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parseNetworkRequestWillBeSentEventData(obj);
    }
  };
}
