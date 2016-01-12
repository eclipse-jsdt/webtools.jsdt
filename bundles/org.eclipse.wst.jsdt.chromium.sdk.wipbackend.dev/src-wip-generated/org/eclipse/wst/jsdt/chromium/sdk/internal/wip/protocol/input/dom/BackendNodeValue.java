// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom;

/**
 Backend node with a friendly name.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface BackendNodeValue {
  /**
   <code>Node</code>'s nodeType.
   */
  long nodeType();

  /**
   <code>Node</code>'s nodeName.
   */
  String nodeName();

  long/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.dom.BackendNodeIdTypedef*/ backendNodeId();

}
