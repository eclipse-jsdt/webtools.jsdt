// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.actions;

import org.eclipse.wst.jsdt.chromium.debug.core.model.Variable;
import org.eclipse.wst.jsdt.chromium.JsVariable;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IExpressionManager;
import org.eclipse.debug.core.model.IWatchExpression;

/**
 * A simple utility action to allow user see JavaScript property descriptors.
 * Eclipse can't seem to have a proper UI to show both property value (main usecase) and
 * property descriptor (rare usecase) in Variables/Expression views.
 * <p>
 * This action is enabled on object properties and builds JavaScript watch expression
 * "Object.getOwnPropertyDescriptor(&lt;object&gt;, &lt;propertyName&gt;) that is
 * hard to build manually every time.
 * <p>
 * User will have to manually remove old expressions.
 */
public abstract class WatchPropertyDescriptor extends VariableBasedAction {
  public static class ForVariable extends WatchPropertyDescriptor {
    public ForVariable() {
      super(VARIABLE_VIEW_ELEMENT_HANDLER);
    }
  }
  public static class ForExpression extends WatchPropertyDescriptor {
    public ForExpression() {
      super(EXPRESSION_VIEW_ELEMENT_HANDLER);
    }
  }

  protected WatchPropertyDescriptor(ElementHandler elementHandler) {
    super(elementHandler);
  }

  @Override
  protected Runnable createRunnable(VariableWrapper wrapper) {
    if (wrapper == null) {
      return null;
    }
    Variable variable = wrapper.getVariable();
    if (variable == null) {
      return null;
    }

    Variable.Real realVariable = variable.asRealVariable();

    if (realVariable == null) {
      return null;
    }

    final JsVariable jsVariable = realVariable.getJsVariable();

    final String qualifiedName = realVariable.createHolderWatchExpression();
    if (qualifiedName == null) {
      return null;
    }

    return new Runnable() {
      @Override
      public void run() {
        String expression = "Object.getOwnPropertyDescriptor(" + qualifiedName + ", \"" +
            jsVariable.getName() + "\")";

        IExpressionManager expressionManager = DebugPlugin.getDefault().getExpressionManager();
        IWatchExpression watchExpression = expressionManager.newWatchExpression(expression);
        expressionManager.addExpression(watchExpression);
      }
    };
  }
}
