package com.apper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyRegisteredException extends RuntimeException {
    public UsernameAlreadyRegisteredException() {
        super();
    }

    public UsernameAlreadyRegisteredException(String message) {
        super(message);
    }

    public UsernameAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

    protected UsernameAlreadyRegisteredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}