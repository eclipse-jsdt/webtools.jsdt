// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.wip;

import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.RemoteValueMapping;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.util.GenericCallback;

/**
 * A {@link RemoteValueMapping} that outlives suspend/resume cycle of debugger.
 * It represents both remote pointer table and local property caches.
 * The remote table should be explicitly deleted when the mapping is no longer used.
 * The table has a unique id.
 */
public interface PermanentRemoteValueMapping extends RemoteValueMapping {
  String getId();

  /**
   * Asynchronously deletes mapping on remote VM. No values from this {@link RemoteValueMapping}
   * must be used after this call.
   */
  RelayOk delete(GenericCallback<Void> callback, SyncCallback syncCallback);

  /**
   * Returns {@link JsEvaluateContext} that is tied with this {@link RemoteValueMapping}.
   * By default all evaluate result values will use this {@link RemoteValueMapping}.
   * (This can be overriden by {@link EvaluateToMappingExtension}).
   */
  JsEvaluateContext getEvaluateContext();
}
