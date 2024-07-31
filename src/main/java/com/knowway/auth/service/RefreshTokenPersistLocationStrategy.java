package com.knowway.auth.service;


public interface RefreshTokenPersistLocationStrategy<K ,V> {
    void persist(K key,V value,long lifeTime);
    boolean isRegistered(K key);
    void delete(K key);
}
