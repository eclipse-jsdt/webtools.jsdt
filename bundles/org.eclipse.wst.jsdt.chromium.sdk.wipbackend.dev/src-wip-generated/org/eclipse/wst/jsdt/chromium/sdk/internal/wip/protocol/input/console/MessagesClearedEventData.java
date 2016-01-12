// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console;

/**
 Issued when console is cleared. This happens either upon <code>clearMessages</code> command or after page navigation.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType(allowsOtherProperties=true)
public interface MessagesClearedEventData extends org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonObjectBased {
  public static final org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.MessagesClearedEventData> TYPE
      = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.MessagesClearedEventData>("Console.messagesCleared", org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.MessagesClearedEventData.class) {
    @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.MessagesClearedEventData parse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parseConsoleMessagesClearedEventData(obj);
    }
  };
}
