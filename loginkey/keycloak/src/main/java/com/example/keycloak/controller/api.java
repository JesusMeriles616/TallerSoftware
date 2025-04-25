package com.example.keycloak.controller;

import com.example.keycloak.dto.AuthResponse;
import com.example.keycloak.dto.LoginRequest;
import com.example.keycloak.dto.UserCreateDTO;
import com.example.keycloak.dto.UserDTO;
import com.example.keycloak.exception.validacion;
import com.example.keycloak.services.keycloakinterface;
import com.example.keycloak.util.KeycloakServiceException;
import org.keycloak.representations.idm.UserRepresentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Autenticación y Gestión de Usuarios", description = "Login, refresh de token y CRUD de usuarios en Keycloak")
public class api {

    private final keycloakinterface keycloakInterface;

    // --------------------
    // 🔐 1. LOGIN
    // --------------------
    @Operation(summary = "Login de usuario", description = "Autentica al usuario con username y password, devolviendo un token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/login")
   // @permitAll
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getUsername());
        validacion.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());
        AuthResponse authResponse = keycloakInterface.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }
    // --------------------
    // 🔄 2. REFRESH TOKEN
    // --------------------
    @Operation(summary = "Refrescar token", description = "Genera un nuevo token de acceso usando el token de refresco")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token refrescado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Refresh token inválido o vacío", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam("refresh_token") String refreshToken) {
        log.info("Solicitando refresh de token");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new KeycloakServiceException("El refresh token no puede estar vacío");
        }
        AuthResponse authResponse = keycloakInterface.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    // --------------------
    // 👤 3. CREAR USUARIO
    // --------------------
    @PostMapping("/register")
public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
    try {
        log.info("Creando nuevo usuario: {}", userCreateDTO.getUsername());
        validacion.validateUserCreateDTO(userCreateDTO);
        String userId = keycloakInterface.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado con ID: " + userId);
    } catch (Exception e) {
        log.error("Error al crear usuario", e);  // Esto mostrará el stacktrace completo en la consola
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
    }
}


    // --------------------
    // 📋 4. LISTAR USUARIOS
    // --------------------
    @Operation(summary = "Listar usuarios", description = "Devuelve la lista de todos los usuarios registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuarios",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRepresentation.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {
        log.info("Obteniendo lista de todos los usuarios");
        List<UserRepresentation> users = keycloakInterface.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // --------------------
    // 🔍 5. OBTENER USUARIO POR ID
    // --------------------
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve los datos del usuario dado su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRepresentation.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserRepresentation> getUserById(@PathVariable String id) {
        log.info("Buscando usuario con ID: {}", id);
        validacion.validateNotBlank(id, validacion.USER_ID_BLANK_MSG);
        UserRepresentation user = keycloakInterface.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // --------------------
    // ✏️ 6. ACTUALIZAR USUARIO
    // --------------------
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos del usuario dado su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRepresentation.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<UserRepresentation> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        log.info("Actualizando usuario con ID: {}", id);
        validacion.validateNotBlank(id, validacion.USER_ID_BLANK_MSG);
        validacion.validateUserDTO(userDTO);
        keycloakInterface.updateUser(id, userDTO);
        UserRepresentation updated = keycloakInterface.getUserById(id);
        return ResponseEntity.ok(updated);
    }

    // --------------------
    // ❌ 7. ELIMINAR USUARIO
    // --------------------
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema dado su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Eliminando usuario con ID: {}", id);
        validacion.validateNotBlank(id, validacion.USER_ID_BLANK_MSG);
        keycloakInterface.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
