// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@135591

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Indicates the visibility of compositing borders.
 */
public class GetCompositingBordersVisibleParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.GetCompositingBordersVisibleData> {
  public GetCompositingBordersVisibleParams() {
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".getCompositingBordersVisible";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.GetCompositingBordersVisibleData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parsePageGetCompositingBordersVisibleData(data.getUnderlyingObject());
  }

}
