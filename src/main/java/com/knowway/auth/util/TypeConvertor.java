package com.knowway.auth.util;

@FunctionalInterface
public interface TypeConvertor<FROM, TARGET> {
    TARGET convert(FROM from);
}