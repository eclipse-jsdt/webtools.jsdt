// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.JavaCodeGenerator.ClassScope;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.JavaCodeGenerator.MethodScope;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.JavaCodeGenerator.Util;

/**
 * This classs is responsible for parsing field values and saving them in {@link ObjectData}
 * for future use.
 */
class FieldLoader {
  private final String fieldName;
  private final int fieldPosInArray;
  private final SlowParser<?> slowParser;
  private final boolean isOptional;

  FieldLoader(int fieldPosInArray, String fieldName, SlowParser<?> slowParser,
      boolean isOptional) {
    this.fieldName = fieldName;
    this.fieldPosInArray = fieldPosInArray;
    this.slowParser = slowParser;
    this.isOptional = isOptional;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void parse(boolean hasValue, Object value, ObjectData objectData)
      throws JsonProtocolParseException {
    if (hasValue) {
      try {
        objectData.getFieldArray()[fieldPosInArray] = slowParser.parseValue(value, objectData);
      } catch (JsonProtocolParseException e) {
        throw new JsonProtocolParseException("Failed to parse field " + getFieldName(), e);
      }
    } else {
      if (!isOptional) {
        throw new JsonProtocolParseException("Field is not optional: " + getFieldName());
      }
    }
  }

  public void writeFieldDeclarationJava(ClassScope scope) {
    scope.startLine("private final ");
    slowParser.appendFinishedValueTypeNameJava(scope);
    scope.append(" field_" + fieldName + ";\n");
  }

  public void writeFieldLoadJava(MethodScope scope, String valueRef, String hasValueRef) {
    scope.startLine("if (" + hasValueRef + ") {\n");
    scope.indentRight();
    if (slowParser.javaCodeThrowsException()) {
      scope.startLine("try {\n");
      scope.indentRight();
      String parsedValueRef = scope.newMethodScopedName("parsedValue");
      slowParser.writeParseCode(scope, valueRef, "this", parsedValueRef);
      scope.startLine("this.field_" + fieldName + " = " + parsedValueRef + ";\n");
      scope.indentLeft();
      scope.startLine("} catch (" + Util.BASE_PACKAGE + ".JsonProtocolParseException e) {\n");
      scope.startLine("  throw new " + Util.BASE_PACKAGE + ".JsonProtocolParseException(" +
          "\"Failed to parse field " + getFieldName() + "\", e);\n");
      scope.startLine("}\n");
    } else {
      String parsedValueRef = scope.newMethodScopedName("parsedValue");
      slowParser.writeParseCode(scope, valueRef, "this", parsedValueRef);
      scope.startLine("this.field_" + fieldName + " = " + parsedValueRef + ";\n");
    }
    scope.indentLeft();
    scope.startLine("} else {\n");
    if (isOptional) {
      scope.startLine("  this.field_" + fieldName + " = null;\n");
    } else {
      scope.startLine("  throw new " + Util.BASE_PACKAGE + ".JsonProtocolParseException(" +
          "\"Field is not optional: " + getFieldName() + "\");\n");
    }
    scope.startLine("}\n");
  }
}
