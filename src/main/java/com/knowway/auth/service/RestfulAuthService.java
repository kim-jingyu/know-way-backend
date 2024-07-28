package com.knowway.auth.service;

import com.knowway.auth.handler.DelegateAuthenticationHandler;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RestfulAuthService implements AuthService {

  private final DelegateAuthenticationHandler delegator;

  @Override
  public void login(ServletRequest request, ServletResponse response) {
    delegator.login(request, response);
  }

  @Override
  public void logout(ServletRequest request, ServletResponse response) {
    delegator.logout(request, response);
  }

  @Override
  public void reAuthentication(ServletRequest request, ServletResponse response) {
    delegator.reAuthentication(request, response);
  }
}
