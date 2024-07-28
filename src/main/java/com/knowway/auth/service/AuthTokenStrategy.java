package com.knowway.auth.service;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthTokenStrategy {
  public void issue(ServletRequest request, ServletResponse response);
  public void invalidate(ServletRequest request, ServletResponse response);
  public void reIssue(ServletRequest request, ServletResponse response);
}
