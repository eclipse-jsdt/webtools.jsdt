// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.JavaCodeGenerator.FileScope;

/**
 * Information about a field type that is used in Java code generation.
 */
interface FieldTypeInfo {
  void appendValueTypeNameJava(FileScope scope);
}