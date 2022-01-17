package ru.softdarom.qrcheck.auth.handler.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.accessTokenDto;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.refreshTokenDto;

@DisplayName("TokenDisabledService Spring Integration Test")
class TokenDisabledServiceTest extends AbstractIntegrationTest {

    @Mock
    private AccessTokenAccessService accessTokenAccessServiceMock;
    @Mock
    private RefreshTokenAccessService refreshTokenAccessServiceMock;

    @Autowired
    private TokenDisabledService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "accessTokenAccessService", accessTokenAccessServiceMock);
        ReflectionTestUtils.setField(service, "refreshTokenAccessService", refreshTokenAccessServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(
                accessTokenAccessServiceMock,
                refreshTokenAccessServiceMock
        );
    }

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableAccessToken(accessToken): disables access token")
    void successfulDisableAccessToken(ProviderType provider) {
        var accessToken = accessTokenDto(provider);
        var refreshToken = refreshTokenDto(provider);
        refreshToken.setAccessTokens(Set.of(accessToken));
        assertAll(() -> {
            assertDoesNotThrow(() -> service.disableAccessToken(accessToken));
            assertEquals(ActiveType.DISABLED, accessToken.getActive());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableOldAccessTokens(refreshToken): disables access tokens")
    void successfulDisableOldAccessTokens(ProviderType provider) {
        var accessToken = accessTokenDto(provider);
        var refreshToken = refreshTokenDto(provider);
        refreshToken.setAccessTokens(Set.of(accessToken));
        assertAll(() -> {
            assertDoesNotThrow(() -> service.disableOldAccessTokens(refreshToken));
            assertEquals(ActiveType.DISABLED, accessToken.getActive());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableOldAccessTokens(refreshTokens): disables access tokens")
    void successfulDisableOldAccessTokensSet(ProviderType provider) {
        var accessToken = accessTokenDto(provider);
        var refreshToken = refreshTokenDto(provider);
        refreshToken.setAccessTokens(Set.of(accessToken));
        var refreshTokens = Set.of(refreshToken);
        assertAll(() -> {
            assertDoesNotThrow(() -> service.disableOldAccessTokens(refreshTokens));
            assertEquals(ActiveType.DISABLED, accessToken.getActive());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableOldRefreshToken(): disables refresh tokens")
    void successfulDisableOldRefreshToken(ProviderType provider) {
        var refreshToken = refreshTokenDto(provider);
        var refreshTokens = Set.of(refreshToken);
        assertAll(() -> {
            assertDoesNotThrow(() -> service.disableOldRefreshToken(refreshTokens, provider));
            assertEquals(ActiveType.DISABLED, refreshToken.getActive());
        });
    }


    //  -----------------------   failure tests   -------------------------


    @Test
    @DisplayName("disableAccessToken(accessToken): throws IllegalArgumentException when an access token is null")
    void failureDisableAccessTokenNullAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> service.disableAccessToken(null));
    }

    @Test
    @DisplayName("disableOldAccessTokens(refreshToken): throws IllegalArgumentException when a refresh tokens is null")
    void failureDisableOldAccessTokensNullRefreshToken() {
        assertThrows(IllegalArgumentException.class, () -> service.disableOldAccessTokens((RefreshTokenDto) null));
    }

    @Test
    @DisplayName("disableOldAccessTokens(refreshTokens): throws IllegalArgumentException when a collection of refresh tokens is null")
    void failureDisableOldAccessTokensNullRefreshTokens() {
        assertThrows(IllegalArgumentException.class, () -> service.disableOldAccessTokens((Collection<RefreshTokenDto>) null));
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableOldRefreshToken(): throws IllegalArgumentException when refresh tokens is null")
    void failureDisableOldRefreshTokenNullRefreshTokens(ProviderType provider) {
        assertThrows(IllegalArgumentException.class, () -> service.disableOldRefreshToken(null, provider));
    }

    @Test
    @DisplayName("disableOldRefreshToken(): throws IllegalArgumentException when a provider is null")
    void failureDisableOldRefreshTokenNullProvider() {
        var refreshTokens = Set.of(refreshTokenDto(ProviderType.QRCHECK));
        assertThrows(IllegalArgumentException.class, () -> service.disableOldRefreshToken(refreshTokens, null));
    }
}