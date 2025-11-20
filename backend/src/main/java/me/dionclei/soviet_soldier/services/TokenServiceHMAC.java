package me.dionclei.soviet_soldier.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.services.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.ZoneOffset;

public class TokenServiceHMAC implements TokenService {

    @Value("${jwt.secret}")
    private String key;

    private final Algorithm algorithm = Algorithm.HMAC256(key);

    @Override
    public boolean isValid(String token) {
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0-soviet-soldier").build();

        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject() != null && !decoded.getSubject().isEmpty();
    }

    @Override
    public String validateToken(String token) {
        return JWT.require(algorithm)
                .withIssuer("auth0-soviet-soldier")
                .build()
                .verify(token)
                .getSubject();
    }

    @Override
    public String generateToken(User user) {
        return JWT.create().withIssuer("auth0-soviet-soldier")
                .withExpiresAt(generateExpiresAt())
                .withSubject(user.getUsername()).sign(algorithm);
    }

    private Instant generateExpiresAt() {
        return Instant.now().atOffset(ZoneOffset.UTC).plusDays(5).toInstant();
    }
}
