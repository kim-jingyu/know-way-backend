package com.knowway.departmentstore.exception;

public class DepartmentStoreNotFoundException extends RuntimeException {
    public DepartmentStoreNotFoundException() {
        super("백화점 정보를 찾지 못했습니다.");
    }
}
