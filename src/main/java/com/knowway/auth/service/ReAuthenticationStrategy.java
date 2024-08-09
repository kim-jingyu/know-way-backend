package com.knowway.auth.service;

/**
 * * 작성자: 구지웅
 */
public interface ReAuthenticationStrategy<K,V> {
  public void reIssue(K key,V value);
}
