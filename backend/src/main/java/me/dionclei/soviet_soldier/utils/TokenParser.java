package me.dionclei.soviet_soldier.utils;

public class TokenParser {

    public static String parseToken(String token) {
        if (token == null) {
            return null;
        }
        return token.substring(7);
    }
}
