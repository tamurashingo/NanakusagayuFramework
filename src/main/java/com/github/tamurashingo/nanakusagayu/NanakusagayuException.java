package com.github.tamurashingo.nanakusagayu;

public class NanakusagayuException extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * construct a new {@code NanakusagayuException}
     * with the specified short message.
     * @param message the short message
     */
    public NanakusagayuException(String message) {
        super(message);
    }

    /**
     * construct a new {@code NanakusagayuException}
     * with the specified short message and cause.
     * @param message the short message
     * @param cause the cause
     */
    public NanakusagayuException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * construct a new {@code NanakusagayuException}
     * with the specified cause.
     * @param cause the cause
     */
    public NanakusagayuException(Throwable cause) {
        super(cause);
    }
}
