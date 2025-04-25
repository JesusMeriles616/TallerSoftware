package com.example.keycloak.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import com.example.keycloak.emu.RoleType;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Datos necesarios para crear un usuario")
public class UserCreateDTO {
    @Schema(description = "Nombre de usuario", example = "usuario1", required = true)
    private String username;

    @Schema(description = "Correo electrónico", example = "usuario@example.com", required = true)
    private String email;

    @Schema(description = "Nombre", example = "Juan")
    private String firstName;

    @Schema(description = "Apellido", example = "Pérez")
    private String lastName;

    @Schema(description = "Contraseña", example = "Pass123!", required = true)
    private String password;
    @Schema(description = "Roles asignados",
            example = "[\"ADMINISTRADOR\"]",
            allowableValues = {"NIÑO", "ADMINISTRADOR", "PADRE"})
    private Set<RoleType> roles = new HashSet<>();
    @Schema(description = "Indica si el usuario está habilitado", example = "true", defaultValue = "true")
    private boolean enabled = true;

    @Schema(description = "Indica si el email ha sido verificado", example = "false", defaultValue = "false")
    private boolean emailVerified = false;
}
