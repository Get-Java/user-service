package com.getjava.userservice.dto.response;

public record ValidationErrorResponse(
        String field,
        String message
) {
}
