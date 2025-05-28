package bo.edu.ucb.microservicios.core.juego.repository;

import bo.edu.ucb.microservicios.core.juego.entity.RespuestaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaUsuarioRepository extends JpaRepository<RespuestaUsuario, Long> {
    List<RespuestaUsuario> findByUsuarioId(Long usuarioId);
    List<RespuestaUsuario> findByUsuarioIdAndJuego_NivelDificultad(Long usuarioId, Integer nivelDificultad);
} 