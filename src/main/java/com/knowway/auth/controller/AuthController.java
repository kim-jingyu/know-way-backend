package com.knowway.auth.controller;

import com.knowway.auth.dto.UserChatMemberIdResponse;
import com.knowway.auth.service.AuthService;
import com.knowway.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController<USERID extends Long> {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserChatMemberIdResponse> login(HttpServletRequest request,
      HttpServletResponse response, @AuthenticationPrincipal USERID userId) {
    authService.login(request, response);
    UserChatMemberIdResponse loginResponse = userService.getUserChatMemberId(userId);
    return ResponseEntity.ok().body(loginResponse);
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
