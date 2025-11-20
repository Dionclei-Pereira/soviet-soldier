package me.dionclei.soviet_soldier.domain;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.enums.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private GameStatus status;
    private List<User> players = new ArrayList<>();
    private User owner;
    private Integer round;
    private Integer time;
    private Boolean isTruco;
    private Integer currentPlayerIndex;

    public void update() {

    }
}
