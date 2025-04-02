package bo.edu.ucb.microservicios.core.dashboard.dto;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos de rendimiento de un niño en un juego")
public class PerformanceDTO {

    @Schema(description = "Identificador del niño")
    @NotNull(message = "El identificador del niño no puede ser nulo")
    private String childId;
    @Schema(description = "Identificador del juego")
    @NotNull(message = "El identificador del juego no puede ser nulo")
    private String gameId;
    @Schema(description = "Número de respuestas correctas")
    @Min(value = 0, message = "El número de respuestas correctas debe ser mayor o igual a 0")
    private int correctAnswers;
    @Schema(description = "Número de respuestas incorrectas")
    @Min(value = 0, message = "El número de respuestas incorrectas debe ser mayor o igual a 0")
    private int wrongAnswers;
    @Schema(description = "Fecha de registro")
    private Date date;

    public PerformanceDTO(String childId, String gameId, int correctAnswers, int wrongAnswers, Date date) {
        this.childId = childId;
        this.gameId = gameId;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.date = date;
    }

    public String getChildId() {
        return this.childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getCorrectAnswers() {
        return this.correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return this.wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



}