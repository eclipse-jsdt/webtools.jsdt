// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@96703

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Searches for given string in frame / resource tree structure.
 */
public class SearchInResourcesParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.SearchInResourcesData> {
  /**
   @param text String to search for.
   @param caseSensitiveOpt If true, search is case sensitive.
   @param isRegexOpt If true, treats string parameter as regex.
   */
  public SearchInResourcesParams(String text, Boolean caseSensitiveOpt, Boolean isRegexOpt) {
    this.put("text", text);
    if (caseSensitiveOpt != null) {
      this.put("caseSensitive", caseSensitiveOpt);
    }
    if (isRegexOpt != null) {
      this.put("isRegex", isRegexOpt);
    }
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".searchInResources";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.SearchInResourcesData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parsePageSearchInResourcesData(data.getUnderlyingObject());
  }

}
