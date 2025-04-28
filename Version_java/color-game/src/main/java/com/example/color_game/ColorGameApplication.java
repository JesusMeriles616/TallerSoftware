package com.example.colorgame;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.colorgame.model.Game;
import com.example.colorgame.model.requests.EndGameRequest;
import com.example.colorgame.model.requests.PlayRequest;
import com.example.colorgame.model.requests.StartGameRequest;
import com.example.colorgame.model.responses.EndGameResponse;
import com.example.colorgame.model.responses.ErrorResponse;
import com.example.colorgame.model.responses.PlayResponse;
import com.example.colorgame.model.responses.StartGameResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@SpringBootApplication
@RestController
public class ColorGameApplication {
    private final ConcurrentHashMap<Integer, Game> games = new ConcurrentHashMap<>();
    private final AtomicInteger gameId = new AtomicInteger(1);

    public static void main(String[] args) {
        SpringApplication.run(ColorGameApplication.class, args);
    }

    @PostMapping("/start_game")
    @Operation(
        summary = "Inicia un nuevo juego",
        description = "Inicia un nuevo juego con la dificultad especificada",
        tags = {"Juego"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Juego iniciado correctamente",
                content = @Content(schema = @Schema(implementation = StartGameResponse.class))
    )})
        
    public ResponseEntity<StartGameResponse> startGame(
            @RequestBody @Schema(description = "Datos de inicio del juego") StartGameRequest request) {
        
        String difficulty = request.getDifficulty() != null ? request.getDifficulty() : "Fácil";
        int timeleft = switch (difficulty) {
            case "Fácil" -> 60;
            case "Medio" -> 45;
            case "Difícil" -> 30;
            default -> 60;
        };

        int currentGameId = gameId.getAndIncrement();
        games.put(currentGameId, new Game(0, timeleft, difficulty));

        StartGameResponse response = new StartGameResponse(currentGameId, timeleft, difficulty);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/play")
    @Operation(
        summary = "Realiza un movimiento",
        description = "Realiza un movimiento en el juego adivinando el color",
        tags = {"Juego"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Respuesta después de jugar",
                content = @Content(schema = @Schema(implementation = PlayResponse.class))),
            @ApiResponse(
                responseCode = "404",
                description = "Game ID no encontrado",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
    public ResponseEntity<?> play(
            @RequestBody @Schema(description = "Datos del movimiento") PlayRequest request) {
        
        Game game = games.get(request.getGameId());
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Game ID not found"));
        }

        if ("Rojo".equalsIgnoreCase(request.getColor())) {
            game.setScore(game.getScore() + 1);
        }

        PlayResponse response = new PlayResponse(game.getScore(), game.getTimeleft());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/end_game")
    @Operation(
        summary = "Finaliza un juego",
        description = "Finaliza un juego en curso y obtiene el puntaje final",
        tags = {"Juego"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Juego finalizado correctamente",
                content = @Content(schema = @Schema(implementation = EndGameResponse.class))),
            @ApiResponse(
                responseCode = "404",
                description = "Game ID no encontrado",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
    public ResponseEntity<?> endGame(
            @RequestBody @Schema(description = "ID del juego a finalizar") EndGameRequest request) {
        
        Game game = games.remove(request.getGameId());
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Game ID not found"));
        }

        EndGameResponse response = new EndGameResponse("Game ended", game.getScore());
        return ResponseEntity.ok(response);
    }
}