package me.dionclei.soviet_soldier.domain;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.enums.Card;
import me.dionclei.soviet_soldier.domain.enums.GameEventType;
import me.dionclei.soviet_soldier.dto.GameResponse;
import me.dionclei.soviet_soldier.enums.GameStatus;
import me.dionclei.soviet_soldier.exceptions.GameException;

import java.util.*;

public class Game {

    private String gameId;
    private GameStatus status;
    private List<Player> players = new ArrayList<>();
    private String owner;
    private Integer round;
    private Integer time;
    private Boolean isTruco;
    private Integer currentPlayerIndex;
    private List<Card> cards = new ArrayList<>();
    private Map<Card, Integer> playedCards = new HashMap<>();

    public Game(String owner, String gameId) {
        this.gameId = gameId;
        this.owner = owner;
        this.round = 3;
        this.time = 15;
        this.isTruco = false;
        this.currentPlayerIndex = 0;
        this.status = GameStatus.WAITING;
    }

    public GameResponse toDTO() {
        return new GameResponse(
                this.gameId,
                this.players.stream().map(Player::toDTO).toList(),
                this.owner,
                this.round,
                this.isTruco,
                this.currentPlayerIndex,
                this.playedCards,
                this.status
        );
    }

    public GameEvent update() {
        System.out.println("Updating game");
        switch (this.status) {
            case ROUND_START:
                return handleRoundStart();
            case PLAYER_TURN:
                return handlePlayerTurn();
            case ROUND_END:
                return handleRoundEnd();
            default:
                return null;
        }
    }

    private GameEvent handleRoundStart() {
        this.cards.clear();
        this.cards = new ArrayList<>(Arrays.asList(Card.values()));
        Collections.shuffle(this.cards);
        this.round = 3;

        // Player that will start the round
        String start = this.players.get(currentPlayerIndex).getUsername();

        for (Player p : this.players) {
            p.removeCards();
            for (int i = 0; i < 3; i++) {
                Card card = this.cards.get(0);
                p.addCard(card);
                this.cards.remove(card);
            }
        }

        reorderPlayers(start);
        this.status = GameStatus.PLAYER_TURN;
        return new GameEvent(GameEventType.START, null);
    }

    private GameEvent handlePlayerTurn() {
        // Checking if all players have already played
        if (this.currentPlayerIndex > this.players.size() - 1) {
            this.status = GameStatus.ROUND_END;
        }

        if (time <= 0) {
            this.currentPlayerIndex++;
            time = 15;
            return new GameEvent(GameEventType.PLAY, null);
        }
        time--;
        return null;
    }

    private GameEvent handleRoundEnd() {
        var cards = new ArrayList<>(this.playedCards.keySet().stream().toList());
        Collections.sort(cards, Comparator.comparingInt(Card::getPower));
        if (cards.get(0).getPower() == cards.get(1).getPower()) {
            this.currentPlayerIndex = this.players.size() - 1;
        } else {
            this.currentPlayerIndex = this.playedCards.get(cards.get(0));

            // If it's Truco the player is given double the point amount
            if (isTruco) {
                this.players.get(currentPlayerIndex).addPoints(this.round);
            }
            this.players.get(currentPlayerIndex).addPoints(this.round);

            this.round--;
            if (this.round == 0) {
                handlePlayersPoints();
                this.round = 3;
            }
        }

        if (this.status != GameStatus.FINISHED) {
            this.status = GameStatus.ROUND_START;
            return  new GameEvent(GameEventType.END, null);
        }

        return new GameEvent(GameEventType.FINISH, null);
    }

    private void handlePlayersPoints() {
        for (Player p : this.players) {
            if (p.getPoints() == 12) {
                this.currentPlayerIndex = this.players.indexOf(p);
                this.status = GameStatus.FINISHED;
                return;
            } else if (p.getPoints() > 12) {
                p.setPoints(0);
            }
        }
    }

    // Putting the current player at the beginning of the list
    private void reorderPlayers(String start) {

        int startIndex = -1;
        for (int i = 0; i < this.players.size(); i++) {
            if (this.players.get(i).getUsername().equals(start)) {
                startIndex = i;
                break;
            }
        }

        if (startIndex > 0) {
            List<Player> reorderedPlayers = new ArrayList<>();

            // Adding the players after the startIndex
            for (int i = startIndex; i < this.players.size(); i++) {
                reorderedPlayers.add(this.players.get(i));
            }

            // Adding before the startIndex
            for (int i = 0; i < startIndex; i++) {
                reorderedPlayers.add(this.players.get(i));
            }

            this.players = reorderedPlayers;
        }
    }

    public void playCard(String userId, Integer cardIndex) {
        if (this.status != GameStatus.PLAYER_TURN) {
            throw new GameException("You are not allowed to play this card now");
        }
        try {
            for (int i = 0; i < this.players.size(); i++) {
                Player p = this.players.get(i);
                if (p.getId().equals(userId)) {
                    Card card = p.playCard(p.getCards().get(cardIndex));
                    this.playedCards.put(card, i);
                    break;
                }
            }
            throw new GameException("Invalid player ID");
        } catch (Exception e) {
            throw new GameException("Error in playing card");
        }
    }

    public int countPlayers() {
        return this.players.size();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public Player getPlayer(String username) {
        return this.players.stream().filter(p -> p.getUsername().equals(username)).findFirst().orElse(null);
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void removePlayer(String id) {
        this.players.removeIf(p -> p.getId().equals(id));
    }

    public boolean isPlaying(String username) {
        for (Player player : this.players) {
            if (player.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void start() {
        if (this.status == GameStatus.WAITING) this.status = GameStatus.ROUND_START;
    }

    public String getGameId() {
        return this.gameId;
    }

    public String getOwner() {
        return this.owner;
    }
}
