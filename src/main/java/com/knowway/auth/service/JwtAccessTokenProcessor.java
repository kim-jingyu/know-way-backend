package com.knowway.auth.service;


import com.knowway.auth.exception.AuthException;
import com.knowway.auth.util.JwtUtil;
import java.util.Map;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class JwtAccessTokenProcessor {

  public String accessKey;
  public long accessKeyLifeTime;
  private AccessTokenInvalidationStrategy<String> accessTokenInvalidationStrategy;


  public boolean isValidToken(String token) {
    return validateAccessToken(token);
  }

  public void invalidateToken(String token) {
    accessTokenInvalidationStrategy.invalidate(token);
  }


  public String createAccessToken(String subject, Map<String, Object> claimsList) {
    if (claimsList == null) {
      return JwtUtil.generateToken(subject, accessKey, accessKeyLifeTime);
    }
    return JwtUtil.generateTokenWithClaims(subject, accessKey,
        accessKeyLifeTime, claimsList);
  }

  public String getSubject(String token) {
    return JwtUtil.extractTokenSubject(token, accessKey);
  }

  private boolean validateAccessToken(String token) throws AuthException {

    return !accessTokenInvalidationStrategy.isRegistered(token) && JwtUtil.isTokenValid(token,
        accessKey);

  }

}


