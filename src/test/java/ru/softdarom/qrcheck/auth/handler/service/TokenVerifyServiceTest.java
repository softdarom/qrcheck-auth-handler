package ru.softdarom.qrcheck.auth.handler.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.dao.access.AccessTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RefreshTokenAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.RoleAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.base.TokenValidType;
import ru.softdarom.qrcheck.auth.handler.model.dto.inner.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.googleTokenInfoResponse;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.userDto;

@DisplayName("TokenVerifyService Spring Integration Test")
class TokenVerifyServiceTest extends AbstractIntegrationTest {

    @Mock
    private AccessTokenAccessService accessTokenAccessServiceMock;
    @Mock
    private RefreshTokenAccessService refreshTokenAccessServiceMock;
    @Mock
    private RoleAccessService roleAccessServiceMock;
    @Mock
    private OAuth2ProviderService oAuth2ProviderServiceMock;

    @Autowired
    private TokenVerifyService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "accessTokenAccessService", accessTokenAccessServiceMock);
        ReflectionTestUtils.setField(service, "refreshTokenAccessService", refreshTokenAccessServiceMock);
        ReflectionTestUtils.setField(service, "roleAccessService", roleAccessServiceMock);
        ReflectionTestUtils.setField(service, "oAuth2ProviderService", oAuth2ProviderServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(
                accessTokenAccessServiceMock,
                refreshTokenAccessServiceMock,
                roleAccessServiceMock,
                oAuth2ProviderServiceMock
        );
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("verify(): returns ExpiredTokenInfoResponse when an access token is disabled")
    void successfulVerifyExpiredDisabledAccessToken() {
        var user = userDto(ProviderType.GOOGLE);
        var accessToken = user.getRefreshTokens().stream().map(RefreshTokenDto::getAccessTokens).flatMap(Collection::stream).findAny();
        accessToken.ifPresent(it -> it.setActive(ActiveType.DISABLED));
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(accessToken);
        when(refreshTokenAccessServiceMock.find(any(), any())).thenReturn(user.getRefreshTokens());
        var actual = assertDoesNotThrow(() -> service.verify(UUID.randomUUID().toString()));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(TokenValidType.EXPIRED, actual.getValid());
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
            verify(roleAccessServiceMock, never()).findByUserId(any());
            verify(oAuth2ProviderServiceMock, never()).getTokenInfo(any(), any());
        });
    }

    @Test
    @DisplayName("verify(): returns ExpiredTokenInfoResponse when an access token is disabled")
    void successfulVerifyExpiredAccessToken() {
        var user = userDto(ProviderType.GOOGLE);
        var accessToken = user.getRefreshTokens().stream().map(RefreshTokenDto::getAccessTokens).flatMap(Collection::stream).findAny();
        accessToken.ifPresent(it -> it.setExpires(LocalDateTime.now().minusDays(1L)));
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(accessToken);
        when(refreshTokenAccessServiceMock.find(any(), any())).thenReturn(user.getRefreshTokens());
        var actual = assertDoesNotThrow(() -> service.verify(UUID.randomUUID().toString()));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(TokenValidType.EXPIRED, actual.getValid());
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
            verify(roleAccessServiceMock, never()).findByUserId(any());
            verify(oAuth2ProviderServiceMock, never()).getTokenInfo(any(), any());
        });
    }

    @Test
    @DisplayName("verify(): returns ExpiredTokenInfoResponse when an access token is disabled")
    void successfulVerifyExpiredDisabledRefreshToken() {
        var user = userDto(ProviderType.GOOGLE);
        user.getRefreshTokens().forEach(it -> it.setActive(ActiveType.DISABLED));
        var accessToken = user.getRefreshTokens().stream().map(RefreshTokenDto::getAccessTokens).flatMap(Collection::stream).findAny();
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(accessToken);
        when(refreshTokenAccessServiceMock.find(any(), any())).thenReturn(user.getRefreshTokens());
        var actual = assertDoesNotThrow(() -> service.verify(UUID.randomUUID().toString()));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(TokenValidType.EXPIRED, actual.getValid());
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
            verify(roleAccessServiceMock, never()).findByUserId(any());
            verify(oAuth2ProviderServiceMock, never()).getTokenInfo(any(), any());
        });
    }

    @Test
    @DisplayName("verify(): returns IncorrectTokenInfoResponse when an access token is incorrect")
    void successfulVerifyIncorrect() {
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(Optional.empty());
        var actual = assertDoesNotThrow(() -> service.verify(UUID.randomUUID().toString()));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(TokenValidType.INCORRECT, actual.getValid());
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(oAuth2ProviderServiceMock, never()).getTokenInfo(any(), any());
            verify(roleAccessServiceMock, never()).findByUserId(any());
            verify(refreshTokenAccessServiceMock, never()).find(any(), any());
        });
    }

    @Test
    @DisplayName("verify(): returns GoogleTokenInfoResponse when an access token is valid")
    void successfulVerifyValidGoogle() {
        var user = userDto(ProviderType.GOOGLE);
        var accessToken = user.getRefreshTokens().stream().map(RefreshTokenDto::getAccessTokens).flatMap(Collection::stream).findAny();
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(accessToken);
        when(refreshTokenAccessServiceMock.find(any(), any())).thenReturn(user.getRefreshTokens());
        when(roleAccessServiceMock.findByUserId(any())).thenReturn(user.getRoles());
        when(oAuth2ProviderServiceMock.getTokenInfo(any(), any())).thenReturn(googleTokenInfoResponse());
        var actual = assertDoesNotThrow(() -> service.verify(UUID.randomUUID().toString()));
        assertAll(() -> {
            assertNotNull(actual);
            assertEquals(TokenValidType.VALID, actual.getValid());
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(oAuth2ProviderServiceMock).getTokenInfo(any(), any());
            verify(roleAccessServiceMock).findByUserId(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("verify(): throws IllegalArgumentException when an access token is null")
    void failureVerifyNullAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> service.verify(null));
    }

    @Test
    @DisplayName("verify(): throws IllegalArgumentException when an access token is empty")
    void failureVerifyEmptyAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> service.verify(""));
    }
}