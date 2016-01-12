// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger;

/**
 Detailed information on exception (or error) that was thrown during script compilation or execution.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface ExceptionDetailsValue {
  /**
   Exception text.
   */
  String text();

  /**
   URL of the message origin.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String url();

  /**
   Script ID of the message origin.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String scriptId();

  /**
   Line number in the resource that generated this message.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  Long line();

  /**
   Column number in the resource that generated this message.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  Long column();

  /**
   JavaScript stack trace for assertions and error messages.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.CallFrameValue>/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console.StackTraceTypedef*/ stackTrace();

}
