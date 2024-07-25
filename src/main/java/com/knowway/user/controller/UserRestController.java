package com.knowway.user.controller;


import com.knowway.user.dto.EmailDto;
import com.knowway.user.dto.UserSignUpDto;
import com.knowway.user.service.UserDuplicationChecker;
import com.knowway.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping(value = "/users")
@RestController
public class UserRestController {

  private final UserService userService;
  private final UserDuplicationChecker duplicationChecker;

  @PostMapping("/emails")
  public ResponseEntity<String> emailDuplicationCheck(
      @RequestBody @Valid EmailDto emailDto) {
    duplicationChecker.emailDuplicationChecker(emailDto.getEmail());
    return ResponseEntity.ok().body("중복되지 않은 이메일입니다.");
  }

  @PostMapping
  public ResponseEntity<String> signUp(
      @RequestBody @Valid UserSignUpDto signUpDto) {
    userService.signUp(signUpDto);
    return ResponseEntity.ok().body("회원가입 완료");

  }





}


