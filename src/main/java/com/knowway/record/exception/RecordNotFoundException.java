package com.knowway.record.exception;

public class RecordNotFoundException extends RecordException {

  private static final String MESSAGE ="존재하지 않은 레코드입니다.";

  public RecordNotFoundException() {
    super(MESSAGE);
  }
}
