package ru.softdarom.qrcheck.auth.handler.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.softdarom.qrcheck.auth.handler.config.property.ApiKeyProperties;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.softdarom.qrcheck.auth.handler.test.generator.CommonGenerator.generateString;
import static ru.softdarom.qrcheck.auth.handler.test.helper.UriHelper.generateUri;

abstract class AbstractControllerTest {

    protected static final String DEFAULT_INCOMING_API_KEY = "7c1ac776-0532-4be2-9f29-f637da759421";

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ApiKeyProperties apiKeyProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    protected <T> ResponseEntity<T> get(Class<T> responseType, HttpHeaders headers, String path) {
        var uri = generateUri(path, port);
        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), responseType);
    }

    protected <T> ResponseEntity<T> post(HttpHeaders headers, String path) {
        return exchange(null, HttpMethod.POST, headers, path);
    }

    protected <T> ResponseEntity<T> put(HttpHeaders headers, String path) {
        return exchange(null, HttpMethod.PUT, headers, path);
    }

    protected <T, R> ResponseEntity<T> post(R request, HttpHeaders headers, String path) {
        return exchange(request, HttpMethod.POST, headers, path);
    }

    protected <T, R> ResponseEntity<T> delete(R request, HttpHeaders headers, String path) {
        return exchange(request, HttpMethod.DELETE, headers, path);
    }

    protected <T, R> ResponseEntity<T> exchange(R request, HttpMethod method, HttpHeaders headers, String path) {
        var uri = generateUri(path, port);
        return restTemplate.exchange(uri, method, new HttpEntity<>(request, headers), new ParameterizedTypeReference<>() {
        });
    }

    protected BiConsumer<ResponseEntity<?>, HttpStatus> assertCall() {
        return (response, status) -> {
            assertNotNull(response);
            assertEquals(status, response.getStatusCode());
        };
    }

    protected BiConsumer<ResponseEntity<?>, HttpStatus> assertCallWithBody() {
        return (response, status) -> {
            assertNotNull(response);
            assertEquals(status, response.getStatusCode());
            assertNotNull(response.getBody());
        };
    }

    protected HttpHeaders buildApiKeyHeader() {
        var headers = new HttpHeaders();
        headers.set(apiKeyProperties.getHeaderName(), DEFAULT_INCOMING_API_KEY);
        return headers;
    }

    protected HttpHeaders buildNotAuthHeader() {
        return new HttpHeaders();
    }
}