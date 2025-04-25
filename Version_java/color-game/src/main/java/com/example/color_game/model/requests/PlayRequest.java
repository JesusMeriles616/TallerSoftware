package com.example.colorgame.model.requests;
import io.swagger.v3.oas.annotations.media.Schema;
public class PlayRequest {
    @Schema(description = "ID del juego en curso", required = true)
    private int gameId;
    @Schema(description = "Color que el usuario cree que es correcto", required = true)
    private String color;

    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}