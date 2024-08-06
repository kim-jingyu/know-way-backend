package com.knowway.auth.service;

import com.knowway.auth.exception.AuthException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

public class AccessTokenSetAsBlackListWhenInvalidating<K, V> implements
    AccessTokenInvalidationStrategy<K> {

    private final V value;
    private final long expirationTime;
    private final RedisTemplate<K, V> blackListRedisTemplate;

    public AccessTokenSetAsBlackListWhenInvalidating(V value, long expirationTime,
        @Qualifier("blackListRedisTemplate") RedisTemplate<K, V> blackListRedisTemplate) {
        this.value = value;
        this.blackListRedisTemplate = blackListRedisTemplate;
        this.expirationTime = expirationTime;
    }

    @Override
    public void invalidate(K key) {
        if (Boolean.TRUE.equals(blackListRedisTemplate.hasKey(key))) {
            throw new AuthException("이미 만료된 인증 정보입니다.");
        }
        blackListRedisTemplate.opsForValue().set(key, value, expirationTime);
    }

    @Override
    public boolean isRegistered(K key) {
        return blackListRedisTemplate.opsForValue().get(key) != null;
    }
}