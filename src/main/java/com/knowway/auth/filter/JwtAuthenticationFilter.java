package com.knowway.auth.filter;


import com.knowway.auth.exception.AuthException;
import com.knowway.auth.handler.AccessTokenHandler;
import com.knowway.auth.service.AccessTokenWithRefreshTokenService;
import com.knowway.auth.util.ClaimsWrapper;
import com.knowway.auth.util.TypeConvertor;
import com.knowway.auth.vo.AuthRequestHeaderPrefix;
import com.knowway.user.vo.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This is the JwtAuthenticationFilter, which can be used when there is token at header or not For
 * managing the authorization in one place this filter isn't set shouldNotFilter
 *
 * @see com.knowway.auth.config.SecurityConfig When user is authenticated set the user is
 * authenticated
 */
@AllArgsConstructor
public class JwtAuthenticationFilter<K, V> extends OncePerRequestFilter {

  /**
   * The Raw type with AccessTokenHandler is fine, just because the Generic Type is only used when
   * persisting the token, otherwise this filter just validate the token, So won't be problem
   */
  private final AccessTokenHandler accessTokenHandler;
  private final AccessTokenWithRefreshTokenService<K, V, Long> accessTokenWithRefreshTokenService;
  private final TypeConvertor<String, K> tokenToKeyConvertor;
  private final TypeConvertor<String, V> subjectToValueConvertor;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader(AuthRequestHeaderPrefix.AUTHORIZATION_HEADER);

    if (header == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = extractToken(header);
    if (token == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    try {
      if (!accessTokenHandler.isValidToken(token)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      Role role = accessTokenHandler.getRole(token);
      setSecurityContext(accessTokenHandler.getSubject(token), role);
      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException expiredJwtException) {
      handleExpiredToken(response, token, expiredJwtException);
    } catch (AuthException | MalformedJwtException | StringIndexOutOfBoundsException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  private String extractToken(String header) {
    if (header.startsWith(AuthRequestHeaderPrefix.TOKEN_PREFIX)) {
      return header.substring(AuthRequestHeaderPrefix.TOKEN_PREFIX.length());
    }
    return null;
  }

  private void handleExpiredToken(HttpServletResponse response,
      String oldToken, ExpiredJwtException expiredJwtException) {
    String newToken = accessTokenWithRefreshTokenService.reAuthentication(
        tokenToKeyConvertor.convert(oldToken),
        subjectToValueConvertor.convert(expiredJwtException.getClaims().getSubject()),
        ClaimsWrapper.of(expiredJwtException.getClaims())
    );

    response.addHeader(AuthRequestHeaderPrefix.AUTHORIZATION_HEADER,
        AuthRequestHeaderPrefix.TOKEN_PREFIX + newToken);
    response.setStatus(HttpServletResponse.SC_OK);
  }


  private void setSecurityContext(String subject, Role role) {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(Long.parseLong(subject), null,
            Collections.singletonList(new SimpleGrantedAuthority(role.name()))));
  }
}