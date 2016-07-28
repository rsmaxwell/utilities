
package com.rsmaxwell.utilities.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Thread that reads the process output.
 */
public class OutputThread extends Thread {

    private final InputStream inputStream;
    private final OutputStream outputStream;

    /**
     * OutputThread extends Thread, so OutputThreads must be explicitly started using start()
     * 
     * @param inputStream
     * @param outputStream
     */
    public OutputThread(final InputStream inputStream, final OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        setName(this.getClass().getName());

    }

    /**
    * 
    */
    @Override
    public void run() {
        try {
            int inputByte;
            while ((inputByte = inputStream.read()) != -1) {
                outputStream.write(inputByte);
            }
            outputStream.flush();
            outputStream.close();
        }
        catch (final IOException exception) {
            final PrintStream printStream = new PrintStream(outputStream);
            printStream.println(this.getClass().getSimpleName() + ": " + exception.getMessage());
        }
        catch (final Exception exception) {
            final PrintStream printStream = new PrintStream(outputStream);
            printStream.println(this.getClass().getName() + " caught exception: " + exception);
            exception.printStackTrace(printStream);
        }
    }

    /**
     * @return outputStream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
