package com.knowway.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowway.auth.dto.UserLoginRequest;
import com.knowway.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public UserAuthenticationFilter(@Qualifier("parentManager") AuthenticationManager authenticationManager) {
    super(authenticationManager);
    this.authenticationManager = authenticationManager;
  }

  private UserLoginRequest getLoginDtoFromRequest(HttpServletRequest request)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(request.getInputStream(),
        UserLoginRequest.class);
  }


  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response)
      throws AuthenticationException {
    try {
      UserLoginRequest dto = getLoginDtoFromRequest(request);
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(),null));
    } catch (BadCredentialsException | IOException | AuthException e) {
      response.setStatus(401);
      throw new BadCredentialsException("일치하지 않은 이메일과 패스워드입니다.");
    }
  }


}