package bo.edu.ucb.microservicios.core.dashboard.service;

import bo.edu.ucb.microservicios.core.dashboard.dto.PerformanceDTO;
import bo.edu.ucb.microservicios.core.dashboard.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private final List<PerformanceDTO> performanceData = new ArrayList<>();

    private final List<Map<String, Object>> simulatedGames = List.of(
        new HashMap<>() {{
            put("gameId", "456");
            put("name", "Math Adventure");
            put("difficulty", "Medium");
        }},
        new HashMap<>() {{
            put("gameId", "789");
            put("name", "Word Puzzle");
            put("difficulty", "Easy");
        }},
        new HashMap<>() {{
            put("gameId", "101");
            put("name", "Science Quiz");
            put("difficulty", "Hard");
        }}
    );

    public PerformanceDTO savePerformance(PerformanceDTO performanceDTO) {
        performanceData.add(performanceDTO);
        return performanceDTO;
    }

    public List<PerformanceDTO> getPerformanceByChildId(String childId) {
        return performanceData.stream()
                .filter(performance -> performance.getChildId().equals(childId))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getPerformanceByGameId(String gameId) {

        Map<String, Object> gameInfo = simulatedGames.stream()
                .filter(game -> game.get("gameId").equals(gameId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));

        List<PerformanceDTO> performances = performanceData.stream()
                .filter(performance -> performance.getGameId().equals(gameId))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("gameInfo", gameInfo);
        response.put("performances", performances);

        return response;
    }

    public Map<String, Object> getPerformanceStats() {
        if (performanceData.isEmpty()) {
            throw new ResourceNotFoundException("No hay datos de rendimiento disponibles");
        }

        int totalRecords = performanceData.size();
        int totalCorrect = performanceData.stream().mapToInt(PerformanceDTO::getCorrectAnswers).sum();
        int totalWrong = performanceData.stream().mapToInt(PerformanceDTO::getWrongAnswers).sum();
        double accuracy = (double) totalCorrect / (totalCorrect + totalWrong) * 100;

        Map<String, Long> gamesCount = performanceData.stream()
            .collect(Collectors.groupingBy(
                PerformanceDTO::getGameId,
                Collectors.counting()
            ));

        return Map.of(
            "totalRecords", totalRecords,
            "totalCorrectAnswers", totalCorrect,
            "totalWrongAnswers", totalWrong,
            "averageAccuracy", Math.round(accuracy * 100) / 100.0,
            "gamesPlayed", gamesCount.keySet().size(),
            "performanceByGame", gamesCount
        );
    }

    public boolean deletePerformanceByChildId(String childId) {
        return performanceData.removeIf(performance -> performance.getChildId().equals(childId));
    }
}
