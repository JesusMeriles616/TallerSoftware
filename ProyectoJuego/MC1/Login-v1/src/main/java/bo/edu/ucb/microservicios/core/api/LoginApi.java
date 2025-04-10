package bo.edu.ucb.microservicios.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bo.edu.ucb.microservicios.core.product.dto.UserDto;
import bo.edu.ucb.microservicios.core.product.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "API endpoints for user authentication")
public class LoginApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginApi.class);
    private final LoginService loginService;

    public LoginApi(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns an access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login", 
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(
        @Parameter(description = "Email", required = true, example = "test@test.com") 
        @RequestParam String email,
        @Parameter(description = "Password", required = true, example = "password123")
        @RequestParam String password) {
        try {
            LOGGER.info("Iniciando proceso de login para usuario: {}", email);
            UserDto loginRequest = new UserDto();
            loginRequest.setCorreo(email);
            loginRequest.setPassword(password);
            var tokenResponse = loginService.login(loginRequest);
            return (tokenResponse != null) ? ResponseEntity.ok(tokenResponse) : ResponseEntity.badRequest().body("Credenciales inválidas");
        } catch (Exception e) {
            LOGGER.error("Error en el proceso de login: ", e);
            return ResponseEntity.internalServerError().body("Error en el servidor");
        }
    }

    @Operation(summary = "Logout user", description = "Invalidates the user's access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful logout"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
        @Parameter(description = "Bearer token", required = true)
        @RequestHeader("Authorization") String token) {
        try {
            LOGGER.info("Usuario deslogueado correctamente.");
            return ResponseEntity.ok("Logout exitoso");
        } catch (Exception e) {
            LOGGER.error("Error al hacer logout: ", e);
            return ResponseEntity.internalServerError().body("Error en el servidor");
        }
    }

    @Operation(summary = "Validate token", description = "Validates if the provided token is still valid")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token is valid"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    })
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
        @Parameter(description = "Bearer token", required = true)
        @RequestHeader("Authorization") String token) {
        try {
            String accessToken = token.replace("Bearer ", "");
            LOGGER.info("Validando token: {}", accessToken);
            if (accessToken.isEmpty()) {
                throw new Exception("Token vacío");
            }
            return ResponseEntity.ok("Token válido");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
    }
}
