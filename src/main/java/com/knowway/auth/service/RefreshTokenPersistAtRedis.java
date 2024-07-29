package com.knowway.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RefreshTokenPersistAtRedis implements
    RefreshTokenPersistLocationStrategy {

  private final RedisTemplate<String, Object> redisTemplate;
  @Value("${encrypt.key.refresh.life-time}")
  public long refreshKeyLifeTime;

  @Override
  public void persist(String token) {
    redisTemplate.

  }

  @Override
  public boolean isRegistered(String token) {
    return false;
  }

  @Override
  public void delete(String token) {

  }
}
