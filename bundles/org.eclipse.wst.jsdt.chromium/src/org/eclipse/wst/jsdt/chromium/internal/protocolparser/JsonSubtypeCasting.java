// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks method as method casting to a subtype. Normally the method return type should be
 * some other json type, which serves as subtype; the subtype interface must extend
 * {@link JsonSubtype} (with correct generic parameter).
 * <p>However for types, annotated as <code>{@link JsonType#subtypesChosenManually()} = true</code>,
 * the method may return something other than json type; it also may return any json type (free of
 * mandatory {@link JsonSubtype} inheritance), provided that
 * <code>{@link #reinterpret()} = true</code>.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface JsonSubtypeCasting {
  boolean reinterpret() default false;
}
