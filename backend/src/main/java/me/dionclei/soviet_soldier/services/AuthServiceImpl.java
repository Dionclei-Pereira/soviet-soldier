package me.dionclei.soviet_soldier.services;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.dto.LoginRequest;
import me.dionclei.soviet_soldier.dto.RegisterRequest;
import me.dionclei.soviet_soldier.dto.TokenResponse;
import me.dionclei.soviet_soldier.exceptions.InvalidCredentialsException;
import me.dionclei.soviet_soldier.services.interfaces.AuthService;
import me.dionclei.soviet_soldier.services.interfaces.TokenService;
import me.dionclei.soviet_soldier.services.interfaces.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthServiceImpl(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public Mono<TokenResponse> login(LoginRequest request) {
        Mono<User> user = this.userService.findByUsername(request.username());
        return user.switchIfEmpty(Mono.error(new InvalidCredentialsException(("User not found"))))
                .flatMap(u -> {
                    PasswordEncoder encoder = new BCryptPasswordEncoder();
                    if (!encoder.matches(request.password(), u.getPassword())) {
                        return Mono.error(new InvalidCredentialsException("Invalid username or password"));
                    }

                    String token = this.tokenService.generateToken(u);

                    return Mono.just(new TokenResponse(token));
                });
    }

    @Override
    public Mono<Void> register(RegisterRequest request) {
        var user = this.userService.findByUsername(request.username());
        return user.flatMap(u -> Mono.error(new InvalidCredentialsException("Username must be unique")))
                .switchIfEmpty(this.userService.create(request)).then();
    }

    @Override
    public Mono<Boolean> verify(String token) {
        if (token == null) return Mono.just(false);
        return Mono.just(Boolean.valueOf(tokenService.validateToken(token)));
    }

    @Override
    public Mono<Boolean> isUsernameAvailable(String username) {
        return this.userService.findByUsername(username)
                .hasElement().map(exist -> !exist);
    }
}
