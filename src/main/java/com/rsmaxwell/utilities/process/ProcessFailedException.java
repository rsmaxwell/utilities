
package com.rsmaxwell.utilities.process;

/**
 * Exception indicating an error running a process
 */
public class ProcessFailedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8833506881114991885L;
    private final ProcessBuilder processBuilder;
    private ProcessResult result;

    /**
     * @param pb
     * @param result
     */
    public ProcessFailedException(final ProcessBuilder pb, final ProcessResult result) {
        processBuilder = pb;
        this.result = result;
    }

    /**
     * @param pb
     * @param cause
     */
    public ProcessFailedException(final ProcessBuilder pb, final Throwable cause) {
        super(cause);
        processBuilder = pb;
    }

    /**
     * @return the processBuilder
     */
    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    /**
     * @return the result
     */
    public ProcessResult getResult() {
        return result;
    }

    /**
    * 
    */
    @Override
    public String getMessage() {
        String message = "command: " + processBuilder.toString() + " --> ";
        if (result != null) {
            message += " --> " + result;
        }
        else if (getCause() != null) {
            message += getCause();
        }
        else {
            message += "?";
        }
        return message;
    }

}
