// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page;

/**
 Navigation history entry.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface NavigationEntryValue {
  /**
   Unique id of the navigation history entry.
   */
  long id();

  /**
   URL of the navigation history entry.
   */
  String url();

  /**
   Title of the navigation history entry.
   */
  String title();

}
