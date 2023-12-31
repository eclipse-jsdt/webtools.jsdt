// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.test;

import java.util.Arrays;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolModelParseException;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl.DynamicParserImpl;

/**
 * The class holding a parser and suitable for static field (it saves initialization problem).
 * There is no reason to create new instance of parser for every test. On the other hand,
 * it there were a problem with it, every test should get a proper exception.
 */
class ParserHolder<R> {
  private final InitializedValue<DynamicParserImpl<R>> parser;

  ParserHolder(final Class<R> parserInterface, final Class<?>[] interfaces) {
    InitializedValue.Initializer<DynamicParserImpl<R>> initializer =
        new InitializedValue.Initializer<DynamicParserImpl<R>>() {
      @Override
      public DynamicParserImpl<R> calculate() {
        try {
          return new DynamicParserImpl<R>(parserInterface, Arrays.asList(interfaces));
        } catch (JsonProtocolModelParseException e) {
          throw new RuntimeException(e);
        }
      }
    };
    parser = new InitializedValue<DynamicParserImpl<R>>(initializer);
  }

  R getParser() {
    return parser.get().getParserRoot();
  }
}