package com.knowway.auth.handler;

import lombok.RequiredArgsConstructor;

/**
 * RefreshTokenHandler
 *
 * @author 구지웅
 * @since 2024.8.1
 * @version 1.0

 */
@RequiredArgsConstructor
public class RefreshTokenHandler<K, V> {

  private final RefreshTokenProcessor<K, V> refreshTokenProcessor;
  public void persistToken(K key, V value) {
    refreshTokenProcessor.persist(key,value);
  }

  public boolean isValid(K key) {
    return refreshTokenProcessor.isValid(key);
  }

  public void invalidate(K key) {
    refreshTokenProcessor.invalidate(key);
  }

  public void reIssue(K key, V value) {
    refreshTokenProcessor.reIssue(key, value);
  }

}
