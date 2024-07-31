package com.knowway.auth.service;

import com.knowway.auth.util.TypeConvertor;

public class LongToStringConverter implements TypeConvertor<Long, String> {
    @Override
    public String convert(Long userId) {
        return String.valueOf(userId);
    }
}
