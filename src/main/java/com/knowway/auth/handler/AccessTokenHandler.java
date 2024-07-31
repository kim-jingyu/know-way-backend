package com.knowway.auth.handler;

import com.knowway.auth.service.JwtAccessTokenProcessor;
import com.knowway.auth.util.TypeConvertor;
import com.knowway.user.vo.Role;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessTokenHandler<K> {

  private final TypeConvertor<K,String> keyToStringConvertor;
  private final JwtAccessTokenProcessor jwtAccessTokenProcessor;


  public String createToken(K subject,
      Map<String, Object> claimList) {
    return jwtAccessTokenProcessor.createAccessToken(keyToStringConvertor.convert(subject), claimList);
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