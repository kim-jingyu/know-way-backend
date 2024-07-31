package com.knowway.auth.manager;

import com.knowway.auth.exception.AuthException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
        Collections.singleton(new SimpleGrantedAuthority(getRole(adminUserDetail))));

  }

  private String getRole(UserDetails adminUserDetail) {
    return adminUserDetail.getAuthorities().stream().findFirst()
        .orElseThrow(() -> new AuthException("Admin의 Role이 존재하지 않습니다.")).getAuthority();
  }
}
