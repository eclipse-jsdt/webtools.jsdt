// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.chromium.internal.BaseCommandProcessor;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.WipCommandResponse;

/**
 * An explicit interface for a generic type {@link BaseCommandProcessor.Callback}.
 */
public interface WipCommandCallback extends BaseCommandProcessor.Callback<WipCommandResponse> {

  /**
   * A default implementation of the callback that separates error responses from
   * success responses.
   */
  abstract class Default implements WipCommandCallback {
    protected abstract void onSuccess(WipCommandResponse.Success success);
    protected abstract void onError(String message);

    @Override
    public void messageReceived(WipCommandResponse response) {
      WipCommandResponse.Success asSuccess = response.asSuccess();
      if (asSuccess != null) {
        onSuccess(asSuccess);
      } else {
        String message;
        WipCommandResponse.Error asError = response.asError();
        if (asError == null) {
          message = "Internal messaging error";
        } else {
          List<String> messageList = new ArrayList<String>(2);
          messageList.add(asError.error().message());
          List<String> data = asError.error().data();
          if (data != null) {
            messageList.addAll(data);
          }
          message = messageList.toString();
        }
        onError(message);
      }
    }

    @Override
    public void failure(String message) {
      onError(message);
    }
  }
}
