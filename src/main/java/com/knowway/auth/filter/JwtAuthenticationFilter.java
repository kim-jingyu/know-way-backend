package com.knowway.auth.filter;


import com.knowway.auth.exception.AuthException;
import com.knowway.auth.handler.AccessTokenHandler;
import com.knowway.auth.util.JsonBinderUtil;
import com.knowway.auth.vo.AuthRequestHeaderPrefix;
import com.knowway.auth.vo.ExtractHeaderKeyByRequest;
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
 * This is the JwtAuthenticationFilter, which can be used when there is token at header or not
 * For managing the authorization in one place this filter isn't set shouldNotFilter
 * @see com.knowway.auth.config.SecurityConfig
 * When user is authenticated set the user is authenticated
 * @see JwtAuthenticationFilter#setSecurityContext(String)
 */
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * The Raw type with AccessTokenHandler is fine, just because the Generic Type is only used
   * when persisting the token, otherwise this filter just validate the token, So won't be problem
   */
  private final AccessTokenHandler accessTokenHandler;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader(AuthRequestHeaderPrefix.AUTHORIZATION_HEADER);

    if(header!=null){
    try {
      String token = ExtractHeaderKeyByRequest.extractKey(request, AuthRequestHeaderPrefix.AUTHORIZATION_HEADER).substring(7);
      if (!accessTokenHandler.isValidToken(token)) {
        response.setStatus(401);
      } else {
        Role role = accessTokenHandler.getRole(token);
        setSecurityContext(accessTokenHandler.getSubject(token), role);
        filterChain.doFilter(request, response);
      }
    } catch (ExpiredJwtException expiredJwtException) {
      JsonBinderUtil.setResponseWithJson(response, 401, expiredJwtException.getClaims());
      filterChain.doFilter(request,response);
    } catch (AuthException | MalformedJwtException | StringIndexOutOfBoundsException e) {
      response.setStatus(401);
    }
    }
    else filterChain.doFilter(request,response);

  }

  private void setSecurityContext(String subject, Role role) {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(Long.parseLong(subject),null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role.name()))));
  }
}