/**
 * 
 */
package com.rsmaxwell.utilities.basic;

/**
 *
 */
public class InvalidStateException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7187147231310260462L;

    /**
     * 
     */
    public InvalidStateException() {
        // empty
    }

    /**
     * @param message
     */
    public InvalidStateException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidStateException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
