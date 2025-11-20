package me.dionclei.soviet_soldier.services.interfaces;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.Game;
import reactor.core.publisher.Flux;

public interface SovietGameHandler {

    Game createGame(User owner);
    void addPlayer(String gameId, User user);
    void removePlayer(String gameId, User user);
    Flux<Game> getGameStream(String gameId);
    void startGameLoop(String gameId, Game game);
    void stopGameLoop(String gameId);

}
