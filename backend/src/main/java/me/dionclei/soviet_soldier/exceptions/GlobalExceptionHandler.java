package me.dionclei.soviet_soldier.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public Mono<ResponseEntity<StandardException>> handleTokenException(TokenException e, ServerWebExchange request) {
        var status = HttpStatus.UNAUTHORIZED;
        var message = e.getMessage();
        StandardException ex = new StandardException(Instant.now(), status.value(), message, request.getRequest().getURI().toString());
        return Mono.just(ResponseEntity.status(status).body(ex));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<StandardException>> handleInvalidCredentialsException(InvalidCredentialsException e, ServerWebExchange request) {
        var status = HttpStatus.BAD_REQUEST;
        var message = e.getMessage();
        StandardException ex = new StandardException(Instant.now(), status.value(), message, request.getRequest().getURI().toString());
        return Mono.just(ResponseEntity.status(status).body(ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<StandardException>> handleNotFoundException(NotFoundException e, ServerWebExchange request) {
        var status = HttpStatus.NOT_FOUND;
        var message = e.getMessage();
        StandardException ex = new StandardException(Instant.now(), status.value(), message, request.getRequest().getURI().toString());
        return Mono.just(ResponseEntity.status(status).body(ex));
    }
}
