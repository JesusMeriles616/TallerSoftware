package com.example.colorgame.model.requests;
import io.swagger.v3.oas.annotations.media.Schema;
public class StartGameRequest {
    @Schema(description = "Nivel de dificultad", allowableValues = {"Fácil", "Medio", "Difícil"}, defaultValue = "Fácil")
    private String difficulty;

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}