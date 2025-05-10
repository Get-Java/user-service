package com.getjava.userservice.exception.handler;

import com.getjava.userservice.dto.response.ErrorResponse;
import com.getjava.userservice.dto.response.ValidationErrorResponse;
import com.getjava.userservice.exception.handler.base.BadGatewayException;
import com.getjava.userservice.exception.handler.base.NotFoundException;
import com.getjava.userservice.mapper.ExceptionMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return exceptionMapper.toErrorResponse(ex, request.getRequestURI());
    }

    @ExceptionHandler(BadGatewayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleBadGatewayException(BadGatewayException ex, HttpServletRequest request) {
        return exceptionMapper.toErrorResponse(ex, request.getRequestURI());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ValidationErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrorResponse(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации данных запроса",
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI(),
                errors
        );
    }
}
