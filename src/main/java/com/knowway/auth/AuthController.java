package com.knowway.auth;

import com.knowway.auth.handler.AuthHandler;
import com.knowway.user.dto.UserSignUpDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.AuthenticationHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

  private final AuthHandler authenticationHandler;

  @PostMapping("/login")
  public ResponseEntity<String> signUp(@AuthenticationPrincipal Long userId) {

    return ResponseEntity.ok().body("로그인 완료");

  }
}
