package com.github.tamurashingo.nanakusagayu.init;

import com.github.tamurashingo.nanakusagayu.NanakusagayuException;

public class InitializerException extends NanakusagayuException {

    private static final long serialVersionUID = 1L;

    public InitializerException(String message) {
        super(message);
    }

    public InitializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializerException(Throwable cause) {
        super(cause);
    }
}
