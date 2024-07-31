package com.knowway.auth.service;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public interface AuthTokenStrategy<K,V> {
  public void issue(ServletRequest request, ServletResponse response);
  public void invalidate(ServletRequest request, ServletResponse response);
}
