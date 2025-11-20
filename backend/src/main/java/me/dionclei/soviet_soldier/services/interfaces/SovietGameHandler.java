package me.dionclei.soviet_soldier.services.interfaces;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.Game;
import me.dionclei.soviet_soldier.domain.GameEvent;
import reactor.core.publisher.Flux;

public interface SovietGameHandler {

    Game createGame(User owner);
    void addPlayer(String gameId, User user);
    void removePlayer(String gameId, User user);
    Flux<GameEvent> getGameStream(String gameId);
    void startGameLoop(String gameId, Game game);
    void stopGameLoop(String gameId);

}
