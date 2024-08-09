package com.knowway.auth.controller;

import com.knowway.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication 컨트롤러
 *
 * @author 구지웅
 * @since 2024.8.1
 * @version 1.0

 */
@RequiredArgsConstructor
@RestController
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<String> login() {
    return ResponseEntity.ok().body("유저 로그인 완료");
  }

  @PostMapping("/admin")
  public ResponseEntity<Void> isUserAdmin() {
    return ResponseEntity.ok().build();
  }


  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    return ResponseEntity.ok().build();
  }


}
