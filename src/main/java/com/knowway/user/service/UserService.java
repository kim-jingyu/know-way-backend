package com.knowway.user.service;

import com.knowway.user.dto.UserRecordResponse;
import com.knowway.user.dto.UserSignUpRequest;

public interface UserService {
  void signUp(UserSignUpRequest signUpDto);
  UserRecordResponse getUserRecordHistory(Long userid);
  UserProfileResponse getUserProfileInfo(Long userid);
}
