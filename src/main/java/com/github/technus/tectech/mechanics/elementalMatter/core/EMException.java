package com.github.technus.tectech.mechanics.elementalMatter.core;

/**
 * Created by danie_000 on 19.11.2016.
 */
public final class EMException extends RuntimeException {

    private static final long serialVersionUID = 8030521803764577214L;

    public EMException(String message) {
        super(message);
    }

    public EMException(String message, Throwable cause) {
        super(message, cause);
    }

    public EMException(Throwable cause) {
        super(cause);
    }

    public EMException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
