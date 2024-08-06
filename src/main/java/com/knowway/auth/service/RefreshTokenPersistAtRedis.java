package com.knowway.auth.service;

import com.knowway.auth.vo.RedisRefreshKeyPrefix;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RefreshTokenPersistAtRedis<K, V> implements
    RefreshTokenPersistLocationStrategy<K, V> {

    private final RedisTemplate<K, V> refreshRedisTemplate;

    @Override
    public void persist(K key, V value, long lifeTime) {
        refreshRedisTemplate.opsForValue().set(key, value, lifeTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isRegistered(K key) {
        V value = refreshRedisTemplate.opsForValue()
            .get(RedisRefreshKeyPrefix.REDIS_REFRESH_KEY_PREFIX + key);
        return value != null;
    }

    @Override
    public void delete(K key) {
        refreshRedisTemplate.delete(key);
    }

}
