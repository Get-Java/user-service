package com.getjava.userservice.mapper;

import com.getjava.userservice.dto.response.ErrorResponse;
import com.getjava.userservice.dto.response.ValidationErrorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ExceptionMapper {

    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(getStatus(exception).value())")
    @Mapping(target = "error", expression = "java(getStatus(exception).getReasonPhrase())")
    @Mapping(target = "message", source = "exception.message")
    @Mapping(target = "path", source = "path")
    @Mapping(target = "errors", source = "errors")
    ErrorResponse toErrorResponse(Exception exception, String path, List<ValidationErrorResponse> errors);

    default ErrorResponse toErrorResponse(Exception exception, String path) {
        return toErrorResponse(exception, path, Collections.emptyList());
    }

    default HttpStatus getStatus(Exception exception) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        return responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
