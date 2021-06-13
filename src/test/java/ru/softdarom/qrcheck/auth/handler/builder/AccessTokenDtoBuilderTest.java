package ru.softdarom.qrcheck.auth.handler.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.tag.UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.*;

@UnitTest
@DisplayName("AccessTokenDtoBuilder Unit Test")
class AccessTokenDtoBuilderTest {

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("ByProviderTokenDto.build(): returns correct dto'")
    void successfulByProviderTokenDtoBuild(ProviderType provider) {
        var actual = new AccessTokenDtoBuilder.ByProviderTokenDto(accessToken(), provider).build();
        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.getToken().isEmpty());
            assertNotNull(actual.getExpires());
            assertNotNull(actual.getIssued());
            assertEquals(provider, actual.getProvider());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("ByOAuth2AccessToken.build(): returns correct dto'")
    void successfulByOAuth2AccessTokenDtoBuild(ProviderType provider) {
        var actual = new AccessTokenDtoBuilder.ByOAuth2AccessToken(googleAccessTokenResponse(), refreshTokenDto(provider)).build();
        assertAll(() -> {
            assertNotNull(actual);
            assertFalse(actual.getToken().isEmpty());
            assertNotNull(actual.getExpires());
            assertNotNull(actual.getIssued());
            assertEquals(provider, actual.getProvider());
            assertNotNull(actual.getRefreshToken());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("ByProviderTokenDto.build(): throws IllegalArgumentException when access token is null")
    void failureByProviderTokenDtoBuildNullAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> new AccessTokenDtoBuilder.ByProviderTokenDto(null, ProviderType.QRCHECK).build());
    }

    @Test
    @DisplayName("ByProviderTokenDto.build(): throws IllegalArgumentException when provider is null")
    void failureByProviderTokenDtoBuildNullProvider() {
        assertThrows(IllegalArgumentException.class, () -> new AccessTokenDtoBuilder.ByProviderTokenDto(accessToken(), null).build());
    }

    @Test
    @DisplayName("ByOAuth2AccessToken.build(): throws IllegalArgumentException when access token is null")
    void failureByOAuth2AccessTokenBuildNullAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> new AccessTokenDtoBuilder.ByOAuth2AccessToken(null, refreshTokenDto(ProviderType.QRCHECK)).build());
    }

    @Test
    @DisplayName("ByOAuth2AccessToken.build(): throws IllegalArgumentException when refresh token is null")
    void failureByOAuth2AccessTokenBuildNullRefreshToken() {
        assertThrows(IllegalArgumentException.class, () -> new AccessTokenDtoBuilder.ByOAuth2AccessToken(googleAccessTokenResponse(), null).build());
    }
}