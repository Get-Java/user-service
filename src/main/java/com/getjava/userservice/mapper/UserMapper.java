package com.getjava.userservice.mapper;

import com.getjava.userservice.dto.request.RegisterRequest;
import com.getjava.userservice.dto.response.UserResponse;
import com.getjava.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = SPRING)
public interface UserMapper {
    User toEntity(RegisterRequest registerRequest);

    UserResponse toUserResponse(User user);
}