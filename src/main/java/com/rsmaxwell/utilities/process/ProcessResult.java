
package com.rsmaxwell.utilities.process;

import java.io.OutputStream;

import com.rsmaxwell.utilities.basic.StringUtilities;

/**
 * Helper class used to return the result of executing a script
 */
public class ProcessResult {

    private final static String NL = StringUtilities.newline;

    private final int completionCode;
    private final OutputStream standardOut;
    private final OutputStream standardError;
    private OutputStream standardInput;

    /**
     * @param completionCode
     * @param standardOutputThread
     * @param standardErrorThread
     * @param standardInputThread
     */
    public ProcessResult(final int completionCode, final OutputThread standardOutputThread, final OutputThread standardErrorThread, final InputThread standardInputThread) {
        this.completionCode = completionCode;
        standardOut = standardOutputThread.getOutputStream();
        standardError = standardErrorThread.getOutputStream();

        if (standardInputThread != null) {
            standardInput = standardInputThread.getOutputStream();
        }
    }

    /**
     * @return comp code
     */
    public final int getCompletionCode() {
        return completionCode;
    }

    /**
     * @return stdout
     */
    public final OutputStream getStandardOut() {
        return standardOut;
    }

    /**
     * @return stderr
     */
    public final OutputStream getStandardError() {
        return standardError;
    }

    /**
     * @return stderr
     */
    public final OutputStream getStandardInput() {
        return standardInput;
    }

    /**
     * @param title
     * @return string
     */
    public String toShortString(final String title) {
        final StringBuilder sb = new StringBuilder();
        sb.append(title + ": completionCode: " + completionCode);
        return sb.toString();
    }

    /**
    * 
    */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("completionCode: " + completionCode + NL);

        if (standardInput != null) {
            sb.append("-----< stdin >-------------------------------------------------------------" + NL);
            sb.append(standardInput.toString() + NL);
        }

        sb.append("-----< stdout >-------------------------------------------------------------" + NL);
        sb.append(standardOut.toString() + NL);
        sb.append("-----< stderr >-------------------------------------------------------------" + NL);
        sb.append(standardError.toString() + NL);
        sb.append("----------------------------------------------------------------------------" + NL);
        return sb.toString();
    }
}
