package me.dionclei.soviet_soldier.domain;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.enums.Card;
import me.dionclei.soviet_soldier.enums.GameStatus;

import java.util.*;

public class Game {

    private String gameId;
    private GameStatus status;
    private List<Player> players = new ArrayList<>();
    private User owner;
    private Integer round;
    private Integer time;
    private Boolean isTruco;
    private Integer currentPlayerIndex;
    private List<Card> cards = new ArrayList<>();
    private Map<Card, Integer> playedCards = new HashMap<>();

    public Game(User owner, String gameId) {
        this.gameId = gameId;
        this.owner = owner;
        this.round = 3;
        this.time = 15;
        this.isTruco = false;
        this.currentPlayerIndex = 0;
        this.status = GameStatus.WAITING;
    }

    public void update() {
        switch (this.status) {
            case WAITING:
                break;
            case ROUND_START:
                handleRoundStart();
                break;
            case PLAYER_TURN:
                handlePlayerTurn();
                break;
            case ROUND_END:
                handleRoundEnd();
                break;
        }
    }

    private void handleRoundStart() {
        this.cards.clear();
        this.cards = Arrays.asList(Card.values());
        Collections.shuffle(this.cards);
        this.round = 3;

        // Player that will start the round
        String start = this.players.get(currentPlayerIndex).getUsername();

        for (Player p : this.players) {
            p.removeCards();
            for (int i = 0; i < 3; i++) {
                p.addCard(this.cards.get(0));
                this.cards.remove(0);
            }
        }

        reorderPlayers(start);

        this.status = GameStatus.PLAYER_TURN;
    }

    private void handlePlayerTurn() {
        // Checking if all players have already played
        if (this.currentPlayerIndex > this.players.size() - 1) {
            this.status = GameStatus.ROUND_END;
        }

        if (time <= 0) {
            this.currentPlayerIndex++;
            time = 15;
            return;
        }
        time--;
    }

    private void handleRoundEnd() {
        var cards = this.playedCards.keySet().stream().toList();
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

        if (this.status != GameStatus.FINISHED) this.status = GameStatus.ROUND_START;
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

    public void addPlayer(Player player) {
        this.players.add(player);
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

    public String getGameId() {
        return this.gameId;
    }
}
