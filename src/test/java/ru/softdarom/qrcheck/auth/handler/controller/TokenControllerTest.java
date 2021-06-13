package ru.softdarom.qrcheck.auth.handler.controller;

import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import ru.softdarom.qrcheck.auth.handler.exception.NotFoundException;
import ru.softdarom.qrcheck.auth.handler.model.base.TokenValidType;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.AbstractOAuth2TokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.BaseResponse;
import ru.softdarom.qrcheck.auth.handler.model.dto.response.GoogleTokenInfoResponse;
import ru.softdarom.qrcheck.auth.handler.rest.controller.TokenController;
import ru.softdarom.qrcheck.auth.handler.service.TokenService;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.*;

class TokenControllerTest extends AbstractControllerTest {

    private static final String URI_TOKENS_SAVE = "/tokens/info";
    private static final String URI_TOKENS_VERIFY = "/tokens/verify";
    private static final String URI_TOKENS_REFRESH = "/tokens/refresh";

    @Mock
    private TokenService tokenServiceMock;

    @Autowired
    private TokenController controller;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(controller, "tokenService", tokenServiceMock);
    }

    @AfterEach
    void tearDown() {
        reset(tokenServiceMock);
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("save(): saves a user and tokens")
    void successfulSave() {
        var request = tokenUserInfoRequest();
        var response = tokenUserInfoResponse(request);
        when(tokenServiceMock.saveOAuth2TokenInfo(any())).thenReturn(response);
        var actual = assertDoesNotThrow(() -> post(request, buildApiKeyHeader(), URI_TOKENS_SAVE));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.OK);
            verify(tokenServiceMock).saveOAuth2TokenInfo(any());
        });
    }

    @Test
    @DisplayName("verify(): returns an expired response")
    void successfulVerifyExpired() {
        var uri = URI_TOKENS_VERIFY + "?accessToken=" + UUID.randomUUID();
        var response = expiredTokenInfoResponse();
        when(tokenServiceMock.verify(any())).thenReturn(response);
        var actual =
                assertDoesNotThrow(() -> get(AbstractOAuth2TokenInfoResponse.ExpiredTokenInfoResponse.class, buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.OK);
            assertEquals(TokenValidType.EXPIRED, Objects.requireNonNull(actual.getBody()).getValid());
            verify(tokenServiceMock).verify(any());
        });
    }

    @Test
    @DisplayName("verify(): returns an incorrect response")
    void successfulVerifyIncorrect() {
        var uri = URI_TOKENS_VERIFY + "?accessToken=" + UUID.randomUUID();
        var response = incorrectTokenInfoResponse();
        when(tokenServiceMock.verify(any())).thenReturn(response);
        var actual =
                assertDoesNotThrow(() -> get(AbstractOAuth2TokenInfoResponse.IncorrectTokenInfoResponse.class, buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.OK);
            assertEquals(TokenValidType.INCORRECT, Objects.requireNonNull(actual.getBody()).getValid());
            verify(tokenServiceMock).verify(any());
        });
    }

    @Test
    @DisplayName("verify(): returns a google valid response")
    void successfulVerifyGoogleValid() {
        var uri = URI_TOKENS_VERIFY + "?accessToken=" + UUID.randomUUID();
        var response = googleTokenInfoResponse();
        when(tokenServiceMock.verify(any())).thenReturn(response);
        var actual = assertDoesNotThrow(() -> get(GoogleTokenInfoResponse.class, buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.OK);
            assertEquals(TokenValidType.VALID, Objects.requireNonNull(actual.getBody()).getValid());
            verify(tokenServiceMock).verify(any());
        });
    }

    @Test
    @DisplayName("refresh(): returns a new access token")
    void successfulRefresh() {
        var uri = URI_TOKENS_REFRESH + "?accessToken=" + UUID.randomUUID();
        var response = refreshTokenResponse();
        when(tokenServiceMock.refresh(any())).thenReturn(response);
        var actual = assertDoesNotThrow(() -> post(buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.OK);
            verify(tokenServiceMock).refresh(any());
        });
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("save(): returns 401 when not authentication")
    void failureSaveUnauthorized() {
        var actual = assertDoesNotThrow(() -> post(tokenUserInfoRequest(), buildNotAuthHeader(), URI_TOKENS_SAVE));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(tokenServiceMock, never()).saveOAuth2TokenInfo(any());
        });
    }

    @Test
    @DisplayName("verify(): returns 401 when not authentication")
    void failureVerifyUnauthorized() {
        var uri = URI_TOKENS_VERIFY + "?accessToken=" + UUID.randomUUID();
        var actual = assertDoesNotThrow(() -> get(BaseResponse.class, buildNotAuthHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(tokenServiceMock, never()).verify(any());
        });
    }

    @Test
    @DisplayName("verify(): returns 404 when access token not found")
    void failureVerifyNotFound() {
        var uri = URI_TOKENS_VERIFY + "?accessToken=" + UUID.randomUUID();
        when(tokenServiceMock.verify(any())).thenThrow(NotFoundException.class);
        var actual = assertDoesNotThrow(() -> get(BaseResponse.class, buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.NOT_FOUND);
            verify(tokenServiceMock).verify(any());
        });
    }

    @Test
    @DisplayName("refresh(): returns 401 when not authentication")
    void failureRefreshUnauthorized() {
        var uri = URI_TOKENS_REFRESH + "?accessToken=" + UUID.randomUUID();
        when(tokenServiceMock.refresh(any())).thenThrow(RuntimeException.class);
        var actual =
                assertDoesNotThrow(() -> post(buildNotAuthHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(tokenServiceMock, never()).refresh(any());
        });
    }

    @Test
    @DisplayName("refresh(): returns 404 when access token not found")
    void failureRefreshNotFound() {
        var uri = URI_TOKENS_REFRESH + "?accessToken=" + UUID.randomUUID();
        when(tokenServiceMock.refresh(any())).thenThrow(NotFoundException.class);
        var actual = assertDoesNotThrow(() -> post(BaseResponse.class, buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.NOT_FOUND);
            verify(tokenServiceMock).refresh(any());
        });
    }

    @Test
    @DisplayName("save(): returns 401 when the feign client throws Unauthorized")
    void failureSaveFeignUnauthorized() {
        when(tokenServiceMock.saveOAuth2TokenInfo(any())).thenThrow(FeignException.Unauthorized.class);
        var actual = assertDoesNotThrow(() -> post(tokenUserInfoRequest(), buildApiKeyHeader(), URI_TOKENS_SAVE));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.UNAUTHORIZED);
            verify(tokenServiceMock).saveOAuth2TokenInfo(any());
        });
    }

    @Test
    @DisplayName("save(): returns 500 when unknown exception")
    void failureSaveUnknownException() {
        when(tokenServiceMock.saveOAuth2TokenInfo(any())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> post(tokenUserInfoRequest(), buildApiKeyHeader(), URI_TOKENS_SAVE));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(tokenServiceMock).saveOAuth2TokenInfo(any());
        });
    }

    @Test
    @DisplayName("verify(): returns 500 when unknown exception")
    void failureVerifyUnknownException() {
        var uri = URI_TOKENS_VERIFY + "?accessToken=" + UUID.randomUUID();
        when(tokenServiceMock.verify(any())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> get(BaseResponse.class, buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(tokenServiceMock).verify(any());
        });
    }

    @Test
    @DisplayName("refresh(): returns 500 when unknown exception")
    void failureRefreshUnknownException() {
        var uri = URI_TOKENS_REFRESH + "?accessToken=" + UUID.randomUUID();
        when(tokenServiceMock.refresh(any())).thenThrow(RuntimeException.class);
        var actual = assertDoesNotThrow(() -> post(buildApiKeyHeader(), uri));
        assertAll(() -> {
            assertCall().accept(actual, HttpStatus.INTERNAL_SERVER_ERROR);
            verify(tokenServiceMock).refresh(any());
        });
    }
}