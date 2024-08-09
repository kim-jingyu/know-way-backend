package com.knowway.auth.manager;

import com.knowway.auth.exception.AuthException;
import com.knowway.user.vo.Role;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AdminAuthenticationProvider
 * @author 구지웅
 * @since 2024.8.1
 * @version 1.0

 */
@RequiredArgsConstructor
public class AdminAuthenticationProvider<ADMINID extends Long> implements AuthenticationProvider {

  private final UserDetailsService userDetailService;
  private final PasswordEncoder encoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails adminUserDetail = userDetailService.loadUserByUsername(
        authentication.getPrincipal().toString());

    return new UsernamePasswordAuthenticationToken(ADMINID.valueOf(adminUserDetail.getUsername()),
        encoder.encode(
            adminUserDetail.getPassword()),
        Collections.singleton(new SimpleGrantedAuthority(getRole(adminUserDetail).name())));

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private Role getRole(UserDetails adminUserDetail) {
    return adminUserDetail.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(authority -> authority.contains("ADMIN"))
        .findFirst()
        .map(authority -> Role.ROLE_ADMIN)
        .orElseThrow(() -> new AuthException("ADMIN의 role이 아닙니다."));
  }
}
