package com.knowway.auth.service;

import com.knowway.auth.exception.AuthException;
import com.knowway.auth.handler.AccessTokenHandler;
import com.knowway.auth.handler.RefreshTokenHandler;
import com.knowway.auth.util.ClaimsWrapper;
import com.knowway.auth.util.TypeConvertor;
import com.knowway.auth.vo.AuthRequestHeaderPrefix;
import com.knowway.auth.vo.ClaimsKey;
import com.knowway.auth.vo.RequestHeaderNaming;
import com.knowway.user.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenWithRefreshTokenService<K, V, USERID> extends RestfulAuthService<USERID> {

  private final TypeConvertor<String, K> tokenToKeyConvertor;
  private final TypeConvertor<USERID, V> userIdToValueConvertor;
  private final RefreshTokenHandler<K, V> refreshTokenHandler;
  private final AccessTokenHandler valueAccessTokenHandler;

  @Autowired
  public AccessTokenWithRefreshTokenService(
      @Qualifier("tokenToKeyConverter") TypeConvertor<String, K> tokenToKeyConvertor,
      @Qualifier("userIdToSubjectConverter") TypeConvertor<USERID, String> userIdToSubjectConvertor,
      @Qualifier("memberRepository") MemberRepository memberRepository,
      @Qualifier("userIdToValueConverter") TypeConvertor<USERID, V> userIdToValueConvertor,
      @Qualifier("refreshTokenHandler") RefreshTokenHandler<K, V> refreshTokenHandler,
      @Qualifier("accessTokenHandler") AccessTokenHandler valueAccessTokenHandler) {

    super(userIdToSubjectConvertor, valueAccessTokenHandler,memberRepository);
    this.tokenToKeyConvertor = tokenToKeyConvertor;
    this.userIdToValueConvertor = userIdToValueConvertor;
    this.refreshTokenHandler = refreshTokenHandler;
    this.valueAccessTokenHandler = valueAccessTokenHandler;
  }

  @Override
  public void login(HttpServletRequest request, HttpServletResponse response) {
    super.login(request, response);

    String token = response.getHeader(AuthRequestHeaderPrefix.AUTHORIZATION_HEADER)
        .substring(AuthRequestHeaderPrefix.TOKEN_PREFIX.length());

    USERID userId = (USERID) request.getAttribute(RequestHeaderNaming.REQUEST_HEADER_USER_ID);

    K key = tokenToKeyConvertor.convert(token);
    V value = userIdToValueConvertor.convert(userId);

    refreshTokenHandler.persistToken(key, value);
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    super.logout(request, response);
  }

  public String reAuthentication(K oldKey, V value, ClaimsWrapper claims) {
    Object sub = claims.get("sub");
    String accessToken = valueAccessTokenHandler.createToken((String) sub,
        Map.of(ClaimsKey.ROLE_CLAIMS_KEY, claims.get(ClaimsKey.ROLE_CLAIMS_KEY)));
    K newKey = tokenToKeyConvertor.convert(accessToken);

    if (!refreshTokenHandler.isValid(oldKey)) throw new AuthException("Expired");
    refreshTokenHandler.invalidate(oldKey);
    refreshTokenHandler.persistToken(newKey, value);

    return accessToken;
  }


}

