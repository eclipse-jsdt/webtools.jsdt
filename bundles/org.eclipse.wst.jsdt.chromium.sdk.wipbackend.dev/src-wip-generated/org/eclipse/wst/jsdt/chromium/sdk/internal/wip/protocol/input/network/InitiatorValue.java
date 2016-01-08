// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network;

/**
 Information about the request initiator.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface InitiatorValue {
  /**
   Type of this initiator.
   */
  Type type();

  /**
   Initiator JavaScript stack trace, set for Script only.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.CallFrameValue>/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.StackTraceTypedef*/ stackTrace();

  /**
   Initiator URL, set for Parser type only.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String url();

  /**
   Initiator line number, set for Parser type only.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  Number lineNumber();

  /**
   Type of this initiator.
   */
  public enum Type {
    PARSER,
    SCRIPT,
    OTHER,
  }
}
