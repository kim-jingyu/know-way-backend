package com.knowway.departmentstore.exception;

public class DepartmentStoreFloorNotFoundException extends RuntimeException {
    public DepartmentStoreFloorNotFoundException() {
        super("백화점 층 정보를 찾지 못했습니다.");
    }
}
