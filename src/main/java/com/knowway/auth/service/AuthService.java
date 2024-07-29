package com.knowway.auth.service;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.stereotype.Service;

public interface AuthService {
  public void login(ServletRequest request, ServletResponse response);
  public void logout(ServletRequest request, ServletResponse response);
  public void reAuthentication(ServletRequest request, ServletResponse response);

}
