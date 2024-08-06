package com.knowway.auth.service;


public interface AccessTokenInvalidationStrategy {
  void invalidate(String key);
  boolean isRegistered(String key);
}
