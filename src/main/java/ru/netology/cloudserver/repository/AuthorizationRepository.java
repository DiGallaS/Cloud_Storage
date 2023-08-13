package ru.netology.cloudserver.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AuthorizationRepository {
    private final Map<String, String> tokenAndUser = new ConcurrentHashMap<>();

    public void putTokenAndUsername(String token, String username) {
        tokenAndUser.put(token, username);
    }

    public String getUserNameByToken(String token) {
        return tokenAndUser.get(token);
    }

    public void removeTokenAndUsernameByToken(String token) {
        tokenAndUser.remove(token);
    }
}
