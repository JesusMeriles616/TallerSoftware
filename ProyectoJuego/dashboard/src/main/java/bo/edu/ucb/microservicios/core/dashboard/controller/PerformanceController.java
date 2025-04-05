package bo.edu.ucb.microservicios.core.dashboard.Controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.edu.ucb.microservicios.core.dashboard.Entity.PerformanceEntity;
import bo.edu.ucb.microservicios.core.dashboard.Repository.PerformanceRepository;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired
    private PerformanceRepository performanceRepository;

    @PostMapping
    public ResponseEntity<PerformanceEntity> createPerformance(@RequestBody PerformanceEntity performance) {
        performance.setDate(new Date());
        PerformanceEntity saved = performanceRepository.save(performance);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceEntity> getPerformanceById(@PathVariable String id) {
        return performanceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<List<PerformanceEntity>> getPerformanceByChildId(@PathVariable String childId) {
        return ResponseEntity.ok(performanceRepository.findByChildId(childId));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<PerformanceEntity>> getPerformanceByGameId(@PathVariable String gameId) {
        return ResponseEntity.ok(performanceRepository.findByGameId(gameId));
    }

    @GetMapping("/child/{childId}/game/{gameId}")
    public ResponseEntity<List<PerformanceEntity>> getPerformanceByChildAndGame(
            @PathVariable String childId,
            @PathVariable String gameId) {
        return ResponseEntity.ok(performanceRepository.findByChildIdAndGameId(childId, gameId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PerformanceEntity>> getPerformanceByDateRange(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        return ResponseEntity.ok(performanceRepository.findByDateBetween(startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceEntity> updatePerformance(
            @PathVariable String id,
            @RequestBody PerformanceEntity performance) {
        return performanceRepository.findById(id)
                .map(existingPerformance -> {
                    existingPerformance.setChildId(performance.getChildId());
                    existingPerformance.setGameId(performance.getGameId());
                    existingPerformance.setCorrectAnswers(performance.getCorrectAnswers());
                    existingPerformance.setWrongAnswers(performance.getWrongAnswers());
                    return ResponseEntity.ok(performanceRepository.save(existingPerformance));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable String id) {
        return performanceRepository.findById(id)
                .map(performance -> {
                    performanceRepository.delete(performance);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}