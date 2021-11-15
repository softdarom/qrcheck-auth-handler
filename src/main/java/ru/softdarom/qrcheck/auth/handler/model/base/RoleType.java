package ru.softdarom.qrcheck.auth.handler.model.base;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("user", "ROLE_USER"),
    PROMOTER("promoter", "ROLE_PROMOTER"),
    CHECKMAN("checkman", "ROLE_CHECKMAN");

    @JsonValue
    private final String apiRole;
    private final String role;

    public static RoleType roleOf(String role) {
        return EnumSet.allOf(RoleType.class)
                .stream()
                .filter(it -> Objects.equals(it.role, role))
                .findFirst()
                .orElseThrow();
    }

    public static RoleType apiRoleOf(String apiRole) {
        return EnumSet.allOf(RoleType.class)
                .stream()
                .filter(it -> Objects.equals(it.apiRole, apiRole))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public String toString() {
        return String.valueOf(role);
    }

}