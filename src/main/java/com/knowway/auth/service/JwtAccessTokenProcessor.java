package com.knowway.auth.service;


import com.knowway.auth.exception.AuthException;
import com.knowway.auth.util.JwtUtil;
import com.knowway.auth.vo.ClaimsKey;
import com.knowway.user.vo.Role;
import io.jsonwebtoken.Claims;
import java.util.Map;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class JwtAccessTokenProcessor {

  public String accessKey;
  public long accessKeyLifeTime;
  private AccessTokenInvalidationStrategy<String> accessTokenInvalidationStrategy;


  public boolean isValidToken(String token) {
    return isValidTokenHelper(token);
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

  public Role getRole(String token) {
    if (!isValidToken(token)) {
      throw new AuthException("토큰의 정보가 일치하지 않습니다.");
    }
    try{
    Claims claims = JwtUtil.extractTokenClaims(token, accessKey);
    return Role.valueOf((String)claims.get(ClaimsKey.ROLE_CLAIMS_KEY));
    }
    catch(NullPointerException nullPointerException){
      throw new AuthException("Role이 토큰에 존재하지 않습니다.");
    }
    catch(ClassCastException classCastException){
      throw new AuthException("Role Type이 일치하지 않습니다.");
    }

  }

  public String getSubject(String token) {
    if (!isValidToken(token)) {
      throw new AuthException("토큰의 정보가 일치하지 않습니다.");
    }
    return JwtUtil.extractTokenSubject(token, accessKey);
  }

  private boolean isValidTokenHelper(String token) throws AuthException {

    return !accessTokenInvalidationStrategy.isRegistered(token) && JwtUtil.isTokenValid(token,
        accessKey);

  }

}


