package com.knowway.departmentstore.exception;

public class DepartmentStoreFloorNotFoundException extends DepartmentStoreException {
    private static final String MSG = "백화점 층 정보를 찾지 못했습니다.";
    public DepartmentStoreFloorNotFoundException() {
        super(MSG);
    }
}
