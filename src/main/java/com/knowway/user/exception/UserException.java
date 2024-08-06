package com.knowway.user.exception;

 abstract class UserException extends RuntimeException {
   protected UserException(String message) {
     super(message);
   }
 }
