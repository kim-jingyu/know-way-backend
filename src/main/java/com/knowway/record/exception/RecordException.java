package com.knowway.record.exception;

abstract class RecordException extends RuntimeException {

  protected RecordException(String message) {
    super(message);
  }

}

