package com.knowway.auth.handler;

import com.knowway.auth.service.AuthService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthHandler {

  private final AuthService authService;

  public void login(ServletRequest request, ServletResponse response) {
    authService.login(request, response);
  }

  public void logout(ServletRequest request, ServletResponse response) {
    authService.logout(request, response);
  }

  public void reAuthentication(ServletRequest request, ServletResponse response) {
    authService.reAuthentication(request, response);
  }


}
