package com.knowway.record.exception;


public class RecordAlreadySelectedByAdminException extends
    RecordException {

  private static final String  MESSAGE ="이미 관리자에게 승인된 레코드는 삭제할 수 없습니다.";
  public RecordAlreadySelectedByAdminException() {
    super(MESSAGE);
  }

}
