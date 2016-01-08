// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@135591

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger;

/**
 Fired when the virtual machine resumed execution.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType(allowsOtherProperties=true)
public interface ResumedEventData extends org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonObjectBased {
  public static final org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.ResumedEventData> TYPE
      = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.ResumedEventData>("Debugger.resumed", org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.ResumedEventData.class) {
    @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.ResumedEventData parse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parseDebuggerResumedEventData(obj);
    }
  };
}
