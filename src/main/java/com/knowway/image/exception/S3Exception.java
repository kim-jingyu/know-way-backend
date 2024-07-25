package com.knowway.image.exception;

public class S3Exception extends RuntimeException {
    public S3Exception() {
        super("S3 처리중 오류가 발생했습니다.");
    }
}
