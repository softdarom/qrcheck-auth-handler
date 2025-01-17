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
import ru.softdarom.qrcheck.auth.handler.dao.access.UserAccessService;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.model.dto.internal.RefreshTokenDto;
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.googleAccessTokenResponse;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.userDto;

@SpringIntegrationTest
@DisplayName("TokenRefreshService Spring Integration Test")
class TokenRefreshServiceTest {

    @Mock
    private AccessTokenAccessService accessTokenAccessServiceMock;

    @Mock
    private RefreshTokenAccessService refreshTokenAccessServiceMock;

    @Mock
    private OAuth2ProviderService oAuth2ProviderServiceMock;

    @Mock
    private TokenDisabledService tokenDisabledServiceMock;

    @Mock
    private UserHandlerService userHandlerServiceMock;

    @Mock
    private UserAccessService userAccessServiceMock;

    @Autowired
    private TokenRefreshService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "accessTokenAccessService", accessTokenAccessServiceMock);
        ReflectionTestUtils.setField(service, "refreshTokenAccessService", refreshTokenAccessServiceMock);
        ReflectionTestUtils.setField(service, "oAuth2ProviderService", oAuth2ProviderServiceMock);
        ReflectionTestUtils.setField(service, "tokenDisabledService", tokenDisabledServiceMock);
        ReflectionTestUtils.setField(service, "userHandlerService", userHandlerServiceMock);
        ReflectionTestUtils.setField(service, "userAccessService", userAccessServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(
                accessTokenAccessServiceMock,
                refreshTokenAccessServiceMock,
                oAuth2ProviderServiceMock,
                tokenDisabledServiceMock,
                userHandlerServiceMock,
                userAccessServiceMock
        );
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
        when(userHandlerServiceMock.isExistedUser(any())).thenReturn(true);
        when(refreshTokenAccessServiceMock.find(any(), any())).thenReturn(user.getRefreshTokens());
        when(oAuth2ProviderServiceMock.refreshToken(any(), any())).thenReturn(googleAccessTokenResponse());
        doNothing().when(tokenDisabledServiceMock).disableAccessTokens(any(RefreshTokenDto.class));
        when(accessTokenAccessServiceMock.save(any())).thenReturn(accessToken.orElseThrow());
        var accessTokenAsString = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> service.refresh(accessTokenAsString));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(userHandlerServiceMock).isExistedUser(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
            verify(oAuth2ProviderServiceMock).refreshToken(any(), any());
            verify(tokenDisabledServiceMock).disableAccessTokens(any(RefreshTokenDto.class));
            verify(accessTokenAccessServiceMock).save(any());
            verify(userAccessServiceMock, never()).findByExternalUserId(any());
            verify(userAccessServiceMock, never()).delete(any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("refresh(): throws IllegalArgumentException when an access token is null")
    void failureRefreshAccessTokenNull() {
        assertThrows(IllegalArgumentException.class, () -> service.refresh(null));
    }

    @Test
    @DisplayName("refresh(): throws IllegalArgumentException when an access token is empty")
    void failureRefreshAccessTokenEmpty() {
        assertThrows(IllegalArgumentException.class, () -> service.refresh(""));
    }

    @Test
    @DisplayName("refresh(): throws NotFoundException when an access token not found")
    void failureRefreshNotFoundAccessToken() {
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(Optional.empty());
        var accessTokenAsString = UUID.randomUUID().toString();
        assertThrows(NotFoundException.class, () -> service.refresh(accessTokenAsString));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock, never()).find(any(), any());
            verify(oAuth2ProviderServiceMock, never()).refreshToken(any(), any());
            verify(tokenDisabledServiceMock, never()).disableAccessTokens(any(RefreshTokenDto.class));
            verify(userHandlerServiceMock, never()).isExistedUser(any());
            verify(userAccessServiceMock, never()).findByExternalUserId(any());
            verify(userAccessServiceMock, never()).delete(any());
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
        when(userHandlerServiceMock.isExistedUser(any())).thenReturn(true);
        var accessTokenAsString = UUID.randomUUID().toString();
        assertThrows(NotFoundException.class, () -> service.refresh(accessTokenAsString));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(refreshTokenAccessServiceMock).find(any(), any());
            verify(accessTokenAccessServiceMock, never()).save(any());
            verify(oAuth2ProviderServiceMock, never()).refreshToken(any(), any());
            verify(tokenDisabledServiceMock, never()).disableAccessTokens(any(RefreshTokenDto.class));
            verify(userAccessServiceMock, never()).findByExternalUserId(any());
            verify(userAccessServiceMock, never()).delete(any());
        });
    }

    @ParameterizedTest
    @EnumSource(ProviderType.class)
    @DisplayName("refresh(): throws NotFoundException when a user isn't found")
    void failureRefreshNotFoundUser(ProviderType provider) {
        var user = userDto(provider);
        var accessToken =
                user.getRefreshTokens()
                        .stream()
                        .map(RefreshTokenDto::getAccessTokens)
                        .flatMap(Collection::stream)
                        .findAny();
        when(accessTokenAccessServiceMock.findByToken(any())).thenReturn(accessToken);
        when(userHandlerServiceMock.isExistedUser(any())).thenReturn(false);
        when(userAccessServiceMock.findByExternalUserId(any())).thenReturn(Optional.of(user));
        doNothing().when(userAccessServiceMock).delete(any());
        var accessTokenAsString = UUID.randomUUID().toString();
        assertThrows(NotFoundException.class, () -> service.refresh(accessTokenAsString));
        assertAll(() -> {
            verify(accessTokenAccessServiceMock).findByToken(any());
            verify(userHandlerServiceMock).isExistedUser(any());
            verify(userAccessServiceMock).findByExternalUserId(any());
            verify(userAccessServiceMock).delete(any());
            verify(refreshTokenAccessServiceMock, never()).find(any(), any());
            verify(oAuth2ProviderServiceMock, never()).refreshToken(any(), any());
            verify(tokenDisabledServiceMock, never()).disableAccessTokens(any(RefreshTokenDto.class));
            verify(tokenDisabledServiceMock, never()).disableRefreshTokens(anyCollection(), any());
            verify(accessTokenAccessServiceMock, never()).save(any());
        });
    }
}