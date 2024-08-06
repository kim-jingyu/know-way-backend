package com.knowway.auth.handler;


import com.knowway.auth.service.RestfulAuthService;
import com.knowway.auth.vo.RequestHeaderNaming;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@RequiredArgsConstructor
public class SystemAuthenticationSuccessHandler<USERID> implements AuthenticationSuccessHandler {

  private final RestfulAuthService<USERID> restfulAuthService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) {
    onAuthenticationSuccess(request, response, authentication);
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    request.setAttribute(RequestHeaderNaming.REQUEST_HEADER_USER_ID, authentication.getPrincipal());
    restfulAuthService.login(request, response);
  }



}
