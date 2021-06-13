package ru.softdarom.qrcheck.auth.handler.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.refreshToken;

@UnitTest
@DisplayName("RefreshTokenDtoBuilder Unit Test")
class RefreshTokenDtoBuilderTest {

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("build(): returns correct dto'")
    void successfulBuild(ProviderType provider) {
        var actual = new RefreshTokenDtoBuilder(refreshToken(), provider).build();
        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.getToken().isEmpty());
            assertNotNull(actual.getIssued());
            assertEquals(provider, actual.getProvider());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("build(): throws IllegalArgumentException when refresh token is null")
    void failureBuildNullRefreshToken() {
        assertThrows(IllegalArgumentException.class, () -> new RefreshTokenDtoBuilder(null, ProviderType.QRCHECK).build());
    }

    @Test
    @DisplayName("build(): throws IllegalArgumentException when provider is null")
    void failureBuildProvider() {
        assertThrows(IllegalArgumentException.class, () -> new RefreshTokenDtoBuilder(refreshToken(), null).build());
    }
}