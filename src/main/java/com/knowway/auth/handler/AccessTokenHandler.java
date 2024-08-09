package com.knowway.auth.handler;

import com.knowway.auth.service.JwtAccessTokenProcessor;
import com.knowway.user.vo.Role;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/**
 * AccessTokenHandler
 *
 * @author 구지웅
 * @since 2024.8.1
 * @version 1.0

 */
@RequiredArgsConstructor
public class AccessTokenHandler {

  private final JwtAccessTokenProcessor jwtAccessTokenProcessor;


  public String createToken(String subject,
      Map<String, Object> claimList) {
    return jwtAccessTokenProcessor.createAccessToken(subject, claimList);
  }


  public void invalidateToken(String accessToken) {
    jwtAccessTokenProcessor.invalidateToken(accessToken);
  }


  public boolean isValidToken(String accessToken) {
    return jwtAccessTokenProcessor.isValidToken(accessToken);
  }

  public String getSubject(String accessToken) {
    return jwtAccessTokenProcessor.getSubject(accessToken);
  }

  public Role getRole(String accessToken) {
    return jwtAccessTokenProcessor.getRole(accessToken);
  }
}