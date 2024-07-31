package com.knowway.auth.service;


public interface AccessTokenInvalidationStrategy<K> {
  void invalidate(K key);
  boolean isRegistered(K key);
}
