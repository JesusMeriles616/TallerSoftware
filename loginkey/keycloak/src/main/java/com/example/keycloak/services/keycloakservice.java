package com.example.keycloak.services;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.keycloak.dto.AuthResponse;
import com.example.keycloak.dto.UserCreateDTO;
import com.example.keycloak.dto.UserDTO;
import com.example.keycloak.exception.validacion;
import com.example.keycloak.util.KeycloakServiceException;
import com.example.keycloak.util.keycloakProvider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class keycloakservice implements keycloakinterface {
    private final keycloakProvider keycloakProvider;
    private  final  keycloakconverter keycloakconverter;
    /******** MÉTODOS PRIVADOS AUXILIARES ********/

    /**
     * Verifica si un usuario ya existe en Keycloak.
     * @param usersResource Recurso de usuarios de Keycloak
     * @param username Nombre de usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    private boolean userExists(UsersResource usersResource, String username) {
        return !usersResource.search(username, true).isEmpty();
    }
    private void configureUser(String userId, UserDTO userDTO) {
        try {
            // Configurar credenciales
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userDTO.getPassword());
            credential.setTemporary(false);

            keycloakProvider.getUserResource()
                    .get(userId)
                    .resetPassword(credential);

            // Configurar roles
            if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
                RoleMappingResource roleResource = keycloakProvider.getUserResource()
                        .get(userId)
                        .roles();

                List<RoleRepresentation> rolesToAdd = userDTO.getRoles().stream()
                        .map(role -> keycloakProvider.getRealmResource()
                                .roles()
                                .get(role.name())
                                .toRepresentation())
                        .collect(Collectors.toList());

                roleResource.realmLevel().add(rolesToAdd);
            }
        } catch (Exception e) {
            log.error("Error configuring user with ID: {}", userId, e);
            throw new KeycloakServiceException("Error configuring user", e);
        }
    }
    /**
     * Construye la respuesta de autenticación para el cliente.
     * @param tokenResponse Respuesta de Keycloak con los tokens
     * @return Respuesta formateada para el cliente
     */
    private AuthResponse buildAuthResponse(AccessTokenResponse tokenResponse) {
        return AuthResponse.builder()
                .accessToken(tokenResponse.getToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .expiresIn(tokenResponse.getExpiresIn())
                .tokenType("Bearer")
                .build();
    }
    /**
     * Crea la representación de usuario para Keycloak a partir del DTO.
     * @param userDTO Datos del usuario
     * @return Representación de usuario para Keycloak
     */
    private UserRepresentation createUserRepresentation(UserDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEnabled(userDTO.isEnabled());
        user.setEmailVerified(userDTO.isEmailVerified());
        user.setRequiredActions(Collections.emptyList());
        return user;
    }
 @Override
    public List<UserRepresentation> findAllUsers() {
        try {
            List<UserRepresentation> users = keycloakProvider.getUserResource().list();
            return users != null ? users : Collections.emptyList();
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            throw new KeycloakServiceException("Error fetching users", e);
        }
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        validacion.validateNotBlank(username, "Username cannot be blank");
        try {
            List<UserRepresentation> users = keycloakProvider.getUserResource().search(username);
            return users != null ? users : Collections.emptyList();
        } catch (NotFoundException e) {
            log.debug("User not found: {}", username);
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error searching user: {}", username, e);
            throw new KeycloakServiceException("Error searching user", e);
        }
    }

    @Override
    public String createUser(UserCreateDTO userCreateDTO) {
        validacion.validateUserCreateDTO(userCreateDTO);
        UsersResource usersResource = keycloakProvider.getUserResource();
        // Convertir UserCreateDTO a UserDTO
        UserDTO userDTO = keycloakconverter.todto(userCreateDTO);
        String username = userDTO.getUsername();
        // Verificar si el usuario ya existe antes de intentar crearlo
        if (userExists(usersResource, username)) {
            log.warn("User creation failed - already exists: {}", username);
            throw new KeycloakServiceException("User already exists");
        }
        // Crear la representación del usuario en Keycloak
        UserRepresentation userRepresentation = createUserRepresentation(userDTO);
        Response response = usersResource.create(userRepresentation);
        validacion.validateUserCreation(response);

        String userId = findUserByUsername(userDTO.getUsername()).getId();
        configureUser(userId, userDTO);
        log.info("User created successfully: {}", userDTO.getUsername());
        return userId;
    }
    @Override
    public void deleteUser(String userId) {
        validacion.validateNotBlank(userId, validacion.USER_ID_BLANK_MSG);
        try {
            keycloakProvider.getUserResource().get(userId).remove();
            log.info("User deleted successfully: {}", userId);
        } catch (NotFoundException e) {
            log.warn("User not found for deletion: {}", userId);
            throw new KeycloakServiceException(validacion.USER_NOT_FOUND_MSG + userId, e);
        } catch (Exception e) {
            log.error("Error deleting user: {}", userId, e);
            throw new KeycloakServiceException("Error deleting user", e);
        }
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        validacion.validateNotBlank(userId, validacion.USER_ID_BLANK_MSG);

        try {
            UserRepresentation user = keycloakProvider.getUserResource()
                    .get(userId)
                    .toRepresentation();

            if (user == null) {
                throw new KeycloakServiceException(validacion.USER_NOT_FOUND_MSG + userId);
            }

            log.debug("Retrieved user by ID: {}", userId);
            return user;
        } catch (NotFoundException e) {
            log.warn(validacion.USER_NOT_FOUND_MSG + userId);
            throw new KeycloakServiceException(validacion.USER_NOT_FOUND_MSG + userId, e);
        } catch (Exception e) {
            log.error("Error retrieving user with ID: {}", userId, e);
            throw new KeycloakServiceException("Error retrieving user", e);
        }
    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {
        validacion.validateNotBlank(userId, validacion.USER_ID_BLANK_MSG);
        validacion.validateUserDTO(userDTO);

        try {
            UserResource userResource = keycloakProvider.getUserResource().get(userId);
            UserRepresentation user = createUserRepresentation(userDTO);
            userResource.update(user);
            log.info("User updated successfully: {}", userId);
        } catch (NotFoundException e) {
            log.warn(validacion.USER_NOT_FOUND_MSG + userId);
            throw new KeycloakServiceException(validacion.USER_NOT_FOUND_MSG + userId, e);
        } catch (Exception e) {
            log.error("Error updating user: {}", userId, e);
            throw new KeycloakServiceException("Error updating user", e);
        }
    }
    @Override
    public AuthResponse refreshToken(String refreshToken) {
        validacion.validateNotBlank(refreshToken, "Refresh token cannot be blank");
        try {
            Keycloak keycloakInstance = keycloakProvider.getRefreshTokenInstance(refreshToken);
            AccessTokenResponse tokenResponse = keycloakInstance.tokenManager().refreshToken();
            return buildAuthResponse(tokenResponse);
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            throw new KeycloakServiceException("Invalid refresh token", e);
        }
    }
    @Override
    public AuthResponse login(String username, String password) {
        validacion.validateCredentials(username, password);
        // Obtener tokens usando el flujo de password grant
        try {
            Keycloak keycloakInstance = keycloakProvider.getUserKeycloakInstance(username, password);
            AccessTokenResponse tokenResponse = keycloakInstance.tokenManager().getAccessToken();
            return buildAuthResponse(tokenResponse);
        } catch (Exception e) {
            log.error("Login failed for user: {}", username);
            throw new KeycloakServiceException("Invalid credentials", e);
        }
    }

    @Override
    public UserRepresentation findUserByUsername(String username) throws KeycloakServiceException {
        validacion.validateNotBlank(username, "Username cannot be blank");

        // La búsqueda exacta (true) devuelve máximo 1 resultado
        try {
            List<UserRepresentation> users = keycloakProvider.getUserResource().search(username, true);
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            log.error("Error finding user by username: {}", username, e);
            throw new KeycloakServiceException("Error finding user", e);
        }
    }
}
