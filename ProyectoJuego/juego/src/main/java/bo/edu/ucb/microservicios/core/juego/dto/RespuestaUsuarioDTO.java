package bo.edu.ucb.microservicios.core.juego.dto;

import java.time.LocalDateTime;

public class RespuestaUsuarioDTO {
    private Long id;
    private Long usuarioId;
    private Long juegoId;
    private String respuestaSeleccionada;
    private Boolean esCorrecta;
    private Integer tiempoRespuesta;
    private LocalDateTime fechaRespuesta;

    // Constructor
    public RespuestaUsuarioDTO() {}

    public RespuestaUsuarioDTO(Long id, Long usuarioId, Long juegoId, String respuestaSeleccionada,
                              Boolean esCorrecta, Integer tiempoRespuesta, LocalDateTime fechaRespuesta) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.juegoId = juegoId;
        this.respuestaSeleccionada = respuestaSeleccionada;
        this.esCorrecta = esCorrecta;
        this.tiempoRespuesta = tiempoRespuesta;
        this.fechaRespuesta = fechaRespuesta;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getJuegoId() {
        return juegoId;
    }

    public void setJuegoId(Long juegoId) {
        this.juegoId = juegoId;
    }

    public String getRespuestaSeleccionada() {
        return respuestaSeleccionada;
    }

    public void setRespuestaSeleccionada(String respuestaSeleccionada) {
        this.respuestaSeleccionada = respuestaSeleccionada;
    }

    public Boolean getEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(Boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public Integer getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setTiempoRespuesta(Integer tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
} 