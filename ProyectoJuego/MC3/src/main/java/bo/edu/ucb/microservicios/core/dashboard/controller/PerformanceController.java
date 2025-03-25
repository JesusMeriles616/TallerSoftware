package bo.edu.ucb.microservicios.core.dashboard.controller;

import bo.edu.ucb.microservicios.core.dashboard.dto.PerformanceDTO;
import bo.edu.ucb.microservicios.core.dashboard.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "Obtener datos de rendimiento por juego")
    public ResponseEntity<List<PerformanceDTO>> getPerformanceByGameId(@PathVariable String gameId) {
        List<PerformanceDTO> performances = performanceService.getPerformanceByGameId(gameId);
        if (performances.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(performances);
    }

    @DeleteMapping("/child/{childId}")
    @Operation(summary = "Eliminar datos de rendimiento por niño")
    public ResponseEntity<Void> deletePerformanceByChildId(@PathVariable String childId) {
        performanceService.deletePerformanceByChildId(childId);
        return ResponseEntity.noContent().build();
    }
}
