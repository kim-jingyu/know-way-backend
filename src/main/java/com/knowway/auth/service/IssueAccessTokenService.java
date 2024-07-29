package com.knowway.auth.service;

import com.knowway.auth.handler.TokenHandler;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IssueAccessTokenService implements AuthTokenStrategy {

  private final TokenHandler tokenHandler;

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
