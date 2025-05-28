package bo.edu.ucb.microservicios.core.juego.service;

import bo.edu.ucb.microservicios.core.juego.dto.JuegoDTO;
import bo.edu.ucb.microservicios.core.juego.dto.RespuestaUsuarioDTO;
import bo.edu.ucb.microservicios.core.juego.entity.Juego;
import bo.edu.ucb.microservicios.core.juego.entity.RespuestaUsuario;
import bo.edu.ucb.microservicios.core.juego.exception.ResourceNotFoundException;
import bo.edu.ucb.microservicios.core.juego.repository.JuegoRepository;
import bo.edu.ucb.microservicios.core.juego.repository.RespuestaUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private RespuestaUsuarioRepository respuestaUsuarioRepository;

    private final String[] colores = {"ROJO", "AZUL", "VERDE", "AMARILLO", "NEGRO", "MORADO", "NARANJA"};
    private final Random random = new Random();

    public List<JuegoDTO> obtenerTodosLosJuegos() {
        return juegoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public JuegoDTO obtenerJuegoPorId(Long id) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con id: " + id));
        return convertirADTO(juego);
    }

    public JuegoDTO crearJuego(JuegoDTO juegoDTO) {
        Juego juego = new Juego();
        juego.setPalabra(juegoDTO.getPalabra());
        juego.setColorPalabra(juegoDTO.getColorPalabra());
        juego.setColorCorrecto(juegoDTO.getColorCorrecto());
        juego.setNivelDificultad(juegoDTO.getNivelDificultad());
        juego.setTiempoRespuesta(juegoDTO.getTiempoRespuesta());
        juego.setOpcion1(juegoDTO.getOpcion1());
        juego.setOpcion2(juegoDTO.getOpcion2());
        juego.setOpcion3(juegoDTO.getOpcion3());
        juego.setOpcion4(juegoDTO.getOpcion4());
        juego.setFechaCreacion(LocalDateTime.now());
        juego.setActivo(true);
        juego.setDescripcion("Juego Stroop Test - " + juegoDTO.getPalabra() + " en color " + juegoDTO.getColorPalabra());
        
        Juego juegoGuardado = juegoRepository.save(juego);
        return convertirADTO(juegoGuardado);
    }

    public JuegoDTO actualizarJuego(Long id, JuegoDTO juegoDTO) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con id: " + id));

        juego.setPalabra(juegoDTO.getPalabra());
        juego.setColorPalabra(juegoDTO.getColorPalabra());
        juego.setColorCorrecto(juegoDTO.getColorCorrecto());
        juego.setNivelDificultad(juegoDTO.getNivelDificultad());
        juego.setTiempoRespuesta(juegoDTO.getTiempoRespuesta());
        juego.setOpcion1(juegoDTO.getOpcion1());
        juego.setOpcion2(juegoDTO.getOpcion2());
        juego.setOpcion3(juegoDTO.getOpcion3());
        juego.setOpcion4(juegoDTO.getOpcion4());
        juego.setActivo(juegoDTO.getActivo());

        Juego juegoActualizado = juegoRepository.save(juego);
        return convertirADTO(juegoActualizado);
    }

    public void eliminarJuego(Long id) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con id: " + id));
        juego.setActivo(false);
        juegoRepository.save(juego);
    }

    public List<JuegoDTO> obtenerJuegosActivos() {
        return juegoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<JuegoDTO> obtenerJuegosPorDificultad(Integer nivelDificultad) {
        return juegoRepository.findByNivelDificultad(nivelDificultad).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public JuegoDTO obtenerJuegoAleatorio(Integer nivelDificultad) {
        // Obtener tiempo de respuesta según nivel
        int tiempoRespuesta = switch (nivelDificultad) {
            case 1 -> 5000; // 5 segundos para nivel fácil
            case 2 -> 3000; // 3 segundos para nivel medio
            case 3 -> 2000; // 2 segundos para nivel difícil
            default -> throw new IllegalArgumentException("Nivel de dificultad no válido");
        };

        // Generar palabra y color aleatorios
        String palabra = colores[random.nextInt(colores.length)];
        String colorPalabra = colores[random.nextInt(colores.length)];
        
        // Asegurarse de que el color de la palabra sea diferente al color que representa
        while (colorPalabra.equals(palabra)) {
            colorPalabra = colores[random.nextInt(colores.length)];
        }

        // Generar opciones
        String[] opciones = new String[4];
        opciones[0] = colorPalabra; // La respuesta correcta
        for (int i = 1; i < 4; i++) {
            String opcion;
            do {
                opcion = colores[random.nextInt(colores.length)];
            } while (Arrays.asList(opciones).contains(opcion) || opcion.equals(palabra));
            opciones[i] = opcion;
        }

        // Mezclar las opciones
        for (int i = opciones.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String temp = opciones[i];
            opciones[i] = opciones[j];
            opciones[j] = temp;
        }

        // Crear y guardar el juego
        Juego juego = new Juego();
        juego.setNombre("Juego Stroop " + nivelDificultad);
        juego.setPalabra(palabra);
        juego.setColorPalabra(colorPalabra);
        juego.setColorCorrecto(colorPalabra);
        juego.setNivelDificultad(nivelDificultad);
        juego.setTiempoRespuesta(tiempoRespuesta);
        juego.setOpcion1(opciones[0]);
        juego.setOpcion2(opciones[1]);
        juego.setOpcion3(opciones[2]);
        juego.setOpcion4(opciones[3]);
        juego.setActivo(true);
        juego.setFechaCreacion(LocalDateTime.now());
        juego.setDescripcion("Juego Stroop Test - " + palabra + " en color " + colorPalabra);
        juego.setPuntajeMaximo(100); // Puntaje máximo por defecto

        Juego juegoGuardado = juegoRepository.save(juego);
        return convertirADTO(juegoGuardado);
    }

    public RespuestaUsuarioDTO guardarRespuesta(RespuestaUsuarioDTO respuestaDTO) {
        Juego juego = juegoRepository.findById(respuestaDTO.getJuegoId())
                .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));

        RespuestaUsuario respuesta = new RespuestaUsuario();
        respuesta.setUsuarioId(respuestaDTO.getUsuarioId());
        respuesta.setJuego(juego);
        respuesta.setRespuestaSeleccionada(respuestaDTO.getRespuestaSeleccionada());
        respuesta.setEsCorrecta(respuestaDTO.getRespuestaSeleccionada().equals(juego.getColorCorrecto()));
        respuesta.setTiempoRespuesta(respuestaDTO.getTiempoRespuesta());
        respuesta.setFechaRespuesta(LocalDateTime.now());

        RespuestaUsuario respuestaGuardada = respuestaUsuarioRepository.save(respuesta);
        return convertirRespuestaADTO(respuestaGuardada);
    }

    public List<RespuestaUsuarioDTO> obtenerRespuestasUsuario(Long usuarioId) {
        return respuestaUsuarioRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirRespuestaADTO)
                .collect(Collectors.toList());
    }

    public List<RespuestaUsuarioDTO> obtenerRespuestasUsuarioPorNivel(Long usuarioId, Integer nivelDificultad) {
        return respuestaUsuarioRepository.findByUsuarioIdAndJuego_NivelDificultad(usuarioId, nivelDificultad).stream()
                .map(this::convertirRespuestaADTO)
                .collect(Collectors.toList());
    }

    private JuegoDTO convertirADTO(Juego juego) {
        return new JuegoDTO(
            juego.getId(),
            juego.getNombre(),
            juego.getPalabra(),
            juego.getColorPalabra(),
            juego.getColorCorrecto(),
            juego.getNivelDificultad(),
            juego.getTiempoRespuesta(),
            juego.getOpcion1(),
            juego.getOpcion2(),
            juego.getOpcion3(),
            juego.getOpcion4(),
            juego.getActivo(),
            juego.getFechaCreacion(),
            juego.getDescripcion(),
            juego.getPuntajeMaximo()
        );
    }

    private RespuestaUsuarioDTO convertirRespuestaADTO(RespuestaUsuario respuesta) {
        return new RespuestaUsuarioDTO(
            respuesta.getId(),
            respuesta.getUsuarioId(),
            respuesta.getJuego().getId(),
            respuesta.getRespuestaSeleccionada(),
            respuesta.getEsCorrecta(),
            respuesta.getTiempoRespuesta(),
            respuesta.getFechaRespuesta()
        );
    }
} 