package bo.edu.ucb.microservicios.core.dashboard.controller;

import bo.edu.ucb.microservicios.core.dashboard.dto.PerformanceDTO;
import bo.edu.ucb.microservicios.core.dashboard.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/performance")
@Tag(name = "Performance", description = "API para gestionar el rendimiento de los niños")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping
    @Operation(summary = "Registrar datos de rendimiento")
    public ResponseEntity<PerformanceDTO> savePerformance(@RequestBody PerformanceDTO performanceDTO) {
        PerformanceDTO savedPerformance = performanceService.savePerformance(performanceDTO);
        return ResponseEntity.ok(savedPerformance);
    }

    @GetMapping("/child/{childId}")
    @Operation(summary = "Obtener datos de rendimiento por niño")
    public ResponseEntity<List<PerformanceDTO>> getPerformanceByChildId(@PathVariable String childId) {
        List<PerformanceDTO> performances = performanceService.getPerformanceByChildId(childId);
        if (performances.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "Obtener datos de rendimiento por juego con información del juego")
    public ResponseEntity<Map<String, Object>> getPerformanceByGameId(@PathVariable String gameId) {
        Map<String, Object> response = performanceService.getPerformanceByGameId(gameId);
        if (((List<?>) response.get("performances")).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas generales de rendimiento")
    public ResponseEntity<Map<String, Object>> getPerformanceStats() {
        Map<String, Object> stats = performanceService.getPerformanceStats();
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/child/{childId}")
    @Operation(summary = "Eliminar datos de rendimiento por niño")
    public ResponseEntity<Map<String, String>> deletePerformanceByChildId(@PathVariable String childId) {
        boolean wasDeleted = performanceService.deletePerformanceByChildId(childId);
        
        if (wasDeleted) {
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Datos del niño " + childId + " eliminados exitosamente"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", "error",
                "message", "No se encontraron datos para el niño " + childId
            ));
        }
    }
}