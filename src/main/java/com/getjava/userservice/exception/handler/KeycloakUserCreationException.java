package com.getjava.userservice.exception.handler;

import com.getjava.userservice.exception.handler.base.BadGatewayException;
import lombok.Getter;

@Getter
public class KeycloakUserCreationException extends BadGatewayException {
    private static final String ERROR_MESSAGE = "Error creating user in Keycloak";

    private final int statusCode;

    public KeycloakUserCreationException(int statusCode) {
        super(ERROR_MESSAGE);
        this.statusCode = statusCode;
    }
}
