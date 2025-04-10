package bo.edu.ucb.microservicios.core.product.service;

import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import bo.edu.ucb.microservicios.core.product.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Service
@Tag(name = "Login Service", description = "Service layer for handling user authentication")
public class LoginService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    // Credenciales de prueba
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "password123";

    @Operation(summary = "Process login request", description = "Authenticates user credentials and returns access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login", 
            content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AccessTokenResponse login(
        @Parameter(description = "Login credentials", required = true) UserDto loginRequest) {
        LOGGER.info("Procesando solicitud de login para usuario: {}", loginRequest.getCorreo());

        if (loginRequest.getCorreo() == null || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("El correo y la contraseña son requeridos");
        }

        if (TEST_EMAIL.equals(loginRequest.getCorreo()) && TEST_PASSWORD.equals(loginRequest.getPassword())) {
            AccessTokenResponse response = new AccessTokenResponse();
            response.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
            response.setExpiresIn(300);
            response.setRefreshToken("refresh_token_simulado");
            response.setRefreshExpiresIn(1800);
            response.setTokenType("Bearer");

            LOGGER.info("Login exitoso para usuario: {}", loginRequest.getCorreo());
            return response;
        }

        LOGGER.warn("Intento de login fallido para usuario: {}", loginRequest.getCorreo());
        return null;
    }
}
