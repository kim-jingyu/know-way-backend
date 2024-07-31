package com.knowway.auth.controller;

import com.knowway.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<String> login(HttpServletRequest request, HttpServletResponse response) {
    authService.login(request, response);
    return ResponseEntity.ok().body("로그인 완료");
  }

  @PostMapping("/admin/login")
  public ResponseEntity<String> adminLogin(HttpServletRequest request, HttpServletResponse response) {
    authService.login(request, response);
    return ResponseEntity.ok().body("로그인 완료");
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    return ResponseEntity.ok().body("로그아웃 완료");
  }


}
