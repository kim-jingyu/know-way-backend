package com.knowway.auth.service;

import com.knowway.auth.exception.AuthException;
import com.knowway.auth.handler.AccessTokenHandler;
import com.knowway.auth.util.TypeConvertor;
import com.knowway.auth.vo.AuthRequestHeaderPrefix;
import com.knowway.auth.vo.ClaimsKey;
import com.knowway.auth.vo.RequestHeaderNaming;
import com.knowway.user.repository.MemberRepository;
import com.knowway.user.vo.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class RestfulAuthService<K, USERID extends Long> implements AuthService {

  private final TypeConvertor<String, K> tokenToKeyConvertor;
  private final TypeConvertor<USERID, String> userIdToSubjectConvertor;
  private final AccessTokenHandler<K> accessTokenHandler;
  protected final MemberRepository memberRepository;

  protected RestfulAuthService(
      TypeConvertor<String, K> tokenToKeyConvertor,
      TypeConvertor<USERID, String> userIdToSubjectConvertor,
      AccessTokenHandler<K> accessTokenHandler,
      MemberRepository memberRepository) {
    this.tokenToKeyConvertor = tokenToKeyConvertor;
    this.userIdToSubjectConvertor = userIdToSubjectConvertor;
    this.accessTokenHandler = accessTokenHandler;
    this.memberRepository = memberRepository;
  }

  @Override
  public void login(HttpServletRequest request, HttpServletResponse response) {
    try {
      USERID userId = (USERID) request.getAttribute(
          RequestHeaderNaming.REQUEST_HEADER_USER_ID);
      Role role = getRole();
      String accessSubject = userIdToSubjectConvertor.convert(userId);
      K key = tokenToKeyConvertor.convert(accessSubject);

      String token = accessTokenHandler.createToken(key,
          Map.of(ClaimsKey.ROLE_CLAIMS_KEY, role));
      response.setHeader(
          AuthRequestHeaderPrefix.AUTHORIZATION_HEADER,
          AuthRequestHeaderPrefix.TOKEN_PREFIX + token);

    } catch (ClassCastException e) {
      throw new AuthException("일치 하지 않은 아이디 타입입니다.");
    }
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    String token = request.getHeader(AuthRequestHeaderPrefix.AUTHORIZATION_HEADER)
        .substring(AuthRequestHeaderPrefix.TOKEN_PREFIX.length());
    accessTokenHandler.invalidateToken(token);
  }

  protected Role getRole() {
    String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .findFirst()
        .orElseThrow(() -> new AuthException("Role이 존재하지 않습니다.")).getAuthority();
    return Role.valueOf(role);

  }

}
