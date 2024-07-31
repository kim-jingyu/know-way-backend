package com.knowway.auth.service;

public interface ReAuthenticationStrategy<K,V> {
  public void reIssue(K key,V value);
}
