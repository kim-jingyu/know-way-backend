package com.knowway.auth.service;


public interface RefreshTokenPersistLocationStrategy {
    void persist(String token);
    boolean isRegistered(String token);
    void delete(String token);
}
