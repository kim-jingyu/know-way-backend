package com.knowway.record.exception;

abstract class RecordException extends RuntimeException {

  public RecordException() {
  }

  public RecordException(String message) {
    super(message);
  }


}

