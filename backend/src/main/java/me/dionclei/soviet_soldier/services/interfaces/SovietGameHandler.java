package me.dionclei.soviet_soldier.services.interfaces;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.Game;
import me.dionclei.soviet_soldier.domain.GameEvent;
import me.dionclei.soviet_soldier.dto.GameResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SovietGameHandler {

    Mono<GameResponse> createGame(String owner);
    Mono<GameResponse> addPlayer(String gameId, User user);
    Mono<GameResponse> addPlayer(String gameId, String username);
    Mono<Void> removePlayer(String gameId, String username);
    Mono<Void> removePlayer(String gameId, User user);
    Flux<GameEvent> getGameStream(String gameId, String username);
    Mono<Void> startGameLoop(String gameId, Game game);
    Mono<Void> stopGameLoop(String gameId);
    Mono<Void> startGame(String gameId, String username);
}
