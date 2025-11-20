package me.dionclei.soviet_soldier.controllers;

import me.dionclei.soviet_soldier.domain.GameEvent;
import me.dionclei.soviet_soldier.domain.enums.Card;
import me.dionclei.soviet_soldier.dto.GameResponse;
import me.dionclei.soviet_soldier.services.interfaces.SovietGameHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

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
                .flatMap(game -> Mono.just(ResponseEntity.ok().body(game)));
    }

    @PostMapping("/{gameId}/leave")
    public Mono<ResponseEntity<Void>> leaveGame(@PathVariable String gameId, Principal principal) {
        return this.gameService.removePlayer(gameId, principal.getName())
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PostMapping("/{gameId}/start")
    public Mono<ResponseEntity<GameResponse>> startGame(@PathVariable String gameId, Principal principal) {
        return this.gameService.startGame(gameId, principal.getName())
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PostMapping("/{gameId}/play/{cardIndex}")
    public Mono<ResponseEntity<Void>>  playCard(@PathVariable String gameId, @PathVariable int cardIndex, Principal principal) {
        return this.gameService.playCard(gameId, principal.getName(), cardIndex)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping("/{gameId}/cards")
    public Mono<ResponseEntity<List<Card>>> getCards(@PathVariable String gameId, Principal principal) {
        return this.gameService.getCards(gameId, principal.getName()).flatMap(cards -> {
            return Mono.just(ResponseEntity.ok().body(cards));
        });
    }
}
