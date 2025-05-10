package com.getjava.userservice.exception.handler.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public abstract class BadGatewayException extends RuntimeException {
    protected BadGatewayException(String message) {
        super(message);
    }
}
