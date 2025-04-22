package com.example.keycloak.services;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import com.example.keycloak.dto.AuthResponse;
import com.example.keycloak.dto.UserCreateDTO;
import com.example.keycloak.dto.UserDTO;

public interface keycloakinterface {
    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(UserCreateDTO userCreateDTO);
    void deleteUser(String userId);
    UserRepresentation getUserById(String userId);
    void updateUser(String userId, UserDTO userDTO);
    AuthResponse refreshToken(String refreshToken);
    AuthResponse login(String username, String password);
    UserRepresentation findUserByUsername(String username);
    
}
