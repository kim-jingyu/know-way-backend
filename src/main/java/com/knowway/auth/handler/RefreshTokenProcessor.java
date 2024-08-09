package com.knowway.auth.handler;

import com.knowway.auth.exception.AuthException;
import com.knowway.auth.service.ReAuthenticationStrategy;
import com.knowway.auth.service.RefreshTokenPersistLocationStrategy;
import lombok.RequiredArgsConstructor;

/**
 * RefreshTokenProcessor
 *
 * @author 구지웅
 * @since 2024.8.1
 * @version 1.0

 */
@RequiredArgsConstructor
public class RefreshTokenProcessor<K,V> {

  private final long refreshKeyLifeTime;
  private final RefreshTokenPersistLocationStrategy<K,V> locationStrategy;
  private final ReAuthenticationStrategy<K,V> reIssueStrategy;

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
    reIssueStrategy.reIssue(key,value);
  }
}
