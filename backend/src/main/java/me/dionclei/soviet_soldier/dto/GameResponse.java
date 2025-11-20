package me.dionclei.soviet_soldier.dto;

import me.dionclei.soviet_soldier.domain.enums.Card;
import me.dionclei.soviet_soldier.enums.GameStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record GameResponse(
        String gameId,
        List<PlayerResponse> players,
        String owner,
        Integer round,
        Boolean isTruco,
        Integer currentPlayerIndex,
        Map<Card, Integer> playedCards,
        GameStatus status
) {
}
