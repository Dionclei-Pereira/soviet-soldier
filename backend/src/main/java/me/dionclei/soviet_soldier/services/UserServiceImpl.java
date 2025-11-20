package me.dionclei.soviet_soldier.services;

import me.dionclei.soviet_soldier.documents.User;
import me.dionclei.soviet_soldier.dto.RegisterRequest;
import me.dionclei.soviet_soldier.repositories.UserRepository;
import me.dionclei.soviet_soldier.services.interfaces.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public Mono<User> save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public Mono<User> create(RegisterRequest request) {
        System.out.println(request.toString());
        User user = new User(UUID.randomUUID().toString(), request.username(), request.password(), request.nickname());

        return this.userRepository.save(user);
    }

    @Override
    public Mono<Void> update(User user) {
        return findByUsername(user.getUsername())
                .switchIfEmpty(Mono.empty()).flatMap(usr -> {
                    usr.setNickname(user.getNickname());

                    return this.userRepository.save(usr);
                }).then();
    }

    @Override
    public Mono<User> findById(String id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Flux<User> findAll() {
        return this.userRepository.findAll();
    }
}
