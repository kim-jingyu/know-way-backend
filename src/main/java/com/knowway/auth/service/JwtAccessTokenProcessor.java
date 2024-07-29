package com.knowway.auth.service;


import com.knowway.auth.exception.AuthException;
import com.knowway.auth.util.JwtUtil;
import com.knowway.auth.vo.JwtType;
import java.util.Map;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class JwtAccessTokenProcessor {

  public String accessKey;
  public long accessKeyLifeTime;
  private AccessTokenInvalidationStrategy accessTokenInvalidationStrategy;
  private static final String INVALID_ACCESS_TOKEN_MESSAGE ="ACCESS 토큰이 아닙니다.";


  public boolean isValidToken(JwtType keyType, String token) {
    if (keyType.equals(JwtType.access)) {
      return validateAccessToken(token);
    }
    throw new AuthException(INVALID_ACCESS_TOKEN_MESSAGE);
  }

  public void invalidateToken(String token) {
    accessTokenInvalidationStrategy.invalidate(token);
  }

  private boolean validateAccessToken(String token) throws AuthException {
    return !accessTokenInvalidationStrategy.isRegistered(token) && JwtUtil.isTokenValid(token, accessKey);
  }

  public String createAccessToken(String subject, Map<String, Object> claimsList) {
    if (claimsList == null) {
      return JwtUtil.generateToken(subject, accessKey, accessKeyLifeTime);
    }
    return JwtUtil.generateTokenWithClaims(subject, accessKey,
        accessKeyLifeTime, claimsList);
  }

  public String getSubject(JwtType jwtType,String token){
    if(jwtType.equals(JwtType.access)){
    return JwtUtil.extractTokenSubject(token,accessKey);
    }
    throw new AuthException(INVALID_ACCESS_TOKEN_MESSAGE);
  }
  }


