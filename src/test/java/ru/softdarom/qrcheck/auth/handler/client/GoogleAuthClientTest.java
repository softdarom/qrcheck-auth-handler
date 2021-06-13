package ru.softdarom.qrcheck.auth.handler.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.test.AbstractIntegrationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateString;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.googleAccessTokenResponse;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.googleTokenInfoResponse;

@DisplayName("UserHandlerClient Spring Integration Test")
class GoogleAuthClientTest extends AbstractIntegrationTest {

    private static final String GET_TOKEN_INFO = "/oauth2/tokens/info";
    private static final String POST_TOKEN_REFRESH = "/oauth2/tokens/refresh";

    @Autowired
    private WireMockServer wireGoogleAuthServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoogleAuthClient client;

    @AfterEach
    void tearDown() {
        wireGoogleAuthServiceMock.resetAll();
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("tokenInfo(): returns 200 when a token info is got")
    void successfulTokenInfo() throws JsonProcessingException {
        var accessToken = generateString();
        wireGoogleAuthServiceMock
                .stubFor(get(urlPathEqualTo(GET_TOKEN_INFO))
                        .withQueryParam("accessToken", equalTo(accessToken))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(objectMapper.writeValueAsString(googleTokenInfoResponse()))
                        )

                );
        assertDoesNotThrow(() -> client.tokenInfo(accessToken));
    }

    @Test
    @DisplayName("refresh(): returns 200 when an access token is refreshed")
    void successfulRefresh() throws JsonProcessingException {
        var refreshToken = generateString();
        wireGoogleAuthServiceMock
                .stubFor(post(urlPathEqualTo(POST_TOKEN_REFRESH))
                        .withQueryParam("refreshToken", equalTo(refreshToken))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(objectMapper.writeValueAsString(googleAccessTokenResponse()))
                        )

                );
        assertDoesNotThrow(() -> client.refresh(refreshToken));
    }

    @Test
    @DisplayName("getType(): returns Google")
    void successfulGetType() {
        var actual = assertDoesNotThrow(() -> client.getType());
        assertEquals(ProviderType.GOOGLE, actual);
    }

    //  -----------------------   failure tests   -------------------------

    @Test
    @DisplayName("tokenInfo(): returns 500 when a unknown exception occurs")
    void failureTokenInfoInternalServerError() {
        var accessToken = generateString();
        wireGoogleAuthServiceMock
                .stubFor(get(urlPathEqualTo(GET_TOKEN_INFO))
                        .withQueryParam("accessToken", equalTo(accessToken))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        )

                );
        assertThrows(FeignException.InternalServerError.class, () -> client.tokenInfo(accessToken));
    }

    @Test
    @DisplayName("refresh(): returns 500 when a unknown exception occurs")
    void failureRefreshInternalServerError() {
        var refreshToken = generateString();
        wireGoogleAuthServiceMock
                .stubFor(post(urlPathEqualTo(POST_TOKEN_REFRESH))
                        .withQueryParam("refreshToken", equalTo(refreshToken))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        )

                );
        assertThrows(FeignException.InternalServerError.class, () -> client.refresh(refreshToken));
    }
}