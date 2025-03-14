package bo.edu.ucb.microservicios.core.product.controller;

import com.ejemplo.dashboard.dto.PerformanceDTO;
import com.ejemplo.dashboard.model.Performance;
import com.ejemplo.dashboard.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/performance")
@Tag(name = "Performance", description = "API para gestionar el rendimiento de los niños")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @PostMapping
    @Operation(summary = "Registrar datos de rendimiento")
    public Performance savePerformance(@RequestBody PerformanceDTO performanceDTO) {
        return performanceService.savePerformance(performanceDTO);
    }

    @GetMapping("/child/{childId}")
    @Operation(summary = "Obtener datos de rendimiento por niño")
    public List<Performance> getPerformanceByChildId(@PathVariable String childId) {
        return performanceService.getPerformanceByChildId(childId);
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "Obtener datos de rendimiento por juego")
    public List<Performance> getPerformanceByGameId(@PathVariable String gameId) {
        return performanceService.getPerformanceByGameId(gameId);
    }

    @DeleteMapping("/child/{childId}")
    @Operation(summary = "Eliminar datos de rendimiento por niño")
    public void deletePerformanceByChildId(@PathVariable String childId) {
        performanceService.deletePerformanceByChildId(childId);
    }
}