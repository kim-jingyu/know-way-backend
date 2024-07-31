package com.knowway.user.service;

import com.knowway.user.dto.UserSignUpRequest;

public interface UserService {
  void signUp(UserSignUpRequest signUpDto);
}
