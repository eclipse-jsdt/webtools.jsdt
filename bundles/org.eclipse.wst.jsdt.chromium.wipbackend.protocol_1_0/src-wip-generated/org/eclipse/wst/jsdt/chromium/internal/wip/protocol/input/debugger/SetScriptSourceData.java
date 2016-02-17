// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@89368

package org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger;

/**
 Edits JavaScript source live.
 */
@org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType
public interface SetScriptSourceData {
  /**
   New stack trace in case editing has happened while VM was stopped.
   */
  @org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOptionalField
  java.util.List<org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.CallFrameValue> callFrames();

  /**
   VM-specific description of the changes applied.
   */
  @org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOptionalField
  Result result();

  /**
   VM-specific description of the changes applied.
   */
  @org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType(allowsOtherProperties=true)
  public interface Result extends org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonObjectBased {
  }
}
