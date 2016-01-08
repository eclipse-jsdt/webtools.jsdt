// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: Local file Inspector-1.0.json.r107603.manual_fix

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.debugger;

/**
Returns detailed informtation on given function.
 */
public class GetFunctionDetailsParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.GetFunctionDetailsData> {
  /**
   @param functionId Id of the function to get location for.
   */
  public GetFunctionDetailsParams(String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.runtime.RemoteObjectIdTypedef*/ functionId) {
    this.put("functionId", functionId);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".getFunctionDetails";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.GetFunctionDetailsData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parseDebuggerGetFunctionDetailsData(data.getUnderlyingObject());
  }

}
