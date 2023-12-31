/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.parser;

/**
 * IMPORTANT NOTE: These constants are dedicated to the internal Scanner implementation.
 * It is mirrored in org.eclipse.wst.jsdt.core.compiler public package where it is API.
 * The mirror implementation is using the backward compatible ITerminalSymbols constant
 * definitions (stable with 2.0), whereas the internal implementation uses TerminalTokens
 * which constant values reflect the latest parser generation state.
 */
/**
 * Maps each terminal symbol in the java-grammar into a unique integer.
 * This integer is used to represent the terminal when computing a parsing action.
 *
 * Disclaimer : These constant values are generated automatically using a Java
 * grammar, therefore their actual values are subject to change if new keywords
 * were added to the language (for instance, 'assert' is a keyword in 1.4).
 */
public interface TerminalTokens {

	// special tokens not part of grammar - not autogenerated
	int TokenNameWHITESPACE = 1000,
		TokenNameCOMMENT_LINE = 1001,
		TokenNameCOMMENT_BLOCK = 1002,
		TokenNameCOMMENT_JAVADOC = 1003,
		TokenNameSHEBANG_LINE = 1004,
		TokenNameUNKNOWN = 1005;

    int TokenNameIdentifier = 32,
		TokenNameabstract = 88,
		TokenNameboolean = 89,
		TokenNamebreak = 59,
		TokenNamebyte = 90,
		TokenNamecase = 84,
		TokenNamecatch = 86,
		TokenNamechar = 91,
		TokenNameclass = 92,
		TokenNamecontinue = 60,
		TokenNameconst = 93,
		TokenNamedefault = 85,
		TokenNamedebugger = 61,
		TokenNamedelete = 47,
		TokenNamedo = 62,
		TokenNamedouble = 94,
		TokenNameelse = 72,
		TokenNameenum = 95,
		TokenNameexport = 96,
		TokenNameextends = 97,
		TokenNamefalse = 36,
		TokenNamefinal = 98,
		TokenNamefinally = 87,
		TokenNamefloat = 99,
		TokenNamefor = 63,
		TokenNamefunction = 55,
		TokenNamegoto = 100,
		TokenNameif = 64,
		TokenNamein = 25,
		TokenNameimplements = 101,
		TokenNameimport = 102,
		TokenNameinstanceof = 13,
		TokenNameint = 103,
		TokenNameinterface = 104,
		TokenNamelet = 105,
		TokenNamelong = 106,
		TokenNamenative = 107,
		TokenNamenew = 37,
		TokenNamenull = 38,
		TokenNamepackage = 108,
		TokenNameprivate = 109,
		TokenNameprotected = 110,
		TokenNamepublic = 111,
		TokenNamereturn = 65,
		TokenNameshort = 112,
		TokenNamestatic = 113,
		TokenNamesuper = 114,
		TokenNameswitch = 66,
		TokenNamesynchronized = 115,
		TokenNamethis = 39,
		TokenNamethrow = 67,
		TokenNamethrows = 116,
		TokenNametransient = 117,
		TokenNametrue = 40,
		TokenNametry = 68,
		TokenNametypeof = 48,
		TokenNameundefined = 41,
		TokenNamevar = 57,
		TokenNamevoid = 49,
		TokenNamevolatile = 118,
		TokenNamewhile = 58,
		TokenNamewith = 69,
		TokenNameyield = 119,
		TokenNameIntegerLiteral = 33,
		TokenNameLongLiteral = 42,
		TokenNameFloatingPointLiteral = 43,
		TokenNameDoubleLiteral = 44,
		TokenNameCharacterLiteral = 34,
		TokenNameStringLiteral = 35,
		TokenNameRegExLiteral = 45,
		TokenNamePLUS_PLUS = 3,
		TokenNameMINUS_MINUS = 4,
		TokenNameEQUAL_EQUAL = 18,
		TokenNameEQUAL_EQUAL_EQUAL = 19,
		TokenNameNOT_EQUAL_EQUAL = 20,
		TokenNameLESS_EQUAL = 14,
		TokenNameGREATER_EQUAL = 15,
		TokenNameNOT_EQUAL = 21,
		TokenNameLEFT_SHIFT = 6,
		TokenNameRIGHT_SHIFT = 7,
		TokenNameUNSIGNED_RIGHT_SHIFT = 5,
		TokenNamePLUS_EQUAL = 73,
		TokenNameMINUS_EQUAL = 74,
		TokenNameMULTIPLY_EQUAL = 75,
		TokenNameDIVIDE_EQUAL = 76,
		TokenNameAND_EQUAL = 77,
		TokenNameOR_EQUAL = 78,
		TokenNameXOR_EQUAL = 79,
		TokenNameREMAINDER_EQUAL = 80,
		TokenNameLEFT_SHIFT_EQUAL = 81,
		TokenNameRIGHT_SHIFT_EQUAL = 82,
		TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL = 83,
		TokenNameOR_OR = 29,
		TokenNameAND_AND = 28,
		TokenNamePLUS = 1,
		TokenNameMINUS = 2,
		TokenNameNOT = 50,
		TokenNameREMAINDER = 9,
		TokenNameXOR = 23,
		TokenNameAND = 22,
		TokenNameMULTIPLY = 10,
		TokenNameOR = 27,
		TokenNameTWIDDLE = 46,
		TokenNameDIVIDE = 11,
		TokenNameGREATER = 16,
		TokenNameLESS = 17,
		TokenNameLPAREN = 8,
		TokenNameRPAREN = 52,
		TokenNameLBRACE = 51,
		TokenNameRBRACE = 56,
		TokenNameLBRACKET = 12,
		TokenNameRBRACKET = 54,
		TokenNameSEMICOLON = 24,
		TokenNameQUESTION = 30,
		TokenNameCOLON = 53,
		TokenNameCOMMA = 26,
		TokenNameDOT = 31,
		TokenNameEQUAL = 71,
		TokenNameEOF = 70,
		TokenNameERROR = 120;
}
