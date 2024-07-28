package com.knowway.auth.manager;

import com.knowway.auth.exception.AuthException;
import com.knowway.user.entity.Member;
import com.knowway.user.repository.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
public class UserAuthenticationManager implements AuthenticationManager {

  private PasswordEncoder encoder;
  private MemberRepository memberRepository;

  public UserAuthenticationManager(MemberRepository memberRepository, PasswordEncoder encoder) {
    this.memberRepository = memberRepository;
    this.encoder = encoder;
  }



  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = authentication.getPrincipal().toString();
    String password = authentication.getCredentials().toString();

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> {
          throw new AuthException("존재하지 않은 이메일 입니다.");
        });
    if (!encoder.matches(password, member.getPassword())) {
      throw new AuthException("계정 정보가 불일치 합니다.");
    }
    return new UsernamePasswordAuthenticationToken(member.getId(),member.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())));
  }
}
