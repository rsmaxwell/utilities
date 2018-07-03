/**
 * 
 */
package com.rsmaxwell.utilities.basic;

/**
 *
 */
public class InsufficientStorageException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 5886992476311709164L;

    /**
     * 
     */
    public InsufficientStorageException() {
        // empty
    }

    /**
     * @param message
     */
    public InsufficientStorageException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InsufficientStorageException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InsufficientStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InsufficientStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
