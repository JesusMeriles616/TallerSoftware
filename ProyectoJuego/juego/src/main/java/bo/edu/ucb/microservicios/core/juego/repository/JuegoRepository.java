package bo.edu.ucb.microservicios.core.juego.repository;

import bo.edu.ucb.microservicios.core.juego.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {
    List<Juego> findByActivoTrue();
    List<Juego> findByNivelDificultad(Integer nivelDificultad);
    List<Juego> findByPalabraContainingIgnoreCase(String palabra);
} 