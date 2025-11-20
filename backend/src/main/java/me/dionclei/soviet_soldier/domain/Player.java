package me.dionclei.soviet_soldier.domain;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.domain.enums.Card;
import me.dionclei.soviet_soldier.dto.PlayerResponse;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String id;
    private String name;

    // Handles players by token subject
    private String username;
    private Integer points;
    private List<Card> cards = new ArrayList<>();

    public Player(String id, String name, String username) {
        this.points = 0;
        this.username = username;
        this.id = id;
        this.name = name;
    }

    public Player(User user) {
        this(user.getId(), user.getNickname(), user.getUsername());
    }

    public PlayerResponse toDTO() {
        return new PlayerResponse(
                this.id,
                this.name,
                this.points,
                this.cards.size()
        );
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
        return points;
    }

    public void addPoints(Integer points) {
        this.points += points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void playCard(Card card) {
        if (this.cards.contains(card)) {
            this.cards.remove(card);
        }
    }

    public void removeCards() {
        this.cards.clear();
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }
}
