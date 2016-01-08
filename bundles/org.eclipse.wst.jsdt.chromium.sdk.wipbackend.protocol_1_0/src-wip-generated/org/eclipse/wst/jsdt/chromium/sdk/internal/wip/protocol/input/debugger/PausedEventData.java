// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@96703

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger;

/**
 Fired when the virtual machine stopped on breakpoint or exception or any other stop criteria.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface PausedEventData {
  /**
   Call stack the virtual machine stopped on.
   */
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.CallFrameValue> callFrames();

  /**
   Pause reason.
   */
  Reason reason();

  /**
   Object containing break-specific auxiliary properties.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  Data data();

  public static final org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.PausedEventData> TYPE
      = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.PausedEventData>("Debugger.paused", org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.PausedEventData.class) {
    @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.PausedEventData parse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parseDebuggerPausedEventData(obj);
    }
  };
  /**
   Pause reason.
   */
  public enum Reason {
    XHR,
    DOM,
    EVENTLISTENER,
    EXCEPTION,
    OTHER,
  }
  /**
   Object containing break-specific auxiliary properties.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType(allowsOtherProperties=true)
  public interface Data extends org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonObjectBased {
  }
}
