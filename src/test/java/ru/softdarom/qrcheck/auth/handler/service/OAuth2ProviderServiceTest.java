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
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.client.GoogleAuthClient;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.googleAccessTokenResponse;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.googleTokenInfoResponse;

@DisplayName("OAuth2ProviderService Spring Integration Test")
class OAuth2ProviderServiceTest extends AbstractIntegrationTest {

    @Mock
    private GoogleAuthClient googleAuthClientMock;

    @Autowired
    private OAuth2ProviderService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "providerToClient", Map.of(ProviderType.GOOGLE, googleAuthClientMock));
    }

    @AfterEach
    void tearDown() {
        reset(googleAuthClientMock);
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("getTokenInfo(): returns GoogleTokenInfoResponse")
    void successfulGetTokenInfoGoogle() {
        when(googleAuthClientMock.tokenInfo(any())).thenReturn(ResponseEntity.ok(googleTokenInfoResponse()));
        var actual = assertDoesNotThrow(() -> service.getTokenInfo(UUID.randomUUID().toString(), ProviderType.GOOGLE));
        assertAll(() -> {
            assertNotNull(actual);
            verify(googleAuthClientMock).tokenInfo(any());
        });
    }

    @Test
    @DisplayName("refreshToken(): returns GoogleTokenInfoResponse")
    void successfulRefreshTokenGoogle() {
        when(googleAuthClientMock.refresh(any())).thenReturn(ResponseEntity.ok(googleAccessTokenResponse()));
        var actual = assertDoesNotThrow(() -> service.refreshToken(UUID.randomUUID().toString(), ProviderType.GOOGLE));
        assertAll(() -> {
            assertNotNull(actual);
            verify(googleAuthClientMock).refresh(any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @ParameterizedTest
    @EnumSource(value = ProviderType.class, names = "GOOGLE", mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("getTokenInfo(): throws NotFoundException when not existed client")
    void failureGetTokenInfoNotExistedClient(ProviderType provider) {
        assertThrows(NotFoundException.class, () -> service.getTokenInfo(UUID.randomUUID().toString(), provider));
        verify(googleAuthClientMock, never()).tokenInfo(any());
    }

    @ParameterizedTest
    @EnumSource(value = ProviderType.class, names = "GOOGLE", mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("refreshToken(): throws NotFoundException when not existed client")
    void failureRefreshTokenNotExistedClient(ProviderType provider) {
        assertThrows(NotFoundException.class, () -> service.refreshToken(UUID.randomUUID().toString(), provider));
        verify(googleAuthClientMock, never()).refresh(any());
    }
}