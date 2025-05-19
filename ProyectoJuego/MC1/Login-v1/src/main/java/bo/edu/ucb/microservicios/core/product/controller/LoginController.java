
<<<<<<< Updated upstream
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns an access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful login", 
            content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Parameter(description = "Login credentials", required = true)
        @RequestBody UserDto loginRequest) {
        try {
            LOGGER.info("Recibida solicitud de login para usuario: {}", loginRequest.getCorreo());
            AccessTokenResponse token = loginService.login(loginRequest);
            
            if (token != null) {
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.badRequest().body("Credenciales inválidas");
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Error de validación en login: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error durante el proceso de login: ", e);
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
=======
>>>>>>> Stashed changes
