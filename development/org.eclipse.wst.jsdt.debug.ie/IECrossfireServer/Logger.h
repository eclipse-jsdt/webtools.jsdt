/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


#pragma once

#include <sstream>
#include <winsock2.h>

class Logger {
public:
	static void error(char* message);
	static void error(char* message, int errorCode);
	static void log(char* message);
	static void log(char* message, int code);
	static void log(wchar_t* message);
	static void log(std::wstring* message);

protected:
	Logger();
	~Logger();

private:
	static void send(char* message);

	/* constants */
	static const char* PREAMBLE_ERROR;
	static const char* PREAMBLE_LOG;
};
