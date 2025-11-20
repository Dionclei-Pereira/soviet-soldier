package me.dionclei.soviet_soldier.exceptions;

import java.time.Instant;

public record StandardException(Instant timestamp, Integer status, String message, String path) {
}
