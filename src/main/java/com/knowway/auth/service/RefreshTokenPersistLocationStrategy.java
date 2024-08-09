package com.knowway.auth.service;

/**
 * * 작성자: 구지웅
 */
public interface RefreshTokenPersistLocationStrategy<K ,V> {
    void persist(K key,V value,long lifeTime);
    boolean isRegistered(K key);
    void delete(K key);
}
