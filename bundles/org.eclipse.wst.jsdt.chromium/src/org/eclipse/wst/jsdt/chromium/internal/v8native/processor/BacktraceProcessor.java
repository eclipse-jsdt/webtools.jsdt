// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.processor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.wst.jsdt.chromium.DebugContext;
import org.eclipse.wst.jsdt.chromium.JavascriptVm;
import org.eclipse.wst.jsdt.chromium.Script;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.eclipse.wst.jsdt.chromium.internal.v8native.ContextBuilder;
import org.eclipse.wst.jsdt.chromium.internal.v8native.DebugSession;
import org.eclipse.wst.jsdt.chromium.internal.v8native.DebuggerCommand;
import org.eclipse.wst.jsdt.chromium.internal.v8native.V8CommandProcessor;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.BacktraceCommandBody;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.CommandResponse;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.FrameObject;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.SuccessCommandResponse;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.SomeHandle;
import org.eclipse.wst.jsdt.chromium.internal.v8native.value.ValueLoaderImpl;

/**
 * Handles the "backtrace" V8 command replies.
 */
public class BacktraceProcessor implements V8CommandProcessor.V8HandlerCallback {

  private final ContextBuilder.ExpectingBacktraceStep step2;

  BacktraceProcessor(ContextBuilder.ExpectingBacktraceStep step2) {
    this.step2 = step2;
  }

  @Override
  public void messageReceived(CommandResponse response) {
    String commandString = response.command();

    DebuggerCommand command = DebuggerCommand.forString(commandString);
    if (command != DebuggerCommand.BACKTRACE) {
      handleWrongStacktrace();
    }
    SuccessCommandResponse successResponse = response.asSuccess();
    if (successResponse == null) {
      handleWrongStacktrace();
    }

    final DebugContext debugContext = setFrames(successResponse, step2);
    final DebugSession debugSession = step2.getInternalContext().getDebugSession();

    JavascriptVm.ScriptsCallback afterScriptsAreLoaded = new JavascriptVm.ScriptsCallback() {
      @Override public void failure(String errorMessage) {
        handleWrongStacktrace();
      }

      @Override public void success(Collection<Script> scripts) {
        debugSession.getDebugEventListener().suspended(debugContext);
      }
    };

    debugSession.getScriptManagerProxy().getAllScripts(afterScriptsAreLoaded, null);
  }

  public static DebugContext setFrames(SuccessCommandResponse response,
      ContextBuilder.ExpectingBacktraceStep step2) {
    BacktraceCommandBody body;
    try {
      body = response.body().asBacktraceCommandBody();
    } catch (JsonProtocolParseException e) {
      throw new RuntimeException(e);
    }
    List<FrameObject> jsonFrames = body.frames();
    if (jsonFrames == null) {
      jsonFrames = Collections.emptyList();
    }

    ValueLoaderImpl valueLoader = step2.getInternalContext().getValueLoader();
    for (SomeHandle handle : response.refs()) {
      valueLoader.addHandleFromRefs(handle);
    }

    return step2.setFrames(jsonFrames);
  }

  @Override
  public void failure(String message) {
    handleWrongStacktrace();
  }

  private void handleWrongStacktrace() {
    step2.getInternalContext().getContextBuilder().buildSequenceFailure();
  }
}
