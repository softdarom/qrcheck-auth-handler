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
import ru.softdarom.qrcheck.auth.handler.dao.access.RoleAccessService;
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateLong;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.*;

@SpringIntegrationTest
@DisplayName("TokenService Spring Integration Test")
class TokenServiceTest {

    @Mock
    private RoleAccessService roleAccessServiceMock;

    @Mock
    private UserAccessService userAccessServiceMock;

    @Mock
    private TokenVerifyService tokenVerifyServiceMock;

    @Mock
    private TokenRefreshService tokenRefreshServiceMock;

    @Mock
    private TokenDisabledService tokenDisabledServiceMock;

    @Mock
    private UserHandlerService userHandlerServiceMock;

    @Autowired
    private TokenService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "roleAccessService", roleAccessServiceMock);
        ReflectionTestUtils.setField(service, "userAccessService", userAccessServiceMock);
        ReflectionTestUtils.setField(service, "tokenVerifyService", tokenVerifyServiceMock);
        ReflectionTestUtils.setField(service, "tokenRefreshService", tokenRefreshServiceMock);
        ReflectionTestUtils.setField(service, "tokenDisabledService", tokenDisabledServiceMock);
        ReflectionTestUtils.setField(service, "userHandlerService", userHandlerServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(roleAccessServiceMock,
                userAccessServiceMock,
                tokenVerifyServiceMock,
                tokenRefreshServiceMock,
                tokenDisabledServiceMock,
                userHandlerServiceMock
        );
    }

    //  -----------------------   successful tests   -------------------------

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("saveOAuth2TokenInfo(): returns TokenUserInfoResponse when a new user")
    void successfulSaveOAuth2TokenInfoNewUser(ProviderType provider) {
        when(userHandlerServiceMock.saveUser(any())).thenReturn(Optional.of(generateLong()));
        when(roleAccessServiceMock.defaultRole()).thenReturn(roleDto());
        when(userAccessServiceMock.findByExternalUserId(any())).thenReturn(Optional.empty());
        var actual = assertDoesNotThrow(() -> service.saveOAuth2TokenInfo(tokenUserInfoRequest(provider)));
        assertAll(() -> {
            assertNotNull(actual);
            verify(userHandlerServiceMock).saveUser(any());
            verify(roleAccessServiceMock).defaultRole();
            verify(userAccessServiceMock).findByExternalUserId(any());
            verify(userAccessServiceMock).save(any());
            verify(tokenDisabledServiceMock, never()).disableAccessTokens(anySet());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("saveOAuth2TokenInfo(): returns TokenUserInfoResponse when a new user")
    void successfulSaveOAuth2TokenInfoExistedUser(ProviderType provider) {
        when(userHandlerServiceMock.saveUser(any())).thenReturn(Optional.of(generateLong()));
        when(roleAccessServiceMock.defaultRole()).thenReturn(roleDto());
        when(userAccessServiceMock.findByExternalUserId(any())).thenReturn(Optional.of(userDto(provider)));
        var actual = assertDoesNotThrow(() -> service.saveOAuth2TokenInfo(tokenUserInfoRequest(provider)));
        assertAll(() -> {
            assertNotNull(actual);
            verify(userHandlerServiceMock).saveUser(any());
            verify(roleAccessServiceMock).defaultRole();
            verify(userAccessServiceMock).findByExternalUserId(any());
            verify(userAccessServiceMock).save(any());
        });
    }

    @Test
    @DisplayName("saveOAuth2TokenInfo(): returns TokenUserInfoResponse when a new user and a new token provider")
    void successfulSaveOAuth2TokenInfoExistedUserNewTokenProvider() {
        when(userHandlerServiceMock.saveUser(any())).thenReturn(Optional.of(generateLong()));
        when(roleAccessServiceMock.defaultRole()).thenReturn(roleDto());
        when(userAccessServiceMock.findByExternalUserId(any())).thenReturn(Optional.of(userDto(ProviderType.GOOGLE)));
        var tokenRequest = tokenUserInfoRequest(ProviderType.VKONTAKTE);
        var actual = assertDoesNotThrow(() -> service.saveOAuth2TokenInfo(tokenRequest));
        assertAll(() -> {
            assertNotNull(actual);
            verify(userHandlerServiceMock).saveUser(any());
            verify(roleAccessServiceMock).defaultRole();
            verify(userAccessServiceMock).findByExternalUserId(any());
            verify(userAccessServiceMock).save(any());
        });
    }

    @Test
    @DisplayName("verify(): returns GoogleTokenInfoResponse")
    void successfulVerifyGoogle() {
        when(tokenVerifyServiceMock.verify(any())).thenReturn(googleTokenInfoResponse());
        var actual = assertDoesNotThrow(() -> service.verify(UUID.randomUUID().toString()));
        assertAll(() -> {
            assertNotNull(actual);
            verify(tokenVerifyServiceMock).verify(any());
        });
    }

    @Test
    @DisplayName("refresh(): returns RefreshTokenResponse")
    void successfulRefresh() {
        when(tokenRefreshServiceMock.refresh(any())).thenReturn(refreshTokenResponse());
        var actual = assertDoesNotThrow(() -> service.refresh(UUID.randomUUID().toString()));
        assertAll(() -> {
            assertNotNull(actual);
            verify(tokenRefreshServiceMock).refresh(any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("saveOAuth2TokenInfo(): throws IllegalArgumentException when a request is null")
    void failureSaveOAuth2TokenInfoNullRequest() {
        assertThrows(IllegalArgumentException.class, () -> service.saveOAuth2TokenInfo(null));
        assertAll(() -> {
            verify(userHandlerServiceMock, never()).saveUser(any());
            verify(userAccessServiceMock, never()).findByExternalUserId(any());
            verify(userAccessServiceMock, never()).save(any());
        });
    }

    @Test
    @DisplayName("verify(): throws IllegalArgumentException when an access token is null")
    void failureVerifyNullAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> service.verify(null));
        assertAll(() -> verify(tokenVerifyServiceMock, never()).verify(any()));
    }

    @Test
    @DisplayName("verify(): throws IllegalArgumentException when an access token is empty")
    void failureVerifyEmptyAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> service.verify(""));
        assertAll(() -> verify(tokenVerifyServiceMock, never()).verify(any()));
    }

    @Test
    @DisplayName("refresh(): throws IllegalArgumentException when an access token is null")
    void failureRefreshNullAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> service.refresh(null));
        assertAll(() -> verify(tokenRefreshServiceMock, never()).refresh(any()));
    }

    @Test
    @DisplayName("refresh(): throws IllegalArgumentException when an access token is empty")
    void failureRefreshEmptyAccessToken() {
        assertThrows(IllegalArgumentException.class, () -> service.refresh(""));
        assertAll(() -> verify(tokenRefreshServiceMock, never()).refresh(any()));
    }
}