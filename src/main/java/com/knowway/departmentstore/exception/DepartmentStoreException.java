package com.knowway.departmentstore.exception;

abstract class DepartmentStoreException extends RuntimeException {
    public DepartmentStoreException(String message) {
        super(message);
    }
}
