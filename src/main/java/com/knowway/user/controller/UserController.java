package com.knowway.user.controller;


import com.knowway.auth.dto.UserChatMemberIdResponse;
import com.knowway.user.dto.EmailDuplicationCheckRequset;
import com.knowway.user.dto.UserProfileResponse;
import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.service.UserDuplicationChecker;
import com.knowway.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/users")
@RestController
public class UserController<USERID extends Long> {

  private final UserService userService;
  private final UserDuplicationChecker duplicationChecker;


  @PostMapping("/emails")
  public ResponseEntity<String> emailDuplicationCheck(
      @RequestBody @Valid EmailDuplicationCheckRequset emailDuplicationCheckRequset) {
    duplicationChecker.emailDuplicationChecker(emailDuplicationCheckRequset.getEmail());
    return ResponseEntity.ok().body("중복되지 않은 이메일입니다.");
  }

  @PostMapping
  public ResponseEntity<String> signUp(
      @RequestBody @Valid UserSignUpRequest signUpDto) {
    userService.signUp(signUpDto);
    return ResponseEntity.ok().body("회원가입 완료");

  }

  @GetMapping("/chat-id")
  public ResponseEntity<UserChatMemberIdResponse> getUserChatId(@AuthenticationPrincipal USERID userId) {
    return ResponseEntity.ok().body(userService.getUserChatMemberId(userId));

  }


  @GetMapping
  public ResponseEntity<UserProfileResponse> getProfileInfo(
      @AuthenticationPrincipal USERID userId) {
    return ResponseEntity.ok().body(userService.getUserProfileInfo(userId));

  }

  @GetMapping("/records")
  public ResponseEntity<List<UserRecordResponse>> getUserRecordHistory(
      @AuthenticationPrincipal USERID userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok()
        .body(userService.getUserRecordHistory(userId, page, size).getContent());
  }


}


