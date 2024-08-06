package com.knowway.auth.service;

import com.knowway.auth.exception.AuthException;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;

public class AccessTokenSetAsBlackListWhenInvalidating implements
    AccessTokenInvalidationStrategy {

    private final long expirationTime;
    private final RedisTemplate<String, String> blackListRedisTemplate;

    public AccessTokenSetAsBlackListWhenInvalidating(long expirationTime,
        RedisTemplate<String, String> blackListRedisTemplate) {
        this.expirationTime = expirationTime;
        this.blackListRedisTemplate = blackListRedisTemplate;
    }


    @Override
    public void invalidate(String key) {
        if (Boolean.TRUE.equals(blackListRedisTemplate.hasKey(key))) {
            throw new AuthException("이미 만료된 인증 정보입니다.");
        }
        blackListRedisTemplate.expire(key, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isRegistered(String key) {
        return blackListRedisTemplate.opsForValue().get(key) != null;
    }
}