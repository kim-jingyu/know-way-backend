package com.knowway.record.exception;

abstract class RecordException extends RuntimeException {

  public RecordException() {
  }

  public RecordException(String message) {
    super(message);
  }


}

public class RecordException extends RuntimeException {
    public RecordException(String message) {
        super(message);
    }

    public RecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
