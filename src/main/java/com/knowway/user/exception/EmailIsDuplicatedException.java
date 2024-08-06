package com.knowway.user.exception;

public class EmailIsDuplicatedException extends UserException {
  private static final String MESSAGE ="중복된 이메일입니다.";
  public EmailIsDuplicatedException() {
    super(MESSAGE);
  }
}
