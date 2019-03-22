// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip.tools.protocolgenerator;

import java.util.Arrays;
import java.util.Collections;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParseMethod;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParserRoot;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolModelParseException;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.DynamicParserImpl;
import org.eclipse.wst.jsdt.chromium.internal.wip.tools.protocolgenerator.WipMetamodel.Root;

/**
 * Parser for WIP JSON metamodel.
 */
@JsonParserRoot
interface WipMetamodelParser {

  @JsonParseMethod
  Root parseRoot(Object jsonValue) throws JsonProtocolParseException;

  /**
   * Creates dynamic parser implementation.
   */
  class Impl {
    static WipMetamodelParser get() {
      return INTSTANCE;
    }

    private static final WipMetamodelParser INTSTANCE;
    static {
      Class<?>[] classes = {
          WipMetamodel.Root.class,
          WipMetamodel.Domain.class,
          WipMetamodel.Command.class,
          WipMetamodel.Parameter.class,
          WipMetamodel.Event.class,
          WipMetamodel.StandaloneType.class,
          WipMetamodel.ObjectProperty.class,
          WipMetamodel.ArrayItemType.class,
      };

      DynamicParserImpl<WipMetamodelParser> dynamicParserImpl;
      try {
        dynamicParserImpl = new DynamicParserImpl<WipMetamodelParser>(WipMetamodelParser.class,
            Arrays.asList(classes), Collections.<DynamicParserImpl<?>>emptyList(), true);
      } catch (JsonProtocolModelParseException e) {
        throw new RuntimeException("Failed to build metamodel parser", e);
      }
      INTSTANCE = dynamicParserImpl.getParserRoot();
    }
  }
}
