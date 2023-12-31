// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium;

import java.util.Collection;

import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;

/**
 * A compound JsValue that has zero or more properties. Note that JavaScript {@code null}
 * value while officially being 'object' in the SDK is represented as a plain {@link JsValue}.
 */
public interface JsObject extends JsValue {

  /**
   * @return the class name of this object
   */
  String getClassName();

  /**
   * @return the own properties of this compound value (does <strong>not</strong> include
   *     properties from proto object)
   * @throws MethodIsBlockingException because it may need to load value from remote
   */
  Collection<? extends JsObjectProperty> getProperties() throws MethodIsBlockingException;

  /**
   * @return the internal properties of this compound value (e.g. those properties which
   *         are not detectable with the "in" operator: __proto__ etc)
   * @throws MethodIsBlockingException because it may need to load value from remote
   */
  Collection<? extends JsVariable> getInternalProperties() throws MethodIsBlockingException;

  /**
   * @param name of the property to get
   * @return the own property object or {@code null} if {@code name} does not
   *         designate an existing object property (does <strong>not</strong> return
   *         properties from proto object)
   * @throws MethodIsBlockingException because it may need to load value from remote
   */
  JsVariable getProperty(String name) throws MethodIsBlockingException;

  /**
   * @return this object cast to {@link JsArray} or {@code null} if this object
   *         is not an array
   */
  JsArray asArray();

  /**
   * @return this object cast to {@link JsFunction} or {@code null} if this object
   *         is not a function
   */
  JsFunction asFunction();

  /**
   * Optionally returns unique id for this object. No two distinct objects can have the same id.
   * Lifetime of id is limited to lifetime of its {@link RemoteValueMapping} (typically corresponds
   * to the lifetime of {@link DebugContext}.)
   * @return object id or null
   * @see #getRemoteValueMapping()
   */
  String getRefId();

  /**
   * @return value mapping this object is associated with or null for special-case
   *     immutable objects
   */
  RemoteValueMapping getRemoteValueMapping();
}
