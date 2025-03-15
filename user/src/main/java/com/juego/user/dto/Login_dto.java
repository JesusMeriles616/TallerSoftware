package com.juego.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de login")
public class Login_dto {
    @Schema(description = "Email del usuario", example = "juan.perez@gmail.com")
    private String email;
    @Schema(description = "Contraseña del usuario", example = "123456")
    private String password;
}
