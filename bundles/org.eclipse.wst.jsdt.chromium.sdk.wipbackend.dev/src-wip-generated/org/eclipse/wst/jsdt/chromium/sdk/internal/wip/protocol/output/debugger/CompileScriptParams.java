// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@121014

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.debugger;

/**
Compiles expression.
 */
public class CompileScriptParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.CompileScriptData> {
  /**
   @param expression Expression to compile.
   @param sourceURL Source url to be set for the script.
   */
  public CompileScriptParams(String expression, String sourceURL) {
    this.put("expression", expression);
    this.put("sourceURL", sourceURL);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".compileScript";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.CompileScriptData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parseDebuggerCompileScriptData(data.getUnderlyingObject());
  }

}
