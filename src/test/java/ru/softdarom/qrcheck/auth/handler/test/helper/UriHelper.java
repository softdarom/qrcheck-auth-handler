package ru.softdarom.qrcheck.auth.handler.test.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UriHelper {

    private static final String BASE_SCHEMA = "http";
    private static final String BASE_HOST = "localhost";

    public static String generateUri(String path, int port) {
        return UriComponentsBuilder.newInstance()
                .scheme(BASE_SCHEMA)
                .host(BASE_HOST)
                .port(port)
                .path(path)
                .build().toString();
    }

    public static String generateUri(String path, Map<String, Object> queryParams) {
        var encodedQueryParams =
                queryParams.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                it -> List.of(URLEncoder.encode(it.getValue().toString(), StandardCharsets.UTF_8)))
                        );
        return UriComponentsBuilder
                .newInstance()
                .path(path)
                .queryParams(new LinkedMultiValueMap<>(encodedQueryParams))
                .toUriString();
    }
}