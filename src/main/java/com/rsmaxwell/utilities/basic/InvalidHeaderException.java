/**
 * 
 */
package com.rsmaxwell.utilities.basic;

/**
 *
 */
public class InvalidHeaderException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3310158453592176702L;

    /**
     * 
     */
    public InvalidHeaderException() {
        // empty
    }

    /**
     * @param message
     */
    public InvalidHeaderException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidHeaderException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidHeaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
