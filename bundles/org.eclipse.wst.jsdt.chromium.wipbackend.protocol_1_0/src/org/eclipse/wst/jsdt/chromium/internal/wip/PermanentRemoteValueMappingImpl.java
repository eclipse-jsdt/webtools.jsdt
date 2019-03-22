// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip;

import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.WipCommandResponse;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.runtime.ReleaseObjectGroupParams;
import org.eclipse.wst.jsdt.chromium.util.GenericCallback;
import org.eclipse.wst.jsdt.chromium.wip.PermanentRemoteValueMapping;

class PermanentRemoteValueMappingImpl extends WipValueLoader
    implements PermanentRemoteValueMapping {
  private final String id;

  PermanentRemoteValueMappingImpl(WipTabImpl tabImpl, String id) {
    super(tabImpl);
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public RelayOk delete(final GenericCallback<Void> callback, SyncCallback syncCallback) {
    ReleaseObjectGroupParams params = new ReleaseObjectGroupParams(id);
    WipCommandCallback callbackWrapper;
    if (callback == null) {
      callbackWrapper = null;
    } else {
      callbackWrapper = new WipCommandCallback() {
        @Override
        public void messageReceived(WipCommandResponse response) {
          callback.success(null);
        }

        @Override
        public void failure(String message) {
          callback.failure(new Exception(message));
        }
      };
    }
    return getTabImpl().getCommandProcessor().send(params, callbackWrapper, syncCallback);
  }

  @Override
  String getObjectGroupId() {
    return id;
  }

  @Override
  public JsEvaluateContext getEvaluateContext() {
    return new WipContextBuilder.GlobalEvaluateContext(this);
  }
}
