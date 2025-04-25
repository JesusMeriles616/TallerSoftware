package com.example.colorgame.model.requests;
import io.swagger.v3.oas.annotations.media.Schema;
public class EndGameRequest {
    @Schema(description = "ID del juego a finalizar", required = true)
    private int gameId;

    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }
}