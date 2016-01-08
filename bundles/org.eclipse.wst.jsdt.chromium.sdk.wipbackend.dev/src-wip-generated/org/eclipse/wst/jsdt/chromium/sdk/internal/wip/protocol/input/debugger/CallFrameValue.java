// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger;

/**
 JavaScript call frame. Array of call frames form the call stack.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface CallFrameValue {
  /**
   Call frame identifier. This identifier is only valid while the virtual machine is paused.
   */
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.debugger.CallFrameIdTypedef*/ callFrameId();

  /**
   Name of the JavaScript function called on this call frame.
   */
  String functionName();

  /**
   Location in the source code.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.LocationValue location();

  /**
   Scope chain for this call frame.
   */
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.debugger.ScopeValue> scopeChain();

  /**
   <code>this</code> object for this call frame.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonField(jsonLiteralName="this")
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime.RemoteObjectValue getThis();

}
