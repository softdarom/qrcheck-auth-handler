package ru.softdarom.qrcheck.auth.handler.model.base;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum ApiKeyType {

    OUTGOING("outgoing"),
    INCOMING("incoming");

    @JsonValue
    private final String type;

    public static ApiKeyType typeOf(String type) {
        return EnumSet.allOf(ApiKeyType.class)
                .stream()
                .filter(it -> Objects.equals(it.type, type))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public String toString() {
        return String.valueOf(type);
    }
}