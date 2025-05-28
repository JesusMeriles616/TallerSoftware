package bo.edu.ucb.microservicios.core.juego.controller;

import bo.edu.ucb.microservicios.core.juego.dto.JuegoDTO;
import bo.edu.ucb.microservicios.core.juego.dto.RespuestaUsuarioDTO;
import bo.edu.ucb.microservicios.core.juego.service.JuegoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/juegos")
@Tag(name = "Juego Controller", description = "API para el juego Stroop Test")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @GetMapping("/health")
    @Operation(summary = "Verificar el estado del servicio")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "El servicio de juegos está funcionando correctamente");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nivel/{nivelDificultad}")
    @Operation(summary = "Obtener un juego aleatorio para un nivel de dificultad")
    public ResponseEntity<?> obtenerJuegoAleatorio(@PathVariable Integer nivelDificultad) {
        if (nivelDificultad < 1 || nivelDificultad > 3) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Nivel de dificultad inválido");
            error.put("mensaje", "El nivel de dificultad debe ser: 1 (Fácil), 2 (Medio) o 3 (Difícil)");
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok(juegoService.obtenerJuegoAleatorio(nivelDificultad));
    }

    @PostMapping("/respuesta")
    @Operation(summary = "Guardar la respuesta de un usuario")
    public ResponseEntity<RespuestaUsuarioDTO> guardarRespuesta(@RequestBody RespuestaUsuarioDTO respuestaDTO) {
        return new ResponseEntity<>(juegoService.guardarRespuesta(respuestaDTO), HttpStatus.CREATED);
    }

    @GetMapping("/respuestas/usuario/{usuarioId}")
    @Operation(summary = "Obtener todas las respuestas de un usuario")
    public ResponseEntity<List<RespuestaUsuarioDTO>> obtenerRespuestasUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(juegoService.obtenerRespuestasUsuario(usuarioId));
    }

    @GetMapping("/respuestas/usuario/{usuarioId}/nivel/{nivelDificultad}")
    @Operation(summary = "Obtener las respuestas de un usuario para un nivel específico")
    public ResponseEntity<?> obtenerRespuestasUsuarioPorNivel(
            @PathVariable Long usuarioId,
            @PathVariable Integer nivelDificultad) {
        if (nivelDificultad < 1 || nivelDificultad > 3) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Nivel de dificultad inválido");
            error.put("mensaje", "El nivel de dificultad debe ser: 1 (Fácil), 2 (Medio) o 3 (Difícil)");
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok(juegoService.obtenerRespuestasUsuarioPorNivel(usuarioId, nivelDificultad));
    }
} 