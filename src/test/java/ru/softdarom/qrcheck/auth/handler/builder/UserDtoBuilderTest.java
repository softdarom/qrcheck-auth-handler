package ru.softdarom.qrcheck.auth.handler.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.roleDto;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.tokenUserInfoRequest;

@UnitTest
@DisplayName("UserDtoBuilder Unit Test")
class UserDtoBuilderTest {

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("build(): returns correct dto'")
    void successfulBuild(ProviderType provider) {
        var actual = new UserDtoBuilder(tokenUserInfoRequest(provider), Set.of(roleDto())).build();
        assertAll(() -> {
            assertNotNull(actual);
            assertNotNull(actual.getExternalUserId());
            assertNotNull(actual.getRoles());
            assertFalse(actual.getRoles().isEmpty());
            assertNotNull(actual.getRefreshTokens());
            assertFalse(actual.getRefreshTokens().isEmpty());
            assertNotNull(actual.getTokenInfo());
            assertFalse(actual.getTokenInfo().isEmpty());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("build(): throws IllegalArgumentException when user info is null")
    void failureBuildNullUserInfo() {
        assertThrows(IllegalArgumentException.class, () -> new UserDtoBuilder(null, Set.of(roleDto())).build());
    }

    @Test
    @DisplayName("build(): throws IllegalArgumentException roles are null")
    void failureBuildNullRoles() {
        assertThrows(IllegalArgumentException.class, () -> new UserDtoBuilder(tokenUserInfoRequest(), null).build());
    }

    @Test
    @DisplayName("build(): throws IllegalArgumentException roles are empty")
    void failureBuildEmptyRoles() {
        assertThrows(IllegalArgumentException.class, () -> new UserDtoBuilder(tokenUserInfoRequest(), Set.of()).build());
    }
}