package bo.edu.ucb.microservicios.core.product.controller;

import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.edu.ucb.microservicios.core.product.dto.UserDto;
import bo.edu.ucb.microservicios.core.product.service.LoginService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto loginRequest) {
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
}
