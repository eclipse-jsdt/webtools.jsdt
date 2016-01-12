// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.debugger;

/**
Continues execution until specific location is reached.
 */
public class ContinueToLocationParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param location Location to continue to.
   @param interstatementLocationOpt Allows breakpoints at the intemediate positions inside statements.
   */
  public ContinueToLocationParams(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.debugger.LocationParam location, Boolean interstatementLocationOpt) {
    this.put("location", location);
    if (interstatementLocationOpt != null) {
      this.put("interstatementLocation", interstatementLocationOpt);
    }
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".continueToLocation";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
