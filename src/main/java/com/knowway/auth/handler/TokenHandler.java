package com.knowway.auth.handler;



import com.knowway.auth.exception.AuthException;
import com.knowway.auth.service.JwtAccessTokenProcessor;
import com.knowway.auth.vo.JwtType;
import java.util.Map;
import lombok.AllArgsConstructor;


/**
 * This is the TokenHandler which is adapted the Facade pattern. when Token type is created
 * There should be another TokenProcessor
 * @see JwtType
 */

@AllArgsConstructor
public class TokenHandler {
  private static final String INVALID_TOKEN_MESSAGE ="존재하지 않은 토큰 타입입니다.";
  private JwtAccessTokenProcessor jwtAccessTokenProcessor;
  private RefreshTokenProccessor refreshTokenProccessor;


  public String createToken(JwtType jwtType, String id,
      Map<String, Object> claimList) {
    if (jwtType.equals(JwtType.access)) {
      return jwtAccessTokenProcessor.createAccessToken(id, claimList);
    }
    throw new AuthException(INVALID_TOKEN_MESSAGE);
  }

  public void invalidateToken(JwtType jwtType, String value) {
    if (jwtType.equals(JwtType.access)) {
      jwtAccessTokenProcessor.invalidateToken(value);
    }
    throw new AuthException(INVALID_TOKEN_MESSAGE);
  }

  public boolean isValidToken(JwtType jwtType, String value) {
    if (jwtType.equals(JwtType.access)) {
      return jwtAccessTokenProcessor.isValidToken(jwtType, value);
    }
    throw new AuthException(INVALID_TOKEN_MESSAGE);
  }

  public String getUserId(JwtType jwtType, String value) {
    if (jwtType.equals(JwtType.access)) {
      return jwtAccessTokenProcessor.getSubject(jwtType, value);
    }
    throw new AuthException(INVALID_TOKEN_MESSAGE);
  }

}
