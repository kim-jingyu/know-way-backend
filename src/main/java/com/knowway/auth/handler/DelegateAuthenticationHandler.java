package com.knowway.auth.handler;

import com.knowway.auth.service.AuthTokenStrategy;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DelegateAuthenticationHandler {
  private final List<AuthTokenStrategy> authTokenStrategyList;

  public void login(ServletRequest request, ServletResponse response){
    for(AuthTokenStrategy strategy: authTokenStrategyList){
      strategy.issue(request,response);
    }
  }
  public void logout(ServletRequest request, ServletResponse response) {
      for(AuthTokenStrategy strategy: authTokenStrategyList){
      strategy.invalidate(request,response);
    }

  }
  public void reAuthentication(ServletRequest request, ServletResponse response) {
    for(AuthTokenStrategy strategy: authTokenStrategyList){
      strategy.reIssue(request,response);
    }
  }

}
