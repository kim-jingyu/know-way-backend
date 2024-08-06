package com.knowway.user.exception;

public class UserNotFoundException extends UserException {

  private static final String MESSAGE = "존재 하지 않은 유저입니다.";

  public UserNotFoundException() {
    super(MESSAGE);
  }
}
