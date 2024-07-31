package com.knowway.auth.handler;


import com.knowway.auth.service.RestfulAuthService;
import com.knowway.auth.vo.RequestHeaderUserIdNaming;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;



@RequiredArgsConstructor
public class SystemAuthenticationSuccessHandler<K,USERID extends Long> implements AuthenticationSuccessHandler {

  private final RestfulAuthService<K,USERID> restfulAuthService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) {
    onAuthenticationSuccess(request, response, authentication);
  }

  /**
   * @TokenHandler.createToken(authentication.getName(),null) will not throw an NullPointException
   * Null check at TokenHandler
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    request.setAttribute(RequestHeaderUserIdNaming.REQUEST_HEADER_USER_ID,authentication.getPrincipal());
    restfulAuthService.login(request,response);
  }

}
