package com.getjava.userservice.service.user;

import com.getjava.userservice.dto.request.RegisterRequest;
import com.getjava.userservice.dto.response.UserResponse;
import com.getjava.userservice.exception.handler.KeycloakUserCreationException;
import com.getjava.userservice.mapper.UserMapper;
import com.getjava.userservice.model.User;
import com.getjava.userservice.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Keycloak keycloak;
    private final UserMapper userMapper;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        Response response = keycloak.realm(realm)
                .users()
                .create(user);

        log.info("Keycloak response: {}, {}", response.getStatus(), response.getStatusInfo());

        if (response.getStatus() != 201) {
            throw new KeycloakUserCreationException(response.getStatus());
        }

        String userId = extractId(response);
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(registerRequest.getPassword());
        password.setTemporary(false);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .resetPassword(password);

        User userEntity = userMapper.toEntity(registerRequest);
        userEntity.setKeycloakId(UUID.fromString(userId));
        userEntity = userRepository.save(userEntity);

        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public UserResponse me() {
        return null;
    }

    private String extractId(Response response) {
        return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
    }
}

