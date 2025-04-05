package bo.edu.ucb.microservicios.core.dashboard.Repository;

import bo.edu.ucb.microservicios.core.dashboard.Entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<PerformanceEntity, String> {
    
    List<PerformanceEntity> findByChildId(String childId);
    
    List<PerformanceEntity> findByGameId(String gameId);
    
    
    List<PerformanceEntity> findByChildIdAndGameId(String childId, String gameId);
    
    List<PerformanceEntity> findByDateBetween(Date startDate, Date endDate);
}
