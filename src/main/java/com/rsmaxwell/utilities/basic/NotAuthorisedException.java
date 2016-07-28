/**
 * 
 */
package com.rsmaxwell.utilities.basic;

/**
 *
 */
public class NotAuthorisedException extends Exception {

    /**
     * 
     */
    public NotAuthorisedException() {
        // empty
    }

    /**
     * @param message
     */
    public NotAuthorisedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NotAuthorisedException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotAuthorisedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public NotAuthorisedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
