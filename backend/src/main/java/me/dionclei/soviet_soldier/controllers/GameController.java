package me.dionclei.soviet_soldier.controllers;

import me.dionclei.soviet_soldier.domain.GameEvent;
import me.dionclei.soviet_soldier.services.interfaces.SovietGameHandler;
import org.springframework.http.MediaType;
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
    public Flux<GameEvent> streamGame(@PathVariable String gameId) {
        return gameService.getGameStream(gameId);
    }
}
