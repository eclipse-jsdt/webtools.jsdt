// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Accepts or dismisses a JavaScript initiated dialog (alert, confirm, prompt, or onbeforeunload).
 */
public class HandleJavaScriptDialogParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param accept Whether to accept or dismiss the dialog.
   @param promptTextOpt The text to enter into the dialog prompt before accepting. Used only if this is a prompt dialog.
   */
  public HandleJavaScriptDialogParams(boolean accept, String promptTextOpt) {
    this.put("accept", accept);
    if (promptTextOpt != null) {
      this.put("promptText", promptTextOpt);
    }
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".handleJavaScriptDialog";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
