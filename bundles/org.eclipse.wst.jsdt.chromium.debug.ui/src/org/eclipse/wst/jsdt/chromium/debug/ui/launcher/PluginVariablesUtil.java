// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.launcher;

import org.eclipse.wst.jsdt.chromium.debug.ui.ChromiumDebugUIPlugin;
import org.eclipse.core.variables.VariablesPlugin;

/**
 * Provides convenient access to the variables declared in the
 * org.eclipse.core.variables.valueVariables extension point.
 */
class PluginVariablesUtil {

  /** The default server port variable id. */
  public static final String DEFAULT_HOST =
      ChromiumDebugUIPlugin.PLUGIN_ID + ".chromium_debug_host"; //$NON-NLS-1$

  public static final String DEFAULT_PORT =
      ChromiumDebugUIPlugin.PLUGIN_ID + ".chromium_debug_port"; //$NON-NLS-1$

  /**
   * @param variableName to get the value for
   * @return the variable value parsed as an integer
   * @throws NumberFormatException
   *           if the value cannot be parsed as an integer
   */
  public static int getValueAsInt(String variableName) {
    return Integer.parseInt(getValue(variableName));
  }

  /**
   * @param variableName to get the value for
   * @return the value of the specified variable
   */
  public static String getValue(String variableName) {
    return VariablesPlugin.getDefault().getStringVariableManager()
        .getValueVariable(variableName).getValue();
  }

  private PluginVariablesUtil() {
    // not instantiable
  }
}
