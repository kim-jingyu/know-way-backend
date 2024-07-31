package com.knowway.user.dto;

import com.knowway.user.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserSignUpRequest {
  @NotNull
  @Email(message = "올바르지 않은 이메일 형식입니다.")
  private String email;
  @NotNull
  @Password
  private String password;

}
