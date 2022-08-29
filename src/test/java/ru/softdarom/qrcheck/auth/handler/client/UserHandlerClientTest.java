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
import ru.softdarom.qrcheck.auth.handler.test.tag.SpringIntegrationTest;

import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateLong;
import static ru.softdarom.qrcheck.auth.handler.test.generator.DtoGenerator.providerUserDto;
import static ru.softdarom.qrcheck.auth.handler.test.helper.UriHelper.generateUri;

@SpringIntegrationTest
@DisplayName("UserHandlerClient Spring Integration Test")
class UserHandlerClientTest {

    private static final String POST_SAVE_USER = "/internal/users";
    private static final String GET_FIND_USER = "/internal/users/%s";

    @Autowired
    private WireMockServer wireUserHandlerServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserHandlerClient client;

    @AfterEach
    void tearDown() {
        wireUserHandlerServiceMock.resetAll();
    }

    //  -----------------------   successful tests   -------------------------

    @Test
    @DisplayName("save(): returns 200 when a user is saved")
    void successfulSave() throws JsonProcessingException {
        var request = providerUserDto();
        var apiKey = UUID.randomUUID();
        wireUserHandlerServiceMock
                .stubFor(post(urlEqualTo(POST_SAVE_USER))
                        .withHeader("X-ApiKey-Authorization", equalTo(apiKey.toString()))
                        .withRequestBody(containing(objectMapper.writeValueAsString(request)))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(objectMapper.writeValueAsString(providerUserDto()))
                        )

                );
        assertDoesNotThrow(() -> client.save(apiKey, request));
    }

    @Test
    @DisplayName("get(): returns 200 when a user is found")
    void successfulGet() throws JsonProcessingException {
        var externalUserId = generateLong();
        var apiKey = UUID.randomUUID();
        wireUserHandlerServiceMock
                .stubFor(get(urlEqualTo(String.format(GET_FIND_USER, externalUserId)))
                        .withHeader("X-ApiKey-Authorization", equalTo(apiKey.toString()))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(objectMapper.writeValueAsString(providerUserDto()))
                        )

                );
        assertDoesNotThrow(() -> client.get(apiKey, externalUserId));
    }

    //  -----------------------   failure tests   -------------------------

    //  -----------------------   save()   -------------------------
    @Test
    @DisplayName("save(): returns 401 when unauthorized calls")
    void failureSaveUnauthorized() throws JsonProcessingException {
        var request = providerUserDto();
        var apiKey = UUID.randomUUID();
        wireUserHandlerServiceMock
                .stubFor(post(urlEqualTo(POST_SAVE_USER))
                        .withHeader("X-ApiKey-Authorization", equalTo(apiKey.toString()))
                        .withRequestBody(containing(objectMapper.writeValueAsString(request)))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        )

                );
        assertThrows(FeignException.Unauthorized.class, () -> client.save(apiKey, request));
    }

    @Test
    @DisplayName("save(): returns 500 when a unknown exception occurs")
    void failureSaveInternalServerError() throws JsonProcessingException {
        var request = providerUserDto();
        var apiKey = UUID.randomUUID();
        wireUserHandlerServiceMock
                .stubFor(post(urlEqualTo(POST_SAVE_USER))
                        .withHeader("X-ApiKey-Authorization", equalTo(apiKey.toString()))
                        .withRequestBody(containing(objectMapper.writeValueAsString(request)))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        )

                );
        assertThrows(FeignException.InternalServerError.class, () -> client.save(apiKey, request));
    }

    //  -----------------------   get()   -------------------------

    @Test
    @DisplayName("get(): returns 401 when unauthorized calls")
    void failureGetUnauthorized() {
        var externalUserId = generateLong();
        var apiKey = UUID.randomUUID();
        wireUserHandlerServiceMock
                .stubFor(get(urlEqualTo(String.format(GET_FIND_USER, externalUserId)))
                        .withHeader("X-ApiKey-Authorization", equalTo(apiKey.toString()))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        )

                );
        assertThrows(FeignException.Unauthorized.class, () -> client.get(apiKey, externalUserId));
    }

    @Test
    @DisplayName("get(): returns 404 when a user isn't found")
    void failureGetNotFound() {
        var externalUserId = generateLong();
        var apiKey = UUID.randomUUID();
        wireUserHandlerServiceMock
                .stubFor(get(urlEqualTo(String.format(GET_FIND_USER, externalUserId)))
                        .withHeader("X-ApiKey-Authorization", equalTo(apiKey.toString()))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.NOT_FOUND.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        )

                );
        assertThrows(FeignException.NotFound.class, () -> client.get(apiKey, externalUserId));
    }

    @Test
    @DisplayName("get(): returns 500 when a unknown exception occurs")
    void failureGetInternalServerError() {
        var externalUserId = generateLong();
        var apiKey = UUID.randomUUID();
        wireUserHandlerServiceMock
                .stubFor(get(urlEqualTo(String.format(GET_FIND_USER, externalUserId)))
                        .withHeader("X-ApiKey-Authorization", equalTo(apiKey.toString()))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        )

                );
        assertThrows(FeignException.InternalServerError.class, () -> client.get(apiKey, externalUserId));
    }
}