package bo.edu.ucb.microservicios.core.dashboard.repository;

import com.ejemplo.dashboard.model.Performance;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PerformanceRepository extends MongoRepository<Performance, String> {
    List<Performance> findByChildId(String childId);
    List<Performance> findByGameId(String gameId);
}