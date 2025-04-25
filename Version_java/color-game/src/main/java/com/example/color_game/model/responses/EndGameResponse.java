package com.example.colorgame.model.responses;
import io.swagger.v3.oas.annotations.media.Schema;
public class EndGameResponse {
    @Schema(description = "Mensaje de confirmación")
    private String message;
    @Schema(description = "Puntaje final del juego")
    private int finalScore;

    public EndGameResponse(String message, int finalScore) {
        this.message = message;
        this.finalScore = finalScore;
    }

    // Getters
    public String getMessage() { return message; }
    public int getFinalScore() { return finalScore; }
}