package com.knowway.record.exception;

public class RecordAlreadySelectedByAdminException extends
    RecordException {

  public static final String  MESSAGE ="이미 관리자에게 승인된 레코드는 삭제할 수 없습니다.";
  public RecordAlreadySelectedByAdminException() {
  }

  public RecordAlreadySelectedByAdminException(String message) {
    super(MESSAGE);
  }
}
