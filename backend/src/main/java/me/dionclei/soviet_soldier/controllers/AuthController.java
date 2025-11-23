package me.dionclei.soviet_soldier.controllers;

import jakarta.validation.Valid;
import me.dionclei.soviet_soldier.dto.LoginRequest;
import me.dionclei.soviet_soldier.dto.RegisterRequest;
import me.dionclei.soviet_soldier.dto.TokenResponse;
import me.dionclei.soviet_soldier.services.interfaces.AuthService;
import me.dionclei.soviet_soldier.utils.TokenParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/ping")
    public Mono<ResponseEntity<String>> ping() {
        return  Mono.just(ResponseEntity.ok("pong"));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> register(@RequestBody @Valid RegisterRequest request) {
        return this.authService.register(request).map(ResponseEntity::ok);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponse>> login(@RequestBody LoginRequest request) {
        return this.authService.login(request).map(ResponseEntity::ok);
    }

    @GetMapping("/verify")
    public Mono<ResponseEntity<Boolean>> verify(ServerWebExchange request) {
        String token = TokenParser.parseToken(request.getRequest().getHeaders().getFirst("Authorization"));
        return this.authService.verify(token).map(ResponseEntity::ok);
    }

    @GetMapping("/username-available/{username}")
    public Mono<ResponseEntity<Boolean>> usernameAvailable(@PathVariable String username) {
        return this.authService.isUsernameAvailable(username).map(ResponseEntity::ok);
    }
}
