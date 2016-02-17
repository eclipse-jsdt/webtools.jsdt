// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger;

/**
 Sets JavaScript breakpoint at a given location.
 */
@org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType
public interface SetBreakpointData {
  /**
   Id of the created breakpoint for further reference.
   */
  String/*See org.eclipse.wst.jsdt.chromium.internal.wip.protocol.common.debugger.BreakpointIdTypedef*/ breakpointId();

  /**
   Location this breakpoint resolved into.
   */
  org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.LocationValue actualLocation();

}
