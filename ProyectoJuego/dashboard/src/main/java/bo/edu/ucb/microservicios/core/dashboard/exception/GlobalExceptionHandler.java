package bo.edu.ucb.microservicios.core.dashboard.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @Operation(
        summary = "Maneja errores de recurso no encontrado",
        description = "Se activa cuando el sistema no puede encontrar el recurso solicitado",
        responses = {
            @ApiResponse(
                responseCode = "404",
                description = "Recurso no encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                        value = "{\"error\": \"Recurso no encontrado\", \"detalle\": \"Niño con ID 123 no existe\"}"
                    )
                )
            )
        }
    )
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Recurso no encontrado");
        response.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Operation(
        summary = "Maneja errores de validación",
        description = "Se activa cuando los datos de entrada no cumplen con las validaciones requeridas",
        responses = {
            @ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                        value = "{\"campo1\": \"No debe estar vacío\", \"campo2\": \"Debe ser un número positivo\"}"
                    )
                )
            )
        }
    )
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    @Operation(
        summary = "Maneja errores internos del servidor",
        description = "Se activa cuando ocurre un error inesperado en el servidor",
        responses = {
            @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                        value = "{\"error\": \"Error interno del servidor\", \"detalle\": \"Mensaje de error específico\"}"
                    )
                )
            )
        }
    )
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error interno del servidor");
        response.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}