package com.apper.theblogservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BloggerNotFoundException extends RuntimeException {
    public BloggerNotFoundException(String message) {
        super(message);
    }
}
