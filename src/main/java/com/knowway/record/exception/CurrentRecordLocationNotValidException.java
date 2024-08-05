package com.knowway.record.exception;

public class CurrentRecordLocationNotValidException extends
    RecordException {

  public static final String MESSAGE = "현재 위치가 백화점 내에 있지 않습니다. 다시 시도해주세요.";


  public CurrentRecordLocationNotValidException() {
    super(MESSAGE);
  }
}
