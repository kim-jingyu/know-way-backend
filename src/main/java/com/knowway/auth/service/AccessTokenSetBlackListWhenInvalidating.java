package com.knowway.auth.service;

import com.knowway.auth.vo.RedisBlackListKeyPrefix;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class AccessTokenSetBlackListWhenInvalidating implements AccessTokenInvalidationStrategy {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${encryption.key.access.key.lifetime}")
    private final long expirationTime;


    @Override
    public void invalidate(String token) {
        redisTemplate.opsForValue()
            .set(RedisBlackListKeyPrefix.REDIS_BLACKLIST_KEY_PREFIX + token, true, expirationTime,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isRegistered(String token) {
        Boolean isBlacklisted = (Boolean) redisTemplate.opsForValue()
            .get(RedisBlackListKeyPrefix.REDIS_BLACKLIST_KEY_PREFIX + token);
        return isBlacklisted != null && isBlacklisted;
    }
}
