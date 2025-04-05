package bo.edu.ucb.microservicios.core.dashboard.Repository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import bo.edu.ucb.microservicios.core.dashboard.Entity.PerformanceEntity;

@DataJpaTest
@ActiveProfiles("test")
public class PerformanceRepositoryTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Test
    public void testSaveAndFindById() {
        // Given
        PerformanceEntity performance = new PerformanceEntity();
        performance.setChildId("child1");
        performance.setGameId("game1");
        performance.setCorrectAnswers(5);
        performance.setWrongAnswers(2);

        // When
        PerformanceEntity saved = performanceRepository.save(performance);
        PerformanceEntity found = performanceRepository.findById(saved.getId()).orElse(null);

        // Then
        assertNotNull(found);
        assertEquals("child1", found.getChildId());
        assertEquals("game1", found.getGameId());
        assertEquals(5, found.getCorrectAnswers());
        assertEquals(2, found.getWrongAnswers());
    }

    @Test
    public void testFindByChildId() {
        // Given
        PerformanceEntity performance1 = new PerformanceEntity("child1", "game1", 3, 1);
        PerformanceEntity performance2 = new PerformanceEntity("child1", "game2", 4, 2);
        performanceRepository.save(performance1);
        performanceRepository.save(performance2);

        // When
        List<PerformanceEntity> results = performanceRepository.findByChildId("child1");

        // Then
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByGameId() {
        // Given
        PerformanceEntity performance1 = new PerformanceEntity("child1", "game1", 3, 1);
        PerformanceEntity performance2 = new PerformanceEntity("child2", "game1", 4, 2);
        performanceRepository.save(performance1);
        performanceRepository.save(performance2);

        // When
        List<PerformanceEntity> results = performanceRepository.findByGameId("game1");

        // Then
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByChildIdAndGameId() {
        // Given
        PerformanceEntity performance = new PerformanceEntity("child1", "game1", 3, 1);
        performanceRepository.save(performance);

        // When
        List<PerformanceEntity> results = performanceRepository.findByChildIdAndGameId("child1", "game1");

        // Then
        assertEquals(1, results.size());
        assertEquals("child1", results.get(0).getChildId());
        assertEquals("game1", results.get(0).getGameId());
    }

    @Test
    public void testFindByDateBetween() {
        // Given
        Date startDate = new Date(System.currentTimeMillis() - 100000);
        Date endDate = new Date(System.currentTimeMillis() + 100000);
        
        PerformanceEntity performance = new PerformanceEntity("child1", "game1", 3, 1);
        performanceRepository.save(performance);

        // When
        List<PerformanceEntity> results = performanceRepository.findByDateBetween(startDate, endDate);

        // Then
        assertEquals(1, results.size());
    }
} 