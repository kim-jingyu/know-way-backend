package com.knowway.user.service;

import com.knowway.user.exception.UserException;
import com.knowway.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * This is the helper class for handling the user duplication check
 */
@RequiredArgsConstructor
@Service
public class UserDuplicationChecker {

  private final MemberRepository memberRepository;

   public void emailDuplicationChecker(String email){
    if(memberRepository.findByEmail(email).isPresent()) throw new UserException("이미 존재하는 이메일입니다.");

  }

}
