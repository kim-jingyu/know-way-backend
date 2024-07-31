package com.knowway.user.service;

import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.mapper.UserMapper;
import com.knowway.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserDuplicationChecker userDuplicationChecker;
  private final MemberRepository memberRepository;
  private final PasswordEncoder encoder;

  @Override
  public void signUp(UserSignUpRequest signUpDto) {
    userDuplicationChecker.emailDuplicationChecker(signUpDto.getEmail());
    memberRepository.save(
        UserMapper.INSTANCE.toMember(signUpDto, encoder.encode(signUpDto.getPassword())));
  }
}
