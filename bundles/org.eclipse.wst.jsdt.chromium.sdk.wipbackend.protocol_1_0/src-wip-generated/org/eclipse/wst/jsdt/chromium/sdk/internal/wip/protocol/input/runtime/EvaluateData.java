// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@96703

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime;

/**
 Evaluates expression on global object.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface EvaluateData {
  /**
   Evaluation result.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime.RemoteObjectValue result();

  /**
   True if the result was thrown during the evaluation.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  Boolean wasThrown();

}
