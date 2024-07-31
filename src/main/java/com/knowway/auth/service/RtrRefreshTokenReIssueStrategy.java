package com.knowway.auth.service;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RtrRefreshTokenReIssueStrategy<K,V> implements
    ReAuthenticationStrategy<K,V> {

  private final RefreshTokenPersistLocationStrategy<K,V> persistLocationStrategy;
  private final long refreshLifeTime;

  @Override
  public void reIssue(K key,V value) {
    persistLocationStrategy.persist(key,value,refreshLifeTime);
  }
}
