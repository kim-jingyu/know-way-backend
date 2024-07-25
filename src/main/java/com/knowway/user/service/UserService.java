package com.knowway.user.service;

import com.knowway.user.dto.UserSignUpDto;

public interface UserService {
  void signUp(UserSignUpDto signUpDto);
}
