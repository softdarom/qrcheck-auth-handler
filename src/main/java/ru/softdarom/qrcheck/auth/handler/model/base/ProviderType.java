package ru.softdarom.qrcheck.auth.handler.model.base;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum ProviderType {

    GOOGLE("google"),
    FACEBOOK("facebook"),
    QRCHECK("qrcheck"),
    VKONTAKTE("vkontakte");

    @JsonValue
    private final String type;

    public static ProviderType typeOf(String type) {
        return EnumSet.allOf(ProviderType.class)
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