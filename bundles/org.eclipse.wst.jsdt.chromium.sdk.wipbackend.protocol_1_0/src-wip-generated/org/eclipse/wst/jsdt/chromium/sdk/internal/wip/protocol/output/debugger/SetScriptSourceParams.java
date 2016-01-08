// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.debugger;

/**
Edits JavaScript source live.
 */
public class SetScriptSourceParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.SetScriptSourceData> {
  /**
   @param scriptId Id of the script to edit.
   @param scriptSource New content of the script.
   @param previewOpt  If true the change will not actually be applied. Preview mode may be used to get result description without actually modifying the code.
   */
  public SetScriptSourceParams(String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.debugger.ScriptIdTypedef*/ scriptId, String scriptSource, Boolean previewOpt) {
    this.put("scriptId", scriptId);
    this.put("scriptSource", scriptSource);
    if (previewOpt != null) {
      this.put("preview", previewOpt);
    }
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".setScriptSource";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.SetScriptSourceData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parseDebuggerSetScriptSourceData(data.getUnderlyingObject());
  }

}
