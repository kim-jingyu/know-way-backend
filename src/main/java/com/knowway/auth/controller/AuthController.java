package com.knowway.auth.controller;

import com.knowway.auth.service.AuthService;
import com.knowway.user.service.UserService;
import com.knowway.user.vo.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController<USERID extends Long> {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<String> login() {
    return ResponseEntity.ok().body("유저 로그인 완료");
  }

  @GetMapping("/admin")
  public ResponseEntity<Boolean> isUserAdmin(@AuthenticationPrincipal USERID userId) {
    Role role = userService.getRole(userId);
    return ResponseEntity.ok().body(role.equals(Role.ROLE_ADMIN));
  }


  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    return ResponseEntity.ok().body("로그아웃 완료");
  }


}
