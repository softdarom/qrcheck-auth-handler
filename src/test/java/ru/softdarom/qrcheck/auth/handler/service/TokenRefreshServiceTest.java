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
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.*;

@DisplayName("TokenRefreshService Spring Integration Test")
class TokenRefreshServiceTest extends AbstractIntegrationTest {

    @Mock
    private AccessTokenAccessService accessTokenAccessServiceMock;

    @Mock
    private RefreshTokenAccessService refreshTokenAccessServiceMock;

    @Mock
    private OAuth2ProviderService oAuth2ProviderServiceMock;

    @Autowired
    private TokenRefreshService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "accessTokenAccessService", accessTokenAccessServiceMock);
        ReflectionTestUtils.setField(service, "refreshTokenAccessService", refreshTokenAccessServiceMock);
        ReflectionTestUtils.setField(service, "oAuth2ProviderService", oAuth2ProviderServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(accessTokenAccessServiceMock, refreshTokenAccessServiceMock, oAuth2ProviderServiceMock);
    }

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("refresh(): returns RefreshTokenResponse")
    void successfulRefresh(ProviderType provider) {
        var user = userDto(provider);
        var accessToken =
                user.getRefreshTokens()
                        .stream()
                        .map(RefreshTokenDto::getAccessTokens)
                        .flatMap(Collection::stream)
                        .findAny();
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(accessToken);
        when(refreshTokenAccessServiceMock.find(any(), any())).thenReturn(user.getRefreshTokens());
        when(oAuth2ProviderServiceMock.refreshToken(any(), any())).thenReturn(googleAccessTokenResponse());
        when(accessTokenAccessServiceMock.save(any())).thenReturn(accessToken.orElseThrow());
        assertDoesNotThrow(() -> service.refresh(UUID.randomUUID().toString()));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
            verify(oAuth2ProviderServiceMock).refreshToken(any(), any());
            verify(refreshTokenAccessServiceMock).save(any());
            verify(accessTokenAccessServiceMock).save(any());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableOldAccessTokens(refreshTokens, provider): disables access tokens")
    void successfulDisableOldAccessTokensSet(ProviderType provider) {
        var accessToken = accessTokenDto(provider);
        var refreshToken = refreshTokenDto(provider);
        refreshToken.setAccessTokens(Set.of(accessToken));
        var refreshTokens = Set.of(refreshToken);
        assertAll(() -> {
            assertDoesNotThrow(() -> service.disableOldAccessTokens(refreshTokens, provider));
            assertEquals(ActiveType.DISABLED, accessToken.getActive());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableOldAccessTokens(refreshToken, provider): disables access tokens")
    void successfulDisableOldAccessTokens(ProviderType provider) {
        var accessToken = accessTokenDto(provider);
        var refreshToken = refreshTokenDto(provider);
        refreshToken.setAccessTokens(Set.of(accessToken));
        assertAll(() -> {
            assertDoesNotThrow(() -> service.disableOldAccessTokens(refreshToken, provider));
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
    @DisplayName("refresh(): throws IllegalArgumentException when an access token is null")
    void failureRefreshAccessTokenNull() {
        assertThrows(IllegalArgumentException.class, () -> service.refresh(null));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock, never()).findByToken(any());
            verify(refreshTokenAccessServiceMock, never()).find(any(), any());
            verify(oAuth2ProviderServiceMock, never()).refreshToken(any(), any());
        });
    }

    @Test
    @DisplayName("refresh(): throws IllegalArgumentException when an access token is empty")
    void failureRefreshAccessTokenEmpty() {
        assertThrows(IllegalArgumentException.class, () -> service.refresh(""));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock, never()).findByToken(any());
            verify(refreshTokenAccessServiceMock, never()).find(any(), any());
            verify(oAuth2ProviderServiceMock, never()).refreshToken(any(), any());
        });
    }

    @Test
    @DisplayName("refresh(): throws NotFoundException when an access token not found")
    void failureRefreshNotFoundAccessToken() {
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.refresh(UUID.randomUUID().toString()));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock, never()).find(any(), any());
            verify(oAuth2ProviderServiceMock, never()).refreshToken(any(), any());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("refresh(): throws NotFoundException when a refresh token not found")
    void failureRefreshNotFoundRefreshToken(ProviderType provider) {
        var user = userDto(provider);
        var accessToken =
                user.getRefreshTokens()
                        .stream()
                        .map(RefreshTokenDto::getAccessTokens)
                        .flatMap(Collection::stream)
                        .findAny();
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(accessToken);
        when(refreshTokenAccessServiceMock.find(any(), any())).thenReturn(Set.of());
        assertThrows(NotFoundException.class, () -> service.refresh(UUID.randomUUID().toString()));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
            verify(oAuth2ProviderServiceMock, never()).refreshToken(any(), any());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("disableOldAccessTokens(refreshToken, provider): throws IllegalArgumentException when a refresh token is null")
    void failureDisableOldAccessTokensNullRefreshToken(ProviderType provider) {
        assertThrows(IllegalArgumentException.class, () -> service.disableOldAccessTokens((RefreshTokenDto) null, provider));
    }

    @Test
    @DisplayName("disableOldAccessTokens(refreshToken, provider): throws IllegalArgumentException when a provider is null")
    void failureDisableOldAccessTokensNullProvider() {
        assertThrows(IllegalArgumentException.class, () -> service.disableOldAccessTokens(refreshTokenDto(ProviderType.QRCHECK), null));
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
        assertThrows(IllegalArgumentException.class, () -> service.disableOldRefreshToken(Set.of(refreshTokenDto(ProviderType.QRCHECK)), null));
    }
}