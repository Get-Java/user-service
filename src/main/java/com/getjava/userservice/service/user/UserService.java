package com.getjava.userservice.service.user;

import com.getjava.userservice.dto.request.RegisterRequest;
import com.getjava.userservice.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest registerRequest);

    UserResponse me();
}
