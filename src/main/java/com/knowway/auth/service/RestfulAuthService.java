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

public abstract class RestfulAuthService<K, USERID> implements AuthService {

  private final TypeConvertor<USERID, String> userIdToSubjectConvertor;
  private final AccessTokenHandler accessTokenHandler;
  protected final MemberRepository memberRepository;

  protected RestfulAuthService(
      TypeConvertor<USERID, String> userIdToSubjectConvertor,
      AccessTokenHandler accessTokenHandler,
      MemberRepository memberRepository) {
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

      String token = accessTokenHandler.createToken(accessSubject,
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
