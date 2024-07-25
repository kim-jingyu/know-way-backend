package com.knowway.user.service;

import com.knowway.user.dto.UserSignUpDto;
import com.knowway.user.mapper.UserMapper;
import com.knowway.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KnowWayService implements UserService {

  private final UserDuplicationChecker userDuplicationChecker;
  private final MemberRepository memberRepository;
  private final PasswordEncoder encoder;

  @Override
  public void signUp(UserSignUpDto signUpDto) {
    userDuplicationChecker.emailDuplicationChecker(signUpDto.getEmail());
    memberRepository.save(
        UserMapper.INSTANCE.toMember(signUpDto, encoder.encode(signUpDto.getPassword())));
  }
}
