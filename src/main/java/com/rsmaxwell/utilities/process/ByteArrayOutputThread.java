
package com.rsmaxwell.utilities.process;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Thread that reads the process output. Since ByteArrayOutputThread extends Thread, instances of
 * ByteArrayOutputThread should be explicitly started using start().
 */
public class ByteArrayOutputThread extends OutputThread {

    /**
     * @param inputStream
     */
    public ByteArrayOutputThread(final InputStream inputStream) {
        super(inputStream, new ByteArrayOutputStream());
    }
}
