package me.dionclei.soviet_soldier.services.interfaces;

import me.dionclei.soviet_soldier.dto.LoginRequest;
import me.dionclei.soviet_soldier.dto.RegisterRequest;
import me.dionclei.soviet_soldier.dto.TokenResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<TokenResponse> login(LoginRequest request);
    Mono<Void> register(RegisterRequest request);
    Mono<Boolean> verify(String token);
}
