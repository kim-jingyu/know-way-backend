package com.knowway.auth.service;

import org.springframework.stereotype.Service;

@Service
public class PersistAccessTokenAtRedis implements
    AccessTokenInvalidationStrategy {


  @Override
  public void invalidate(String value) {

  }

  @Override
  public boolean isRegistered(String value) {
    return false;
  }
}
