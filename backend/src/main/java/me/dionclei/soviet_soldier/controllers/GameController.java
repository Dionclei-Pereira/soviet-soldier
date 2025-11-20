package me.dionclei.soviet_soldier.controllers;

import me.dionclei.soviet_soldier.domain.GameEvent;
import me.dionclei.soviet_soldier.dto.GameResponse;
import me.dionclei.soviet_soldier.services.interfaces.SovietGameHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/game")
public class GameController {

    private final SovietGameHandler gameService;

    public GameController(SovietGameHandler gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = "/{gameId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<GameEvent> streamGame(@PathVariable String gameId, Principal principal) {
        return gameService.getGameStream(gameId, principal.getName());
    }

    @PostMapping
    public Mono<ResponseEntity<GameResponse>> createGame(Principal principal) {
        return this.gameService.createGame(principal.getName()).flatMap(game -> {
            return Mono.just(ResponseEntity.ok().body(game));
        });
    }

    @PostMapping("/{gameId}/join")
    public Mono<ResponseEntity<GameResponse>> joinGame(@PathVariable String gameId, Principal principal) {
        return this.gameService.addPlayer(gameId, principal.getName())
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PostMapping("/{gameId}/leave")
    public Mono<ResponseEntity<Void>> leaveGame(@PathVariable String gameId, Principal principal) {
        return this.gameService.removePlayer(gameId, principal.getName())
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PostMapping("/{gameId}/start")
    public Mono<ResponseEntity<GameResponse>> startGame(@PathVariable String gameId, Principal principal) {
        return  this.gameService.startGame(gameId, principal.getName())
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}
