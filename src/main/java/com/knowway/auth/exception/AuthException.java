package com.knowway.auth.exception;
/**
 * * 작성자: 구지웅
 */
public class AuthException extends RuntimeException{

  public AuthException(String message) {
    super(message);
  }

  public AuthException(String message, Throwable cause) {
    super(message, cause);
  }
}
