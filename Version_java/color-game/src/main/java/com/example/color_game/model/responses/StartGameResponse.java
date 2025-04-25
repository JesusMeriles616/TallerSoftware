package com.example.colorgame.model.responses;
import io.swagger.v3.oas.annotations.media.Schema;
public class StartGameResponse {
    @Schema(description = "ID del juego creado")
    private int gameId;
    @Schema(description = "Tiempo restante en segundos")
    private int timeleft;
    @Schema(description = "Dificultad del juego")
    private String difficulty;

    public StartGameResponse(int gameId, int timeleft, String difficulty) {
        this.gameId = gameId;
        this.timeleft = timeleft;
        this.difficulty = difficulty;
    }

    // Getters
    public int getGameId() { return gameId; }
    public int getTimeleft() { return timeleft; }
    public String getDifficulty() { return difficulty; }
}