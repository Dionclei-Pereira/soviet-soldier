package me.dionclei.soviet_soldier.services;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.Game;
import me.dionclei.soviet_soldier.domain.GameEvent;
import me.dionclei.soviet_soldier.domain.Player;
import me.dionclei.soviet_soldier.domain.enums.Card;
import me.dionclei.soviet_soldier.dto.GameResponse;
import me.dionclei.soviet_soldier.enums.GameStatus;
import me.dionclei.soviet_soldier.exceptions.GameException;
import me.dionclei.soviet_soldier.exceptions.NotFoundException;
import me.dionclei.soviet_soldier.services.interfaces.SovietGameHandler;
import me.dionclei.soviet_soldier.services.interfaces.UserService;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameHandler implements SovietGameHandler {

    private UserService userService;

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    // Sinks for individual SSE
    private final Map<String, Sinks.Many<GameEvent>> gameSinks = new ConcurrentHashMap<>();

    private final Map<String, Disposable> gameLoops = new ConcurrentHashMap<>();

    public GameHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<GameResponse> createGame(String owner) {
        String gameId = Integer.valueOf(games.size() + 1).toString();

        Game game = new Game(owner, gameId);
        games.put(gameId, game);

        gameSinks.put(gameId, Sinks.many().multicast().directAllOrNothing());

        return this.userService.findByUsername(owner)
                .doOnNext(u -> addPlayer(gameId, u)).map(u -> game.toDTO());

    }

    @Override
    public Mono<GameResponse> addPlayer(String gameId, User user) {
        Game game = games.get(gameId);
        if (game != null) {
            if (game.isPlaying(user.getUsername())) {
                return Mono.error(new GameException("You are already in this room"));
            }
            if (game.countPlayers() >= 4) {
                return Mono.error(new GameException("This room is full"));
            }
            if (game.getStatus() != GameStatus.WAITING) {
                return Mono.error(new GameException("Game is already playing"));
            }
            Player player = new Player(user);
            game.addPlayer(player);
            return Mono.just(game.toDTO());
        } else {
            return Mono.error(new NotFoundException("Game not found"));
        }
    }

    @Override
    public Mono<GameResponse> addPlayer(String gameId, String username) {
        return this.userService.findByUsername(username)
                .flatMap(u -> addPlayer(gameId, u));
    }

    @Override
    public Mono<Void> removePlayer(String gameId, String username) {
        return this.userService.findByUsername(username).flatMap(u -> removePlayer(gameId, u));
    }

    @Override
    public Mono<Void> removePlayer(String gameId, User user) {
        Game game = games.get(gameId);
        if (game != null) {
            game.removePlayer(user.getId());
            if (game.countPlayers() <= 0) {
                stopGameLoop(gameId);
            }

            return Mono.empty();
        } else {
            return Mono.error(new NotFoundException("Game not found"));
        }
    }

    @Override
    public Flux<GameEvent> getGameStream(String gameId, String username) {
        Game game =  games.get(gameId);
        if (game == null || !game.isPlaying(username)) {
            Mono.error(new GameException("Invalid request"));
        }

        Sinks.Many<GameEvent> sink = gameSinks.get(gameId);
        if (sink != null) {
            return sink.asFlux();
        } else {
            Mono.error(new GameException("Invalid request"));
        }
        return Flux.empty();
    }

    @Override
    public Mono<Void> startGameLoop(String gameId, Game game) {
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
        return Mono.empty();
    }

    @Override
    public Mono<Void> stopGameLoop(String gameId) {
        Disposable disposable = gameLoops.remove(gameId);
        if (disposable != null) {
            disposable.dispose();
        }

        Sinks.Many<GameEvent> sink = gameSinks.get(gameId);
        if (sink != null) {
            sink.tryEmitComplete();
        }
        gameSinks.remove(gameId);
        games.remove(gameId);
        return Mono.empty();
    }

    public Mono<Void> playCard(String gameId, String userId, Integer cardIndex) {
        Game game = games.get(gameId);
        if (game == null || !game.isPlaying(userId)) {
            Mono.error(new GameException("Invalid request"));
        }

        game.playCard(userId, cardIndex);
        return Mono.empty();
    }

    @Override
    public Mono<List<Card>> getCards(String gameId, String username) {
        Game game = games.get(gameId);
        if (game == null || !game.isPlaying(username)) {
            return Mono.error(new GameException("Invalid request"));
        }

        Player player = game.getPlayer(username);
        return Mono.just(player.getCards());
    }

    @Override
    public Mono<Void> startGame(String gameId, String username) {
        Game game = games.get(gameId);
        if (game != null) {
            if (!game.getOwner().equals(username)) return Mono.error(new GameException("Invalid request"));
            if (game.countPlayers() < 2) return Mono.error(new GameException("Insufficient players"));
            game.start();
            return Mono.empty().then(startGameLoop(gameId, game));
        } else {
            return Mono.error(new NotFoundException("Game not found"));
        }
    }
}
