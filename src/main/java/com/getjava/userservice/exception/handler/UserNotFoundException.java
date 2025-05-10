package com.getjava.userservice.exception.handler;

import com.getjava.userservice.exception.handler.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private static final String ERROR_MESSAGE = "User not found";

    public UserNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
