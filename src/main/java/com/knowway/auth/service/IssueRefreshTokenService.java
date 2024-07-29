package com.knowway.auth.service;

import com.knowway.auth.handler.TokenHandler;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class IssueRefreshTokenService implements AuthTokenStrategy {

  private final TokenHandler tokenHandler;
  private final RefreshTokenPersistLocationStrategy locationStrategy;
  private final RefreshTokenIssueStrategy reIssueStrategy;

  @Override
  public void issue(ServletRequest request, ServletResponse response) {

  }

  @Override
  public void invalidate(ServletRequest request, ServletResponse response) {

  }

  @Override
  public void reIssue(ServletRequest request, ServletResponse response) {

  }
}
