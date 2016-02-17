// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.page;

/**
 Search result for resource.
 */
@org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType
public interface SearchResultValue {
  /**
   Resource URL.
   */
  String url();

  /**
   Resource frame id.
   */
  String/*See org.eclipse.wst.jsdt.chromium.internal.wip.protocol.common.network.FrameIdTypedef*/ frameId();

  /**
   Number of matches in the resource content.
   */
  Number matchesCount();

}
