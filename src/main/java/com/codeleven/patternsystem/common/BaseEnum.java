package com.codeleven.patternsystem.common;

public interface BaseEnum<E extends Enum<?>, T> {
    T getValue();
    String getDisplayName();
}
