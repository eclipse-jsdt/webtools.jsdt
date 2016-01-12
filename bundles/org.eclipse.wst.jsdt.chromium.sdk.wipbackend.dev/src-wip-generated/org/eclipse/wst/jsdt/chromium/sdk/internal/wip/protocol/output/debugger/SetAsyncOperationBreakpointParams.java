// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.debugger;

/**
Sets breakpoint on AsyncOperation callback handler.
 */
public class SetAsyncOperationBreakpointParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param operationId ID of the async operation to set breakpoint for.
   */
  public SetAsyncOperationBreakpointParams(long operationId) {
    this.put("operationId", operationId);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".setAsyncOperationBreakpoint";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
