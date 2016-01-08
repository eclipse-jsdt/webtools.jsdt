// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network;

/**
Loads a resource in the context of a frame on the inspected page without cross origin checks.
 */
public class LoadResourceForFrontendParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.LoadResourceForFrontendData> {
  /**
   @param frameId Frame to load the resource from.
   @param url URL of the resource to load.
   */
  public LoadResourceForFrontendParams(String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.FrameIdTypedef*/ frameId, String url) {
    this.put("frameId", frameId);
    this.put("url", url);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.NETWORK + ".loadResourceForFrontend";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.LoadResourceForFrontendData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parseNetworkLoadResourceForFrontendData(data.getUnderlyingObject());
  }

}
