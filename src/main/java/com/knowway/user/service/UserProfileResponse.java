package com.knowway.user.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserProfileResponse {
  private String email;
  private Long pointTotal;
}
