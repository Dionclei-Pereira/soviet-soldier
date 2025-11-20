package me.dionclei.soviet_soldier.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public Mono<ResponseEntity<StandardException>> handleTokenException(TokenException e, ServerHttpRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var message = e.getMessage();
        StandardException ex = new StandardException(Instant.now(), status.value(), message, request.getURI().toString());
        return Mono.just(ResponseEntity.status(status).body(ex));
    }
}
