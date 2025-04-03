package bo.edu.ucb.microservicios.core.dashboard.Controller;

import bo.edu.ucb.microservicios.core.dashboard.Entity.PerformanceEntity;
import bo.edu.ucb.microservicios.core.dashboard.Repository.PerformanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PerformanceRepository repository;

    @Test
    void testCreatePerformance() throws Exception {
        PerformanceEntity performance = new PerformanceEntity("child1", "game1", 5, 2);
        
        mockMvc.perform(post("/api/performance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(performance)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.childId", is("child1")))
                .andExpect(jsonPath("$.gameId", is("game1")));
    }

    @Test
    void testGetPerformanceById() throws Exception {
        PerformanceEntity saved = repository.save(new PerformanceEntity("child1", "game1", 3, 1));
        
        mockMvc.perform(get("/api/performance/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId())));
    }

    @Test
    void testGetPerformanceByChildId() throws Exception {
        repository.save(new PerformanceEntity("child1", "game1", 3, 1));
        
        mockMvc.perform(get("/api/performance/child/child1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
} 