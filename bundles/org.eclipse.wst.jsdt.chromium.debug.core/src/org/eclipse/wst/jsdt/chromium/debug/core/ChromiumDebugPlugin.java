// Copyright (c) 2009, 2017 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.chromium.JavascriptVmFactory;
import org.eclipse.wst.jsdt.chromium.debug.core.internal.sourcemap.SourceMapManager;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ChromiumBreakpointWBAFactory;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ChromiumLineBreakpoint;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ConnectedTargetData;
import org.eclipse.wst.jsdt.chromium.debug.core.model.DebugTargetImpl;
import org.eclipse.wst.jsdt.chromium.debug.core.model.VmResource;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension.ISourceMapManager;
import org.eclipse.wst.jsdt.chromium.debug.core.util.ScriptTargetMapping;
import org.osgi.framework.BundleContext;

/**
 * The activator class that controls the plug-in life cycle.
 */
public class ChromiumDebugPlugin extends Plugin {

  /** The plug-in ID. */
  public static final String PLUGIN_ID = "org.eclipse.wst.jsdt.chromium.debug.core"; //$NON-NLS-1$

  /** The JavaScript line breakpoint marker. */
  public static final String BP_MARKER = PLUGIN_ID + ".LineBP"; //$NON-NLS-1$

  /** The JavaScript exception breakpoint marker. */
  public static final String EXCEPTION_BP_MARKER = PLUGIN_ID + ".ExceptionBP"; //$NON-NLS-1$

  /** The shared instance. */
  private static ChromiumDebugPlugin plugin;

  private ChromiumBreakpointWBAFactory breakpointWorkbenchAdapterFactory;

  public ChromiumDebugPlugin() {
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    IAdapterManager manager = Platform.getAdapterManager();
    breakpointWorkbenchAdapterFactory = new ChromiumBreakpointWBAFactory();
    manager.registerAdapters(breakpointWorkbenchAdapterFactory, ChromiumLineBreakpoint.class);
    plugin = this;

    SourceMapManager.getInstance().initialize();
    JavascriptVmFactory.getRootLogger().addHandler(SDK_LOG_HANDLER);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    JavascriptVmFactory.getRootLogger().removeHandler(SDK_LOG_HANDLER);
    SourceMapManager.getInstance().destroy();
    plugin = null;
    IAdapterManager manager = Platform.getAdapterManager();
    manager.unregisterAdapters(breakpointWorkbenchAdapterFactory);
    super.stop(context);
  }

  /**
   * @return the shared instance
   */
  public static ChromiumDebugPlugin getDefault() {
    return plugin;
  }

  /**
   * Finds all file pairs for a user working file. One working file may correspond to several
   * scripts if there are more than one debug sessions.
   */
  public static List<? extends ScriptTargetMapping> getScriptTargetMapping(IFile localFile) {
    List<ConnectedTargetData> targetDataList = DebugTargetImpl.getAllConnectedTargetDatas();
    ArrayList<ScriptTargetMapping> result =
        new ArrayList<ScriptTargetMapping>(targetDataList.size());

    for (ConnectedTargetData targetData : targetDataList) {
      Collection<? extends VmResource> resources;
      try {
        resources = targetData.getVmResource(localFile);
      } catch (CoreException e) {
        throw new RuntimeException("Failed to resolve script from the file " + localFile, e);
      }
      if (resources == null || resources.isEmpty()) {
        continue;
      }
      result.add(new ScriptTargetMapping(localFile, resources, targetData));
    }
    return result;
  }

  public static boolean isDebug() {
    ChromiumDebugPlugin thePlugin = getDefault();
    return thePlugin != null && thePlugin.isDebugging();
  }

  public static boolean isTransportDebug() {
    return isDebug() &&
        Boolean.valueOf(Platform.getDebugOption(PLUGIN_ID + "/debug/transport")); //$NON-NLS-1$
  }

  public static boolean isV8DebuggerToolDebug() {
    return isDebug() &&
        Boolean.valueOf(Platform.getDebugOption(PLUGIN_ID + "/debug/v8DebuggerTool")); //$NON-NLS-1$
  }

  public static void log(IStatus status) {
    ChromiumDebugPlugin plugin = getDefault();
    if (plugin != null) {
      plugin.getLog().log(status);
    } else {
      System.err.println(status.getPlugin() + ": " + status.getMessage()); //$NON-NLS-1$
    }
  }

  public static void log(Throwable e) {
    if (e instanceof CoreException) {
      log(new Status(IStatus.ERROR, PLUGIN_ID,
          ((CoreException) e).getStatus().getSeverity(), e.getMessage(), e.getCause()));
    } else {
      log(new Status(IStatus.ERROR, PLUGIN_ID, Messages.ChromiumDebugPlugin_InternalError, e));
    }
  }

  public static void logError(String message, Object... arguments) {
    log(new Status(Status.ERROR, PLUGIN_ID, getPossiblyFormattedString(message, arguments)));
  }

  public static void logWarning(String message, Object... arguments) {
    log(new Status(Status.WARNING, PLUGIN_ID, getPossiblyFormattedString(message, arguments)));
  }

  private static String getPossiblyFormattedString(String message, Object... arguments) {
    return arguments.length > 0
        ? MessageFormat.format(message, arguments)
        : message;
  }

  private static final Handler SDK_LOG_HANDLER = new Handler() {
    @Override
    public void publish(LogRecord record) {
      int statusSeverity = 0;
      Level level = record.getLevel();
      if (level == Level.SEVERE) {
        statusSeverity = Status.ERROR;
      } else if (level == Level.WARNING) {
        statusSeverity = Status.WARNING;
      } else {
        statusSeverity = Status.INFO;
      }
      log(new Status(statusSeverity, PLUGIN_ID, "SDK:" + record.getLoggerName() + ": " +
          record.getMessage(), record.getThrown()));
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
  };

	public static ISourceMapManager getSourceMapManager() {
		return SourceMapManager.getInstance();
	}
}
