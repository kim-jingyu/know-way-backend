package com.knowway.auth;

import com.knowway.auth.handler.AuthHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

  private final AuthHandler authenticationHandler;

  @PostMapping("/login")
  public ResponseEntity<String> signUp(HttpServletRequest request, HttpServletResponse response) {
    authenticationHandler.login(request,response);
    return ResponseEntity.ok().body("로그인 완료");
  }

}
