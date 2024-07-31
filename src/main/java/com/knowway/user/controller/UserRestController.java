package com.knowway.user.controller;


import com.knowway.user.dto.EmailDuplicationCheckRequset;
import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;
import com.knowway.user.service.UserDuplicationChecker;
import com.knowway.user.dto.UserProfileResponse;
import com.knowway.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
<<<<<<< Updated upstream:src/main/java/com/knowway/user/controller/UserRestController.java
=======
import org.springframework.data.domain.Page;
>>>>>>> Stashed changes:src/main/java/com/knowway/user/controller/UserController.java
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
<<<<<<< Updated upstream:src/main/java/com/knowway/user/controller/UserRestController.java
public class UserRestController {
=======
public class UserController<USERID extends Long> {
>>>>>>> Stashed changes:src/main/java/com/knowway/user/controller/UserController.java

  private final UserService userService;
  private final UserDuplicationChecker duplicationChecker;

<<<<<<< Updated upstream:src/main/java/com/knowway/user/controller/UserRestController.java
=======

>>>>>>> Stashed changes:src/main/java/com/knowway/user/controller/UserController.java
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


  @GetMapping
  public ResponseEntity<UserProfileResponse> getProfileInfo(
      @AuthenticationPrincipal USERID userId) {
    return ResponseEntity.ok().body(userService.getUserProfileInfo(userId));

  }

  @GetMapping("/user/records")
  public Page<UserRecordResponse> getUserRecordHistory(@AuthenticationPrincipal USERID userId,
      @RequestParam int page,
      @RequestParam int size) {
    return userService.getUserRecordHistory(userId, page, size);
  }


}


