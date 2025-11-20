package me.dionclei.soviet_soldier.dto;

public record PlayerResponse(
        String id,
        String name,
        Integer points,
        Integer cardsQuantity
) {

}
