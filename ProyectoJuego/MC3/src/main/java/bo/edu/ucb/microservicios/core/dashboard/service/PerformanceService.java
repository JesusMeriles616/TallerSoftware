package bo.edu.ucb.microservicios.core.dashboard.service;

import bo.edu.ucb.microservicios.core.dashboard.dto.PerformanceDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private final List<PerformanceDTO> performanceData = new ArrayList<>();

    public PerformanceDTO savePerformance(PerformanceDTO performanceDTO) {
        performanceData.add(performanceDTO); // Simula el guardado
        return performanceDTO;
    }

    public List<PerformanceDTO> getPerformanceByChildId(String childId) {
        return performanceData.stream()
                .filter(performance -> performance.getChildId().equals(childId))
                .collect(Collectors.toList());
    }

    public List<PerformanceDTO> getPerformanceByGameId(String gameId) {
        return performanceData.stream()
                .filter(performance -> performance.getGameId().equals(gameId))
                .collect(Collectors.toList());
    }

    public void deletePerformanceByChildId(String childId) {
        performanceData.removeIf(performance -> performance.getChildId().equals(childId)); // Simula la eliminación
    }
}
