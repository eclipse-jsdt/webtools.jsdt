// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@121014

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger;

/**
 Compiles expression.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface CompileScriptData {
  /**
   Id of the script.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.debugger.ScriptIdTypedef*/ scriptId();

  /**
   Syntax error message if compilation failed.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String syntaxErrorMessage();

}
