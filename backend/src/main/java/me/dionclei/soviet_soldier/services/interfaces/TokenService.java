package me.dionclei.soviet_soldier.services.interfaces;

import me.dionclei.soviet_soldier.documents.User;

public interface TokenService {

    boolean isValid(String token);

    String validateToken(String token);

    String generateToken(User user);

}
