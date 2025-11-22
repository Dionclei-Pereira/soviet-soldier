package me.dionclei.soviet_soldier.configs;

import me.dionclei.soviet_soldier.services.interfaces.TokenService;
import me.dionclei.soviet_soldier.services.interfaces.UserService;
import me.dionclei.soviet_soldier.utils.TokenParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class SecurityFilter implements WebFilter {

    private final TokenService tokenService;
    private final UserService userService;

    public SecurityFilter(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = TokenParser.parseToken(exchange.getRequest().getHeaders().getFirst("Authorization"));
        if (token != null) {
            try {
                String subject = this.tokenService.validateToken(token);

                return this.userService.findByUsername(subject).flatMap(u -> {
                    var auth = new UsernamePasswordAuthenticationToken((UserDetails) u, null, u.getAuthorities());
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                });
            } catch (Exception e) {
                return chain.filter(exchange);
            }
        }

        return chain.filter(exchange);
    }
}
