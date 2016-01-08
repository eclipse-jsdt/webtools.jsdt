// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@106352

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.console;

/**
 Stack entry for console errors and assertions.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface CallFrameValue {
  /**
   JavaScript function name.
   */
  String functionName();

  /**
   JavaScript script name or url.
   */
  String url();

  /**
   JavaScript script line number.
   */
  long lineNumber();

  /**
   JavaScript script column number.
   */
  long columnNumber();

}
