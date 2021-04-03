package com.codeleven.common.constants;

public interface BaseEnum<E extends Enum<?>, T> {
    T getValue();
    String getDisplayName();
}
