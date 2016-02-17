// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.internal.v8native;

import java.io.IOException;

import org.eclipse.wst.jsdt.chromium.Breakpoint;
import org.eclipse.wst.jsdt.chromium.BreakpointTypeExtension;
import org.eclipse.wst.jsdt.chromium.CallbackSemaphore;
import org.eclipse.wst.jsdt.chromium.FunctionScopeExtension;
import org.eclipse.wst.jsdt.chromium.IgnoreCountBreakpointExtension;
import org.eclipse.wst.jsdt.chromium.JavascriptVm;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.RestartFrameExtension;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.Version;
import org.eclipse.wst.jsdt.chromium.internal.v8native.value.JsFunctionImpl;
import org.eclipse.wst.jsdt.chromium.util.GenericCallback;
import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;

/**
 * Base implementation of JavascriptVm.
 */
public abstract class JavascriptVmImpl implements JavascriptVm {

  protected JavascriptVmImpl() {
  }

  @Override
  public void suspend(SuspendCallback callback) {
    getDebugSession().suspend(callback);
  }

  // TODO: make sure we do not return those scripts that are reported compiled but not loaded yet.
  @Override
  public void getScripts(ScriptsCallback callback) throws MethodIsBlockingException {
    CallbackSemaphore callbackSemaphore = new CallbackSemaphore();
    RelayOk relayOk =
        getDebugSession().getScriptManagerProxy().getAllScripts(callback, callbackSemaphore);

    boolean res = callbackSemaphore.tryAcquireDefault(relayOk);
    if (!res) {
      callback.failure("Timeout");
    }
  }

  @Override
  public RelayOk setBreakpoint(Breakpoint.Target target, int line,
      int column, boolean enabled, String condition,
      BreakpointCallback callback, SyncCallback syncCallback) {
    return getDebugSession().getBreakpointManager()
        .setBreakpoint(target, line, column, enabled, condition, callback, syncCallback);
  }

  @Override
  public RelayOk listBreakpoints(final ListBreakpointsCallback callback,
      SyncCallback syncCallback) {
    return getDebugSession().getBreakpointManager().reloadBreakpoints(callback, syncCallback);
  }

  @Override
  public RelayOk enableBreakpoints(Boolean enabled, GenericCallback<Boolean> callback,
      SyncCallback syncCallback) {
    return getDebugSession().getBreakpointManager().enableBreakpoints(enabled,
        callback, syncCallback);
  }

  @Override
  public RelayOk setBreakOnException(ExceptionCatchMode catchMode,
      GenericCallback<ExceptionCatchMode> callback, SyncCallback syncCallback) {
    return getDebugSession().getBreakpointManager().setBreakOnException(catchMode,
        callback, syncCallback);
  }

  @Override
  public Version getVersion() {
    return getDebugSession().getVmVersion();
  }

  @Override
  public BreakpointTypeExtension getBreakpointTypeExtension() {
    return getDebugSession().getBreakpointManager().getBreakpointTypeExtension();
  }

  @Override
  public IgnoreCountBreakpointExtension getIgnoreCountBreakpointExtension() {
    return BreakpointImpl.IGNORE_COUNT_EXTENSION;
  }

  @Override
  public FunctionScopeExtension getFunctionScopeExtension() {
    if (!V8VersionFeatures.isFunctionScopeSupported(getDebugSession().getVmVersion())) {
      return null;
    }
    return JsFunctionImpl.FUNCTION_SCOPE_EXTENSION;
  }

  @Override
  public RestartFrameExtension getRestartFrameExtension() {
    if (!V8VersionFeatures.isRestartFrameSupported(getDebugSession().getVmVersion())) {
      return null;
    }
    return CallFrameImpl.RESTART_FRAME_EXTENSION;
  }

  public abstract DebugSession getDebugSession();

  // TODO(peter.rybin): This message will be obsolete in JavaSE-1.6.
  public static IOException newIOException(String message, Throwable cause) {
    IOException result = new IOException(message);
    result.initCause(cause);
    return result;
  }
}
