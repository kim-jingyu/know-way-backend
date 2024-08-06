package com.knowway.departmentstore.exception;

public class DepartmentStoreNotFoundException extends DepartmentStoreException {
    private static final String MSG = "백화점 정보를 찾지 못했습니다.";
    public DepartmentStoreNotFoundException() {
        super(MSG);
    }
}
