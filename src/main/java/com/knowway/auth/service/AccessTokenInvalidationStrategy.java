package com.knowway.auth.service;

/**
 * * 작성자: 구지웅
 */
public interface AccessTokenInvalidationStrategy {

  /**
   * * 작성자: 구지웅
   * @param key :
   */
  void invalidate(String key);
  boolean isRegistered(String key);
}
