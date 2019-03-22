// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium;

/**
 * This exception is thrown if the SDK protocol version is not compatible with
 * that supported by the browser.
 */
public class UnsupportedVersionException extends Exception {

  private static final long serialVersionUID = 1L;
  private final Version localVersion;
  private final Version remoteVersion;

  public UnsupportedVersionException(Version localVersion, Version remoteVersion) {
    this(localVersion, remoteVersion, "localVersion=" + localVersion
        + "; remoteVersion=" + remoteVersion);
  }

  public UnsupportedVersionException(Version localVersion, Version remoteVersion, String message) {
    super(message);
    this.localVersion = localVersion;
    this.remoteVersion = remoteVersion;
  }

  /**
   * @return the protocol version supported by the SDK
   */
  public Version getLocalVersion() {
    return localVersion;
  }

  /**
   * @return the incompatible protocol version supported by the browser
   */
  public Version getRemoteVersion() {
    return remoteVersion;
  }
}
