package com.example.colorgame.model.responses;
import io.swagger.v3.oas.annotations.media.Schema;
public class PlayResponse {
    @Schema(description = "Puntaje actual")
    private int score;
    @Schema(description = "Tiempo restante en segundos")
    private int timeleft;

    public PlayResponse(int score, int timeleft) {
        this.score = score;
        this.timeleft = timeleft;
    }
    public int getScore() { return score; }
    public int getTimeleft() { return timeleft; }
}