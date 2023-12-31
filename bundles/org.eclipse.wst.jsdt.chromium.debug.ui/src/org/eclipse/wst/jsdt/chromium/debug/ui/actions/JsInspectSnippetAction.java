// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.actions;

import org.eclipse.wst.jsdt.chromium.debug.core.model.EvaluateContext;
import org.eclipse.wst.jsdt.chromium.debug.ui.ChromiumDebugUIPlugin;
import org.eclipse.wst.jsdt.chromium.debug.ui.PluginUtil;
import org.eclipse.wst.jsdt.chromium.debug.ui.editors.JavascriptUtil;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext.ResultOrException;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.model.IExpression;
import org.eclipse.debug.ui.DebugPopup;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.InspectPopupDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Action for inspecting a JavaScript snippet.
 */
public class JsInspectSnippetAction implements IEditorActionDelegate {

  private static final String ACTION_DEFINITION_ID = "org.eclipse.wst.jsdt.chromium.debug.ui.commands.Inspect"; //$NON-NLS-1$

  private IEditorPart activeEditorPart;

  public void setActiveEditor(IAction action, IEditorPart targetEditor) {
    activeEditorPart = targetEditor;
  }

  public void selectionChanged(IAction action, ISelection selection) {
  }

  private static ISelection getTargetSelection(IWorkbenchPart targetPart) {
    if (targetPart != null) {
      ISelectionProvider provider = targetPart.getSite().getSelectionProvider();
      if (provider != null) {
        return provider.getSelection();
      }
    }
    return null;
  }

  public void run(IAction action) {
    IAdaptable context = DebugUITools.getDebugContext();
    if (context == null) { // debugger not active
      return;
    }
    EvaluateContext evaluateContext = (EvaluateContext) context.getAdapter(EvaluateContext.class);
    if (evaluateContext == null) {
      return;
    }
    IEditorPart editorPart = activeEditorPart;
    String currentSelectedText = retrieveSelection(editorPart);
    EvaluateCallbackImpl callback =
        new EvaluateCallbackImpl(evaluateContext, editorPart, currentSelectedText);
    evaluateContext.getJsEvaluateContext().evaluateAsync(currentSelectedText, null,
        callback, null);
  }

  private static String retrieveSelection(IWorkbenchPart targetPart) {
    ISelection targetSelection = getTargetSelection(targetPart);
    if (targetSelection instanceof ITextSelection) {
      ITextSelection ts = (ITextSelection) targetSelection;
      String text = ts.getText();
      if (textHasContent(text)) {
        return text;
      } else if (targetPart instanceof IEditorPart) {
        IEditorPart editor = (IEditorPart) targetPart;
        if (editor instanceof ITextEditor) {
          return extractSurroundingWord(ts, (ITextEditor) editor);
        }
      }
    }
    return null;
  }

  private static String extractSurroundingWord(ITextSelection targetSelection, ITextEditor editor) {
    return JavascriptUtil.extractSurroundingJsIdentifier(
        editor.getDocumentProvider().getDocument(editor.getEditorInput()),
        targetSelection.getOffset());
  }

  private static boolean textHasContent(String text) {
    return text != null && JavascriptUtil.ID_PATTERN.matcher(text).find();
  }

  private static class EvaluateCallbackImpl implements JsEvaluateContext.EvaluateCallback  {
    private final EvaluateContext evaluateContext;
    private final IEditorPart editorPart;
    private final String selectedText;

    EvaluateCallbackImpl(EvaluateContext evaluateContext, IEditorPart editorPart, String selectedText) {
      this.evaluateContext = evaluateContext;
      this.editorPart = editorPart;
      this.selectedText = selectedText;
    }

    @Override
    public void success(ResultOrException result) {
      if (ChromiumDebugUIPlugin.getDefault() == null) {
        return;
      }
      if (ChromiumDebugUIPlugin.getDisplay().isDisposed()) {
        return;
      }
      result.accept(new ResultOrException.Visitor<Void>() {
        @Override
        public Void visitResult(JsValue value) {
          displayResult(value, null);
          return null;
        }
        @Override
        public Void visitException(JsValue exception) {
          displayResult(null, exception.getValueString());
          return null;
        }
      });
    }

    public void failure(Exception cause) {
      displayResult(null, cause.getMessage());
    }

    private void displayResult(final JsValue value, String errorMessage) {
      final StyledText styledText = getStyledText(editorPart);
      if (styledText == null) {
        return; // TODO(apavlov): fix this when adding inspected variables
      } else {
        final IExpression expression = new JsInspectExpression(evaluateContext, selectedText,
            value, errorMessage);
        ChromiumDebugUIPlugin.getDisplay().asyncExec(new Runnable() {
          public void run() {
            showPopup(styledText, expression);
          }
        });
      }
    }

    private void showPopup(StyledText textWidget, IExpression expression) {
      final ITextEditor textEditor;
      final ISelection originalSelection;

      IWorkbenchPart part = editorPart;
      if (part instanceof ITextEditor) {
        textEditor = (ITextEditor) part;
        originalSelection = getTargetSelection(editorPart);
      } else {
        textEditor = null;
        originalSelection = null;
      }
      Shell shell = editorPart.getSite().getShell();
      DebugPopup displayPopup =
          new InspectPopupDialog(shell, getPopupAnchor(textWidget), ACTION_DEFINITION_ID,
              expression) {
            @Override
            public boolean close() {
              boolean returnValue = super.close();
              if (textEditor != null && originalSelection != null) {
                textEditor.getSelectionProvider().setSelection(originalSelection);
              }
              return returnValue;
            }
          };
      displayPopup.open();
    }

    private StyledText getStyledText(IWorkbenchPart part) {
      ITextViewer viewer = (ITextViewer) part.getAdapter(ITextViewer.class);
      StyledText textWidget = null;
      if (viewer == null) {
        Control control = (Control) part.getAdapter(Control.class);
        if (control instanceof StyledText) {
          textWidget = (StyledText) control;
        }
      } else {
        textWidget = viewer.getTextWidget();
      }
      return textWidget;
    }
  }

  private static Point getPopupAnchor(StyledText textWidget) {
    if (textWidget != null) {
      Point docRange = textWidget.getSelectionRange();
      int midOffset = docRange.x + (docRange.y / 2);
      Point point = textWidget.getLocationAtOffset(midOffset);
      point = textWidget.toDisplay(point);
      point.y += getFontHeight(textWidget);
      return point;
    }
    return null;
  }

  private static int getFontHeight(StyledText textWidget) {
    return PluginUtil.getFontMetrics(textWidget, textWidget.getFont()).getHeight();
  }
}
