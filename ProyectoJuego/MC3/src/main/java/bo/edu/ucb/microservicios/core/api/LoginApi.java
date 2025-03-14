package bo.edu.ucb.microservicios.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.edu.ucb.microservicios.core.product.dto.UserDto;
import bo.edu.ucb.microservicios.core.product.service.LoginService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen
public class LoginApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginApi.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody UserDto loginRequest) {
        try {
            LOGGER.info("Iniciando proceso de login para usuario: {}", loginRequest.getCorreo());

            var tokenResponse = loginService.login(loginRequest);
            if (tokenResponse != null) {
                return ResponseEntity.ok(tokenResponse);
            } else {
                return ResponseEntity.badRequest().body("Credenciales inválidas");
            }

        } catch (Exception e) {
            LOGGER.error("Error en el proceso de login: ", e);
            return ResponseEntity.internalServerError().body("Error en el servidor");
        }
    }

        @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            String accessToken = token.replace("Bearer ", "");
            // Lógica para invalidar el token (dependiendo de Keycloak o sesión manual)
            LOGGER.info("Usuario deslogueado correctamente.");
            return ResponseEntity.ok("Logout exitoso");
        } catch (Exception e) {
            LOGGER.error("Error al hacer logout: ", e);
            return ResponseEntity.internalServerError().body("Error en el servidor");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String accessToken = token.replace("Bearer ", "");
            // Lógica para validar el token (puedes agregar un servicio para esto)
            return ResponseEntity.ok("Token válido");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
    }
}
