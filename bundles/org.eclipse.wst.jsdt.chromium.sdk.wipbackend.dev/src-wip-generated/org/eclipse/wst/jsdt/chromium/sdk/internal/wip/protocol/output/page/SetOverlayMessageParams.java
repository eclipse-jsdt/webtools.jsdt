// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Sets overlay message.
 */
public class SetOverlayMessageParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param messageOpt Overlay message to display when paused in debugger.
   */
  public SetOverlayMessageParams(String messageOpt) {
    if (messageOpt != null) {
      this.put("message", messageOpt);
    }
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".setOverlayMessage";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
