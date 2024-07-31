package com.knowway.auth.service;

import com.knowway.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefreshTokenProcessor<K, V> {

  private final long refreshKeyLifeTime;
  private final RefreshTokenPersistLocationStrategy<K,V> locationStrategy;
  private final ReAuthenticationStrategy<K,V> tokenReIssueStrategy;

  public void persist(K key,V value){
    locationStrategy.persist(key,value,refreshKeyLifeTime);
  }
  public void invalidate(K key){
    locationStrategy.delete(key);
  }
  public boolean isValid(K key){
    if(!locationStrategy.isRegistered(key)) throw new AuthException("존재하지 않은 Refresh 토큰입니다.");
    return true;
  }
  public void reIssue(K key,V value){
    tokenReIssueStrategy.reIssue(key,value);
  }

}
