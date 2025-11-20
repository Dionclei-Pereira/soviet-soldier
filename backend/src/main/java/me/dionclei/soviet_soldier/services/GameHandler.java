package me.dionclei.soviet_soldier.services;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.Game;
import me.dionclei.soviet_soldier.domain.GameEvent;
import me.dionclei.soviet_soldier.domain.Player;
import me.dionclei.soviet_soldier.services.interfaces.SovietGameHandler;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameHandler implements SovietGameHandler {

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    // Sinks for individual SSE
    private final Map<String, Sinks.Many<GameEvent>> gameSinks = new ConcurrentHashMap<>();

    private final Map<String, Disposable> gameLoops = new ConcurrentHashMap<>();

    @Override
    public Game createGame(User owner) {
        String gameId = Integer.valueOf(games.size() + 1).toString();

        Game game = new Game(owner, gameId);
        games.put(gameId, game);

        gameSinks.put(gameId, Sinks.many().multicast().directAllOrNothing());

        startGameLoop(gameId, game);

        return game;
    }

    @Override
    public void addPlayer(String gameId, User user) {
        Game game = games.get(gameId);
        if (game != null) {
            Player player = new Player(user);
            game.addPlayer(player);
        }
    }

    @Override
    public void removePlayer(String gameId, User user) {
        Game game = games.get(gameId);
        if (game != null) {
            game.removePlayer(user.getId());
        }
    }

    @Override
    public Flux<GameEvent> getGameStream(String gameId) {
        Sinks.Many<GameEvent> sink = gameSinks.get(gameId);
        if (sink != null) {
            return sink.asFlux();
        } else {
            return Flux.empty();
        }
    }

    @Override
    public void startGameLoop(String gameId, Game game) {
        Disposable disposable = Flux.interval(Duration.ofSeconds(1))
                .doOnNext(tick -> {
                    GameEvent event = game.update();

                    if (event != null) {
                        Sinks.Many<GameEvent> sink = gameSinks.get(gameId);
                        if (sink != null) {
                            sink.tryEmitNext(event);
                        }
                    }
                })
                .subscribe();

        this.gameLoops.put(gameId, disposable);
    }

    @Override
    public void stopGameLoop(String gameId) {
        Disposable disposable = gameLoops.remove(gameId);
        if (disposable != null) {
            disposable.dispose();
        }

        Sinks.Many<GameEvent> sink = gameSinks.get(gameId);
        if (sink != null) {
            sink.tryEmitComplete();
        }
    }
}
