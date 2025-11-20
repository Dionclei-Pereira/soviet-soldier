package me.dionclei.soviet_soldier.controllers;

import me.dionclei.soviet_soldier.dto.LoginRequest;
import me.dionclei.soviet_soldier.dto.RegisterRequest;
import me.dionclei.soviet_soldier.dto.TokenResponse;
import me.dionclei.soviet_soldier.services.interfaces.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> register(@RequestBody RegisterRequest request) {
        return this.authService.register(request).map(ResponseEntity::ok);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponse>> login(@RequestBody LoginRequest request) {
        return this.authService.login(request).map(ResponseEntity::ok);
    }

}
