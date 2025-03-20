package example.security;

import example.security.jwt.JwtTokenUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlacklistService {
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();
    private final JwtTokenUtil jwtTokenUtils;

    public BlacklistService(JwtTokenUtil jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public void blacklistToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    @Scheduled(fixedRate = 1000) // Every 1 min
    public void cleanupExpiredTokens() {
        tokenBlacklist.removeIf(jwtTokenUtils::isTokenExpired);
    }
}

