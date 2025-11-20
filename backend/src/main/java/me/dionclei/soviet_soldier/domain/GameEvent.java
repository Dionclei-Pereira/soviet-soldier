package me.dionclei.soviet_soldier.domain;

import me.dionclei.soviet_soldier.domain.enums.GameEventType;

public record GameEvent(GameEventType type, Object payload) {
}
