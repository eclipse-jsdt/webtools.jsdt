// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip.tools.protocolgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A class that makes accurate java source file update. If only header
 * (with source file revision and other comments) changed, the file is left intact.
 * <p>User first writes all the content into a {@link Writer} provided and then
 * calls {@link #update()}.
 */
class JavaFileUpdater {
  private final File file;
  private final StringWriter writer;

  JavaFileUpdater(File file) {
    this.file = file;
    this.writer = new StringWriter();
  }

  Writer getWriter() {
    return writer;
  }

  void update() throws IOException {
    writer.close();

    String newContent = writer.getBuffer().toString();

    if (file.isFile()) {
      String oldContent =
          StreamUtil.readStringFromStream(new FileInputStream(file), StreamUtil.UTF8_CHARSET);

      if (stripHeader(oldContent).equals(stripHeader(newContent))) {
        return;
      }
    } else {
      File dir = file.getParentFile();
      boolean dirCreated = dir.mkdirs();
      if (!dirCreated && !dir.isDirectory()) {
        throw new RuntimeException("Failed to create directory " + dir.getPath());
      }
    }

    OutputStream outputStream = new FileOutputStream(file);
    Writer fileWriter = new OutputStreamWriter(outputStream, StreamUtil.UTF8_CHARSET);
    fileWriter.write(newContent);
    fileWriter.close();
    outputStream.close();
  }

  private static String stripHeader(String content) {
    int pos = content.indexOf("\npackage ");
    if (pos == -1) {
      return content;
    }
    return content.substring(pos);
  }
}
