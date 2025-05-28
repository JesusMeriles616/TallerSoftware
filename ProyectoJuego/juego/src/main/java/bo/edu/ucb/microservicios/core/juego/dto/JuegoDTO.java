package bo.edu.ucb.microservicios.core.juego.dto;

import java.time.LocalDateTime;

public class JuegoDTO {
    private Long id;
    private String nombre;
    private String palabra;
    private String colorPalabra;
    private String colorCorrecto;
    private Integer nivelDificultad;
    private Integer tiempoRespuesta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private String descripcion;
    private Integer puntajeMaximo;

    // Constructor
    public JuegoDTO() {}

    public JuegoDTO(Long id, String nombre, String palabra, String colorPalabra, String colorCorrecto,
                   Integer nivelDificultad, Integer tiempoRespuesta, String opcion1,
                   String opcion2, String opcion3, String opcion4, Boolean activo,
                   LocalDateTime fechaCreacion, String descripcion, Integer puntajeMaximo) {
        this.id = id;
        this.nombre = nombre;
        this.palabra = palabra;
        this.colorPalabra = colorPalabra;
        this.colorCorrecto = colorCorrecto;
        this.nivelDificultad = nivelDificultad;
        this.tiempoRespuesta = tiempoRespuesta;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.puntajeMaximo = puntajeMaximo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getColorPalabra() {
        return colorPalabra;
    }

    public void setColorPalabra(String colorPalabra) {
        this.colorPalabra = colorPalabra;
    }

    public String getColorCorrecto() {
        return colorCorrecto;
    }

    public void setColorCorrecto(String colorCorrecto) {
        this.colorCorrecto = colorCorrecto;
    }

    public Integer getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(Integer nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public Integer getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setTiempoRespuesta(Integer tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public void setOpcion4(String opcion4) {
        this.opcion4 = opcion4;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(Integer puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }
} 