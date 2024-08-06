package com.knowway.auth.util;

public interface UserIdConverter<USERID> {
    USERID convert(Object value);
}