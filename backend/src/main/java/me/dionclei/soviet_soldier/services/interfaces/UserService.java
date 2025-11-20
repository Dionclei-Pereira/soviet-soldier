package me.dionclei.soviet_soldier.services.interfaces;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.dto.RegisterRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> findByUsername(String username);
    Mono<User> save(User user);
    Mono<User> create(RegisterRequest request);
    Mono<Void> update(User user);
    Mono<User> findById(String id);
    Flux<User> findAll();
}
