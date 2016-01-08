// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom;

/**
 Returns search results from given <code>fromIndex</code> to given <code>toIndex</code> from the sarch with the given identifier.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface GetSearchResultsData {
  /**
   Ids of the search result nodes.
   */
  java.util.List<Long/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.dom.NodeIdTypedef*/> nodeIds();

}
