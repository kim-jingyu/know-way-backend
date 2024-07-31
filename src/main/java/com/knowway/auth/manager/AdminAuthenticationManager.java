package com.knowway.auth.manager;

import com.knowway.auth.exception.AuthException;
import com.knowway.user.vo.Role;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AdminAuthenticationManager<ADMINID extends Long> implements AuthenticationManager {

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

  private Role getRole(UserDetails adminUserDetail) {
    return adminUserDetail.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(authority -> authority.contains("ADMIN"))
        .findFirst()
        .map(authority -> Role.ADMIN)
        .orElseThrow(() -> new AuthException("ADMIN의 role이 아닙니다."));
  }
}
