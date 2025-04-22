package com.example.keycloak.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Datos completos de usuario para creación por gerente")
public class AuthResponse {
    @Schema(description = "Token de acceso JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Token de refresco", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Tipo de token", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Tiempo de expiración en segundos", example = "3600")
    private long expiresIn;

    @Schema(description = "Ámbito del token (scope)", example = "openid profile email", required = false)
    private String scope;
    // Static factory method for easier construction
    public static AuthResponse of(String accessToken, String refreshToken, long expiresIn) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .build();
    }

    // Static factory method with scope
    public static AuthResponse of(String accessToken, String refreshToken, long expiresIn, String scope) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .scope(scope)
                .build();
    }
}
