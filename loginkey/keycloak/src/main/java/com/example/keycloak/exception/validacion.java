package com.example.keycloak.exception;

import io.micrometer.common.util.StringUtils;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;

import com.example.keycloak.dto.UserCreateDTO;
import com.example.keycloak.dto.UserDTO;
import com.example.keycloak.util.KeycloakServiceException;
/**
 * Clase utilitaria para validaciones comunes en el sistema
 */
@AllArgsConstructor
public class validacion {
    // Mensajes de error constantes
    public static final String USER_NOT_FOUND_MSG = "User not found with ID: ";
    public static final String USERNAME_BLANK_MSG = "Username cannot be blank";
    public static final String USER_ID_BLANK_MSG = "User ID cannot be blank";
    public static final String USER_DTO_NULL_MSG = "UserDTO cannot be null";
    public static final String USER_CREATE_DTO_NULL_MSG = "UserCreateDTO cannot be null";
    public static final String USERNAME_REQUIRED_MSG = "Username is required";
    public static final String PASSWORD_REQUIRED_MSG = "Password is required";
    public static final String INVALID_AUTH_RESPONSE_MSG = "Invalid authentication response";



    /**
     * Valida la respuesta de creación de usuario en Keycloak
     * @param response Respuesta HTTP de Keycloak
     * @throws KeycloakServiceException si el estado no es CREATED (201)
     */
    public static void validateUserCreation(Response response) {
        if (response == null) {
            throw new KeycloakServiceException("Null response from Keycloak");
        }

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            String errorMsg = String.format("User creation failed. Status: %d - Reason: %s",
                    response.getStatus(),
                    response.getStatusInfo().getReasonPhrase());
            throw new KeycloakServiceException(errorMsg);
        }
    }

    /**
     * Valida que una cadena no sea nula o vacía
     * @param value Cadena a validar
     * @param message Mensaje de error personalizado
     * @throws KeycloakServiceException si el valor es nulo o vacío
     */
    public static void validateNotBlank(String value, String message) {
        if (StringUtils.isBlank(value)) {
            throw new KeycloakServiceException(message);
        }
    }

    /**
     * Valida la respuesta de autenticación
     * @param tokenResponse Respuesta con tokens
     * @throws KeycloakServiceException si la respuesta o el token son nulos
     */
    public static void validateTokenResponse(AccessTokenResponse tokenResponse) {
        if (tokenResponse == null || tokenResponse.getToken() == null) {
            throw new KeycloakServiceException(INVALID_AUTH_RESPONSE_MSG);
        }
    }

    /**
     * Valida un UserDTO
     * @param userDTO Objeto a validar
     * @throws KeycloakServiceException si el DTO es nulo o faltan campos requeridos
     */
    public static void validateUserDTO(UserDTO userDTO) {
        if (userDTO == null) {
            throw new KeycloakServiceException(USER_DTO_NULL_MSG);
        }
        validateNotBlank(userDTO.getUsername(), USERNAME_REQUIRED_MSG);
    }

    /**
     * Valida un UserCreateDTO
     * @param userCreateDTO Objeto a validar
     * @throws KeycloakServiceException si el DTO es nulo o faltan campos requeridos
     */
    public static void validateUserCreateDTO(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            throw new KeycloakServiceException(USER_CREATE_DTO_NULL_MSG);
        }
        validateNotBlank(userCreateDTO.getUsername(), USERNAME_REQUIRED_MSG);
        validateNotBlank(userCreateDTO.getPassword(), PASSWORD_REQUIRED_MSG);
    }

    /**
     * Valida credenciales de usuario
     * @param username Nombre de usuario
     * @param password Contraseña
     * @throws KeycloakServiceException si alguno de los campos es nulo o vacío
     */
    public static void validateCredentials(String username, String password) {
        validateNotBlank(username, USERNAME_REQUIRED_MSG);
        validateNotBlank(password, PASSWORD_REQUIRED_MSG);
    }

    /**
     * Extrae el ID de usuario de la respuesta de Keycloak
     * @param response Respuesta HTTP
     * @return ID del usuario creado
     * @throws KeycloakServiceException si no se puede extraer el ID
     */
    public static String extractUserId(Response response) {
        try {
            String location = response.getHeaderString("Location");
            if (StringUtils.isBlank(location)) {
                throw new KeycloakServiceException("Empty location header in response");
            }
            return location.substring(location.lastIndexOf('/') + 1);
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to extract user ID from response", e);
        }
    }
}
