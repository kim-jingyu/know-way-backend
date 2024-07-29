package com.knowway.auth.service;


public interface AccessTokenInvalidationStrategy {
  void invalidate(String value);
  boolean isRegistered(String value);
}
