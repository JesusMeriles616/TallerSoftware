package bo.edu.ucb.microservicios.core.product.service;

import com.ejemplo.dashboard.dto.PerformanceDTO;
import com.ejemplo.dashboard.model.Performance;
import com.ejemplo.dashboard.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;

    public Performance savePerformance(PerformanceDTO performanceDTO) {
        Performance performance = new Performance();
        performance.setChildId(performanceDTO.getChildId());
        performance.setGameId(performanceDTO.getGameId());
        performance.setCorrectAnswers(performanceDTO.getCorrectAnswers());
        performance.setWrongAnswers(performanceDTO.getWrongAnswers());
        performance.setDate(new Date());
        return performanceRepository.save(performance);
    }

    public List<Performance> getPerformanceByChildId(String childId) {
        return performanceRepository.findByChildId(childId);
    }

    public List<Performance> getPerformanceByGameId(String gameId) {
        return performanceRepository.findByGameId(gameId);
    }

    public void deletePerformanceByChildId(String childId) {
        performanceRepository.deleteByChildId(childId);
    }
}