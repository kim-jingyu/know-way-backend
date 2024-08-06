package com.knowway.auth.util;

public class LongUserIdConvertor implements UserIdConverter<Long> {

 @Override
    public Long convert(Object value) {
        if (value instanceof String) {
            return Long.parseLong((String) value);
        } else if (value instanceof Long) {
            return (Long) value;
        }
        throw new IllegalArgumentException("지원하지 않는 유저 아이디 타입입니다.");
    }
}
